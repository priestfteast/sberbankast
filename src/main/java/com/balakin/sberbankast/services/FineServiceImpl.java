package com.balakin.sberbankast.services;

import com.balakin.sberbankast.commands.FineCommand;
import com.balakin.sberbankast.converters.FineCommandToFine;
import com.balakin.sberbankast.converters.FineToFineCommand;
import com.balakin.sberbankast.domain.Fine;
import com.balakin.sberbankast.domain.Operator;
import com.balakin.sberbankast.repositories.FineRepository;
import com.balakin.sberbankast.repositories.OperatorRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
public class FineServiceImpl implements FineService {
    private final FineToFineCommand fineToFineCommand;
    private final FineCommandToFine fineCommandToFine;
    private final OperatorRepository operatorRepository;
    private final FineRepository fineRepository;

    public FineServiceImpl(FineToFineCommand fineToFineCommand, FineCommandToFine fineCommandToFine, OperatorRepository operatorRepository, FineRepository fineRepository) {
        this.fineToFineCommand = fineToFineCommand;
        this.fineCommandToFine = fineCommandToFine;
        this.operatorRepository = operatorRepository;
        this.fineRepository = fineRepository;
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

    @Override
    @Transactional
    public FineCommand saveFineCommand(FineCommand command) {

        Optional<Operator> operatorOptional = operatorRepository.findById(command.getOperatorId());

        if (!operatorOptional.isPresent()) {

            //todo toss error if not found!
            log.error("Operator not found for id: " + command.getOperatorId());
            return new FineCommand();
        } else {
            Operator operator = operatorOptional.get();

            Optional<Fine> fineOptional = operator
                    .getFines()
                    .stream()
                    .filter(fine -> fine.getId().equals(command.getId()))
                    .findFirst();

            if (fineOptional.isPresent()) {
                Fine fineFound = fineOptional.get();
                fineFound.setDescription(command.getDescription());
                fineFound.setSize(command.getSize());

            } else {
                //add new Bonus
                Fine fine = fineCommandToFine.convert(command);
                operator.addFine(fine);
            }

            Operator savedOperator = operatorRepository.save(operator);

            Optional<Fine> savedOptionalFine = savedOperator.getFines().stream()
                    .filter(operatorFine -> operatorFine.getId().equals(command.getId()))
                    .findFirst();

            //check by description
            if (!savedOptionalFine.isPresent()) {
                //not totally safe... But best guess
                savedOptionalFine = savedOperator.getFines().stream()
                        .filter(operatorFine -> operatorFine.getDescription().equals(command.getDescription()))
                        .filter(operatorFine -> operatorFine.getSize().equals(command.getSize()))
                        .findFirst();
            }

            //to do check for fail
            return fineToFineCommand.convert(savedOptionalFine.get());


        }
    }

    @Override
    public void deleteById(Long operatorId, Long idToDelete) {
        log.debug("Deleting fine: " + operatorId + ":" + idToDelete);

        if(fineRepository.findById(idToDelete).isPresent()){
            fineRepository.deleteById(fineRepository.findById(idToDelete).get().getId());

        } else {
            log.debug("Fine Id Not found. Id:" + idToDelete);
        }
    }


}