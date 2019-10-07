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
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import se.inera.intyg.intygsadmin.persistence.entity.UserEntity;
import se.inera.intyg.intygsadmin.persistence.service.UserPersistenceService;
import se.inera.intyg.intygsadmin.web.auth.IntygsadminUser;
import se.inera.intyg.intygsadmin.web.controller.dto.UserDTO;
import se.inera.intyg.intygsadmin.web.exception.IaErrorCode;
import se.inera.intyg.intygsadmin.web.exception.IaServiceException;
import se.inera.intyg.intygsadmin.web.mapper.UserMapper;

@Service
public class UserService {

    private UserPersistenceService userPersistenceService;
    private UserMapper userMapper;

    @Autowired
    public UserService(UserPersistenceService userPersistenceService, UserMapper userMapper) {
        this.userPersistenceService = userPersistenceService;
        this.userMapper = userMapper;
    }

    public IntygsadminUser getActiveUser() {
        if (SecurityContextHolder.getContext().getAuthentication() == null
            || !(SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof IntygsadminUser)) {
            return null;
        }

        return (IntygsadminUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public Page<UserDTO> getUsers(Pageable pageable) {
        Page<UserEntity> userEntities = userPersistenceService.findAll(pageable);

        List<UserDTO> mapUsers = userMapper.toListDTO(userEntities.getContent());

        return new PageImpl<>(mapUsers, pageable, userEntities.getTotalElements());
    }

    public void deleteUser(UUID id) {
        if (getActiveUser().getId().equals(id)) {
            throw new IaServiceException(IaErrorCode.BAD_STATE);
        }

        userPersistenceService.delete(id);
    }

    public UserDTO updateUser(UserDTO userDTO) {
        if (getActiveUser().getId().equals(userDTO.getId())) {
            throw new IaServiceException(IaErrorCode.BAD_STATE);
        }

        checkIfUserWithHsaIdExists(userDTO);

        UserEntity upsertedUser = userPersistenceService.update(userMapper.toEntity(userDTO));
        return userMapper.toDTO(upsertedUser);
    }

    public UserDTO addUser(UserDTO userDTO) {
        checkIfUserWithHsaIdExists(userDTO);

        userDTO.setId(null);

        UserEntity upsertedUser = userPersistenceService.add(userMapper.toEntity(userDTO));

        return userMapper.toDTO(upsertedUser);
    }

    private void checkIfUserWithHsaIdExists(UserDTO userDTO) {
        Optional<UserEntity> foundUser = userPersistenceService.findByEmployeeHsaId(userDTO.getEmployeeHsaId());

        if (foundUser.isPresent() && !foundUser.get().getId().equals(userDTO.getId())) {
            throw new IaServiceException(IaErrorCode.ALREADY_EXISTS);
        }
    }

}
