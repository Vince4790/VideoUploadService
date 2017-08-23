package com.video.upload.repository;

import com.video.upload.model.AwsCredential;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AwsCredentialRepository extends JpaRepository<AwsCredential, Long> {
}
