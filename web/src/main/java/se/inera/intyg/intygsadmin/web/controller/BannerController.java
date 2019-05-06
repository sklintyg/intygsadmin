package se.inera.intyg.intygsadmin.web.controller;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    @PutMapping("}")
    public ResponseEntity<BannerDTO> createBanner(@RequestBody BannerDTO bannerDTO) {
        BannerDTO savedDTO = bannerService.createBanner(bannerDTO);

        return ResponseEntity.ok(savedDTO);
    }

    @PostMapping("/{id}")
    public ResponseEntity<BannerDTO> updateBanner(@PathVariable Long id, @RequestBody BannerDTO bannerDTO) {
        bannerDTO.setId(id);
        BannerDTO savedDTO = bannerService.save(bannerDTO);

        return ResponseEntity.ok(savedDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteBanner(@PathVariable Long id) {
        bannerService.deleteBanner(id);

        return ResponseEntity.ok().build();
    }
}
