package com.balakin.sberbankast.services;

import com.balakin.sberbankast.commands.FineCommand;
import com.balakin.sberbankast.converters.FineToFineCommand;
import com.balakin.sberbankast.domain.Operator;
import com.balakin.sberbankast.repositories.OperatorRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class FineServiceImpl implements FineService {
    private final FineToFineCommand fineToFineCommand;
    private final OperatorRepository operatorRepository;

    public FineServiceImpl(FineToFineCommand fineToFineCommand, OperatorRepository operatorRepository) {
        this.fineToFineCommand = fineToFineCommand;
        this.operatorRepository = operatorRepository;
    }


    @Override
    public FineCommand findByOperatorIdAndFineId(Long operatorId, Long fineId) {

        Optional<Operator> operatorOptional = operatorRepository.findById(operatorId);

        if (!operatorOptional.isPresent()){
            //todo impl error handling
            log.error("operator id not found. Id: " + operatorId);
        }

        Operator operator = operatorOptional.get();

        Optional<FineCommand> fineCommandOptional = operator.getFines().stream()
                .filter(fine -> fine.getId().equals(fineId))
                .map( fine -> fineToFineCommand.convert(fine)).findFirst();

        if(!fineCommandOptional.isPresent()){
            //todo impl error handling
            log.error("Fine id not found: " + fineId);
        }

        return fineCommandOptional.get();
    }


}