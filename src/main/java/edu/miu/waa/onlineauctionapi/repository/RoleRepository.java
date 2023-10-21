package edu.miu.waa.onlineauctionapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import edu.miu.waa.onlineauctionapi.model.Role;
import edu.miu.waa.onlineauctionapi.security.RoleEnum;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    
    Role findByRole(RoleEnum roleEnum);
}
