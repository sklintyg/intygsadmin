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
package se.inera.intyg.intygsadmin.persistence.service;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import se.inera.intyg.intygsadmin.persistence.entity.IntygInfoEntity;
import se.inera.intyg.intygsadmin.persistence.repository.IntygInfoRepository;
import se.inera.intyg.intygsadmin.persistence.util.PersistenceUtil;

@Service
@Transactional
public class IntygInfoPersistenceService {

    private IntygInfoRepository intygInfoRepository;

    public IntygInfoPersistenceService(IntygInfoRepository intygInfoRepository) {
        this.intygInfoRepository = intygInfoRepository;
    }

    public Page<IntygInfoEntity> findAll(Pageable pageable) {
        return intygInfoRepository.findAll(PersistenceUtil.alwaysTrue(), pageable);
    }

    public IntygInfoEntity create(IntygInfoEntity intygInfoEntity) {
        intygInfoEntity.setId(null);
        return intygInfoRepository.save(intygInfoEntity);
    }
}
