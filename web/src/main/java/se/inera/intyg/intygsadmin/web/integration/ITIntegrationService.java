package se.inera.intyg.intygsadmin.web.integration;

import se.inera.intyg.infra.intyginfo.dto.ItIntygInfo;

public interface ITIntegrationService {

    ItIntygInfo getIntygInfo(String intygId);
}
