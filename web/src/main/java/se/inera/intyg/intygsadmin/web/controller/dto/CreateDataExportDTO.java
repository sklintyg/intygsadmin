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
package se.inera.intyg.intygsadmin.web.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateDataExportDTO {

    @Schema(description = "Care provider ID", requiredMode = RequiredMode.REQUIRED)
    private String hsaId;

    @Schema(description = "Organization ID", requiredMode = RequiredMode.REQUIRED)
    private String organizationNumber;

    @Schema(description = "Personal id of the receiving person", requiredMode = RequiredMode.REQUIRED)
    private String personId;

    @Schema(description = "Email address for the receiving person", requiredMode = RequiredMode.REQUIRED)
    private String emailAddress;

    @Schema(description = "Phone number to the receiving person", requiredMode = RequiredMode.REQUIRED)
    private String phoneNumber;

}
