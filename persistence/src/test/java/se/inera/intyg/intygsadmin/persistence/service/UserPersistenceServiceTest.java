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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import se.inera.intyg.intygsadmin.persistence.TestContext;
import se.inera.intyg.intygsadmin.persistence.TestSupport;
import se.inera.intyg.intygsadmin.persistence.entity.UserEntity;
import se.inera.intyg.intygsadmin.persistence.enums.IntygsadminRole;
import se.inera.intyg.intygsadmin.persistence.repository.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestContext
public class UserPersistenceServiceTest extends TestSupport {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserPersistenceService userPersistenceService;

    private final int total = 10;

    @BeforeEach
    public void before() {
        userRepository.deleteAll();
        randomizer()
                .objects(UserEntity.class, total)
                .forEach(userPersistenceService::upsert);
    }

    @Test
    public void findByEmployeeHsaIdTest() {
        List<UserEntity> list = userPersistenceService.findAll();
        assertEquals(total, list.size());
        UserEntity userEntityFromList = list.get(0);

        Optional<UserEntity> byEmployeeHsaId = userPersistenceService.findByEmployeeHsaId(userEntityFromList.getEmployeeHsaId());
        assertTrue(byEmployeeHsaId.isPresent());

        UserEntity userEntity = byEmployeeHsaId.get();
        assertEquals(userEntityFromList.getEmployeeHsaId(), userEntity.getEmployeeHsaId());
        assertEquals(userEntityFromList.getIntygsadminRole(), userEntity.getIntygsadminRole());
        assertEquals(userEntityFromList.getId(), userEntity.getId());
    }

    @Test
    public void deleteTest() {
        List<UserEntity> list = userPersistenceService.findAll();
        assertEquals(total, list.size());
        UserEntity userEntity = list.get(0);

        userPersistenceService.delete(userEntity.getEmployeeHsaId());
        list = userPersistenceService.findAll();
        assertEquals(total - 1, list.size());

        Optional<UserEntity> byEmployeeHsaId = userPersistenceService.findByEmployeeHsaId(userEntity.getEmployeeHsaId());
        assertTrue(byEmployeeHsaId.isEmpty());
    }

    @Test
    public void upsertTest() {
        userRepository.deleteAll();

        List<UserEntity> list = userPersistenceService.findAll();
        assertEquals(0, list.size());

        UserEntity newUser = new UserEntity(null, "TEST-HSA-1", IntygsadminRole.ADMIN);
        userPersistenceService.upsert(newUser);

        list = userPersistenceService.findAll();
        assertEquals(1, list.size());

        UserEntity newUserEntity = list.get(0);
        assertEquals(newUser.getEmployeeHsaId(), newUserEntity.getEmployeeHsaId());
        assertEquals(newUser.getIntygsadminRole(), newUserEntity.getIntygsadminRole());
        assertNotNull(newUserEntity.getId());

        newUserEntity.setIntygsadminRole(IntygsadminRole.BASIC);
        userPersistenceService.upsert(newUserEntity);

        list = userPersistenceService.findAll();
        assertEquals(1, list.size());

        UserEntity updatedUserEntity = list.get(0);
        assertEquals(newUserEntity.getEmployeeHsaId(), updatedUserEntity.getEmployeeHsaId());
        assertEquals(newUserEntity.getIntygsadminRole(), updatedUserEntity.getIntygsadminRole());
        assertEquals(newUserEntity.getId(), updatedUserEntity.getId());

    }

    @Test
    public void findAllTest() {
        List<UserEntity> list = userPersistenceService.findAll();
        assertEquals(total, list.size());
    }
}
