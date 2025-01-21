/*
 * Copyright (C) 2025 Inera AB (http://www.inera.se)
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
package se.inera.intyg.intygsadmin.persistence.converters;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import se.inera.intyg.intygsadmin.persistence.enums.BannerPriority;

@Converter(autoApply = true)
public class BannerPriorityConverter implements AttributeConverter<BannerPriority, String> {

    @Override
    public String convertToDatabaseColumn(BannerPriority priority) {
        return priority.getDbValue();
    }

    @Override
    public BannerPriority convertToEntityAttribute(String dbData) {
        return BannerPriority.fromDbValue(dbData);
    }
}
