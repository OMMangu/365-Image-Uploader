package com.ampmangu.imageuploader.resource;

import com.ampmangu.imageuploader.service.ImageService;
import com.ampmangu.imageuploader.utils.BucketResponse;
import com.google.cloud.storage.Blob;
import com.google.gson.Gson;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class ImageResource {
    private final ImageService imageService;

    ImageResource(ImageService imageService) {
        this.imageService = imageService;
    }

    @GetMapping(value = "/images/{userId}/{imageId}", produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody
    ResponseEntity<byte[]> getImageByPath(@PathVariable String userId, @PathVariable String imageId) {
        Optional<Blob> imageByPath = imageService.getImageByPath(userId, imageId);
        return imageByPath.map(blob -> ResponseEntity.ok().body(blob.getContent())).orElseGet(() -> ResponseEntity.status(404).build());
    }

    //TODO GET AUTHENTICATED IMAGE
    @GetMapping(value = "/images/link/{userId}/{imageId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getImageLinkByPath(@PathVariable String userId, @PathVariable String imageId) {
        String imageUrlByPath = imageService.getImageUrlByPath(userId, imageId);
        BucketResponse bucketResponse;
        if (imageUrlByPath.isEmpty()) {
            return ResponseEntity.status(404).build();
        } else {
            bucketResponse = new BucketResponse.Builder("OK")
                    .withCode(200).withPath(imageUrlByPath).build();
            return ResponseEntity.ok().body(new Gson().toJson(bucketResponse));
        }
    }

    @PostMapping(value = "/images/{userId}/{imageId}", produces = MediaType.APPLICATION_JSON_VALUE , consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> postImage(@PathVariable String userId, @PathVariable String imageId, @RequestParam("file") MultipartFile file) throws IOException {
        Blob blob = imageService.uploadImage(file.getInputStream(), file.getContentType(), userId, imageId);
        BucketResponse bucketResponse = new BucketResponse.Builder("OK")
                .withCode(200).withPath(blob.getBucket() + "/" + blob.getName()).build();
        return ResponseEntity.ok().body(new Gson().toJson(bucketResponse));
    }
}
