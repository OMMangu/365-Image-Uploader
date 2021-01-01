package com.ampmangu.imageuploader;

import com.google.cloud.storage.Blob;
import org.mockito.Mockito;

import java.io.InputStream;

import static org.mockito.Mockito.mock;

public class TestUtils {
    public static final String DUMMY = "dummy";
    public static final String TEST_IMAGE_1_JPEG = "test/image1";


    public static InputStream getTestImage() {
        return Thread.currentThread().getContextClassLoader().getResourceAsStream("test-image.png");
    }

    public static Blob getMockedBlob() {
        Blob mockedBlob = mock(Blob.class);
        Mockito.when(mockedBlob.getBucket()).thenReturn(DUMMY);
        Mockito.when(mockedBlob.getName()).thenReturn(TEST_IMAGE_1_JPEG);
        return mockedBlob;
    }

}
