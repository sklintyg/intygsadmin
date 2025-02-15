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
package se.inera.intyg.intygsadmin.persistence.testdata;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import java.io.IOException;
import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;
import se.inera.intyg.infra.driftbannerdto.Application;
import se.inera.intyg.intygsadmin.persistence.entity.BannerEntity;
import se.inera.intyg.intygsadmin.persistence.entity.UserEntity;
import se.inera.intyg.intygsadmin.persistence.enums.BannerPriority;
import se.inera.intyg.intygsadmin.persistence.repository.BannerRepository;
import se.inera.intyg.intygsadmin.persistence.repository.UserRepository;

@Component
@Profile({"dev", "init-bootstrap-data"})
@Transactional
public class TestDataBootstrapper {

    public static final int DAY_SPAN = 10;
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final Random RANDOM = new Random();

    private final BannerRepository bannerRepository;
    private final UserRepository userRepository;

    @Autowired
    public TestDataBootstrapper(BannerRepository bannerRepository, UserRepository userRepository) {
        this.bannerRepository = bannerRepository;
        this.userRepository = userRepository;
    }

    @PostConstruct
    void init() {
        bootstrapBanners();
        bootstrapUsers();
    }

    private void bootstrapUsers() {

        try {

            PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            Resource[] resources = resolver.getResources("classpath:bootstrap/users/*.json");

            List<UserEntity> userEntities = new ArrayList<>();
            ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            for (Resource resource : resources) {
                try (InputStream jsonUserStream = resource.getInputStream()) {
                    var userEntity = objectMapper.readValue(jsonUserStream, UserEntity.class);
                    if (userRepository.findByEmployeeHsaId(userEntity.getEmployeeHsaId()).isEmpty()) {
                        userEntities.add(userEntity);
                    }
                }
            }

            userRepository.saveAll(userEntities);

            LOG.info("Finished: Bootstrap USER data. {} entries added", userEntities.size());
        } catch (IOException e) {
            throw new RuntimeException("Failed bootstrapping users");
        }

    }

    private void bootstrapBanners() {
        List<BannerEntity> bannerEntities = new ArrayList<>();

        generateBanners(Application.WEBCERT, bannerEntities);
        generateBanners(Application.INTYGSSTATISTIK, bannerEntities);
        generateBanners(Application.REHABSTOD, bannerEntities);

        bannerEntities.sort((b1, b2) -> {
            if (b1.getDisplayTo().isEqual(b2.getDisplayTo())) {
                return 0;
            }

            return b1.getDisplayTo().isBefore(b2.getDisplayTo()) ? -1 : 1;
        });

        bannerRepository.saveAll(bannerEntities);

        LOG.info("Finished: Bootstrap BANNER data. {} entries added", bannerEntities.size());
    }

    private void generateBanners(Application application, List<BannerEntity> bannerEntities) {
        LocalDateTime now = LocalDateTime.now();
        if (bannerRepository.countByApplicationEquals(application, now.minusYears(100), now.plusYears(100)) == 0) {
            BannerEntity currentEntity = new BannerEntity();
            currentEntity.setMessage("Current test message " + application);
            currentEntity.setApplication(application);
            currentEntity.setPriority(BannerPriority.HOG);
            currentEntity.setDisplayFrom(randomizePastDate());
            currentEntity.setDisplayTo(randomizeFutureDate());

            BannerEntity futureEntity = new BannerEntity();
            futureEntity.setMessage("Future test message " + application);
            futureEntity.setApplication(application);
            futureEntity.setPriority(BannerPriority.MEDEL);
            futureEntity.setDisplayFrom(randomizeFutureDate(currentEntity.getDisplayTo()));
            futureEntity.setDisplayTo(randomizeFutureDate(futureEntity.getDisplayFrom()));

            BannerEntity prevEntity = new BannerEntity();
            prevEntity.setMessage("Past test message " + application);
            prevEntity.setApplication(application);
            prevEntity.setPriority(BannerPriority.LAG);
            prevEntity.setDisplayTo(randomizePastDate(currentEntity.getDisplayFrom()));
            prevEntity.setDisplayFrom(randomizePastDate(prevEntity.getDisplayTo()));

            bannerEntities.add(prevEntity);
            bannerEntities.add(currentEntity);
            bannerEntities.add(futureEntity);
        }
    }

    private LocalDateTime randomizePastDate() {
        return randomizePastDate(LocalDateTime.now());
    }

    private LocalDateTime randomizePastDate(LocalDateTime fromDate) {
        return fromDate.minusDays(RANDOM.nextInt(DAY_SPAN + 1) + 1);
    }

    private LocalDateTime randomizeFutureDate() {
        return randomizeFutureDate(LocalDateTime.now());
    }

    private LocalDateTime randomizeFutureDate(LocalDateTime fromDate) {
        return fromDate.plusDays(RANDOM.nextInt(DAY_SPAN + 1) + 1);
    }
}
