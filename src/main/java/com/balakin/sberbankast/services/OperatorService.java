package com.balakin.sberbankast.services;

import com.balakin.sberbankast.commands.OperatorCommand;
import com.balakin.sberbankast.domain.Operator;
import com.balakin.sberbankast.domain.Specialty;

import java.util.List;


public interface OperatorService {

    List<Operator> getOperators(String request);
    List<Operator> getOperatorsBySpecialties();
    Operator findById(Long l);
    OperatorCommand findCommandById(Long l);
    OperatorCommand saveOperatorCommand(OperatorCommand operatorCommand);
    void deleteById(Long idToDelete);
    List<Specialty> getSpecialties(Long id);

    void fireById(Long id);
}
