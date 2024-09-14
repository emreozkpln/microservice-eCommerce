package dev.buddly.imageservice.image;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import dev.buddly.imageservice.exception.ImageNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static java.lang.String.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImageService {

    private final AmazonS3 s3client;
    private final ImageRepository imageRepository;
    private final ImageMapper mapper;

    private String bucketName = "";

    public String uploadFile(String keyName, MultipartFile file) throws IOException {
        try {
            s3client.putObject(bucketName, keyName, file.getInputStream(), null);
            String fileUrl = s3client.getUrl(bucketName, keyName).toString();
            Image savedImage = saveImage(fileUrl, keyName);
            return savedImage.getUrl();

        } catch (AmazonServiceException e) {
            log.error("Amazon S3'ta dosya yükleme hatası: {}", e.getMessage());
            throw new IOException("Dosya yüklenemedi.");
        }
    }

    private Image saveImage(String fileUrl, String keyName) {
        Image image = new Image();
        image.setUrl(fileUrl);
        image.setKeyName(keyName);
        return imageRepository.save(image);
    }

    public ImageResponse getImageById(Integer imageId) {
        var image = imageRepository.findById(imageId)
                .orElseThrow(()-> new ImageNotFoundException(
                        format("Cannot update product:: No product found with the provided ID: %s",imageId)
                ));
        return mapper.toResponse(image);
    }
}
