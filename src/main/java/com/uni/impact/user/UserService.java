package com.uni.impact.user;

import com.uni.impact.college.College;
import com.uni.impact.college.CollegeRepository;
import com.uni.impact.user.dto.UserRequestDTO;
import com.uni.impact.user.dto.UserResponseDTO;
import com.uni.impact.util.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final CollegeRepository collegeRepository;
    private final UserMapper userMapper;

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
        if (user.getPhoto() == null) {
            user.setPhoto("https://i.pravatar.cc/400?u=" + userDTO.getEmail());
        }
        applyRelations(user, userDTO);
        return userMapper.toResponseDto(userRepository.save(user));
    }

    @Transactional
    public UserResponseDTO update(final Long userId, final UserRequestDTO userDTO) {

        User user = userRepository.findById(userId).orElseThrow(NotFoundException::new);
        userMapper.updateEntity(user, userDTO);
        applyRelations(user, userDTO);
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
