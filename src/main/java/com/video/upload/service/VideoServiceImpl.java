package com.video.upload.service;

import com.video.upload.aws.AwsS3Connector;
import com.video.upload.model.User;
import com.video.upload.model.Video;
import com.video.upload.repository.VideoRepository;
import javassist.NotFoundException;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ServerErrorException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class VideoServiceImpl implements VideoService {
    private static final String FILE_UPLOAD_STATUS_INCOMPLETE = "Incomplete";
    private static final String FILE_UPLOAD_STATUS_COMPLETE = "Completed";
    private static final String TMP_DIR = System.getProperty("os.name").toLowerCase().contains("win") ? "": "/tmp/";

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private AwsS3Connector awsS3Connector;

    @Override
    public List<Video> getVideosByCurrentUser() {
        User currentUser = userService.getCurrentUser();
        return videoRepository.findByUserId(currentUser.getId());
    }

    @Override
    public void deleteVideosByCurrentUser(int[] ids) {
        List<Long> videoIds = new ArrayList<>();
        for(int i : ids){
            videoIds.add((long) i);
        }
        videoRepository.deleteByIds(videoIds);
    }

    @Override
    public void deleteAllVideosByCurrentUser() {
        Long userId = userService.getCurrentUser().getId();
        videoRepository.deleteAllByUserId(userId);
    }

    @Override
    public void storeChunkTemporary(MultipartFile file, String name, String chunk, String checksum) throws IOException {
        FileOutputStream fos;
        String trimmed = name.replace(" ","_");
        File ofile = new File(TMP_DIR+trimmed+".part"+chunk);
        System.out.println("absolute path:"+ofile.getAbsolutePath());
        fos = new FileOutputStream(ofile,false);
        byte[] fileBytes;
        try {
            fileBytes = file.getBytes();
            String md5Hex = DigestUtils
                    .md5Hex(fileBytes).toUpperCase();
            System.out.println("Md5:"+ md5Hex);
            validateMD5Checksum(md5Hex, checksum);
            fos.write(fileBytes);
        }catch (Exception exception){
            exception.printStackTrace();
        } finally{
            fos.flush();
            fos.close();
        }

        System.out.println("completed stored chunk, md5");
    }

    private void validateMD5Checksum(String fileChecksume, String requestChecksum) {
        if (!fileChecksume.equalsIgnoreCase(requestChecksum)){
            throw new ServerErrorException("File checksum invalid!!");
        }
    }

    @Override
    public Video mergeAndUploadNewVideo(String videoName, int chunks, String ext) throws Exception {
        FileOutputStream fos;
        String trimmed = videoName.replace(" ","_");
        File ofile = new File(TMP_DIR+trimmed+"."+ext);

        byte[] fileBytes;
        for (int i=0;i<chunks;i++){
            fos = new FileOutputStream(ofile,true);
            File chunk = new File(TMP_DIR+trimmed+".part"+i);
            if (!chunk.exists()){
                fos.close();
                ofile.delete();
                throw new NotFoundException(String.format("Chunk %s not found", chunk.getName()));
            }
            FileInputStream fin = new FileInputStream(chunk);

            try {
                fileBytes = new byte[(int)chunk.length()];
                fin.read(fileBytes);
                fos.write(fileBytes);
                fos.flush();
            } catch (Exception exception) {
                exception.printStackTrace();
                // if exception happen, clean up all remaining chunks and the merged file itself
                cleanupChunksIfMergeFailed(trimmed, chunks);
                ofile.delete();
            } finally{
                fos.close();
                fin.close();
                chunk.delete();
            }
        }
        System.out.println("completed merge file:"+ofile.getName()+" size:"+ofile.length());

        System.out.println("start upload to S3");

        try {
            String url = awsS3Connector.pushVideoToS3Bucked(ofile);
            System.out.println("done upload to S3");
            System.out.println("save to database");
            Video newVideo = new Video();
            newVideo.setUserId(userService.getCurrentUser().getId());
            newVideo.setUrl(url);
            newVideo.setName(videoName);

            Video created = videoRepository.save(newVideo);
            System.out.println("done save to database");

            return created;
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            // delete temporary file in folder
            ofile.delete();
        }

        return null;
    }

    private void cleanupChunksIfMergeFailed(String name, int total) {
        for (int i=0;i<total;i++){
            File chunk = new File(TMP_DIR+name+".part"+i);
            if(chunk.exists()) {
                chunk.delete();
            }
        }
    }

    @Override
    public String validateAllChunksUploaded(String name, int total) {
        String trimmed = name.replace(" ","_");
        for (int i=0;i<total;i++){
            File check = new File(TMP_DIR+trimmed+".part"+i);
            if(!check.exists()) {
                return FILE_UPLOAD_STATUS_INCOMPLETE;
            }

        }
        return FILE_UPLOAD_STATUS_COMPLETE;
    }
}
