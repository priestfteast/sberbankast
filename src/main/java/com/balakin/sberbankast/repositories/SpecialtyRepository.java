package com.balakin.sberbankast.repositories;

import com.balakin.sberbankast.domain.Specialty;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface SpecialtyRepository extends CrudRepository<Specialty,Long> {
    Optional<Specialty> findByDescription(String description);

}
