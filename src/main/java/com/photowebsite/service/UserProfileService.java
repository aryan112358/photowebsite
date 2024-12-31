package com.photowebsite.service;

import com.photowebsite.dto.request.UpdateProfileRequest;
import com.photowebsite.dto.response.PhotoStatsResponse;
import com.photowebsite.dto.response.UserProfileResponse;
import com.photowebsite.entity.User;
import com.photowebsite.entity.UserProfile;
import com.photowebsite.exception.ResourceNotFoundException;
import com.photowebsite.repository.UserProfileRepository;
import com.photowebsite.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class UserProfileService {

    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final FileStorageService fileStorageService;
    private final PhotoService photoService;

    @Transactional(readOnly = true)
    public UserProfileResponse getProfile(String username) {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        UserProfile profile = userProfileRepository.findById(user.getId())
            .orElseGet(() -> createDefaultProfile(user));

        return mapToProfileResponse(user, profile);
    }

    @Transactional
    public UserProfileResponse updateProfile(UpdateProfileRequest request, String username) {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        UserProfile profile = userProfileRepository.findById(user.getId())
            .orElseGet(() -> createDefaultProfile(user));

        updateProfileFields(profile, request);

        if (request.getAvatar() != null && !request.getAvatar().isEmpty()) {
            String avatarUrl = uploadAvatar(request.getAvatar());
            profile.setAvatarUrl(avatarUrl);
        }

        userProfileRepository.save(profile);
        return mapToProfileResponse(user, profile);
    }

    private UserProfile createDefaultProfile(User user) {
        UserProfile profile = new UserProfile();
        profile.setUser(user);
        return userProfileRepository.save(profile);
    }

    private void updateProfileFields(UserProfile profile, UpdateProfileRequest request) {
        profile.setBio(request.getBio());
        profile.setLocation(request.getLocation());
        profile.setWebsite(request.getWebsite());
        profile.setSocialMediaLinks(request.getSocialMediaLinks());
        profile.setPhotographyInterests(request.getPhotographyInterests());
        profile.setCameraGear(request.getCameraGear());
    }

    private String uploadAvatar(MultipartFile avatar) {
        return fileStorageService.storeFile(avatar);
    }

    private UserProfileResponse mapToProfileResponse(User user, UserProfile profile) {
        return UserProfileResponse.builder()
            .username(user.getUsername())
            .email(user.getEmail())
            .avatarUrl(profile.getAvatarUrl())
            .bio(profile.getBio())
            .location(profile.getLocation())
            .website(profile.getWebsite())
            .socialMediaLinks(profile.getSocialMediaLinks())
            .photographyInterests(profile.getPhotographyInterests())
            .cameraGear(profile.getCameraGear())
            .stats(buildPhotoStats(user.getUsername()))
            .build();
    }

    private PhotoStatsResponse buildPhotoStats(String username) {
        return PhotoStatsResponse.builder()
            .totalPhotos(photoService.countUserPhotos(username))
            .totalAlbums(photoService.countUserAlbums(username))
            .totalLikes(photoService.countUserPhotoLikes(username))
            .totalComments(photoService.countUserPhotoComments(username))
            .build();
    }
} 