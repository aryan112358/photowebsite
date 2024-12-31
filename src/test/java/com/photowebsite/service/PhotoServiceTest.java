package com.photowebsite.service;

import com.photowebsite.dto.request.PhotoRequest;
import com.photowebsite.entity.Photo;
import com.photowebsite.exception.PhotoException;
import com.photowebsite.exception.ResourceNotFoundException;
import com.photowebsite.repository.PhotoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PhotoServiceTest {

    @Mock
    private PhotoRepository photoRepository;

    @Mock
    private FileStorageService fileStorageService;

    @InjectMocks
    private PhotoService photoService;

    private PhotoRequest photoRequest;
    private Photo photo;
    private MultipartFile multipartFile;

    @BeforeEach
    void setUp() {
        multipartFile = new MockMultipartFile(
            "file",
            "test.jpg",
            "image/jpeg",
            "test image content".getBytes()
        );

        photoRequest = new PhotoRequest();
        photoRequest.setTitle("Test Photo");
        photoRequest.setDescription("Test Description");
        photoRequest.setFile(multipartFile);

        photo = new Photo();
        photo.setId(1L);
        photo.setTitle("Test Photo");
        photo.setFileName("test.jpg");
    }

    @Test
    void addPhoto_Success() {
        when(fileStorageService.storeFile(any())).thenReturn("stored_test.jpg");
        when(photoRepository.save(any())).thenReturn(photo);

        var result = photoService.addPhoto(photoRequest, "testUser");

        assertNotNull(result);
        assertEquals("Test Photo", result.getTitle());
        verify(fileStorageService).storeFile(any());
        verify(photoRepository).save(any());
    }

    @Test
    void addPhoto_WithInvalidFile_ThrowsException() {
        photoRequest.setFile(null);

        assertThrows(PhotoException.class, () -> 
            photoService.addPhoto(photoRequest, "testUser")
        );
    }

    @Test
    void getPhotoById_Success() {
        when(photoRepository.findById(1L)).thenReturn(Optional.of(photo));

        var result = photoService.getPhotoById(1L);

        assertNotNull(result);
        assertEquals(photo.getTitle(), result.getTitle());
    }

    @Test
    void getPhotoById_NotFound_ThrowsException() {
        when(photoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> 
            photoService.getPhotoById(1L)
        );
    }
} 