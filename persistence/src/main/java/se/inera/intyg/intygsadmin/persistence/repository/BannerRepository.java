package se.inera.intyg.intygsadmin.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import se.inera.intyg.intygsadmin.persistence.entity.BannerEntity;

@Repository
public interface BannerRepository extends JpaRepository<BannerEntity, Long>,
        QuerydslPredicateExecutor<BannerEntity> {

}
