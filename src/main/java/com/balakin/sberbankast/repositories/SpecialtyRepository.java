package com.balakin.sberbankast.repositories;

import com.balakin.sberbankast.domain.Specialty;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface SpecialtyRepository extends CrudRepository<Specialty,Long> {
    Optional<Specialty> findByDescription(String description);
}
