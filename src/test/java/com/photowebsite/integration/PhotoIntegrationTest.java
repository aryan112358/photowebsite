package com.photowebsite.integration;

import com.photowebsite.dto.request.PhotoRequest;
import com.photowebsite.repository.PhotoRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class PhotoIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PhotoRepository photoRepository;

    @AfterEach
    void cleanup() {
        photoRepository.deleteAll();
    }

    @Test
    @WithMockUser
    void fullPhotoLifecycle() throws Exception {
        // Create photo
        MockMultipartFile file = new MockMultipartFile(
            "file",
            "test.jpg",
            MediaType.IMAGE_JPEG_VALUE,
            "test image content".getBytes()
        );

        String photoId = mockMvc.perform(multipart("/api/photos")
                .file(file)
                .param("title", "Test Photo"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Photo"))
                .andReturn()
                .getResponse()
                .getContentAsString()
                .split("\"id\":")[1]
                .split(",")[0];

        // Get photo
        mockMvc.perform(get("/api/photos/" + photoId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Photo"));

        // Delete photo
        mockMvc.perform(delete("/api/photos/" + photoId))
                .andExpect(status().isNoContent());
    }
} 