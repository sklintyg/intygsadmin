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
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import se.inera.intyg.intygsadmin.persistence.entity.BannerEntity;
import se.inera.intyg.intygsadmin.persistence.service.BannerPersistenceService;
import se.inera.intyg.intygsadmin.web.controller.dto.BannerDTO;
import se.inera.intyg.intygsadmin.web.controller.dto.BannerStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
public class BannerServiceTest {

    @Mock
    private BannerPersistenceService bannerPersistenceService;

    @InjectMocks
    private BannerService bannerService;

    @Test
    public void testGetBannersEmptyResult() {
        Pageable pageable = PageRequest.of(0, 10);

        Page<BannerEntity> persistenceResult = new PageImpl<>(new ArrayList<>(), pageable, 0);
        when(bannerPersistenceService.findAll(any(Pageable.class))).thenReturn(persistenceResult);

        Page<BannerDTO> bannerDTOPage = bannerService.getBanners(pageable);

        verify(bannerPersistenceService, times(1)).findAll(eq(pageable));
        assertEquals(0, bannerDTOPage.getTotalElements());
    }

    @Test
    public void testGetBanners() {
        Pageable pageable = PageRequest.of(0, 10);

        LocalDateTime today = LocalDateTime.now();

        List<BannerEntity> bannerEntities = new ArrayList<>();
        BannerEntity bannerEntity1 = new BannerEntity();
        bannerEntity1.setId(1L);
        bannerEntity1.setDisplayFrom(today.minusDays(10));
        bannerEntity1.setDisplayTo(today.minusDays(2));

        BannerEntity bannerEntity2 = new BannerEntity();
        bannerEntity2.setId(2L);
        bannerEntity2.setDisplayFrom(today.plusDays(2));
        bannerEntity2.setDisplayTo(today.plusDays(10));

        BannerEntity bannerEntity3 = new BannerEntity();
        bannerEntity3.setId(3L);
        bannerEntity3.setDisplayFrom(today.minusDays(10));
        bannerEntity3.setDisplayTo(today.plusDays(2));

        bannerEntities.add(bannerEntity1);
        bannerEntities.add(bannerEntity2);
        bannerEntities.add(bannerEntity3);

        Page<BannerEntity> persistenceResult = new PageImpl<>(bannerEntities, pageable, 0);
        when(bannerPersistenceService.findAll(any(Pageable.class))).thenReturn(persistenceResult);

        Page<BannerDTO> bannerDTOPage = bannerService.getBanners(pageable);

        verify(bannerPersistenceService, times(1)).findAll(eq(pageable));
        assertEquals(3, bannerDTOPage.getTotalElements());
        assertEquals(BannerStatus.FINISHED, bannerDTOPage.getContent().get(0).getStatus());
        assertEquals(BannerStatus.FUTURE, bannerDTOPage.getContent().get(1).getStatus());
        assertEquals(BannerStatus.ACTIVE, bannerDTOPage.getContent().get(2).getStatus());
    }
}
