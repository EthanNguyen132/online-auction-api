package edu.miu.waa.onlineauctionapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import edu.miu.waa.onlineauctionapi.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

//    public User2 findByUsername(String username) {
////        return new User("admin", "admin"); // test user before having JPA & passwordEncoder
//        // encrypted with bcrypt
//        return new User2("admin", "$2y$10$AXcmBDpkI570ZzTTahuuyujfao/ll1BAEobHEbC9ec/wyQzmrr1Dq",
//                RoleEnum.ADMIN);
//    }

    User findByEmail(String email);

    Integer countUserByEmail(String email);
}
