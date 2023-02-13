package com.petspace.dev.util.s3;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.petspace.dev.util.exception.ReviewException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import com.petspace.dev.util.exception.AwsException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

import static com.petspace.dev.util.BaseResponseStatus.*;

@Slf4j
@RequiredArgsConstructor
@Component
public class AwsS3Uploader {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    public String bucket;  // S3 버킷 이름

    public String upload(MultipartFile multipartFile, String dirName){

        try {
            if(!multipartFile.isEmpty()){
                // 확장자 확인
                String originFileName = multipartFile.getOriginalFilename().toUpperCase();
                log.info("fileName={}", originFileName);

                String ext = originFileName.substring(originFileName.lastIndexOf(".") + 1);
                log.info("ext={}", ext);

                if(!ext.matches("JPEG|JPG|HEIC|PNG|" )){
                    throw new ReviewException(POST_IMAGE_INVALID_EXTENSION);
                }
            }
            File uploadFile = convert(multipartFile)  // 파일 변환할 수 없으면 에러
                    .orElseThrow(() -> new AwsException(POST_IMAGE_CONVERT_ERROR));

            String fileName = dirName + "/" + UUID.randomUUID();   // S3에 저장된 파일 이름

            amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, uploadFile)   // S3에 업로드
                    .withCannedAcl(CannedAccessControlList.PublicRead));

            removeNewFile(uploadFile);
            log.info("S3 upload complete!");
            return amazonS3Client.getUrl(bucket, fileName).toString();

        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    // 로컬서버에 저장된 이미지 지우기
    private void removeNewFile(File targetFile) {
        if (targetFile.delete()) {
            log.info("File delete success");
            return;
        }
        log.info("File delete fail");
    }

    // 로컬서버에 파일 변환 하기
    private Optional<File> convert(MultipartFile file) throws IOException {
        File convertFile = new File(System.getProperty("user.dir") + "/" + file.getOriginalFilename());
        if (convertFile.createNewFile()) { // 바로 위에서 지정한 경로에 File이 생성됨 (경로가 잘못되었다면 생성 불가능)
            try (FileOutputStream fos = new FileOutputStream(convertFile)) { // FileOutputStream 데이터를 파일에 바이트 스트림으로 저장하기 위함
                fos.write(file.getBytes());
            }
            return Optional.of(convertFile);
        }

        return Optional.empty();
    }

    public void deleteReviewImage(String imageUrl) {
        log.info("deleteImage = {}", imageUrl);
        amazonS3Client.deleteObject(new DeleteObjectRequest(bucket, imageUrl));
    }
}