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

package se.inera.intyg.intygsadmin.web.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import se.inera.intyg.infra.intyginfo.dto.IntygInfoEvent;
import se.inera.intyg.infra.intyginfo.dto.ItIntygInfo;
import se.inera.intyg.infra.intyginfo.dto.WcIntygInfo;
import se.inera.intyg.intygsadmin.persistence.entity.IntygInfoEntity;
import se.inera.intyg.intygsadmin.persistence.service.IntygInfoPersistenceService;
import se.inera.intyg.intygsadmin.web.controller.dto.IntygInfoDTO;
import se.inera.intyg.intygsadmin.web.controller.dto.IntygInfoListDTO;
import se.inera.intyg.intygsadmin.web.integration.ITIntegrationService;
import se.inera.intyg.intygsadmin.web.integration.WCIntegrationService;
import se.inera.intyg.intygsadmin.web.mapper.IntygInfoMapper;

@Service
public class IntygInfoService {

    private static final Logger LOG = LoggerFactory.getLogger(IntygInfoService.class);

    private IntygInfoPersistenceService intygInfoPersistenceService;
    private IntygInfoMapper intygInfoMapper;
    private UserService userService;
    private ITIntegrationService itIntegrationService;
    private WCIntegrationService wcIntegrationService;

    public IntygInfoService(IntygInfoPersistenceService intygInfoPersistenceService, IntygInfoMapper intygInfoMapper,
        UserService userService, ITIntegrationService itIntegrationService, WCIntegrationService wcIntegrationService) {
        this.intygInfoPersistenceService = intygInfoPersistenceService;
        this.intygInfoMapper = intygInfoMapper;
        this.userService = userService;
        this.itIntegrationService = itIntegrationService;
        this.wcIntegrationService = wcIntegrationService;
    }

    public Page<IntygInfoListDTO> getIntygInfoList(Pageable pageable) {
        Page<IntygInfoEntity> intygInfoEntities = intygInfoPersistenceService.findAll(pageable);

        List<IntygInfoListDTO> mapIntygInfo = intygInfoMapper.toListDTO(intygInfoEntities.getContent());

        return new PageImpl<>(mapIntygInfo, pageable, intygInfoEntities.getTotalElements());
    }

    public Optional<IntygInfoDTO> getIntygInfo(String intygId) {
        IntygInfoDTO info = new IntygInfoDTO();
        info.setEvents(new ArrayList<>());

        getInfoFromIT(intygId, info);
        List<IntygInfoEvent> events = new ArrayList<>(info.getEvents());

        getInfoFromWC(intygId, info, events);

        // Intyg not found
        if (info.getIntygId() == null) {
            return Optional.empty();
        }

        storeLog(intygId, info.getCareUnitHsaId(), info.getCareGiverHsaId());

        events.sort(Comparator.comparing(IntygInfoEvent::getDate, Comparator.nullsFirst(Comparator.naturalOrder())).reversed());
        info.setEvents(events);

        return Optional.of(info);
    }

    private void getInfoFromIT(String intygId, IntygInfoDTO intygInfo) {
        ItIntygInfo itIntygInfoDTO = null;
        try {
            itIntygInfoDTO = itIntegrationService.getIntygInfo(intygId);
        } catch (Exception e) {
            LOG.error("Error while querying IT for intyg '" + intygId + "'", e);
        }

        if (itIntygInfoDTO != null) {
            intygInfoMapper.updateInfoFromIT(itIntygInfoDTO, intygInfo);
        }
    }

    private void getInfoFromWC(String intygId, IntygInfoDTO intygInfo, List<IntygInfoEvent> events) {
        WcIntygInfo wcIntygInfoDTO = null;
        try {
            wcIntygInfoDTO = wcIntegrationService.getIntygInfo(intygId);
        } catch (Exception e) {
            LOG.error("Error while querying WC for intyg '" + intygId + "'", e);
        }

        if (wcIntygInfoDTO != null) {
            intygInfoMapper.updateInfoFromWC(wcIntygInfoDTO, intygInfo);
            events.addAll(intygInfo.getEvents());
        }
    }

    private void storeLog(String intygId, String enhetsId, String vardgivarId) {
        IntygInfoEntity intygInfoEntity = new IntygInfoEntity();
        intygInfoEntity.setIntygId(intygId);
        intygInfoEntity.setEmployeeHsaId(userService.getActiveUser().getEmployeeHsaId());
        intygInfoEntity.setEmployeeName(userService.getActiveUser().getName());
        intygInfoEntity.setEnhetsId(enhetsId);
        intygInfoEntity.setVardgivarId(vardgivarId);

        intygInfoPersistenceService.create(intygInfoEntity);
    }
}
