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

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import se.inera.intyg.intygsadmin.persistence.entity.BannerEntity;
import se.inera.intyg.intygsadmin.persistence.service.BannerPersistenceService;
import se.inera.intyg.intygsadmin.web.controller.dto.BannerDTO;

@Service
public class BannerService {

    private BannerPersistenceService bannerPersistenceService;

    public BannerService(BannerPersistenceService bannerPersistenceService) {
        this.bannerPersistenceService = bannerPersistenceService;
    }

    public List<BannerDTO> getBanners(Pageable pageable) {

        bannerPersistenceService.findAll(pageable);

        return new ArrayList<>();
    }

    public BannerDTO createBanner(BannerDTO bannerDTO) {

        BannerEntity map = new BannerEntity();

        bannerPersistenceService.create(map);

        return bannerDTO;
    }

    public BannerDTO save(BannerDTO bannerDTO) {

        BannerEntity map = new BannerEntity();

        bannerPersistenceService.update(map);

        return bannerDTO;
    }

    public boolean deleteBanner(Long id) {
        bannerPersistenceService.delete(id);

        return true;
    }
}
