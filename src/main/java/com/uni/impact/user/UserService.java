package com.uni.impact.user;

import com.uni.impact.college.College;
import com.uni.impact.college.CollegeRepository;
import com.uni.impact.user.dto.UserRequestDTO;
import com.uni.impact.user.dto.UserResponseDTO;
import com.uni.impact.util.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final CollegeRepository collegeRepository;
    private final UserMapper userMapper;

    @Value("${app.upload.dir:uploads/photos}")
    private String uploadDir;

    @Transactional(readOnly = true)
    public User findById(final Long userId) {
        return userRepository.findById(userId).orElseThrow(NotFoundException::new);
    }

    @Transactional(readOnly = true)
    public User findByEmail(final String email) {
        return userRepository.findByEmailIgnoreCase(email).orElseThrow(NotFoundException::new);
    }
    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmailIgnoreCase(email);
    }

    @Transactional(readOnly = true)
    public Page<UserResponseDTO> findAll(Pageable pageable) {
        return userRepository.findAll(pageable).map(userMapper::toResponseDto);
    }

    @Transactional(readOnly = true)
    public UserResponseDTO findDtoById(final Long userId) {
        return userMapper.toResponseDto(findById(userId));
    }

    @Transactional(readOnly = true)
    public UserResponseDTO findDtoByEmail(final String email) {
        return userMapper.toResponseDto(findByEmail(email));
    }

    @Transactional
    public UserResponseDTO create(final UserRequestDTO userDTO) {
        if (emailExists(userDTO.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }
        User user = userMapper.toEntity(userDTO);
        applyPhoto(user, userDTO);
        applyRelations(user, userDTO);
        return userMapper.toResponseDto(userRepository.save(user));
    }

    @Transactional
    public UserResponseDTO update(final Long userId, final UserRequestDTO userDTO) {

        User user = userRepository.findById(userId).orElseThrow(NotFoundException::new);
        userMapper.updateEntity(user, userDTO);
        applyPhoto(user, userDTO);
        applyRelations(user, userDTO);
        return userMapper.toResponseDto(userRepository.save(user));
    }

    @Transactional
    public UserResponseDTO updatePhoto(final Long userId, final MultipartFile photoFile) {
        User user = userRepository.findById(userId).orElseThrow(NotFoundException::new);
        user.setPhoto(storePhoto(photoFile, user.getEmail()));
        return userMapper.toResponseDto(userRepository.save(user));
    }

    @Transactional
    public void delete(final Long userId) {
        final User user = userRepository.findById(userId).orElseThrow(NotFoundException::new);
        userRepository.delete(user);
    }

    private void applyRelations(final User user, final UserRequestDTO userDTO) {
        final College college = userDTO.getCollegeId() == null ? null : collegeRepository.findById(userDTO.getCollegeId())
                .orElseThrow(() -> new NotFoundException("college not found"));
        user.setCollege(college);
    }

    private void applyPhoto(final User user, final UserRequestDTO userDTO) {
        MultipartFile photoFile = userDTO.getPhotoFile();
        if (photoFile != null && !photoFile.isEmpty()) {
            user.setPhoto(storePhoto(photoFile, user.getEmail()));
            return;
        }

        if (user.getPhoto() == null) {
            user.setPhoto("https://i.pravatar.cc/400?u=" + userDTO.getEmail());
        }
    }

    private String storePhoto(final MultipartFile photoFile, final String email) {
        if (photoFile == null || photoFile.isEmpty()) {
            throw new IllegalArgumentException("Photo file cannot be empty");
        }

        try {
            Path uploadPath = Paths.get(uploadDir);
            Files.createDirectories(uploadPath);

            String originalName = photoFile.getOriginalFilename() == null ? "photo" : Paths.get(photoFile.getOriginalFilename()).getFileName().toString();
            String filename = UUID.randomUUID() + "_" + originalName;
            Files.write(uploadPath.resolve(filename), photoFile.getBytes());
            return "/uploads/photos/" + filename;
        } catch (IOException e) {
            throw new IllegalStateException("Failed to upload photo for " + email, e);
        }
    }

    public boolean emailExists(final String email) {
        return userRepository.existsByEmailIgnoreCase(email);
    }

    @Transactional
    public UserResponseDTO toggleBan(Long id) {
        User user = userRepository.findById(id).orElseThrow(NotFoundException::new);
        user.setIsBanned(!user.getIsBanned());
        return userMapper.toResponseDto(userRepository.save(user));
    }
}
