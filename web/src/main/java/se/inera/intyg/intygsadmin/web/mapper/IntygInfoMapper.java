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
package se.inera.intyg.intygsadmin.web.mapper;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import se.inera.intyg.infra.intyginfo.dto.ItIntygInfo;
import se.inera.intyg.infra.intyginfo.dto.WcIntygInfo;
import se.inera.intyg.intygsadmin.persistence.entity.IntygInfoEntity;
import se.inera.intyg.intygsadmin.web.controller.dto.IntygInfoDTO;
import se.inera.intyg.intygsadmin.web.controller.dto.IntygInfoListDTO;

@Mapper(componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface IntygInfoMapper {

    void updateIntygInfoFromIT(ItIntygInfo s, @MappingTarget IntygInfoDTO target);

    void updateIntygInfoFromWC(WcIntygInfo s, @MappingTarget IntygInfoDTO target);

    List<IntygInfoListDTO> toListDTO(List<IntygInfoEntity> s);

    IntygInfoListDTO map(IntygInfoEntity value);
}
