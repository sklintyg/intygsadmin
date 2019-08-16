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

import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.stereotype.Service;
import se.inera.intyg.intygsadmin.persistence.entity.UserEntity;
import se.inera.intyg.intygsadmin.persistence.repository.UserRepository;

@Service
@Transactional
public class UserPersistenceService {

    private UserRepository userRepository;

    public UserPersistenceService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<UserEntity> findByEmployeeHsaId(String employeeHsaId) {
        return userRepository.findByEmployeeHsaId(employeeHsaId);
    }

    public void delete(String employeeHsaId) {
        userRepository.deleteByEmployeeHsaId(employeeHsaId);
    }

    public UserEntity upsert(UserEntity newUserEntity) {
        UserEntity userEntity = userRepository.findByEmployeeHsaId(newUserEntity.getEmployeeHsaId())
            .orElse(new UserEntity());

        userEntity.setEmployeeHsaId(newUserEntity.getEmployeeHsaId());
        userEntity.setIntygsadminRole(newUserEntity.getIntygsadminRole());

        return userRepository.save(userEntity);
    }

    public List<UserEntity> findAll() {
        return userRepository.findAll();
    }

}
