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
package se.inera.intyg.intygsadmin.web.controller.endpoint;

import java.util.List;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.endpoint.annotation.Selector;
import org.springframework.boot.actuate.endpoint.web.annotation.WebEndpoint;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import se.inera.intyg.infra.driftbannerdto.Application;
import se.inera.intyg.infra.driftbannerdto.Banner;
import se.inera.intyg.intygsadmin.web.service.BannerService;

@Component
@WebEndpoint(id = "banner")
public class BannerEndpoint {

    private final BannerService bannerService;

    public BannerEndpoint(BannerService bannerService) {
        this.bannerService = bannerService;
    }

    @ReadOperation
    public ResponseEntity<List<Banner>> getActiveAndFutureBanners(@Selector Application application) {
        List<Banner> banners = bannerService.getActiveAndFutureBanners(application);

        return ResponseEntity.ok(banners);
    }
}
