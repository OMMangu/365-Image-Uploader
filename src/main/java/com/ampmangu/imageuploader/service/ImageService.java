package com.ampmangu.imageuploader.service;

import com.google.cloud.storage.Blob;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

public interface ImageService {
    Blob uploadImage(InputStream inputStream, String contentType, String userId, String imageId) throws IOException;

    Optional<Blob> getImageByPath(String userId, String imageId);

    String getImageUrlByPath(String userId, String imageId);
}
