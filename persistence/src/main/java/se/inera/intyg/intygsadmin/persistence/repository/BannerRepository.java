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
package se.inera.intyg.intygsadmin.persistence.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import se.inera.intyg.infra.driftbannerdto.Application;
import se.inera.intyg.intygsadmin.persistence.entity.BannerEntity;

@Repository
public interface BannerRepository extends JpaRepository<BannerEntity, UUID>,
    QuerydslPredicateExecutor<BannerEntity> {

    List<BannerEntity> findAllByApplicationEqualsAndDisplayToAfter(Application application, LocalDateTime displayTo);

    @Query(value = "SELECT count(t.id) FROM BannerEntity t WHERE t.application = :application AND t.id <> :id AND "
        + "(t.displayFrom BETWEEN :fromDate AND :toDate OR t.displayTo BETWEEN :fromDate AND :toDate "
        + " OR :toDate BETWEEN t.displayFrom AND t.displayTo)")
    long countByApplicationEqualsAndIdNot(@Param("application") Application application, @Param("id") UUID id,
        @Param("fromDate") LocalDateTime fromDate, @Param("toDate") LocalDateTime toDate);

    @Query(value = "SELECT count(t.id) FROM BannerEntity t WHERE t.application = :application AND "
        + "(t.displayFrom BETWEEN :fromDate AND :toDate OR t.displayTo BETWEEN :fromDate AND :toDate "
        + " OR :toDate BETWEEN t.displayFrom AND t.displayTo)")
    long countByApplicationEquals(@Param("application") Application application,
        @Param("fromDate") LocalDateTime fromDate, @Param("toDate") LocalDateTime toDate);
}
