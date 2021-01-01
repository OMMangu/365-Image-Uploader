package com.ampmangu.imageuploader.service;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.context.TestPropertySource;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

import static com.ampmangu.imageuploader.TestUtils.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;

@TestPropertySource(locations = "classpath:application.yaml")
@RunWith(MockitoJUnitRunner.class)
public class GcpImageServiceImplTest {

    public static final String IMAGE_URL = "https://storage.googleapis.com/download/storage/v1/b/dummy/o/test%2Fimage1?generation=1609249148884157&alt=media";
    private ImageService imageService;

    private InputStream inputStream;
    @Mock
    private Storage storage;


    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        imageService = new GcpImageServiceImpl(storage);
        ((GcpImageServiceImpl) imageService).setBucketName(DUMMY);
        inputStream = mock(InputStream.class);
    }

    @Test
    public void testStorageUploadsProperBlob() throws IOException {
        Blob mockedBlob = getMockedBlob();
        Mockito.when(storage.create(any(BlobInfo.class), (byte[]) any(), any(Storage.BlobTargetOption.class))).thenReturn(mockedBlob);
        Blob blob = imageService.uploadImage(inputStream, "image/jpeg", "test", "image1");
        Assertions.assertThat(blob).isNotNull();
        Assertions.assertThat(blob.getBucket()).isEqualTo(DUMMY);
        Assertions.assertThat(blob.getName()).isEqualTo(TEST_IMAGE_1_JPEG);
    }

    @Test
    public void testStorageReturnsProperBlob() {
        Blob mockedBlob = mock(Blob.class);
        Mockito.when(mockedBlob.getMediaLink()).thenReturn(IMAGE_URL);
        Mockito.when(storage.get(any(BlobId.class))).thenReturn(mockedBlob);
        String imageUrlByPath = imageService.getImageUrlByPath("test", "image1");
        Assertions.assertThat(imageUrlByPath).isEqualTo(IMAGE_URL);
    }

    @Test
    public void testStorageReturnsEmptyLinkForWrongId() {
        //No need to mock, blob will be null anyways
        String imageUrlByPath = imageService.getImageUrlByPath("test", "wrong");
        Assertions.assertThat(imageUrlByPath).isEmpty();
    }

    @Test
    public void testStorageReturnsNoBlobForWrongId() {
        Optional<Blob> imageByPath = imageService.getImageByPath("test", "wrong");
        Assertions.assertThat(imageByPath).isEmpty();
    }

}
