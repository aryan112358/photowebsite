package com.photowebsite.controller;

import com.photowebsite.dto.request.PhotoRequest;
import com.photowebsite.dto.response.PhotoResponse;
import com.photowebsite.service.PhotoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PhotoController.class)
class PhotoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PhotoService photoService;

    @Test
    @WithMockUser
    void getAllPhotos_Success() throws Exception {
        Page<PhotoResponse> photoPage = new PageImpl<>(List.of(
            PhotoResponse.builder()
                .id(1L)
                .title("Test Photo")
                .build()
        ));

        when(photoService.getAllPhotos(any(), any())).thenReturn(photoPage);

        mockMvc.perform(get("/api/photos")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].title").value("Test Photo"));
    }

    @Test
    @WithMockUser
    void addPhoto_Success() throws Exception {
        PhotoResponse response = PhotoResponse.builder()
            .id(1L)
            .title("Test Photo")
            .build();

        when(photoService.addPhoto(any(), any())).thenReturn(response);

        MockMultipartFile file = new MockMultipartFile(
            "file",
            "test.jpg",
            MediaType.IMAGE_JPEG_VALUE,
            "test image content".getBytes()
        );

        mockMvc.perform(multipart("/api/photos")
                .file(file)
                .param("title", "Test Photo"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Photo"));
    }
} 