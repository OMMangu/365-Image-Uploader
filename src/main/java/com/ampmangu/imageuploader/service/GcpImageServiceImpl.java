package com.ampmangu.imageuploader.service;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

@Service
public class GcpImageServiceImpl implements ImageService {
    private final Storage storage;

    @Value("${gcp.bucket}")
    public String bucketName;

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public GcpImageServiceImpl(Storage storage) {
        this.storage = storage;
    }

    @Override
    public Blob uploadImage(InputStream inputStream, String contentType, String userId, String imageId) throws IOException {
        BlobId blobId = BlobId.of(bucketName, formBlobId(userId, imageId));
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType(contentType).build();
        return storage.create(blobInfo, inputStream.readAllBytes());
    }

    private String formBlobId(String userId, String imageId) {
        return userId + "/" + imageId;
    }

    @Override
    public Optional<Blob> getImageByPath(String userId, String imageId) {
        BlobId blobId = BlobId.of(bucketName, formBlobId(userId, imageId));
        return Optional.ofNullable(storage.get(blobId));
    }

    @Override
    public String getImageUrlByPath(String userId, String imageId) {
        Optional<Blob> imageByPath = this.getImageByPath(userId, imageId);
        return imageByPath.isPresent() ? imageByPath.get().getMediaLink() : "";
    }

}
