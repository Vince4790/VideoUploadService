package com.video.upload.MyVideoUpload.repository;

import com.video.upload.MyVideoUpload.model.AwsCredential;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AwsCredentialRepository extends JpaRepository<AwsCredential, Long> {
}
