package com.balakin.sberbankast.services;

import com.balakin.sberbankast.commands.BonusCommand;
import com.balakin.sberbankast.commands.FineCommand;
import com.balakin.sberbankast.converters.BonusToBonusCommand;
import com.balakin.sberbankast.converters.FineToFineCommand;
import com.balakin.sberbankast.domain.Operator;
import com.balakin.sberbankast.repositories.OperatorRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class BonusServiceImpl implements BonusService {
    private final BonusToBonusCommand bonusToBonusCommand;
    private final OperatorRepository operatorRepository;



    public BonusServiceImpl(BonusToBonusCommand bonusToBonusCommand, OperatorRepository operatorRepository) {
        this.bonusToBonusCommand = bonusToBonusCommand;
        this.operatorRepository = operatorRepository;
    }


    @Override
    public BonusCommand findByOperatorIdAndBonusId(Long operatorId, Long bonusId) {

        Optional<Operator> operatorOptional = operatorRepository.findById(operatorId);

        if (!operatorOptional.isPresent()){
            //todo impl error handling
            log.error("operator id not found. Id: " + operatorId);
        }

        Operator operator = operatorOptional.get();

        Optional<BonusCommand> bonusCommandOptional = operator.getBonuses().stream()
                .filter(bonus -> bonus.getId().equals(bonusId))
                .map( bonus -> bonusToBonusCommand.convert(bonus)).findFirst();

        if(!bonusCommandOptional.isPresent()){
            //todo impl error handling
            log.error("Bonus id not found: " + bonusId);
        }

        return bonusCommandOptional.get();
    }


}