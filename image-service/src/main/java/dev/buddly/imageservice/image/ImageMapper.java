package dev.buddly.imageservice.image;

import org.springframework.stereotype.Service;

@Service
public class ImageMapper {

    public ImageResponse toResponse(Image image){
        return new ImageResponse(
                image.getUrl()
        );
    }
}
