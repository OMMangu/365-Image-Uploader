package com.ampmangu.imageuploader.resource;

import com.ampmangu.imageuploader.Application;
import com.ampmangu.imageuploader.service.ImageService;
import com.google.cloud.storage.Blob;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.InputStream;
import java.util.Optional;

import static com.ampmangu.imageuploader.TestUtils.getMockedBlob;
import static com.ampmangu.imageuploader.TestUtils.getTestImage;
import static com.ampmangu.imageuploader.service.GcpImageServiceImplTest.IMAGE_URL;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = Application.class)
@RunWith(SpringRunner.class)
public class ImageResourceIT {
    public static final String API_IMAGES_TEST_IMAGE_1 = "/api/images/test/image1";
    public static final String API_LINK_IMAGES_TEST_IMAGE_1 = "/api/images/link/test/image1";
    @Mock
    ImageService imageService;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
        ImageResource imageResource = new ImageResource(imageService);
        this.mockMvc = MockMvcBuilders.standaloneSetup(imageResource).build();
    }

    @Test
    public void testProperUpload() throws Exception {
        Blob mockBlob = getMockedBlob();
        final MockMultipartFile file = new MockMultipartFile("file", "image.jpg", MediaType.IMAGE_PNG_VALUE, getTestImage());
        when(imageService.uploadImage(any(InputStream.class), anyString(), anyString(), anyString())).thenReturn(mockBlob);
        mockMvc.perform(
                MockMvcRequestBuilders.multipart(API_IMAGES_TEST_IMAGE_1)
                        .file(file)
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
        ).andExpect(status().isOk()).
                andExpect(jsonPath("$.path").value("dummy/test/image1"));

    }

    @Test
    public void testGetImage() throws Exception {
        Blob mockBlob = getMockedBlob();
        when(imageService.getImageByPath(anyString(), anyString())).thenReturn(Optional.of(mockBlob));
        mockMvc.perform(
                get(API_IMAGES_TEST_IMAGE_1)

        ).andExpect(status().isOk());
    }

    @Test
    public void testGetImageLink() throws Exception {
        when(imageService.getImageUrlByPath(anyString(), anyString())).thenReturn(IMAGE_URL);
        mockMvc.perform(
                get(API_LINK_IMAGES_TEST_IMAGE_1)

        ).andExpect(status().isOk()).andExpect(jsonPath("$.path").value(IMAGE_URL));
    }

    @Test
    public void testNoImageLinkFound() throws Exception {
        when(imageService.getImageUrlByPath(anyString(), anyString())).thenReturn("");
        mockMvc.perform(
                get(API_LINK_IMAGES_TEST_IMAGE_1)
        ).andExpect(status().is(404));
    }
}
