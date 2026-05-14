package com.uni.impact.security;

import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.uni.impact.user.dto.UserRequestDTO;
import com.uni.impact.user.dto.UserResponseDTO;
import com.uni.impact.user.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor
public class KeycloakUserSyncFilter extends OncePerRequestFilter {
    private final UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();
        if (isPermittedEndpoint(path)) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = request.getHeader("Authorization");
        if (token == null ) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Invalid or missing token");
            return;
        }

        UserRequestDTO userDTO = getUserDetails(token);
        if(userDTO != null)
        {
            UserResponseDTO user;
            if (userService.findUserByEmail(userDTO.getEmail()).isEmpty()) {
                user = userService.create(userDTO);
                log.info("User registered successfully");
            } else {
                user = userService.findDtoByEmail(userDTO.getEmail());
                log.info("User already exists, skipping sync");
            }


            request = new CustomHeaderRequestWrapper(request, "X-User-ID", user.getUserId().toString());//this contains user id in database not keycloak id
        }

        filterChain.doFilter(request, response);
    }

    private boolean isPermittedEndpoint(String path) {
        return path.startsWith("/swagger-ui/") || path.equals("/swagger-ui") ||
               path.startsWith("/v3/api-docs/") || path.equals("/v3/api-docs")||
               path.startsWith("/webhook/");
    }

    private UserRequestDTO getUserDetails(String token) {
        if (token == null) return null;

        try {
            String tokenWithoutBearer = token.replace("Bearer ", "").trim();
            SignedJWT signedJWT = SignedJWT.parse(tokenWithoutBearer);
            JWTClaimsSet claims = signedJWT.getJWTClaimsSet();

            UserRequestDTO userDTO = new UserRequestDTO();
            userDTO.setEmail(claims.getStringClaim("email"));
            userDTO.setKeycloakId(claims.getStringClaim("sub"));
            userDTO.setFirstName(claims.getStringClaim("given_name"));
            userDTO.setLastName(claims.getStringClaim("family_name"));
            if (userDTO.getEmail() == null || userDTO.getFirstName() == null || userDTO.getLastName() == null) {
                log.debug("Token missing user claims (email/given_name/family_name) - skipping user sync");
                return null;
            }
            return userDTO;
        } catch (Exception e) {
            log.error("Error parsing JWT token", e);
            return null;
        }
    }
}
