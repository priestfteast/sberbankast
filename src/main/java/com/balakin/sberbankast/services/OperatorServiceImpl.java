package com.balakin.sberbankast.services;


import com.balakin.sberbankast.domain.Operator;
import com.balakin.sberbankast.repositories.OperatorRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
public class OperatorServiceImpl implements OperatorService {
    private final OperatorRepository operatorRepository;

    public OperatorServiceImpl(OperatorRepository operatorRepository) {
        this.operatorRepository = operatorRepository;
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
            throw new RuntimeException(String.format("There is no operator wit id %f",l));
        return operatorOptional.get();
    }
}
