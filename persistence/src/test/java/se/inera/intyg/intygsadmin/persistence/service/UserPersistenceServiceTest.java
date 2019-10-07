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

package se.inera.intyg.intygsadmin.persistence.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import se.inera.intyg.intygsadmin.persistence.TestContext;
import se.inera.intyg.intygsadmin.persistence.TestSupport;
import se.inera.intyg.intygsadmin.persistence.entity.UserEntity;
import se.inera.intyg.intygsadmin.persistence.enums.IntygsadminRole;
import se.inera.intyg.intygsadmin.persistence.repository.UserRepository;

@TestContext
public class UserPersistenceServiceTest extends TestSupport {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserPersistenceService userPersistenceService;

    private final int total = 10;
    private final int pageSize = 20;

    @BeforeEach
    public void before() {
        userRepository.deleteAll();
        randomizer()
            .objects(UserEntity.class, total)
            .forEach(userPersistenceService::add);
    }

    @Test
    public void findByEmployeeHsaIdTest() {
        Pageable pageable = PageRequest.of(0, pageSize);
        Page<UserEntity> list = userPersistenceService.findAll(pageable);
        assertEquals(total, list.getTotalElements());
        UserEntity userEntityFromList = list.getContent().get(0);

        Optional<UserEntity> byEmployeeHsaId = userPersistenceService.findByEmployeeHsaId(userEntityFromList.getEmployeeHsaId());
        assertTrue(byEmployeeHsaId.isPresent());

        UserEntity userEntity = byEmployeeHsaId.get();
        assertEquals(userEntityFromList.getEmployeeHsaId(), userEntity.getEmployeeHsaId());
        assertEquals(userEntityFromList.getIntygsadminRole(), userEntity.getIntygsadminRole());
        assertEquals(userEntityFromList.getId(), userEntity.getId());
    }

    @Test
    public void deleteTest() {
        Pageable pageable = PageRequest.of(0, pageSize);
        Page<UserEntity> list = userPersistenceService.findAll(pageable);
        assertEquals(total, list.getTotalElements());
        UserEntity userEntity = list.getContent().get(0);

        userPersistenceService.delete(userEntity.getId());
        list = userPersistenceService.findAll(pageable);
        assertEquals(total - 1, list.getTotalElements());

        Optional<UserEntity> byEmployeeHsaId = userPersistenceService.findByEmployeeHsaId(userEntity.getEmployeeHsaId());
        assertTrue(byEmployeeHsaId.isEmpty());
    }

    @Test
    public void upsertTest() {
        Pageable pageable = PageRequest.of(0, pageSize);
        userRepository.deleteAll();

        Page<UserEntity> list = userPersistenceService.findAll(pageable);
        assertEquals(0, list.getTotalElements());

        UserEntity newUser = new UserEntity(null, null, "TEST-HSA-1", "name", IntygsadminRole.FULL);
        userPersistenceService.add(newUser);

        list = userPersistenceService.findAll(pageable);
        assertEquals(1, list.getTotalElements());

        UserEntity newUserEntity = list.getContent().get(0);
        assertEquals(newUser.getEmployeeHsaId(), newUserEntity.getEmployeeHsaId());
        assertEquals(newUser.getIntygsadminRole(), newUserEntity.getIntygsadminRole());
        assertNotNull(newUserEntity.getId());

        newUserEntity.setIntygsadminRole(IntygsadminRole.BAS);
        userPersistenceService.update(newUserEntity);

        list = userPersistenceService.findAll(pageable);
        assertEquals(1, list.getTotalElements());

        UserEntity updatedUserEntity = list.getContent().get(0);
        assertEquals(newUserEntity.getEmployeeHsaId(), updatedUserEntity.getEmployeeHsaId());
        assertEquals(newUserEntity.getIntygsadminRole(), updatedUserEntity.getIntygsadminRole());
        assertEquals(newUserEntity.getId(), updatedUserEntity.getId());

    }

    @Test
    public void findAllTest() {
        Pageable pageable = PageRequest.of(0, pageSize);
        Page<UserEntity> list = userPersistenceService.findAll(pageable);

        assertEquals(total, list.getTotalElements());
    }
}
