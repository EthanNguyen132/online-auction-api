package edu.miu.waa.onlineauctionapi.service;

import edu.miu.waa.onlineauctionapi.dto.TokenResponse;
import edu.miu.waa.onlineauctionapi.model.User;
import edu.miu.waa.onlineauctionapi.repository.UserRepository;
import edu.miu.waa.onlineauctionapi.security.JwtTokenManager;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;

import static edu.miu.waa.onlineauctionapi.common.Constants.EXPIRATION_TIME;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JwtTokenManager jwtTokenManager;

    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public TokenResponse createTokenResponse(User user) {
        final long now = System.currentTimeMillis();
        String token =
                jwtTokenManager.create(
                        org.springframework.security.core.userdetails.User.builder()
                                .username(user.getUsername())
                                .password(user.getPassword())
                                .authorities(
                                        Objects.nonNull(user.getRole()) ? user.getRole().name() : "")
                                        // return user role name in subject, i.e. ADMIN/SELLER
                                .build(),
                        new Date(now),
                        new Date(now + EXPIRATION_TIME)
                        );
        return TokenResponse.builder()
                .accessToken(token)
                .expiresIn(EXPIRATION_TIME)
                .build();
    }
}
