package se.inera.intyg.intygsadmin.web.service.banner;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import se.inera.intyg.intygsadmin.persistence.entity.BannerEntity;
import se.inera.intyg.intygsadmin.persistence.service.BannerPersistenceServiceImpl;
import se.inera.intyg.intygsadmin.web.controller.dto.BannerDTO;

@Service
public class BannerService {

    private BannerPersistenceServiceImpl bannerPersistenceService;

    public BannerService(BannerPersistenceServiceImpl bannerPersistenceService) {
        this.bannerPersistenceService = bannerPersistenceService;
    }

    public List<BannerDTO> getBanners(Pageable pageable) {

        Page<BannerEntity> page = bannerPersistenceService.findAll(pageable);

        return new ArrayList<>();
    }

    public BannerDTO createBanner(BannerDTO bannerDTO) {

        BannerEntity map = new BannerEntity();

        BannerEntity entity = bannerPersistenceService.create(map);

        return bannerDTO;
    }

    public BannerDTO save(BannerDTO bannerDTO) {

        BannerEntity map = new BannerEntity();

        BannerEntity entity = bannerPersistenceService.update(map);

        return bannerDTO;
    }

    public boolean deleteBanner(Long id) {
        bannerPersistenceService.delete(id);

        return true;
    }
}
