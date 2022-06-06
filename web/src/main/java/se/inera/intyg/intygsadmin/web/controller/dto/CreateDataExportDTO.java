/*
 * Copyright (C) 2022 Inera AB (http://www.inera.se)
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

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateDataExportDTO {

    @ApiModelProperty(notes = "Care provider ID", required = true)
    private String hsaId;

    @ApiModelProperty(notes = "Organization ID", required = true)
    private String organizationNumber;

    @ApiModelProperty(notes = "Personal id of the receiving person", required = true)
    private String personId;

    @ApiModelProperty(notes = "Email address for the receiving person", required = true)
    private String emailAddress;

    @ApiModelProperty(notes = "Phone number to the receiving person", required = true)
    private String phoneNumber;

}
