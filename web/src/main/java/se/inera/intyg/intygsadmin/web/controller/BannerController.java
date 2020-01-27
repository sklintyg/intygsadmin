/*
 * Copyright (C) 2020 Inera AB (http://www.inera.se)
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

package se.inera.intyg.intygsadmin.web.controller;

import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import se.inera.intyg.infra.driftbannerdto.Application;
import se.inera.intyg.infra.driftbannerdto.Banner;
import se.inera.intyg.intygsadmin.web.controller.dto.BannerDTO;
import se.inera.intyg.intygsadmin.web.service.BannerService;

@RestController
@RequestMapping("/api/banner")
public class BannerController {

    private BannerService bannerService;

    public BannerController(BannerService bannerService) {
        this.bannerService = bannerService;
    }

    @GetMapping
    public ResponseEntity<Page<BannerDTO>> listBanners(
        @PageableDefault(size = 20, sort = "createdAt")
            Pageable pageable) {
        Page<BannerDTO> bannerDTOS = bannerService.getBanners(pageable);

        return ResponseEntity.ok(bannerDTOS);
    }

    @GetMapping("/activeAndFuture")
    public ResponseEntity<List<Banner>> listBannersActiveAndFutureBanners(
        @RequestParam Application application) {
        List<Banner> bannerDTOS = bannerService.getActiveAndFutureBanners(application);

        return ResponseEntity.ok(bannerDTOS);
    }

    @PutMapping
    public ResponseEntity<BannerDTO> createBanner(@RequestBody BannerDTO bannerDTO) {
        BannerDTO savedDTO = bannerService.createBanner(bannerDTO);

        return ResponseEntity.ok(savedDTO);
    }

    @PostMapping("/{id}")
    public ResponseEntity<BannerDTO> updateBanner(@PathVariable UUID id, @RequestBody BannerDTO bannerDTO) {
        bannerDTO.setId(id);
        BannerDTO savedDTO = bannerService.save(bannerDTO);

        return ResponseEntity.ok(savedDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteBanner(@PathVariable UUID id) {
        bannerService.deleteBanner(id);

        return ResponseEntity.ok().build();
    }
}
