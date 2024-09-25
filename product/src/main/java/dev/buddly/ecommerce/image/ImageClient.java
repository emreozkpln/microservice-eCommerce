package dev.buddly.ecommerce.image;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "image-service",
        url = "${application.config.image-url}"
)
public interface ImageClient {
    @GetMapping("/{imageId}")
    ImageResponse getImageById(
            @PathVariable Integer imageId
    );
}
