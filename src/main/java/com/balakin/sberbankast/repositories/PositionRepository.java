package com.balakin.sberbankast.repositories;


import com.balakin.sberbankast.domain.Position;
import org.springframework.data.repository.CrudRepository;

public interface PositionRepository extends CrudRepository<Position, Long> {
    Position findPositionByYear(String year);
}
