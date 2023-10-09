package edu.miu.waa.onlineauctionapi.model;

import edu.miu.waa.onlineauctionapi.security.RoleEnum;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class User {
    private String username;
    private String password;
    private RoleEnum role;

    // helper method to add new role
//    public void addRole(String role)
}
