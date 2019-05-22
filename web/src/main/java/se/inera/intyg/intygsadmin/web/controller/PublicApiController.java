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

package se.inera.intyg.intygsadmin.web.controller;

import java.time.LocalDateTime;
import java.time.ZoneId;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.info.BuildProperties;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import se.inera.intyg.intygsadmin.web.controller.dto.AppConfigDTO;
import se.inera.intyg.intygsadmin.web.controller.dto.VersionInfoDTO;

@RestController
@RequestMapping("/public-api")
public class PublicApiController {

    private BuildProperties buildProperties;
    private Environment environment;

    public PublicApiController(BuildProperties buildProperties, Environment environment) {
        this.buildProperties = buildProperties;
        this.environment = environment;
    }

    @GetMapping(path = "/version", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<VersionInfoDTO> getVersion() {
        var versionInfo = getVersionDTO();

        return ResponseEntity.ok(versionInfo);
    }

    @GetMapping(path = "/appconfig", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppConfigDTO> getAppConfig() {
        var versionInfo = getVersionDTO();
        String loginUrl = "";

        var appConfig = new AppConfigDTO(loginUrl, versionInfo);

        return ResponseEntity.ok(appConfig);
    }

    private VersionInfoDTO getVersionDTO() {
        var applicationName = buildProperties.getArtifact();
        var buildVersion = buildProperties.getVersion();
        var buildTimestamp = LocalDateTime.ofInstant(buildProperties.getTime(), ZoneId.systemDefault());
        var activeProfiles = StringUtils.join(environment.getActiveProfiles(), ", ");

        return new VersionInfoDTO(applicationName, buildVersion, buildTimestamp, activeProfiles);
    }
}
