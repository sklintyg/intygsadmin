/*
 * Copyright (C) 2022 Inera AB (http://www.inera.se)
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

import java.util.Optional;
import java.util.UUID;
import javax.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import se.inera.intyg.intygsadmin.persistence.entity.DataExportEntity;
import se.inera.intyg.intygsadmin.persistence.entity.UserEntity;
import se.inera.intyg.intygsadmin.persistence.repository.DataExportRepository;
import se.inera.intyg.intygsadmin.persistence.repository.UserRepository;
import se.inera.intyg.intygsadmin.persistence.util.PersistenceUtil;

@Service
@Transactional
public class DataExportPersistenceService {

    private final DataExportRepository dataExportRepository;

    public DataExportPersistenceService(DataExportRepository dataExportRepository) {
        this.dataExportRepository = dataExportRepository;
    }

    public Optional<DataExportEntity> findById(UUID id) {
        return dataExportRepository.findById(id);
    }

    /*public void delete(UUID id) {
        userRepository.deleteById(id);
    }*/

    public DataExportEntity add(DataExportEntity dataExportEntity) {
        return dataExportRepository.save(dataExportEntity);
    }

    public DataExportEntity update(DataExportEntity newDataExportEntity) {
        final var dataExportEntity = dataExportRepository.getOne(newDataExportEntity.getId());
        dataExportEntity.setRepresentativePersonId(newDataExportEntity.getRepresentativePersonId());
        dataExportEntity.setRepresentativePhoneNumber(newDataExportEntity.getRepresentativePhoneNumber());

        return dataExportRepository.save(dataExportEntity);
    }

    public Page<DataExportEntity> findAll(Pageable pageable) {
        return dataExportRepository.findAll(PersistenceUtil.alwaysTrue(), pageable);
    }

}
