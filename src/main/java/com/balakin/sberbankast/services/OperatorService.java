package com.balakin.sberbankast.services;

import com.balakin.sberbankast.domain.Operator;

import java.util.Set;

public interface OperatorService {

    Set<Operator> getOperators();
    Operator findById(Long l);
}
