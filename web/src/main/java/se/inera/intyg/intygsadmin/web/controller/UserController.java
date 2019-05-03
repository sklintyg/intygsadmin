package se.inera.intyg.intygsadmin.web.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import se.inera.intyg.intygsadmin.web.controller.dto.UserResponseDTO;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @GetMapping
    public ResponseEntity<UserResponseDTO> getUser() {
        return null;
    }
}
