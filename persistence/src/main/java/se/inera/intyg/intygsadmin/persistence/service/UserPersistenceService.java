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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import se.inera.intyg.intygsadmin.persistence.entity.UserEntity;
import se.inera.intyg.intygsadmin.persistence.repository.UserRepository;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class UserPersistenceService {

    private UserRepository userRepository;

    public UserPersistenceService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Page<UserEntity> findAll(Pageable pageable) {
        BooleanBuilder builder = new BooleanBuilder();
        Predicate predicate = builder.getValue();

        return userRepository.findAll(predicate, pageable);
    }

    public Optional<UserEntity> findOne(UUID id) {
        return userRepository.findById(id);
    }

    public Optional<UserEntity> findByEmployeeHsaId(String employeeHsaId) {
        return userRepository.findByEmployeeHsaId(employeeHsaId);
    }

    public UserEntity create(UserEntity userEntity) {
        return userRepository.save(userEntity);
    }

    public UserEntity update(UserEntity userEntity) {
        return userRepository.save(userEntity);
    }

    public void delete(UUID id) {
        userRepository.deleteById(id);
    }
}
