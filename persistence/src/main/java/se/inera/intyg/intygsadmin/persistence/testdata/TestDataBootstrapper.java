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

package se.inera.intyg.intygsadmin.persistence.testdata;

import java.lang.invoke.MethodHandles;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import se.inera.intyg.intygsadmin.persistence.entity.BannerEntity;
import se.inera.intyg.intygsadmin.persistence.enums.Application;
import se.inera.intyg.intygsadmin.persistence.enums.BannerPriority;
import se.inera.intyg.intygsadmin.persistence.repository.BannerRepository;

@Component
@Profile({"dev", "init-bootstrap-data"})
@Transactional
public class TestDataBootstrapper {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private BannerRepository bannerRepository;

    public TestDataBootstrapper(BannerRepository bannerRepository) {
        this.bannerRepository = bannerRepository;
    }

    @PostConstruct
    void init() {
        bootstrapBanners();
    }

    private void bootstrapBanners() {
        List<BannerEntity> bannerEntities = new ArrayList<>();

        generateBanners(Application.WEBCERT, bannerEntities);
        generateBanners(Application.STATISTIK, bannerEntities);
        generateBanners(Application.REHABSTOD, bannerEntities);


        bannerRepository.saveAll(bannerEntities);

        LOG.info("Finished: Bootstrap data");
    }

    private void generateBanners(Application application, List<BannerEntity> bannerEntities) {
        BannerEntity currentEntity = new BannerEntity();
        currentEntity.setMessage("Current test message " + application);
        currentEntity.setApplication(application);
        currentEntity.setPriority(BannerPriority.HIGH);
        currentEntity.setDisplayFrom(randomizePastDate());
        currentEntity.setDisplayTo(randomizeFutureDate());

        BannerEntity futureEntity = new BannerEntity();
        futureEntity.setMessage("Future test message " + application);
        futureEntity.setApplication(application);
        futureEntity.setPriority(BannerPriority.MEDIUM);
        futureEntity.setDisplayFrom(randomizeFutureDate());
        futureEntity.setDisplayTo(randomizeFutureDate(futureEntity.getDisplayFrom()));

        BannerEntity prevEntity = new BannerEntity();
        prevEntity.setMessage("Past test message " + application);
        prevEntity.setApplication(application);
        prevEntity.setPriority(BannerPriority.LOW);
        prevEntity.setDisplayTo(randomizePastDate());
        prevEntity.setDisplayFrom(randomizePastDate(prevEntity.getDisplayTo()));

        bannerEntities.add(currentEntity);
        bannerEntities.add(futureEntity);
        bannerEntities.add(prevEntity);
    }

    private LocalDateTime randomizePastDate() {
        return randomizePastDate(LocalDateTime.now());
    }

    private LocalDateTime randomizePastDate(LocalDateTime fromDate) {
        var daySpan = 30;
        return fromDate.minusDays(new Random().nextInt(daySpan + 1));
    }

    private LocalDateTime randomizeFutureDate() {
        return randomizeFutureDate(LocalDateTime.now());
    }

    private LocalDateTime randomizeFutureDate(LocalDateTime fromDate) {
        var daySpan = 30;
        return fromDate.plusDays(new Random().nextInt(daySpan + 1));
    }
}