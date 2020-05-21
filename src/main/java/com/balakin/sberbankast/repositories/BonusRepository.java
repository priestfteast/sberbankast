package com.balakin.sberbankast.repositories;

import com.balakin.sberbankast.domain.Bonus;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface BonusRepository extends CrudRepository<Bonus, Long> {
    Set<Bonus> findAllByOperatorId(Long id);
}
