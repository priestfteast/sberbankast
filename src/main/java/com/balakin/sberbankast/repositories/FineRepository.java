package com.balakin.sberbankast.repositories;

import com.balakin.sberbankast.domain.Fine;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;

public interface FineRepository extends CrudRepository<Fine,Long> {
    Set<Fine> findAllByOperatorId(Long id);
}
