package com.balakin.sberbankast.services;


import com.balakin.sberbankast.commands.OperatorCommand;
import com.balakin.sberbankast.converters.OperatorCommandToOperator;
import com.balakin.sberbankast.converters.OperatorToOperatorCommand;
import com.balakin.sberbankast.domain.Operator;
import com.balakin.sberbankast.exceptions.NotFoundException;
import com.balakin.sberbankast.repositories.OperatorRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Service
public class OperatorServiceImpl implements OperatorService {
    private final OperatorRepository operatorRepository;
    private final OperatorCommandToOperator operatorCommandToOperator;
    private final OperatorToOperatorCommand operatorToOperatorCommand;

    public OperatorServiceImpl(OperatorRepository operatorRepository, OperatorCommandToOperator operatorCommandToOperator, OperatorToOperatorCommand operatorToOperatorCommand) {
        this.operatorRepository = operatorRepository;
        this.operatorCommandToOperator = operatorCommandToOperator;
        this.operatorToOperatorCommand = operatorToOperatorCommand;
    }

    @Override
    public List<Operator> getOperatorsByName() {
        log.debug("we are in service");
        List<Operator> operators = new ArrayList<>();
        operatorRepository.findAllByOrderByLastName().forEach(operators::add);

        return operators;
    }

    @Override
    public List<Operator> getOperatorsByEmployementDate() {
        log.debug("we are in service");
        List<Operator> operators = new ArrayList<>();
        operatorRepository.findAllByOrderByEmployementDate().forEach(operators::add);

        return operators;
    }

    @Override
    public List<Operator> getOperatorsBySpecialties() {
        log.debug("we are in service");
        List<Operator> operators = new ArrayList<>();
        operatorRepository.findAll().forEach(operators::add);
        operators.sort(new Comparator<Operator>() {
            @Override
            public int compare(Operator o1, Operator o2) {
                if(o1.getSpecialties().size()>o2.getSpecialties().size())
                return -1;
                else
                    return 1;
            }
        });

        return operators;
    }



    @Override
    public Operator findById(Long l) {

        Optional<Operator> operatorOptional = operatorRepository.findById(l);
        if(!operatorOptional.isPresent())
//            throw new RuntimeException(String.format("There is no operator wit id %f",l));
            throw new NotFoundException("There is no operator with ID: "+l.toString());

        return operatorOptional.get();
    }

    @Override
    public OperatorCommand findCommandById(Long l) {
        return operatorToOperatorCommand.convert(findById(l));
    }

    @Override
    @Transactional
    public OperatorCommand saveOperatorCommand(OperatorCommand operatorCommand){
        Operator detachedOperator = operatorCommandToOperator.convert(operatorCommand);
        Operator savedOperator = operatorRepository.save(detachedOperator);

        return operatorToOperatorCommand.convert(savedOperator);
    }

    @Override
    public void deleteById(Long idToDelete) {
        operatorRepository.deleteById(idToDelete);
    }
}
