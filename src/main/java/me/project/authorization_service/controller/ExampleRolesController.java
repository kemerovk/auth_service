package me.project.authorization_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("example")
public class ExampleRolesController {

    @GetMapping("base-info")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_PREMIUM_USER', 'ROLE_GUEST')")
    public String getInfoForAll() {
        return "Everybody can see it";
    }

    @GetMapping("not-guest")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_PREMIUM_USER')")
    public String getInfoForNotGuest() {
        return "Admins and premium users can see it";
    }

    @GetMapping("admin-only")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public String getInfoForAdminOnly() {
        return "Only admins can see it";
    }

}
