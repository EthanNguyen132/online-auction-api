package edu.miu.waa.onlineauctionapi.controller;

import edu.miu.waa.onlineauctionapi.dto.LoginRequest;
import edu.miu.waa.onlineauctionapi.dto.TokenResponse;
import edu.miu.waa.onlineauctionapi.model.User;
import edu.miu.waa.onlineauctionapi.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static edu.miu.waa.onlineauctionapi.common.Constants.TOKEN_URL;

@RestController
@AllArgsConstructor
public class AuthController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping(TOKEN_URL)
    public ResponseEntity<TokenResponse> signIn(@RequestBody LoginRequest loginRequest) {
        User userEntity = userService.findUserByUsername(loginRequest.getUsername());
        if (passwordEncoder.matches(loginRequest.getPassword(), userEntity.getPassword())) {
            return ResponseEntity.ok(userService.createTokenResponse(userEntity));
        }
        throw new InsufficientAuthenticationException("Unauthorized.");
    }
}
