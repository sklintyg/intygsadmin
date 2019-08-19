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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import se.inera.intyg.intygsadmin.persistence.entity.BannerEntity;
import se.inera.intyg.intygsadmin.persistence.enums.Application;
import se.inera.intyg.intygsadmin.persistence.service.BannerPersistenceService;
import se.inera.intyg.intygsadmin.web.controller.dto.BannerDTO;
import se.inera.intyg.intygsadmin.web.controller.dto.BannerStatus;
import se.inera.intyg.intygsadmin.web.exception.IaServiceException;
import se.inera.intyg.intygsadmin.web.mapper.BannerMapper;


@ExtendWith(MockitoExtension.class)
public class BannerServiceTest {

    @Spy
    private BannerMapper bannerMapper = Mappers.getMapper(BannerMapper.class);

    @Mock
    private BannerPersistenceService bannerPersistenceService;

    @Mock
    private BannerValidationService bannerValidationService;

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
        bannerEntity1.setId(UUID.randomUUID());
        bannerEntity1.setDisplayFrom(today.minusDays(10));
        bannerEntity1.setDisplayTo(today.minusDays(2));

        BannerEntity bannerEntity2 = new BannerEntity();
        bannerEntity2.setId(UUID.randomUUID());
        bannerEntity2.setDisplayFrom(today.plusDays(2));
        bannerEntity2.setDisplayTo(today.plusDays(10));

        BannerEntity bannerEntity3 = new BannerEntity();
        bannerEntity3.setId(UUID.randomUUID());
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

    @Test
    public void testGetActiveAndFutureBanners() {
        LocalDateTime today = LocalDateTime.now();

        List<BannerEntity> bannerEntities = new ArrayList<>();
        BannerEntity bannerEntity2 = new BannerEntity();
        bannerEntity2.setId(UUID.randomUUID());
        bannerEntity2.setDisplayFrom(today.plusDays(2));
        bannerEntity2.setDisplayTo(today.plusDays(10));

        BannerEntity bannerEntity3 = new BannerEntity();
        bannerEntity3.setId(UUID.randomUUID());
        bannerEntity3.setDisplayFrom(today.minusDays(10));
        bannerEntity3.setDisplayTo(today.plusDays(2));

        bannerEntities.add(bannerEntity2);
        bannerEntities.add(bannerEntity3);

        when(bannerPersistenceService.findActiveAndFuture(any(), eq(Application.WEBCERT))).thenReturn(bannerEntities);

        List<BannerDTO> banners = bannerService.getActiveAndFutureBanners(Application.WEBCERT);

        verify(bannerPersistenceService, times(1)).findActiveAndFuture(any(), eq(Application.WEBCERT));
        assertEquals(2, banners.size());
        assertEquals(BannerStatus.FUTURE, banners.get(0).getStatus());
        assertEquals(BannerStatus.ACTIVE, banners.get(1).getStatus());
    }

    @Test
    public void testUpdateBanner() {
        BannerDTO bannerDTO = new BannerDTO();
        bannerDTO.setId(UUID.randomUUID());
        bannerDTO.setMessage("new message");
        bannerDTO.setDisplayFrom(LocalDateTime.now().minusDays(10));
        bannerDTO.setDisplayTo(LocalDateTime.now().plusDays(10));

        BannerEntity bannerEntity = new BannerEntity();
        bannerEntity.setDisplayFrom(LocalDateTime.now().minusDays(1));
        bannerEntity.setDisplayTo(LocalDateTime.now().plusDays(1));

        when(bannerPersistenceService.findOne(any())).thenReturn(Optional.of(bannerEntity));
        when(bannerPersistenceService.update(any())).then(returnsFirstArg());

        BannerDTO saved = bannerService.save(bannerDTO);

        assertNotNull(saved);
        assertEquals("new message", saved.getMessage());
        verify(bannerPersistenceService, times(1)).update(any());
        verify(bannerValidationService, times(1)).validateBanner(any());
    }

    @Test
    public void testUpdateBanner_notFound() {
        BannerDTO bannerDTO = new BannerDTO();
        bannerDTO.setId(UUID.randomUUID());
        bannerDTO.setMessage("new message");
        bannerDTO.setDisplayFrom(LocalDateTime.now().minusDays(10));
        bannerDTO.setDisplayTo(LocalDateTime.now().plusDays(10));

        when(bannerPersistenceService.findOne(any())).thenReturn(Optional.empty());

        assertThrows(IaServiceException.class, () -> bannerService.save(bannerDTO));

        verify(bannerPersistenceService, times(0)).update(any());
        verify(bannerValidationService, times(1)).validateBanner(any());
    }

    @Test
    public void testUpdateBanner_finished() {
        BannerDTO bannerDTO = new BannerDTO();
        bannerDTO.setId(UUID.randomUUID());
        bannerDTO.setMessage("new message");
        bannerDTO.setDisplayFrom(LocalDateTime.now().minusDays(10));
        bannerDTO.setDisplayTo(LocalDateTime.now().minusDays(10));

        BannerEntity bannerEntity = new BannerEntity();
        bannerEntity.setDisplayFrom(LocalDateTime.now().minusDays(10));
        bannerEntity.setDisplayTo(LocalDateTime.now().minusDays(1));

        when(bannerPersistenceService.findOne(any())).thenReturn(Optional.of(bannerEntity));

        assertThrows(IaServiceException.class, () -> bannerService.save(bannerDTO));

        verify(bannerPersistenceService, times(0)).update(any());
        verify(bannerValidationService, times(1)).validateBanner(any());
    }

    @Test
    public void testCreateBanner() {
        String message = "new message";
        LocalDateTime from = LocalDateTime.now().plusDays(2);
        LocalDateTime to = LocalDateTime.now().plusDays(10);

        BannerDTO bannerDTO = new BannerDTO();
        bannerDTO.setMessage(message);
        bannerDTO.setDisplayFrom(from);
        bannerDTO.setDisplayTo(to);

        when(bannerPersistenceService.create(any())).then(returnsFirstArg());

        BannerDTO created = bannerService.createBanner(bannerDTO);

        assertNotNull(created);
        assertEquals(message, created.getMessage());
        assertEquals(from, created.getDisplayFrom());
        assertEquals(to, created.getDisplayTo());
        verify(bannerPersistenceService, times(1)).create(any());
        verify(bannerValidationService, times(1)).validateBanner(any());
    }

    @Test
    public void testCreateBanner_updateDateFrom() {
        String message = "new message";
        LocalDateTime from = LocalDateTime.now().minusDays(10);
        LocalDateTime to = LocalDateTime.now().plusDays(10);

        BannerDTO bannerDTO = new BannerDTO();
        bannerDTO.setMessage(message);
        bannerDTO.setDisplayFrom(from);
        bannerDTO.setDisplayTo(to);

        when(bannerPersistenceService.create(any())).then(returnsFirstArg());

        BannerDTO created = bannerService.createBanner(bannerDTO);

        assertNotNull(created);
        assertEquals(message, created.getMessage());
        assertNotEquals(from, created.getDisplayFrom());
        assertEquals(to, created.getDisplayTo());
        verify(bannerPersistenceService, times(1)).create(any());
        verify(bannerValidationService, times(1)).validateBanner(any());
    }

    @Test
    public void testDeleteBanner_notFound() {
        UUID notFoundId = UUID.randomUUID();

        when(bannerPersistenceService.findOne(eq(notFoundId))).thenReturn(Optional.empty());

        boolean deleted = bannerService.deleteBanner(notFoundId);

        assertFalse(deleted);
        verify(bannerPersistenceService, times(0)).delete(any());
    }

    @Test
    public void testDeleteBanner_futureBanner() {
        UUID id = UUID.randomUUID();
        BannerEntity bannerEntity = new BannerEntity();
        bannerEntity.setDisplayFrom(LocalDateTime.now().plusDays(5));
        bannerEntity.setDisplayTo(LocalDateTime.now().plusDays(10));

        when(bannerPersistenceService.findOne(eq(id))).thenReturn(Optional.of(bannerEntity));

        boolean deleted = bannerService.deleteBanner(id);

        assertTrue(deleted);
        verify(bannerPersistenceService, times(1)).delete(eq(id));
        verify(bannerPersistenceService, times(0)).update(any());
    }

    @Test
    public void testDeleteBanner_activeBanner() {
        UUID id = UUID.randomUUID();
        BannerEntity bannerEntity = new BannerEntity();
        bannerEntity.setDisplayFrom(LocalDateTime.now().minusDays(5));
        bannerEntity.setDisplayTo(LocalDateTime.now().plusDays(10));

        when(bannerPersistenceService.findOne(eq(id))).thenReturn(Optional.of(bannerEntity));

        boolean deleted = bannerService.deleteBanner(id);

        assertTrue(deleted);
        verify(bannerPersistenceService, times(0)).delete(any());
        verify(bannerPersistenceService, times(1)).update(eq(bannerEntity));
    }

    @Test
    public void testDeleteBanner_finishedBanner() {
        UUID id = UUID.randomUUID();
        BannerEntity bannerEntity = new BannerEntity();
        bannerEntity.setDisplayFrom(LocalDateTime.now().minusDays(5));
        bannerEntity.setDisplayTo(LocalDateTime.now().minusDays(2));

        when(bannerPersistenceService.findOne(eq(id))).thenReturn(Optional.of(bannerEntity));

        assertThrows(IaServiceException.class, () -> bannerService.deleteBanner(id));

        verify(bannerPersistenceService, times(0)).delete(any());
        verify(bannerPersistenceService, times(0)).update(eq(bannerEntity));
    }
}
