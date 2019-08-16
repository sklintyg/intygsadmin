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

package se.inera.intyg.intygsadmin.persistence.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import se.inera.intyg.intygsadmin.persistence.TestContext;
import se.inera.intyg.intygsadmin.persistence.TestSupport;
import se.inera.intyg.intygsadmin.persistence.entity.BannerEntity;
import se.inera.intyg.intygsadmin.persistence.enums.Application;
import se.inera.intyg.intygsadmin.persistence.enums.BannerPriority;
import se.inera.intyg.intygsadmin.persistence.repository.BannerRepository;

@TestContext
public class BannerPersistenceServiceTest extends TestSupport {

    @Autowired
    private BannerRepository bannerRepository;

    @Autowired
    private BannerPersistenceService bannerPersistenceService;

    private final int pageSize = 5;
    private final int total = 3 * pageSize;

    @BeforeEach
    public void before() {
        bannerRepository.deleteAll();
        randomizer()
            .objects(BannerEntity.class, total)
            .forEach(bannerPersistenceService::create);
    }

    @Test
    public void findBannersTest() {
        Pageable pageable = PageRequest.of(0, pageSize);

        Page<BannerEntity> list = bannerPersistenceService.findAll(pageable);
        assertEquals(total, list.getTotalElements());
        assertEquals(3, list.getTotalPages());
        assertEquals(0, list.getPageable().getPageNumber());

        list = bannerPersistenceService.findAll(PageRequest.of(1, 10));
        assertEquals(total, list.getTotalElements());
        assertEquals(2, list.getTotalPages());
        assertEquals(1, list.getPageable().getPageNumber());
    }

    @Test
    public void findActiveAndFutureBannersTest() {
        LocalDateTime localDateTime = LocalDateTime.now();

        List<BannerEntity> list = bannerPersistenceService.findActiveAndFuture(localDateTime, Application.WEBCERT);
        int beforeSize = list.size();

        BannerEntity entity = new BannerEntity();
        entity.setMessage("Test message");
        entity.setApplication(Application.WEBCERT);
        entity.setPriority(BannerPriority.HOG);
        entity.setDisplayFrom(localDateTime.minusDays(30));
        entity.setDisplayTo(localDateTime.minusDays(10));

        bannerPersistenceService.create(entity);

        BannerEntity currentEntity = new BannerEntity();
        currentEntity.setMessage("Test message");
        currentEntity.setApplication(Application.WEBCERT);
        currentEntity.setPriority(BannerPriority.HOG);
        currentEntity.setDisplayFrom(localDateTime);
        currentEntity.setDisplayTo(localDateTime.plusDays(30));

        bannerPersistenceService.create(currentEntity);

        List<BannerEntity> listAfter = bannerPersistenceService.findActiveAndFuture(localDateTime, Application.WEBCERT);

        assertEquals(beforeSize + 1, listAfter.size());
    }

    @Test
    public void createDeleteBannersTest() {
        BannerEntity entity = new BannerEntity();
        entity.setMessage("Test message");
        entity.setApplication(Application.WEBCERT);
        entity.setPriority(BannerPriority.HOG);
        entity.setDisplayFrom(LocalDateTime.MIN);
        entity.setDisplayTo(LocalDateTime.MAX);

        // Create
        BannerEntity savedEntity = bannerPersistenceService.create(entity);
        assertNotNull(savedEntity.getId());

        // Load
        BannerEntity loadedEntity = bannerPersistenceService.findOne(savedEntity.getId()).orElse(null);
        assertNotNull(loadedEntity);
        assertEquals(entity.getMessage(), loadedEntity.getMessage());

        // Update
        loadedEntity.setMessage("new Message");
        bannerPersistenceService.update(loadedEntity);

        // Load updated
        BannerEntity updatedEntity = bannerPersistenceService.findOne(loadedEntity.getId()).orElse(null);
        assertNotNull(updatedEntity);
        assertEquals(loadedEntity.getMessage(), updatedEntity.getMessage());

        // Remove
        bannerPersistenceService.delete(savedEntity.getId());

        Optional<BannerEntity> afterDeleteEntity = bannerPersistenceService.findOne(savedEntity.getId());
        assertFalse(afterDeleteEntity.isPresent());
    }

    @Test
    public void countByApplicationAndTime_emptyRepo() {
        bannerRepository.deleteAll();

        LocalDateTime now = LocalDateTime.now();
        long count = bannerPersistenceService.countByApplicationAndTime(Application.WEBCERT, now, now.plusDays(1), null);

        assertEquals(0, count);
    }

    @Test
    public void countByApplicationAndTime() {
        bannerRepository.deleteAll();

        Application application = Application.WEBCERT;

        LocalDateTime now = LocalDateTime.now();

        BannerEntity banner = randomize(BannerEntity.class);
        banner.setApplication(application);
        banner.setDisplayFrom(now);
        banner.setDisplayTo(now.plusDays(3));

        banner = bannerRepository.save(banner);

        // Create future banner
        long futureCount = bannerPersistenceService.countByApplicationAndTime(application, now.plusDays(10), now.plusDays(11), null);
        assertEquals(0, futureCount);

        // Create past banner
        long pastCount = bannerPersistenceService.countByApplicationAndTime(application, now.minusDays(3), now.minusDays(1), null);
        assertEquals(0, pastCount);

        // Update banner
        long updateCount = bannerPersistenceService
            .countByApplicationAndTime(application, banner.getDisplayFrom(), banner.getDisplayTo(), banner.getId());
        assertEquals(0, updateCount);

        long updateCount2 = bannerPersistenceService
            .countByApplicationAndTime(application, banner.getDisplayFrom().minusDays(1), banner.getDisplayTo(), banner.getId());
        assertEquals(0, updateCount2);

        // Another application banner
        long otherAppCount = bannerPersistenceService
            .countByApplicationAndTime(Application.REHABSTOD, banner.getDisplayFrom(), banner.getDisplayTo(), null);
        assertEquals(0, otherAppCount);

        // Same dates
        long sameDatesCount = bannerPersistenceService
            .countByApplicationAndTime(application, banner.getDisplayFrom(), banner.getDisplayTo(), null);
        assertEquals(1, sameDatesCount);

        // To in interval
        long toInIntervalCount = bannerPersistenceService.countByApplicationAndTime(application, now.minusDays(3), now.plusDays(1), null);
        assertEquals(1, toInIntervalCount);

        // From in interval
        long fromInIntervalCount = bannerPersistenceService.countByApplicationAndTime(application, now.plusDays(1), now.plusDays(10), null);
        assertEquals(1, fromInIntervalCount);

        // Existing banner in interval
        long existingInIntervalCount = bannerPersistenceService
            .countByApplicationAndTime(application, now.minusDays(1), now.plusDays(5), null);
        assertEquals(1, existingInIntervalCount);

        // From and To in interval
        long fromAndToInIntervalCount = bannerPersistenceService
            .countByApplicationAndTime(application, now.plusDays(1), now.plusDays(2), null);
        assertEquals(1, fromAndToInIntervalCount);
    }

}
