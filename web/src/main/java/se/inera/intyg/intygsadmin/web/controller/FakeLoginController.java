/*
 * Copyright (C) 2024 Inera AB (http://www.inera.se)
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

package se.inera.intyg.intygsadmin.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.inera.intyg.intygsadmin.web.controller.dto.FakeLoginDTO;

@RestController
@RequestMapping("/api/fake")
public class FakeLoginController {

  //private final FakeLoginService fakeLoginService;
  //private final ObjectMapper objectMapper;

  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
  public void login(HttpServletRequest request, FakeLoginDTO fakeLoginDTO) {
    //fakeLoginService.login(fakeLoginDTO, request);
  }


  @PostMapping(value = "/logout", consumes = MediaType.APPLICATION_JSON_VALUE)
  public void logout(HttpServletRequest request) {
    //fakeLoginService.logout(request.getSession(false));
  }

}
