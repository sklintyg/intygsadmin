/*
 * Copyright (C) 2024 Inera AB (http://www.inera.se)
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import se.inera.intyg.intygsadmin.persistence.entity.UserEntity;
import se.inera.intyg.intygsadmin.persistence.enums.IntygsadminRole;
import se.inera.intyg.intygsadmin.persistence.service.UserPersistenceService;
import se.inera.intyg.intygsadmin.web.WithMockIntygsadminUser;
import se.inera.intyg.intygsadmin.web.auth.IntygsadminUser;
import se.inera.intyg.intygsadmin.web.controller.dto.UserDTO;
import se.inera.intyg.intygsadmin.web.exception.IaErrorCode;
import se.inera.intyg.intygsadmin.web.exception.IaServiceException;
import se.inera.intyg.intygsadmin.web.mapper.UserMapper;

@ExtendWith({MockitoExtension.class, SpringExtension.class})
@ContextConfiguration
public class UserServiceImplTest {

    @Spy
    private UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    @Mock
    private UserPersistenceService userPersistenceService;

    @InjectMocks
    private UserServiceImpl userService;

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
        Pageable pageable = PageRequest.of(0, 10);

        UserEntity ue1 = new UserEntity(UUID.randomUUID(), LocalDateTime.now(), "HSA1", "name1", IntygsadminRole.BAS);
        UserEntity ue2 = new UserEntity(UUID.randomUUID(), LocalDateTime.now(), "HSA2", "name2", IntygsadminRole.FULL);
        List<UserEntity> userEntities = List.of(ue1, ue2);

        Page<UserEntity> persistenceResult = new PageImpl<>(userEntities, pageable, userEntities.size());
        when(userPersistenceService.findAll(any(Pageable.class))).thenReturn(persistenceResult);

        Page<UserDTO> users = userService.getUsers(pageable);

        verify(userPersistenceService, times(1)).findAll(eq(pageable));
        assertEquals(2, users.getTotalElements());
    }

    @Test
    @WithMockIntygsadminUser
    public void testDeleteUser() {
        UUID id = UUID.randomUUID();

        userService.deleteUser(id);
        verify(userPersistenceService, times(1)).delete(id);
    }

    @Test
    @WithMockIntygsadminUser
    public void testDeleteUser_currentUser() {
        UUID id = userService.getActiveUser().getId();

        try {
            userService.deleteUser(id);
            fail();
        } catch (IaServiceException e) {
            assertEquals(IaErrorCode.BAD_STATE, e.getErrorCode());
        }

        verify(userPersistenceService, times(0)).delete(id);
    }

    @Test
    @WithMockIntygsadminUser
    public void testUpdateUser() {
        UUID id = UUID.randomUUID();
        UserDTO userDTO = new UserDTO();
        userDTO.setId(id);
        userDTO.setName("name");
        userDTO.setEmployeeHsaId("HSA-1");
        userDTO.setIntygsadminRole(IntygsadminRole.FULL);

        UserEntity entity = new UserEntity();
        entity.setId(id);

        Optional<UserEntity> userEntity = Optional.of(entity);

        when(userPersistenceService.findByEmployeeHsaId(userDTO.getEmployeeHsaId())).thenReturn(userEntity);

        userService.updateUser(userDTO);
        verify(userPersistenceService, times(1)).update(any());
    }

    @Test
    @WithMockIntygsadminUser
    public void testUpdateUser_currentUser() {
        UUID id = userService.getActiveUser().getId();
        UserDTO userDTO = new UserDTO();
        userDTO.setId(id);
        userDTO.setName("name");
        userDTO.setEmployeeHsaId("HSA-1");
        userDTO.setIntygsadminRole(IntygsadminRole.BAS);

        try {
            userService.updateUser(userDTO);
            fail();
        } catch (IaServiceException e) {
            assertEquals(IaErrorCode.BAD_STATE, e.getErrorCode());
        }

        verify(userPersistenceService, times(0)).update(any());
    }

    @Test
    @WithMockIntygsadminUser
    public void testUpdateUser_hsaExists() {
        UUID id = UUID.randomUUID();

        UserDTO userDTO = new UserDTO();
        userDTO.setId(id);
        userDTO.setName("name");
        userDTO.setEmployeeHsaId("HSA-1");
        userDTO.setIntygsadminRole(IntygsadminRole.FULL);

        UserEntity entity = new UserEntity();
        entity.setId(UUID.randomUUID());

        Optional<UserEntity> userEntity = Optional.of(entity);

        when(userPersistenceService.findByEmployeeHsaId(userDTO.getEmployeeHsaId())).thenReturn(userEntity);

        try {
            userService.updateUser(userDTO);
            fail();
        } catch (IaServiceException e) {
            assertEquals(IaErrorCode.ALREADY_EXISTS, e.getErrorCode());
        }

        verify(userPersistenceService, times(0)).update(any());
    }

    @Test
    public void testAddUser() {
        UserDTO userDTO = new UserDTO();
        userDTO.setName("name");
        userDTO.setEmployeeHsaId("HSA-1");
        userDTO.setIntygsadminRole(IntygsadminRole.FULL);

        userService.addUser(userDTO);
        verify(userPersistenceService, times(1)).add(any());
    }

    @Test
    public void testAddUser_hsaExists() {
        UserDTO userDTO = new UserDTO();
        userDTO.setName("name");
        userDTO.setEmployeeHsaId("HSA-1");
        userDTO.setIntygsadminRole(IntygsadminRole.FULL);

        UserEntity entity = new UserEntity();
        entity.setId(UUID.randomUUID());

        Optional<UserEntity> userEntity = Optional.of(entity);

        when(userPersistenceService.findByEmployeeHsaId(userDTO.getEmployeeHsaId())).thenReturn(userEntity);

        try {
            userService.addUser(userDTO);
            fail();
        } catch (IaServiceException e) {
            assertEquals(IaErrorCode.ALREADY_EXISTS, e.getErrorCode());
        }

        verify(userPersistenceService, times(0)).add(any());
    }
}
