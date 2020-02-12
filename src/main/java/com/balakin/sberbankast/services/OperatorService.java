package com.balakin.sberbankast.services;

import com.balakin.sberbankast.commands.OperatorCommand;
import com.balakin.sberbankast.domain.Operator;

import java.util.List;


public interface OperatorService {

    List<Operator> getOperatorsByName();
    List<Operator> getOperatorsByEmployementDate();
    List<Operator> getOperatorsBySpecialties();
    Operator findById(Long l);
    OperatorCommand findCommandById(Long l);
    OperatorCommand saveOperatorCommand(OperatorCommand operatorCommand);
    void deleteById(Long idToDelete);

}
