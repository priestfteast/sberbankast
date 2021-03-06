package com.balakin.sberbankast.converters;

import com.balakin.sberbankast.commands.OperatorCommand;
import com.balakin.sberbankast.domain.Operator;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class OperatorToOperatorCommand implements Converter<Operator, OperatorCommand> {

    private final NotesToNotesCommand notesToNotesCommand;
    private final SpecialtyToSpecialtyCommand specialtyToSpecialtyCommand;
    private final BonusToBonusCommand bonusToBonusCommand;
    private final FineToFineCommand fineToFineCommand;

    public OperatorToOperatorCommand(NotesToNotesCommand notesToNotesCommand, SpecialtyToSpecialtyCommand specialtyToSpecialtyCommand, BonusToBonusCommand bonusToBonusCommand, FineToFineCommand fineToFineCommand) {
        this.notesToNotesCommand = notesToNotesCommand;
        this.specialtyToSpecialtyCommand = specialtyToSpecialtyCommand;
        this.bonusToBonusCommand = bonusToBonusCommand;
        this.fineToFineCommand = fineToFineCommand;
    }

    @Nullable
    @Synchronized
    @Override
    public OperatorCommand convert(Operator operator) {
        if(operator==null) {
            return null;
        }


        OperatorCommand operatorCommand = new OperatorCommand();
        operatorCommand.setId(operator.getId());
        operatorCommand.setFirstName(operator.getFirstName());
        operatorCommand.setLastName(operator.getLastName());
        operatorCommand.setEmployementDate(operator.getEmployementDate());
        operatorCommand.setNumber(operator.getNumber());
        operatorCommand.setAdditionalNumber(operator.getAdditionalNumber());
        operatorCommand.setIncoming(operator.isIncoming());
        operatorCommand.setOutgoing(operator.isOutgoing());
        operatorCommand.setStake(operator.isStake());
        operatorCommand.setCard(operator.isCard());
        operatorCommand.setShift(operator.getShift());
        operatorCommand.setCompany(operator.getCompany());
        operatorCommand.setContractType(operator.getContractType());
        operatorCommand.setImage(operator.getImage());
        operatorCommand.setNotes(notesToNotesCommand.convert(operator.getNotes()));

        if(operator.getSpecialties()!=null&&operator.getSpecialties().size()>0){
            operator.getSpecialties().
                    forEach(specialty -> operatorCommand.getSpecialties().add(specialtyToSpecialtyCommand.convert(specialty)));
        }

        if(operator.getBonuses()!=null&&operator.getBonuses().size()>0){
            operator.getBonuses().
                    forEach(bonus -> operatorCommand.getBonuses().add(bonusToBonusCommand.convert(bonus)));
        }

        if(operator.getFines()!=null&&operator.getFines().size()>0){
            operator.getFines().
                    forEach(fine -> operatorCommand.getFines().add(fineToFineCommand.convert(fine)));
        }

        return operatorCommand;
    }
}
