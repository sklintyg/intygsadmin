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

package se.inera.intyg.intygsadmin.web.auth.fake;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static se.inera.intyg.intygsadmin.web.auth.AuthenticationConstansts.FAKE_PROFILE;

@Profile(FAKE_PROFILE)
@RestController
@RequestMapping(FakeApiController.FAKE_API_REQUEST_MAPPING)
public class FakeApiController {

    public static final String FAKE_API_REQUEST_MAPPING = "/fake-api";

    private List<FakeUser> fakeUsers;

    @PostConstruct
    public void init() throws IOException {

        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = resolver.getResources("classpath:bootstrap/users/*.json");

        fakeUsers = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        for (Resource resource : resources) {
            try (InputStream jsonUserStream = resource.getInputStream()) {
                fakeUsers.add(objectMapper.readValue(jsonUserStream, FakeUser.class));
            }
        }

    }

    @GetMapping(path = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<FakeUser>> getFakeUsers() {

        return ResponseEntity.ok(fakeUsers);
    }
}
