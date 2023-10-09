package edu.miu.waa.onlineauctionapi.repository;

import edu.miu.waa.onlineauctionapi.security.RoleEnum;
import edu.miu.waa.onlineauctionapi.model.User;
import org.springframework.stereotype.Repository;

import java.util.Arrays;

@Repository
public class UserRepository {

    public User findByUsername(String username) {
//        return new User("admin", "admin"); // test user before having JPA & passwordEncoder
        // encrypted with bcrypt
        return new User("admin", "$2y$10$AXcmBDpkI570ZzTTahuuyujfao/ll1BAEobHEbC9ec/wyQzmrr1Dq",
                RoleEnum.ADMIN);
    }
}
