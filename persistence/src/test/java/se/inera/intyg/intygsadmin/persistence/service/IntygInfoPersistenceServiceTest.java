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

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import se.inera.intyg.intygsadmin.persistence.TestContext;
import se.inera.intyg.intygsadmin.persistence.TestSupport;
import se.inera.intyg.intygsadmin.persistence.entity.IntygInfoEntity;
import se.inera.intyg.intygsadmin.persistence.repository.IntygInfoRepository;

@TestContext
public class IntygInfoPersistenceServiceTest extends TestSupport {

    @Autowired
    private IntygInfoRepository intygInfoRepository;

    @Autowired
    private IntygInfoPersistenceService intygInfoPersistenceService;

    private final int total = 10;
    private final int pageSize = 20;

    @BeforeEach
    public void before() {
        intygInfoRepository.deleteAll();
        randomizer()
            .objects(IntygInfoEntity.class, total)
            .forEach(intygInfoPersistenceService::create);
    }

    @Test
    public void findAllTest() {
        Pageable pageable = PageRequest.of(0, pageSize);
        Page<IntygInfoEntity> list = intygInfoPersistenceService.findAll(pageable);

        assertEquals(total, list.getTotalElements());
    }
}
