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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import se.inera.intyg.intygsadmin.persistence.entity.BannerEntity;
import se.inera.intyg.intygsadmin.persistence.enums.Application;
import se.inera.intyg.intygsadmin.persistence.service.BannerPersistenceService;
import se.inera.intyg.intygsadmin.web.controller.dto.BannerDTO;
import se.inera.intyg.intygsadmin.web.controller.dto.BannerStatus;
import se.inera.intyg.intygsadmin.web.mapper.BannerMapper;

@Service
public class BannerService {

    private BannerPersistenceService bannerPersistenceService;
    private BannerMapper bannerMapper;

    public BannerService(BannerPersistenceService bannerPersistenceService, BannerMapper bannerMapper) {
        this.bannerPersistenceService = bannerPersistenceService;
        this.bannerMapper = bannerMapper;
    }

    public Page<BannerDTO> getBanners(Pageable pageable) {
        Page<BannerEntity> banners = bannerPersistenceService.findAll(pageable);

        List<BannerDTO> mapBanners = banners.getContent().stream()
                .map(bannerEntity -> {

                    BannerDTO dto = bannerMapper.toDTO(bannerEntity);
                    dto.setStatus(getBannerStatus(dto));

                    return dto;
                })
                .collect(Collectors.toList());

        return new PageImpl<>(mapBanners, pageable, banners.getTotalElements());
    }

    public List<BannerDTO> getActiveAndFutureBanners(Application application) {
        LocalDateTime today = LocalDateTime.now();

        List<BannerEntity> banners = bannerPersistenceService.findActiveAndFuture(today, application);

        return banners.stream()
                .map(bannerEntity -> {
                    BannerDTO dto = bannerMapper.toDTO(bannerEntity);
                    dto.setStatus(getBannerStatus(dto));

                    return dto;
                })
                .collect(Collectors.toList());
    }

    public BannerDTO createBanner(BannerDTO bannerDTO) {
        BannerEntity map = bannerMapper.toEntity(bannerDTO);

        BannerEntity createdEntity = bannerPersistenceService.create(map);
        BannerDTO dto = bannerMapper.toDTO(createdEntity);
        dto.setStatus(getBannerStatus(dto));

        return dto;
    }

    public BannerDTO save(BannerDTO bannerDTO) {
        BannerEntity map = bannerMapper.toEntity(bannerDTO);

        BannerEntity updatedEntity = bannerPersistenceService.update(map);
        BannerDTO dto = bannerMapper.toDTO(updatedEntity);
        dto.setStatus(getBannerStatus(dto));

        return dto;
    }

    public boolean deleteBanner(UUID id) {
        Optional<BannerEntity> optionalBanner = bannerPersistenceService.findOne(id);

        if (optionalBanner.isEmpty()) {
            return false;
        }

        BannerEntity banner = optionalBanner.get();
        BannerStatus status = getBannerStatus(banner.getDisplayFrom(), banner.getDisplayTo());

        if (BannerStatus.FUTURE.equals(status)) {
            bannerPersistenceService.delete(id);
        } else {
            banner.setDisplayTo(LocalDateTime.now());
            bannerPersistenceService.update(banner);
        }

        return true;
    }

    private BannerStatus getBannerStatus(BannerDTO dto) {
        return getBannerStatus(dto.getDisplayFrom(), dto.getDisplayTo());
    }

    private BannerStatus getBannerStatus(LocalDateTime from, LocalDateTime to) {
        LocalDateTime today = LocalDateTime.now();

        if (from.isAfter(today)) {
            return BannerStatus.FUTURE;
        }

        if (to.isBefore(today)) {
            return BannerStatus.FINISHED;
        }

        return BannerStatus.ACTIVE;
    }
}
