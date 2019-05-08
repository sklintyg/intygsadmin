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

package se.inera.intyg.intygsadmin.web.service.banner;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import se.inera.intyg.intygsadmin.persistence.entity.BannerEntity;
import se.inera.intyg.intygsadmin.persistence.service.BannerPersistenceService;
import se.inera.intyg.intygsadmin.web.controller.dto.BannerDTO;
import se.inera.intyg.intygsadmin.web.mapper.BannerMapper;

@Service
public class BannerService {

    private BannerPersistenceService bannerPersistenceService;
    private BannerMapper bannerMapper;

    public BannerService(BannerPersistenceService bannerPersistenceService, BannerMapper bannerMapper) {
        this.bannerPersistenceService = bannerPersistenceService;
        this.bannerMapper = bannerMapper;
    }

    public List<BannerDTO> getBanners(Pageable pageable) {
        Page<BannerEntity> banners = bannerPersistenceService.findAll(pageable);

        return bannerMapper.toListDTO(banners.getContent());
    }

    public BannerDTO createBanner(BannerDTO bannerDTO) {

        BannerEntity map = bannerMapper.toEntity(bannerDTO);

        BannerEntity createdEntity = bannerPersistenceService.create(map);

        return bannerMapper.toDTO(createdEntity);
    }

    public BannerDTO save(BannerDTO bannerDTO) {

        BannerEntity map = bannerMapper.toEntity(bannerDTO);

        BannerEntity updatedEntity = bannerPersistenceService.update(map);

        return bannerMapper.toDTO(updatedEntity);
    }

    public boolean deleteBanner(Long id) {
        bannerPersistenceService.delete(id);

        return true;
    }
}
