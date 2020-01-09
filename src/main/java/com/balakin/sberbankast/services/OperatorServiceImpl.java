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

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

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
    public Set<Operator> getOperators() {
        log.debug("we are in service");
        Set<Operator> operators = new HashSet<>();
        operatorRepository.findAll().forEach(operators::add);

        return operators;
    }

    @Override
    public Operator findById(Long l) {

        Optional<Operator> operatorOptional = operatorRepository.findById(l);
        if(!operatorOptional.isPresent())
//            throw new RuntimeException(String.format("There is no operator wit id %f",l));
            throw new NotFoundException(String.format("There is no operator with id %f",(double)l));
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
