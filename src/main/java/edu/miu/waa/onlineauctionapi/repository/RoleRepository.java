package edu.miu.waa.onlineauctionapi.repository;

import edu.miu.waa.onlineauctionapi.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {}
