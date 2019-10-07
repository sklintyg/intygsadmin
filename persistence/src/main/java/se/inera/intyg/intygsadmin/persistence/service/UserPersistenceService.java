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

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import java.util.Optional;
import java.util.UUID;
import javax.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    public void delete(UUID id) {
        userRepository.deleteById(id);
    }

    public UserEntity add(UserEntity newUserEntity) {
        UserEntity userEntity = new UserEntity();

        return upsert(userEntity, newUserEntity);
    }

    public UserEntity update(UserEntity newUserEntity) {
        UserEntity userEntity = userRepository.getOne(newUserEntity.getId());

        return upsert(userEntity, newUserEntity);
    }

    private UserEntity upsert(UserEntity userEntity, UserEntity newUserEntity) {
        userEntity.setEmployeeHsaId(newUserEntity.getEmployeeHsaId());
        userEntity.setIntygsadminRole(newUserEntity.getIntygsadminRole());
        userEntity.setName(newUserEntity.getName());

        return userRepository.save(userEntity);
    }

    public Page<UserEntity> findAll(Pageable pageable) {
        BooleanBuilder builder = new BooleanBuilder();
        Predicate predicate = builder.getValue();

        return userRepository.findAll(predicate, pageable);
    }

}
