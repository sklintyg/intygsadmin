/*
 * Copyright (C) 2020 Inera AB (http://www.inera.se)
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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import se.inera.intyg.infra.intyginfo.dto.IntygInfoEvent;

@Data
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
public class IntygInfoDTO {

    private String intygId;
    private String intygType;
    private String intygVersion;

    private LocalDateTime draftCreated;
    private LocalDateTime receivedDate;
    private LocalDateTime signedDate;
    private LocalDateTime sentToRecipient;
    private LocalDateTime receivedByRecipient;

    private int numberOfRecipients;

    private String signedByName;
    private String signedByHsaId;
    private String signedByEmail;

    private String careUnitName;
    private String careUnitHsaId;

    private String careGiverName;
    private String careGiverHsaId;

    private boolean createdInWC;

    private int kompletteringar;
    private int kompletteringarAnswered;

    private int administrativaFragorSent;
    private int administrativaFragorSentAnswered;

    private int administrativaFragorReceived;
    private int administrativaFragorReceivedAnswered;

    private boolean testCertificate;

    private List<IntygInfoEvent> events;
}
