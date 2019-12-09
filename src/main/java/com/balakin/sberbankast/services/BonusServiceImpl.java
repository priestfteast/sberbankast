package com.balakin.sberbankast.services;

import com.balakin.sberbankast.commands.BonusCommand;
import com.balakin.sberbankast.converters.BonusCommandToBonus;
import com.balakin.sberbankast.converters.BonusToBonusCommand;
import com.balakin.sberbankast.domain.Bonus;
import com.balakin.sberbankast.domain.Operator;
import com.balakin.sberbankast.repositories.BonusRepository;
import com.balakin.sberbankast.repositories.OperatorRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j
public class BonusServiceImpl implements BonusService {
    private final BonusToBonusCommand bonusToBonusCommand;
    private final BonusCommandToBonus bonusCommandToBonus;
    private final OperatorRepository operatorRepository;
    private final BonusRepository bonusRepository;




    public BonusServiceImpl(BonusToBonusCommand bonusToBonusCommand, BonusCommandToBonus bonusCommandToBonus, OperatorRepository operatorRepository, BonusRepository bonusRepository) {
        this.bonusToBonusCommand = bonusToBonusCommand;
        this.bonusCommandToBonus = bonusCommandToBonus;
        this.operatorRepository = operatorRepository;
        this.bonusRepository = bonusRepository;
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

    @Override
    @Transactional
    public BonusCommand saveBonusCommand(BonusCommand command) {

        Optional<Operator> operatorOptional = operatorRepository.findById(command.getOperatorId());

        if (!operatorOptional.isPresent()) {

            //todo toss error if not found!
            log.error("Operator not found for id: " + command.getOperatorId());
            return new BonusCommand();
        } else {
            Operator operator = operatorOptional.get();

            Optional<Bonus> bonusOptional = operator
                    .getBonuses()
                    .stream()
                    .filter(bonus -> bonus.getId().equals(command.getId()))
                    .findFirst();

            if (bonusOptional.isPresent()) {
                Bonus bonusFound = bonusOptional.get();
                bonusFound.setDescription(command.getDescription());
                bonusFound.setSize(command.getSize());

            } else {
                //add new Bonus
                Bonus bonus = bonusCommandToBonus.convert(command);
                operator.addBonus(bonus);
            }

            Operator savedOperator = operatorRepository.save(operator);

            Optional<Bonus> savedOptionalBonus = savedOperator.getBonuses().stream()
                    .filter(operatorsBonus -> operatorsBonus.getId().equals(command.getId()))
                    .findFirst();

            //check by description
            if (!savedOptionalBonus.isPresent()) {
                //not totally safe... But best guess
                savedOptionalBonus = savedOperator.getBonuses().stream()
                        .filter(operatorBonus -> operatorBonus.getDescription().equals(command.getDescription()))
                        .filter(operatorBonus -> operatorBonus.getSize().equals(command.getSize()))
                        .findFirst();
            }

            //to do check for fail
            return bonusToBonusCommand.convert(savedOptionalBonus.get());


        }
    }


    @Override
    public void deleteById(Long operatorId, Long idToDelete) {

        log.debug("Deleting bonus: " + operatorId + ":" + idToDelete);

        if(bonusRepository.findById(idToDelete).isPresent()){
                bonusRepository.deleteById(bonusRepository.findById(idToDelete).get().getId());

        } else {
            log.debug("Bonus Id Not found. Id:" + idToDelete);
        }

    }
}