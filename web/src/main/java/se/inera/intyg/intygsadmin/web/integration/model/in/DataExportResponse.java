/*
 * Copyright (C) 2025 Inera AB (http://www.inera.se)
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
package se.inera.intyg.intygsadmin.web.integration.model.in;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class DataExportResponse {

    @Schema(name = "Export ID", requiredMode = RequiredMode.REQUIRED)
    private UUID terminationId;

    //JSON formating for JAVA 8 types
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(name = "Date when the request was first created", requiredMode = RequiredMode.REQUIRED)
    private LocalDateTime created;

    @Schema(name = "Current status of the export", requiredMode = RequiredMode.REQUIRED)
    private String status;

    @Schema(name = "The name of the administrator who created the request.", requiredMode = RequiredMode.REQUIRED)
    private String creatorName;

    @Schema(name = "The HSAiD of the administrator who created the request.", requiredMode = RequiredMode.REQUIRED)
    private String creatorHSAId;

    @Schema(name = "Care provider ID", requiredMode = RequiredMode.REQUIRED)
    private String hsaId;

    @Schema(name = "Organization ID", requiredMode = RequiredMode.REQUIRED)
    private String organizationNumber;

    @Schema(name = "Personal id of the receiving person", requiredMode = RequiredMode.REQUIRED)
    private String personId;

    @Schema(name = "Email address for the receiving person", requiredMode = RequiredMode.REQUIRED)
    private String emailAddress;

    @Schema(name = "Phone number to the receiving person", requiredMode = RequiredMode.REQUIRED)
    private String phoneNumber;

}
