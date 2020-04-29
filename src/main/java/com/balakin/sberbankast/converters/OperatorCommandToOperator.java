package com.balakin.sberbankast.converters;

import com.balakin.sberbankast.commands.OperatorCommand;
import com.balakin.sberbankast.domain.Operator;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class OperatorCommandToOperator implements Converter<OperatorCommand, Operator> {

    private final NotesCommandToNotes notesCommandToNotes;
    private final SpecialtyCommandToSpecialty specialtyCommandToSpecialty;
    private final BonusCommandToBonus bonusCommandToBonus;
    private final FineCommandToFine fineCommandToFine;

    public OperatorCommandToOperator(NotesCommandToNotes notesCommandToNotes, SpecialtyCommandToSpecialty specialtyCommandToSpecialty, BonusCommandToBonus bonusCommandToBonus, FineCommandToFine fineCommandToFine) {
        this.notesCommandToNotes = notesCommandToNotes;
        this.specialtyCommandToSpecialty = specialtyCommandToSpecialty;
        this.bonusCommandToBonus = bonusCommandToBonus;
        this.fineCommandToFine = fineCommandToFine;
    }

    @Nullable
    @Synchronized
    @Override
    public Operator convert(OperatorCommand operatorCommand) {
        if(operatorCommand==null) {
            return null;
        }



        Operator operator = new Operator();
        operator.setId(operatorCommand.getId());
        operator.setFirstName(operatorCommand.getFirstName());
        operator.setLastName(operatorCommand.getLastName());
        operator.setEmployementDate(operatorCommand.getEmployementDate());
//        System.out.println(operator.getEmployementDate());
        operator.setNumber(operatorCommand.getNumber());
        if(operatorCommand.getAdditionalNumber().equals("")) {
            operator.setAdditionalNumber(null);
        }
            else {
            operator.setAdditionalNumber(operatorCommand.getAdditionalNumber());
        }
        operator.setShift(operatorCommand.getShift());
        operator.setCompany(operatorCommand.getCompany());
        operator.setContractType(operatorCommand.getContractType());
        operator.setIncoming(operatorCommand.isIncoming());
        operator.setOutgoing(operatorCommand.isOutgoing());
        operator.setStake(operatorCommand.isStake());
        operator.setCard(operatorCommand.isCard());
        operator.setNotes(notesCommandToNotes.convert(operatorCommand.getNotes()));

        if(operatorCommand.getSpecialties()!=null && operatorCommand.getSpecialties().size()>0){
            operatorCommand.getSpecialties().
                    forEach(specialty -> operator.getSpecialties().add(specialtyCommandToSpecialty.convert(specialty)));
        }

        if(operatorCommand.getBonuses()!=null&&operatorCommand.getBonuses().size()>0){
            operatorCommand.getBonuses().
                    forEach(bonusCommand -> operator.getBonuses().add(bonusCommandToBonus.convert(bonusCommand)));
        }

        if(operatorCommand.getFines()!=null&&operatorCommand.getFines().size()>0){
            operatorCommand.getFines().
                    forEach(fineCommand -> operator.getFines().add(fineCommandToFine.convert(fineCommand)));
        }

        return operator;
    }
}
