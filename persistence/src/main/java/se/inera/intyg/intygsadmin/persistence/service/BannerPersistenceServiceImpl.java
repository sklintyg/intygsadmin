package se.inera.intyg.intygsadmin.persistence.service;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import se.inera.intyg.intygsadmin.persistence.entity.BannerEntity;
import se.inera.intyg.intygsadmin.persistence.repository.BannerRepository;

@Service
@Transactional
public class BannerPersistenceServiceImpl {

    private BannerRepository bannerRepository;

    public BannerPersistenceServiceImpl(BannerRepository bannerRepository) {
        this.bannerRepository = bannerRepository;
    }

    public Page<BannerEntity> findAll(Pageable pageable) {
        BooleanBuilder builder = new BooleanBuilder();
        Predicate predicate = builder.getValue();

        Page<BannerEntity> allBanners = bannerRepository.findAll(predicate, pageable);

        return allBanners;
    }

    public BannerEntity create(BannerEntity bannerEntity) {
        return bannerRepository.save(bannerEntity);
    }

    public BannerEntity update(BannerEntity bannerEntity) {
        return bannerRepository.save(bannerEntity);
    }

    public void delete(Long id) {
        bannerRepository.deleteById(id);
    }
}
