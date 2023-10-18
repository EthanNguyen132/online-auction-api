package edu.miu.waa.onlineauctionapi.controller;

import static edu.miu.waa.onlineauctionapi.common.Constants.*;

import edu.miu.waa.onlineauctionapi.dto.LoginRequest;
import edu.miu.waa.onlineauctionapi.dto.RegistrationRequest;
import edu.miu.waa.onlineauctionapi.dto.TokenResponse;
import edu.miu.waa.onlineauctionapi.model.User;
import edu.miu.waa.onlineauctionapi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

  private final UserService userService;
  private final PasswordEncoder passwordEncoder;

  @PostMapping(TOKEN_URL)
  public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest loginRequest) {
    User userEntity = userService.findUser(loginRequest.getEmail());
    if (passwordEncoder.matches(loginRequest.getPassword(), userEntity.getPassword())) {
      return ResponseEntity.ok(userService.createTokenResponse(userEntity));
    }
    throw new InsufficientAuthenticationException("Unauthorized.");
  }

  @PostMapping(SIGNUP_URL)
  public ResponseEntity<TokenResponse> register(@RequestBody RegistrationRequest reg) {
    return ResponseEntity.ok(userService.registerNormalUser(reg));
  }

  @PostMapping(SIGNUP_ADMIN_URL)
  public ResponseEntity<TokenResponse> registerAdmin(@RequestBody RegistrationRequest reg) {
    return ResponseEntity.ok(userService.registerAdmin(reg));
  }
}
