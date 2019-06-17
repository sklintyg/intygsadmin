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

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import se.inera.intyg.intygsadmin.persistence.enums.Application;
import se.inera.intyg.intygsadmin.persistence.enums.BannerPriority;
import se.inera.intyg.intygsadmin.persistence.service.BannerPersistenceService;
import se.inera.intyg.intygsadmin.web.controller.dto.BannerDTO;
import se.inera.intyg.intygsadmin.web.controller.dto.ValidationDTO;
import se.inera.intyg.intygsadmin.web.exception.IaValidationException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BannerValidationServiceTest {

    @Mock
    private BannerPersistenceService bannerPersistenceService;

    @InjectMocks
    private BannerValidationService bannerValidationService;

    @Test
    public void testValidation_missingFields() {
        BannerDTO bannerDTO = new BannerDTO();

        try {
            bannerValidationService.validateBanner(bannerDTO);
            fail();
        } catch (IaValidationException e) {
            List<ValidationDTO> validationErrors = e.getValidationErrors();

            assertFalse(validationErrors.isEmpty());
        }

        // Missing application
        try {
            bannerDTO = getBannerDTO();
            bannerDTO.setApplication(null);
            bannerValidationService.validateBanner(bannerDTO);
            fail();
        } catch (IaValidationException e) {
            List<ValidationDTO> validationErrors = e.getValidationErrors();

            assertEquals(1, validationErrors.size());
        }

        // Missing priority
        try {
            bannerDTO = getBannerDTO();
            bannerDTO.setPriority(null);
            bannerValidationService.validateBanner(bannerDTO);
            fail();
        } catch (IaValidationException e) {
            List<ValidationDTO> validationErrors = e.getValidationErrors();

            assertEquals(1, validationErrors.size());
        }

        // Missing message
        try {
            bannerDTO = getBannerDTO();
            bannerDTO.setMessage("");
            bannerValidationService.validateBanner(bannerDTO);
            fail();
        } catch (IaValidationException e) {
            List<ValidationDTO> validationErrors = e.getValidationErrors();

            assertEquals(1, validationErrors.size());
        }

        // Missing displayFrom
        try {
            bannerDTO = getBannerDTO();
            bannerDTO.setDisplayFrom(null);
            bannerValidationService.validateBanner(bannerDTO);
            fail();
        } catch (IaValidationException e) {
            List<ValidationDTO> validationErrors = e.getValidationErrors();

            assertEquals(1, validationErrors.size());
        }

        // Missing displayTo
        try {
            bannerDTO = getBannerDTO();
            bannerDTO.setDisplayTo(null);
            bannerValidationService.validateBanner(bannerDTO);
            fail();
        } catch (IaValidationException e) {
            List<ValidationDTO> validationErrors = e.getValidationErrors();

            assertEquals(1, validationErrors.size());
        }

        verify(bannerPersistenceService, times(0)).countByApplicationAndTime(any(), any(), any(), any());
    }

    @Test
    public void testValidation_dates() {
        BannerDTO bannerDTO = getBannerDTO();


        // To before from
        try {
            bannerDTO.setDisplayTo(bannerDTO.getDisplayFrom().minusDays(1));
            bannerValidationService.validateBanner(bannerDTO);
            fail();
        } catch (IaValidationException e) {
            List<ValidationDTO> validationErrors = e.getValidationErrors();

            assertFalse(validationErrors.isEmpty());
        }

        verify(bannerPersistenceService, times(0)).countByApplicationAndTime(any(), any(), any(), any());

        // To before today
        try {
            bannerDTO.setDisplayFrom(LocalDateTime.now().minusDays(10));
            bannerDTO.setDisplayTo(LocalDateTime.now().minusDays(2));
            bannerValidationService.validateBanner(bannerDTO);
            fail();
        } catch (IaValidationException e) {
            List<ValidationDTO> validationErrors = e.getValidationErrors();

            assertFalse(validationErrors.isEmpty());
        }

        verify(bannerPersistenceService, times(0)).countByApplicationAndTime(any(), any(), any(), any());
    }

    @Test
    public void testValidation_bannerInInterval() {
        BannerDTO bannerDTO = getBannerDTO();

        when(bannerPersistenceService.countByApplicationAndTime(
                eq(bannerDTO.getApplication()), eq(bannerDTO.getDisplayFrom()), eq(bannerDTO.getDisplayTo()), any()))
                .thenReturn(1L);

        try {
            bannerValidationService.validateBanner(bannerDTO);
            fail();
        } catch (IaValidationException e) {
            List<ValidationDTO> validationErrors = e.getValidationErrors();

            assertEquals(1, validationErrors.size());
        }

        verify(bannerPersistenceService, times(1)).countByApplicationAndTime(any(), any(), any(), any());
    }

    @Test
    public void testValidation_valid() {
        BannerDTO bannerDTO = getBannerDTO();

        when(bannerPersistenceService.countByApplicationAndTime(
                eq(bannerDTO.getApplication()), eq(bannerDTO.getDisplayFrom()), eq(bannerDTO.getDisplayTo()), any()))
                .thenReturn(0L);

        bannerValidationService.validateBanner(bannerDTO);

        verify(bannerPersistenceService, times(1)).countByApplicationAndTime(any(), any(), any(), any());
    }

    private BannerDTO getBannerDTO() {
        BannerDTO bannerDTO = new BannerDTO();
        bannerDTO.setMessage("Hej");
        bannerDTO.setApplication(Application.WEBCERT);
        bannerDTO.setPriority(BannerPriority.HIGH);
        bannerDTO.setDisplayFrom(LocalDateTime.now().plusMinutes(1));
        bannerDTO.setDisplayTo(LocalDateTime.now().plusDays(1));

        return bannerDTO;
    }
}
