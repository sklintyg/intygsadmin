/*
 * Copyright (C) 2022 Inera AB (http://www.inera.se)
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
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import se.inera.intyg.intygsadmin.persistence.service.BannerPersistenceService;
import se.inera.intyg.intygsadmin.web.controller.dto.BannerDTO;
import se.inera.intyg.intygsadmin.web.controller.dto.ValidationDTO;
import se.inera.intyg.intygsadmin.web.exception.IaErrorCode;
import se.inera.intyg.intygsadmin.web.exception.IaServiceException;
import se.inera.intyg.intygsadmin.web.exception.IaValidationException;

@Service
public class BannerValidationService {

    private static final String FIELD_DISPLAY_FROM = "displayFrom";
    private static final String FIELD_DISPLAY_TO = "displayTo";
    private static final String FIELD_APPLICATION = "application";
    private static final String FIELD_PRIORITY = "priority";
    private static final String FIELD_MESSAGE = "message";

    private static final String ERROR_REQUIRED = "Obligatoriskt fält";

    private BannerPersistenceService bannerPersistenceService;

    public BannerValidationService(BannerPersistenceService bannerPersistenceService) {
        this.bannerPersistenceService = bannerPersistenceService;
    }

    public void validateBanner(BannerDTO bannerDTO) {

        ValidationInstance validationInstance = new ValidationInstance();

        List<ValidationDTO> validationErrors = validationInstance.validate(bannerDTO);

        if (!validationErrors.isEmpty()) {
            throw new IaValidationException(validationErrors);
        }
    }

    private class ValidationInstance {

        private List<ValidationDTO> validations = new ArrayList<>();
        private BannerDTO bannerDTO;

        List<ValidationDTO> validate(BannerDTO bannerDTO) {
            this.bannerDTO = bannerDTO;

            checkForEmptyFields();
            validateDates();
            checkForExistingBanner();

            return validations;
        }

        private void checkForEmptyFields() {
            if (StringUtils.isEmpty(bannerDTO.getMessage())) {
                addValidation(FIELD_MESSAGE, ERROR_REQUIRED);
            }

            if (bannerDTO.getApplication() == null) {
                addValidation(FIELD_APPLICATION, ERROR_REQUIRED);
            }

            if (bannerDTO.getPriority() == null) {
                addValidation(FIELD_PRIORITY, ERROR_REQUIRED);
            }
        }

        private void validateDates() {
            if (bannerDTO.getDisplayFrom() == null) {
                addValidation(FIELD_DISPLAY_FROM, ERROR_REQUIRED);
            }

            if (bannerDTO.getDisplayTo() == null) {
                addValidation(FIELD_DISPLAY_TO, ERROR_REQUIRED);
            }

            if (!validations.isEmpty()) {
                return;
            }

            LocalDateTime now = LocalDateTime.now();

            if (bannerDTO.getDisplayTo().isBefore(now)) {
                addValidation(FIELD_DISPLAY_TO, "To is before today");
            }

            if (bannerDTO.getDisplayTo().isBefore(bannerDTO.getDisplayFrom())) {
                addValidation(FIELD_DISPLAY_TO, "To is before from");
                addValidation(FIELD_DISPLAY_FROM, "To is before from");
            }
        }

        private void checkForExistingBanner() {
            if (!validations.isEmpty()) {
                return;
            }

            LocalDateTime from = bannerDTO.getDisplayFrom();

            long count = bannerPersistenceService.countByApplicationAndTime(
                bannerDTO.getApplication(), from, bannerDTO.getDisplayTo(),
                bannerDTO.getId());

            if (count > 0) {
                throw new IaServiceException(IaErrorCode.ALREADY_EXISTS);
            }
        }


        private void addValidation(String field, String message) {
            ValidationDTO validationDTO = new ValidationDTO(field, message);

            validations.add(validationDTO);
        }
    }


}
