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

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import se.inera.intyg.intygsadmin.persistence.entity.UserEntity;
import se.inera.intyg.intygsadmin.persistence.enums.IntygsadminRole;
import se.inera.intyg.intygsadmin.persistence.service.UserPersistenceService;
import se.inera.intyg.intygsadmin.web.WithMockIntygsadminUser;
import se.inera.intyg.intygsadmin.web.auth.IntygsadminUser;
import se.inera.intyg.intygsadmin.web.controller.dto.UserEntityDTO;
import se.inera.intyg.intygsadmin.web.mapper.UserMapper;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith({ MockitoExtension.class, SpringExtension.class })
@ContextConfiguration
public class UserServiceTest {

    @Spy
    private UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    @Mock
    private UserPersistenceService userPersistenceService;

    @InjectMocks
    private UserService userService;

    @Test
    @WithMockIntygsadminUser
    public void testGetActiveUser_found() throws NoSuchMethodException {
        IntygsadminUser activeUser = userService.getActiveUser();

        assertNotNull(activeUser);
        assertEquals(WithMockIntygsadminUser.class.getMethod("employeeHsaId").getDefaultValue(), activeUser.getEmployeeHsaId());
        assertEquals(WithMockIntygsadminUser.class.getMethod("intygsadminRole").getDefaultValue(), activeUser.getIntygsadminRole().name());
        assertEquals(WithMockIntygsadminUser.class.getMethod("name").getDefaultValue(), activeUser.getName());
    }

    @Test
    public void testGetActiveUser_notFound() {
        assertNull(userService.getActiveUser());
    }

    @Test
    public void testGetUsers() {

        UserEntity ue1 = new UserEntity(UUID.randomUUID(), "HSA1", IntygsadminRole.BASIC);
        UserEntity ue2 = new UserEntity(UUID.randomUUID(), "HSA2", IntygsadminRole.ADMIN);
        List<UserEntity> userEntities = List.of(ue1, ue2);

        when(userPersistenceService.findAll()).thenReturn(userEntities);

        List<UserEntityDTO> users = userService.getUsers();

        verify(userPersistenceService, times(1)).findAll();

        assertNotNull(users);
        assertEquals(2, users.size());
    }

    @Test
    public void testGetUser_found() {
        UserEntity ue1 = new UserEntity(UUID.randomUUID(), "HSA1", IntygsadminRole.BASIC);

        when(userPersistenceService.findByEmployeeHsaId(any(String.class))).thenReturn(Optional.of(ue1));

        UserEntityDTO userEntityDTO = userService.getUser(ue1.getEmployeeHsaId());

        verify(userPersistenceService, times(1)).findByEmployeeHsaId(ue1.getEmployeeHsaId());
        assertNotNull(userEntityDTO);
        assertEquals(ue1.getId().toString(), userEntityDTO.getId());
        assertEquals(ue1.getEmployeeHsaId(), userEntityDTO.getEmployeeHsaId());
        assertEquals(ue1.getIntygsadminRole().name(), userEntityDTO.getIntygsadminRole());

    }

    @Test
    public void testGetUser_notFound() {
        UserEntity ue1 = new UserEntity(UUID.randomUUID(), "HSA1", IntygsadminRole.BASIC);

        when(userPersistenceService.findByEmployeeHsaId(any(String.class))).thenReturn(Optional.empty());

        UserEntityDTO userEntityDTO = userService.getUser(ue1.getEmployeeHsaId());

        verify(userPersistenceService, times(1)).findByEmployeeHsaId(ue1.getEmployeeHsaId());
        assertNull(userEntityDTO);
    }

    @Test
    public void testDeleteUser() {

        String dummy_hsa_id = "DUMMY_HSA_ID";
        userService.deleteUser(dummy_hsa_id);
        verify(userPersistenceService, times(1)).delete(dummy_hsa_id);
    }

    @Test
    public void testUpsertUser() {
        UserEntity ue1 = new UserEntity(UUID.randomUUID(), "HSA1", IntygsadminRole.BASIC);

        when(userPersistenceService.upsert(any(UserEntity.class))).thenReturn(ue1);

        UserEntityDTO userEntityDTO = userService.upsertUser(userMapper.toDTO(ue1));

        verify(userPersistenceService, times(1)).upsert(any(UserEntity.class));
        assertNotNull(userEntityDTO);
        assertEquals(ue1.getId().toString(), userEntityDTO.getId());
        assertEquals(ue1.getEmployeeHsaId(), userEntityDTO.getEmployeeHsaId());
        assertEquals(ue1.getIntygsadminRole().name(), userEntityDTO.getIntygsadminRole());

    }
}
