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

package se.inera.intyg.intygsadmin.web.service;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import se.inera.intyg.intygsadmin.persistence.entity.IntygInfoEntity;
import se.inera.intyg.intygsadmin.persistence.service.IntygInfoPersistenceService;
import se.inera.intyg.intygsadmin.web.controller.dto.IntygInfoDTO;
import se.inera.intyg.intygsadmin.web.controller.dto.IntygInfoListDTO;
import se.inera.intyg.intygsadmin.web.mapper.IntygInfoMapper;

@Service
public class IntygInfoService {

    private IntygInfoPersistenceService intygInfoPersistenceService;
    private IntygInfoMapper intygInfoMapper;
    private UserService userService;

    public IntygInfoService(IntygInfoPersistenceService intygInfoPersistenceService, IntygInfoMapper intygInfoMapper,
        UserService userService) {
        this.intygInfoPersistenceService = intygInfoPersistenceService;
        this.intygInfoMapper = intygInfoMapper;
        this.userService = userService;
    }


    public Page<IntygInfoListDTO> getIntygInfoList(Pageable pageable) {
        Page<IntygInfoEntity> intygInfoEntities = intygInfoPersistenceService.findAll(pageable);

        List<IntygInfoListDTO> mapBanners = intygInfoMapper.toListDTO(intygInfoEntities.getContent());

        return new PageImpl<>(mapBanners, pageable, intygInfoEntities.getTotalElements());
    }

    public IntygInfoDTO getIntygInfo(String intygId) {
        storeLog(intygId);

        // Temp code
        if (intygId.equals("9ae0c3b4-3d80-46f3-acde-b332970ba0ea")) {
            return null;
        }

        return new IntygInfoDTO(intygId);
    }

    private void storeLog(String intygId) {
        IntygInfoEntity intygInfoEntity = new IntygInfoEntity();
        intygInfoEntity.setIntygId(intygId);
        intygInfoEntity.setEmployeeHsaId(userService.getActiveUser().getEmployeeHsaId());
        intygInfoEntity.setEmployeeName(userService.getActiveUser().getName());

        intygInfoPersistenceService.create(intygInfoEntity);
    }
}
