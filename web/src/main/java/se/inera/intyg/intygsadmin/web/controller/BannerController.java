package se.inera.intyg.intygsadmin.web.controller;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import se.inera.intyg.intygsadmin.web.controller.dto.BannerDTO;
import se.inera.intyg.intygsadmin.web.service.banner.BannerService;

@RestController
@RequestMapping("/api/banner")
public class BannerController {

    private BannerService bannerService;

    public BannerController(BannerService bannerService) {
        this.bannerService = bannerService;
    }

    @GetMapping
    public ResponseEntity<List<BannerDTO>> listBanners(Pageable pageable) {
        List<BannerDTO> bannerDTOS = bannerService.getBanners(pageable);

        return ResponseEntity.ok(bannerDTOS);
    }
}
