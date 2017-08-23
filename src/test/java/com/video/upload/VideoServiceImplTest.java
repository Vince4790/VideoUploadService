package com.video.upload;

import com.video.upload.aws.AwsS3Connector;
import com.video.upload.model.User;
import com.video.upload.model.Video;
import com.video.upload.repository.VideoRepository;
import com.video.upload.service.UserService;
import com.video.upload.service.VideoServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class VideoServiceImplTest {

    private static final String FILE_UPLOAD_STATUS_INCOMPLETE = "Incomplete";
    private static final String FILE_UPLOAD_STATUS_COMPLETE = "Completed";

    @Mock
    private UserService userService;

    @Mock
    private VideoRepository videoRepository;

    @Mock
    private AwsS3Connector awsS3Connector;

    @InjectMocks
    private VideoServiceImpl videoService;

    @Test
    public void testGetVideosByCurrentUser(){
        User currentUser = new User();
        currentUser.setId(1L);
        User mockUser = new User();
        mockUser.setId(1L);


        Video video1 = new Video();
        video1.setName("test video 1");
        video1.setUrl("test url 1");
        video1.setUserId(1L);

        Video video2 = new Video();
        video2.setName("test video 2");
        video2.setUrl("test url 2");
        video2.setUserId(1L);

        when(videoRepository.findByUserId(anyLong())).thenReturn(Arrays.asList(video1, video2));
        when(userService.getCurrentUser()).thenReturn(mockUser);

        List<Video> output = videoService.getVideosByCurrentUser();

        assertEquals(output.size(), 2);
        assertEquals(output.get(0).getUserId().longValue(), 1L);
        assertEquals(output.get(0).getName(), "test video 1");
        assertEquals(output.get(0).getUrl(), "test url 1");
        assertEquals(output.get(1).getUserId().longValue(), 1L);
        assertEquals(output.get(1).getName(), "test video 2");
        assertEquals(output.get(1).getUrl(), "test url 2");
    }

    @Test
    public void testValidateChunksNotUploadComplete() throws IOException {
        String status = videoService.validateAllChunksUploaded("test", 1);
        assertEquals(status, FILE_UPLOAD_STATUS_INCOMPLETE);

        File testFile = new File("test_part0");
        FileOutputStream fos = new FileOutputStream(testFile,false);
        fos.flush();
        fos.close();

        status = videoService.validateAllChunksUploaded("test", 1);
        assertEquals(status, FILE_UPLOAD_STATUS_COMPLETE);

        testFile.delete();
    }

    @Test
    public void testMergeAndUploadNewVideo() throws Exception {
        User mockUser = new User();
        mockUser.setId(1L);

        Video mockReturnedVideo = new Video();
        mockReturnedVideo.setUserId(1L);
        mockReturnedVideo.setUrl("http://test-url");
        mockReturnedVideo.setName("test_video.mp4");

        when(awsS3Connector.pushVideoToS3Bucked(any(File.class))).thenReturn("http://test-aws.s3.com");
        when(userService.getCurrentUser()).thenReturn(mockUser);
        when(videoRepository.save(any(Video.class))). thenReturn(mockReturnedVideo);

        File testFile = new File("test_video_part0");
        FileOutputStream fos = new FileOutputStream(testFile,false);
        fos.flush();
        fos.close();

        Video returned = videoService.mergeAndUploadNewVideo("test_video", 1, "mp4");

        assertEquals(returned.getUserId().longValue(), 1L);
        assertEquals(returned.getName(), "test_video.mp4");
        assertEquals(returned.getUrl(), "http://test-url");

        File fileShouldBeDeleted = new File("test_video.mp4");
        assertFalse(fileShouldBeDeleted.exists());
    }
}
