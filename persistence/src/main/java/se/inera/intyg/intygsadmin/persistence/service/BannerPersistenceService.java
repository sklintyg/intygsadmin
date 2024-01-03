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
package se.inera.intyg.intygsadmin.persistence.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import se.inera.intyg.infra.driftbannerdto.Application;
import se.inera.intyg.intygsadmin.persistence.entity.BannerEntity;
import se.inera.intyg.intygsadmin.persistence.repository.BannerRepository;
import se.inera.intyg.intygsadmin.persistence.util.PersistenceUtil;

@Service
@Transactional
public class BannerPersistenceService {

    private BannerRepository bannerRepository;

    public BannerPersistenceService(BannerRepository bannerRepository) {
        this.bannerRepository = bannerRepository;
    }

    public Page<BannerEntity> findAll(Pageable pageable) {
        return bannerRepository.findAll(PersistenceUtil.alwaysTrue(), pageable);
    }

    public List<BannerEntity> findActiveAndFuture(LocalDateTime from, Application application) {
        return bannerRepository.findAllByApplicationEqualsAndDisplayToAfter(application, from);
    }

    public Optional<BannerEntity> findOne(UUID id) {
        return bannerRepository.findById(id);
    }

    public BannerEntity create(BannerEntity bannerEntity) {
        return bannerRepository.save(bannerEntity);
    }

    public BannerEntity update(BannerEntity bannerEntity) {
        BannerEntity entity = bannerRepository.getOne(bannerEntity.getId());

        bannerEntity.setCreatedAt(entity.getCreatedAt());

        return bannerRepository.save(bannerEntity);
    }

    public long countByApplicationAndTime(Application application, LocalDateTime from, LocalDateTime to, UUID id) {

        if (id == null) {
            return bannerRepository.countByApplicationEquals(application, from, to);
        }

        return bannerRepository.countByApplicationEqualsAndIdNot(application, id, from, to);
    }

    public void delete(UUID id) {
        bannerRepository.deleteById(id);
    }
}
