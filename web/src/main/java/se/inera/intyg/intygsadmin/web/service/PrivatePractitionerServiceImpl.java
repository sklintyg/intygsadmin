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

package se.inera.intyg.intygsadmin.web.service;

import static org.springframework.beans.BeanUtils.copyProperties;

import java.io.IOException;
import org.springframework.stereotype.Service;
import se.inera.intyg.intygsadmin.web.controller.dto.PrivatePractitionerDTO;
import se.inera.intyg.intygsadmin.web.integration.ITIntegrationRestService;
import se.inera.intyg.intygsadmin.web.integration.PPIntegrationRestService;
import se.inera.intyg.intygsadmin.web.util.PrivatePractitionerFileWriter;

@Service
public class PrivatePractitionerServiceImpl implements PrivatePractitionerService {

    private final PPIntegrationRestService ppIntegrationRestService;
    private final ITIntegrationRestService itIntegrationRestService;

    public PrivatePractitionerServiceImpl(PPIntegrationRestService ppIntegrationRestService,
        ITIntegrationRestService itIntegrationRestService) {
        this.ppIntegrationRestService = ppIntegrationRestService;
        this.itIntegrationRestService = itIntegrationRestService;
    }

    private static final String YES = "Ja";
    private static final String NO = "Nej";
    private static final String COUNT_FAILURE_MESSAGE = "Uppgift om utfärdade intyg kunde inte hämtas. Prova igen om en stund.";

    @Override
    public PrivatePractitionerDTO getPrivatePractitioner(String personOrHsaId) {
        final var privatePractitioner = ppIntegrationRestService.getPrivatePractitioner(personOrHsaId);

        if (privatePractitioner == null) {
            return null;
        }

        final var certificateCount = itIntegrationRestService.getCertificateCount(privatePractitioner.getHsaId());
        final var hasCertificates = getHasCertificates(certificateCount);
        final var privatePractitionerDTO = new PrivatePractitionerDTO();
        copyProperties(privatePractitioner, privatePractitionerDTO);
        privatePractitionerDTO.setHasCertificates(hasCertificates);

        return privatePractitionerDTO;
    }

    @Override
    public byte[] getPrivatePractitionerFile() throws IOException {
        final var privatePractitionerList = ppIntegrationRestService.getAllPrivatePractitioners();

        if (privatePractitionerList.isEmpty()) {
            return null;
        }

        final var output = new PrivatePractitionerFileWriter().writeExcel(privatePractitionerList);
        return output.toByteArray();
    }

    private String getHasCertificates(Integer certificateCount) {
        if (certificateCount != null) {
            return certificateCount > 0 ? YES : NO;
        } else {
            return COUNT_FAILURE_MESSAGE;
        }
    }
}
