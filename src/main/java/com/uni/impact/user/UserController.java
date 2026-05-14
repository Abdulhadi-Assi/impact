package com.uni.impact.user;

import com.uni.impact.attendance.AttendanceService;
import com.uni.impact.user.dto.UserRequestDTO;
import com.uni.impact.user.dto.UserResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping(value = "/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final AttendanceService attendanceService;

    @GetMapping
    public ResponseEntity<Page<UserResponseDTO>> findAll(Pageable pageable) {
        return ResponseEntity.ok(userService.findAll(pageable));
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> me(@AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(userService.findDtoByEmail(jwt.getClaimAsString("email")));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.findDtoById(id));
    }

    @PostMapping
    public ResponseEntity<UserResponseDTO> create(@Valid @RequestBody UserRequestDTO userDTO) {
        UserResponseDTO created = userService.create(userDTO);
        return ResponseEntity.created(URI.create("/api/v1/users/" + created.getUserId()))
                .body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> update(@PathVariable Long id, @RequestBody @Valid final UserRequestDTO userDTO) {
        return ResponseEntity.ok(userService.update(id, userDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable final Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/ban")
    public ResponseEntity<UserResponseDTO> toggleBan(@PathVariable Long id) {
        return ResponseEntity.ok(userService.toggleBan(id));
    }

    @GetMapping("/{id}/hours")
    public ResponseEntity<Double> getUserHours(@PathVariable Long id) {
        Double hours = attendanceService.getTotalHoursForUser(id);
        return ResponseEntity.ok(hours);
    }

}
