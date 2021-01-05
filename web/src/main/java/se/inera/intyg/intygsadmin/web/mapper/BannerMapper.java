/*
 * Copyright (C) 2021 Inera AB (http://www.inera.se)
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
package se.inera.intyg.intygsadmin.web.mapper;

import java.time.LocalDateTime;
import java.util.List;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import se.inera.intyg.intygsadmin.persistence.entity.BannerEntity;
import se.inera.intyg.intygsadmin.web.controller.dto.BannerDTO;
import se.inera.intyg.intygsadmin.web.controller.dto.BannerStatus;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BannerMapper {

    BannerDTO toDTO(BannerEntity s);

    List<BannerDTO> toListDTO(List<BannerEntity> s);

    BannerEntity toEntity(BannerDTO newSourceAccount);


    @AfterMapping
    default void calculateStatue(BannerEntity bannerEntity, @MappingTarget BannerDTO dto) {
        dto.setStatus(getBannerStatus(bannerEntity.getDisplayFrom(), bannerEntity.getDisplayTo()));
    }

    default BannerStatus getBannerStatus(LocalDateTime from, LocalDateTime to) {
        LocalDateTime today = LocalDateTime.now();

        if (from.isAfter(today)) {
            return BannerStatus.FUTURE;
        }

        if (to.isBefore(today)) {
            return BannerStatus.FINISHED;
        }

        return BannerStatus.ACTIVE;
    }
}
