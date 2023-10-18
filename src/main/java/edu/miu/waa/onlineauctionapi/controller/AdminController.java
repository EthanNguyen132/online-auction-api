package edu.miu.waa.onlineauctionapi.controller;

import edu.miu.waa.onlineauctionapi.common.Constants;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Constants.ADMIN_URL_PREFIX)
public class AdminController {

  @GetMapping("/data")
  @PreAuthorize("hasRole('ADMIN')")
  public String getAdminData() {
    return "Admin data";
  }
}
