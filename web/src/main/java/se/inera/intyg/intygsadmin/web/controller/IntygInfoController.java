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
package se.inera.intyg.intygsadmin.web.controller;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.inera.intyg.intygsadmin.web.controller.dto.IntygInfoDTO;
import se.inera.intyg.intygsadmin.web.controller.dto.IntygInfoListDTO;
import se.inera.intyg.intygsadmin.web.service.IntygInfoService;

@RestController
@RequestMapping("/api/intygInfo")
public class IntygInfoController {

    private IntygInfoService intygInfoService;

    public IntygInfoController(IntygInfoService intygInfoService) {
        this.intygInfoService = intygInfoService;
    }

    @GetMapping
    public ResponseEntity<Page<IntygInfoListDTO>> listHistory(
        @PageableDefault(size = 20, sort = "createdAt")
        Pageable pageable) {
        Page<IntygInfoListDTO> intygInfoList = intygInfoService.getIntygInfoList(pageable);

        return ResponseEntity.ok(intygInfoList);
    }

    @GetMapping("/{intygId}")
    public ResponseEntity<IntygInfoDTO> getIntygInfo(@PathVariable String intygId) {

        Optional<IntygInfoDTO> intygInfoDTO = intygInfoService.getIntygInfo(intygId);
        if (!intygInfoDTO.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(intygInfoDTO.get());
    }

}
