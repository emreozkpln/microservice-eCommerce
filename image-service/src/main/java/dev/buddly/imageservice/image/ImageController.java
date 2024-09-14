package dev.buddly.imageservice.image;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/image")
public class ImageController {

    private final ImageService service;

    @GetMapping("/{imageId}")
    public ResponseEntity<ImageResponse> getImageById(
            @PathVariable Integer imageId
    ){
        return ResponseEntity.ok(service.getImageById(imageId));
    }

    @PostMapping(path = "/upload", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        return ResponseEntity.ok(service.uploadFile(file.getOriginalFilename(), file));
    }
}
