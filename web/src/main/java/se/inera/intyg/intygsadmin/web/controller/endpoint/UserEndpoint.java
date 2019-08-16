/*
 * Copyright (C) 2019 Inera AB (http://www.inera.se)
 *
 * This file is part of sklintyg (https://github.com/sklintyg).
 *
 * sklintyg is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * sklintyg is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package se.inera.intyg.intygsadmin.web.controller.endpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.web.annotation.RestControllerEndpoint;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import se.inera.intyg.intygsadmin.web.controller.dto.UserEntityDTO;
import se.inera.intyg.intygsadmin.web.service.UserService;

import java.util.List;

@Component
@RestControllerEndpoint(id = "user")
public class UserEndpoint {

    private UserService userService;

    @Autowired
    public UserEndpoint(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<UserEntityDTO>> getUsers() {
        return ResponseEntity.ok(userService.getUsers());
    }

    @GetMapping("/{employeeHsaId}")
    public ResponseEntity<UserEntityDTO> getUser(@PathVariable String employeeHsaId) {
        return ResponseEntity.ok(userService.getUser(employeeHsaId));
    }

    @PostMapping
    public ResponseEntity<UserEntityDTO> upsertUser(@RequestBody UserEntityDTO userEntityDTO) {
        return ResponseEntity.ok(userService.upsertUser(userEntityDTO));
    }

    @DeleteMapping("/{employeeHsaId}")
    @ResponseStatus(value = HttpStatus.OK)
    public void deleteUser(@PathVariable String employeeHsaId) {
        userService.deleteUser(employeeHsaId);
    }

}
