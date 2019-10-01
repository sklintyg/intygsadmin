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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import se.inera.intyg.infra.driftbannerdto.Application;
import se.inera.intyg.infra.driftbannerdto.Banner;
import se.inera.intyg.intygsadmin.persistence.entity.BannerEntity;
import se.inera.intyg.intygsadmin.persistence.service.BannerPersistenceService;
import se.inera.intyg.intygsadmin.web.controller.dto.BannerDTO;
import se.inera.intyg.intygsadmin.web.controller.dto.BannerStatus;
import se.inera.intyg.intygsadmin.web.exception.IaErrorCode;
import se.inera.intyg.intygsadmin.web.exception.IaServiceException;
import se.inera.intyg.intygsadmin.web.mapper.BannerApiMapper;
import se.inera.intyg.intygsadmin.web.mapper.BannerMapper;

@Service
public class BannerService {

    private BannerValidationService bannerValidationService;
    private BannerPersistenceService bannerPersistenceService;
    private BannerMapper bannerMapper;
    private BannerApiMapper bannerApiMapper;

    public BannerService(BannerPersistenceService bannerPersistenceService, BannerMapper bannerMapper, BannerApiMapper bannerApiMapper,
        BannerValidationService bannerValidationService) {
        this.bannerPersistenceService = bannerPersistenceService;
        this.bannerMapper = bannerMapper;
        this.bannerApiMapper = bannerApiMapper;
        this.bannerValidationService = bannerValidationService;
    }

    public Page<BannerDTO> getBanners(Pageable pageable) {
        Page<BannerEntity> banners = bannerPersistenceService.findAll(pageable);

        List<BannerDTO> mapBanners = bannerMapper.toListDTO(banners.getContent());

        return new PageImpl<>(mapBanners, pageable, banners.getTotalElements());
    }

    public List<Banner> getActiveAndFutureBanners(Application application) {
        LocalDateTime today = LocalDateTime.now();

        List<BannerEntity> banners = bannerPersistenceService.findActiveAndFuture(today, application);

        return bannerApiMapper.toApiDTO(banners);
    }

    public BannerDTO createBanner(BannerDTO bannerDTO) {
        // A new banner can't be active before now
        if (bannerDTO.getDisplayFrom() != null && LocalDateTime.now().isAfter(bannerDTO.getDisplayFrom())) {
            bannerDTO.setDisplayFrom(LocalDateTime.now());
        }

        bannerValidationService.validateBanner(bannerDTO);

        bannerDTO.setMessage(bannerDTO.getMessage().trim());
        BannerEntity map = bannerMapper.toEntity(bannerDTO);

        BannerEntity createdEntity = bannerPersistenceService.create(map);
        return bannerMapper.toDTO(createdEntity);
    }

    public BannerDTO save(BannerDTO bannerDTO) {
        bannerValidationService.validateBanner(bannerDTO);

        Optional<BannerEntity> optionalBanner = bannerPersistenceService.findOne(bannerDTO.getId());

        if (optionalBanner.isEmpty()) {
            throw new IaServiceException(IaErrorCode.NOT_FOUND);
        }

        BannerEntity banner = optionalBanner.get();
        BannerStatus status = bannerMapper.getBannerStatus(banner.getDisplayFrom(), banner.getDisplayTo());

        // Not allowed to update a finished banner
        if (BannerStatus.FINISHED.equals(status)) {
            throw new IaServiceException(IaErrorCode.BAD_STATE);
        }

        bannerDTO.setMessage(bannerDTO.getMessage().trim());
        BannerEntity map = bannerMapper.toEntity(bannerDTO);

        BannerEntity updatedEntity = bannerPersistenceService.update(map);
        return bannerMapper.toDTO(updatedEntity);
    }

    public boolean deleteBanner(UUID id) {
        Optional<BannerEntity> optionalBanner = bannerPersistenceService.findOne(id);

        if (optionalBanner.isEmpty()) {
            return false;
        }

        BannerEntity banner = optionalBanner.get();
        BannerStatus status = bannerMapper.getBannerStatus(banner.getDisplayFrom(), banner.getDisplayTo());

        switch (status) {
            case FUTURE:
                bannerPersistenceService.delete(id);
                return true;
            case ACTIVE:
                banner.setDisplayTo(LocalDateTime.now().minusMinutes(1));
                bannerPersistenceService.update(banner);
                return true;
            default:
                throw new IaServiceException(IaErrorCode.BAD_STATE);
        }
    }
}
