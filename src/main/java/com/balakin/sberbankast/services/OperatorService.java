package com.balakin.sberbankast.services;

import com.balakin.sberbankast.commands.OperatorCommand;
import com.balakin.sberbankast.domain.Operator;

import java.util.Set;

public interface OperatorService {

    Set<Operator> getOperators();
    Operator findById(Long l);
    OperatorCommand findCommandById(Long l);
    OperatorCommand saveOperatorCommand(OperatorCommand operatorCommand);
}
