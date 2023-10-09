package edu.miu.waa.onlineauctionapi.security;

import edu.miu.waa.onlineauctionapi.model.User;
import edu.miu.waa.onlineauctionapi.repository.UserRepository;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomUserService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        if (Strings.isBlank(username)) {
            throw new UsernameNotFoundException("Invalid user.");
        }
        final String uname = username.trim();
        User userFromDB = userRepository.findByUsername(uname);

        if (userFromDB == null) {
            throw new UsernameNotFoundException(String.format("Given user(%s) not found.", username));
        }

//        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
//        for (RoleEnum role : userFromDB.getRoles()) {
//            grantedAuthorities.add(new SimpleGrantedAuthority(role.getAuthority()));
//        }

        return org.springframework.security.core.userdetails.User.builder()
                .username(userFromDB.getUsername())
                .password(userFromDB.getPassword())
                .authorities(new SimpleGrantedAuthority(userFromDB.getRole().getAuthority()))
                .build();
    }

}
