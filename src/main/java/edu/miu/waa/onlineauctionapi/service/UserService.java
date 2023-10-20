package edu.miu.waa.onlineauctionapi.service;

import edu.miu.waa.onlineauctionapi.dto.RegistrationRequest;
import edu.miu.waa.onlineauctionapi.dto.TokenResponse;
import edu.miu.waa.onlineauctionapi.model.User;

public interface UserService {

  public User findUser(String email);

  public TokenResponse registerNormalUser(RegistrationRequest reg);

  public TokenResponse registerAdmin(RegistrationRequest reg);

  public TokenResponse createTokenResponse(User user);
}
