## Installation Guide

This is a Java Spring Boot app run with MySQL as database server.

### Prerequisite
- Git installed
- JDK 1.8 installed
    http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html
- MySQL server is up and running on localhost.

    To setup the connection to MySQL, open **application.properties** in this project and update accordingly
    ```markdown
    spring.datasource.url = jdbc:mysql://localhost:3306/videodb
    spring.datasource.username = test_user
    spring.datasource.password = pass1234
    spring.datasource.testWhileIdle = true
    spring.datasource.validationQuery = SELECT 1
    ```

### Step 1:
Clone from github
```markdown
git clone git@github.com:Vince4790/VideoUploadService.git
```
### Step 2:
Set up local instance of MySQL server

Copy and run the script in **/db/update/initial_data_table_create.sql** to create schema and data tables

Verify following tables exist in database: **user**, **video**, **aws_credential**

**Note**: For aws credential, if you want to setup on your local, contact me to get my S3 credential or you can insert
your access key id and your secret access key into **aws_credential** table then update the bucket and region using yours accordingly:
```java
In AwsS3Connector, update accordingly if you want to use your own S3 bucket:

    private static final String BUCKET_NAME = "video-upload-vince";
    private static final String S3_REGION_ENDPOINT = "https://s3-ap-southeast-1.amazonaws.com/video-upload-vince";
```


### Step 3:
To build Spring Boot project:
```markdown
gradlew build clean
```
or
```markdown
gradlew build
```

### Step 4:
Configure host and port locally at **application.properties**:
```markdown
server.port= 8080
server.address= localhost
server.display-name = VideoService
```

Run in local using:
```markdown
gradlew bootRun
```