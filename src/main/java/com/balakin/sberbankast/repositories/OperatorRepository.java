package com.balakin.sberbankast.repositories;

import com.balakin.sberbankast.domain.Operator;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


public interface OperatorRepository extends CrudRepository<Operator,Long> {

    Operator findByLastName(String lastName);
}
