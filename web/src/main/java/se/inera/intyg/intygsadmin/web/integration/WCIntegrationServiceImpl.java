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

package se.inera.intyg.intygsadmin.web.integration;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import se.inera.intyg.infra.intyginfo.dto.WcIntygInfo;
import se.inera.intyg.intygsadmin.web.controller.dto.IntegratedUnitDTO;
import se.inera.intyg.intygsadmin.web.service.WCIntegrationService;

@Profile("!wc-unit-stub")
@Service
public class WCIntegrationServiceImpl implements WCIntegrationService {

    @Bean("wcRestTemplate")
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Autowired
    @Qualifier("wcRestTemplate")
    private RestTemplate restTemplate;

    @Value("${webcert.internalapi}")
    private String webcertUrl;

    @Override
    public IntegratedUnitDTO getIntegratedUnit(String hsaId) {
        String url = webcertUrl + "/internalapi/integratedUnits/" + hsaId;

        return restTemplate.getForObject(url, IntegratedUnitDTO.class);
    }

    @Override
    public List<IntegratedUnitDTO> getAllIntegratedUnits() {
        String url = webcertUrl + "/internalapi/integratedUnits/all";

        IntegratedUnitDTO[] integratedUnitDTOArray = restTemplate.getForObject(url, IntegratedUnitDTO[].class);

        if (integratedUnitDTOArray == null) {
            return Collections.emptyList();
        }
        return Arrays.asList(integratedUnitDTOArray);
    }

    @Override
    public WcIntygInfo getIntygInfo(String intygId) {
        String url = webcertUrl + "/internalapi/intygInfo/" + intygId;

        return restTemplate.getForObject(url, WcIntygInfo.class);
    }

}
