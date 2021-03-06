package com.balakin.sberbankast.repositories;

import com.balakin.sberbankast.domain.Operator;
import com.balakin.sberbankast.domain.Specialty;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface OperatorRepository extends CrudRepository<Operator,Long> {

    Operator findByLastName(String lastName);


    int countAllByIdNotNull();
    int countAllByOutgoingTrue();
    int countAllByIncomingTrue();
    int countAllByStakeTrue();

    Operator findByNumber(String number);
    Operator findByAdditionalNumber(String number);

    Iterable<Operator> findAllByOrderByLastName();

    Iterable<Operator> findAllByOrderByEmployementDate();



}
