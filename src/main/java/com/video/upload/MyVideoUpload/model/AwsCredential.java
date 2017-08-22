package com.video.upload.MyVideoUpload.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
@Table(name = "aws_credential")
public class AwsCredential {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "access_key_id")
    @JsonIgnore
    private String accessKeyId;

    @Column(name = "secret_access_key")
    @JsonIgnore
    private String secretAccessKey;

    public Long getId() {
        return id;
    }

    public String getAccessKeyId() {
        return accessKeyId;
    }

    public String getSecretAccessKey() {
        return secretAccessKey;
    }
}
