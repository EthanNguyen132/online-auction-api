package edu.miu.waa.onlineauctionapi.service;

import edu.miu.waa.onlineauctionapi.dto.RegistrationRequest;
import edu.miu.waa.onlineauctionapi.dto.TokenResponse;
import edu.miu.waa.onlineauctionapi.exception.GenericAlreadyExistsException;
import edu.miu.waa.onlineauctionapi.model.Role;
import edu.miu.waa.onlineauctionapi.model.User;
import edu.miu.waa.onlineauctionapi.model.UserRole;
import edu.miu.waa.onlineauctionapi.repository.RoleRepository;
import edu.miu.waa.onlineauctionapi.repository.UserRepository;
import edu.miu.waa.onlineauctionapi.security.JwtTokenManager;
import edu.miu.waa.onlineauctionapi.security.RoleEnum;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final JwtTokenManager jwtTokenManager;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.security.jwt.expire.duration}")
    private long EXPIRE_DURATION;

    public User findUser(String email) {
        return userRepository.findByEmail(email);
    }

    public TokenResponse createTokenResponse(User user) {
        final long now = System.currentTimeMillis();
        String token = jwtTokenManager.create(user, new Date(now), new Date(now + EXPIRE_DURATION));
        return TokenResponse.builder().accessToken(token).expiresIn(EXPIRE_DURATION).build();
    }

    @Transactional
    public TokenResponse registerNormalUser(RegistrationRequest reg) {
        Integer count = userRepository.countUserByEmail(reg.getEmail());
        if (count > 0) {
            throw new GenericAlreadyExistsException("This email has been used");
        }

        User user = toEntity(reg); // this can only copy basic props

        // TODO validate input role first
        // role name from client: ROLE_SELLER, ROLE_CUSTOMER only
        Role role = new Role(reg.getRole());
        roleRepository.save(role);

        UserRole ur = new UserRole(user, role);
        user.addUserRole(ur);
        User newUser = userRepository.save(user);

        // temporarily create token, have to send email with verification code first
        return this.createTokenResponse(newUser);
    }

    @Transactional
    public TokenResponse registerAdmin(RegistrationRequest reg) {
        Integer count = userRepository.countUserByEmail(reg.getEmail());
        if (count > 0) {
            throw new GenericAlreadyExistsException("This email has been used");
        }

        User user = toEntity(reg); // this can only copy basic props
        // TODO validate input role first
        // accept ROLE_ADMIN only
        Role role = new Role(RoleEnum.ADMIN);
        roleRepository.save(role);

        UserRole ur = new UserRole(user, role);
        user.addUserRole(ur);
        User newUser = userRepository.save(user);

        return this.createTokenResponse(newUser);
    }

    private User toEntity(RegistrationRequest reg) {
        User user = new User();
        BeanUtils.copyProperties(reg, user);
        user.setPassword(passwordEncoder.encode(reg.getPassword()));
        return user;
    }
}
