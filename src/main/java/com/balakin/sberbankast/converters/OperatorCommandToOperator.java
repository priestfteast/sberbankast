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

    public OperatorCommandToOperator(NotesCommandToNotes notesCommandToNotes, SpecialtyCommandToSpecialty specialtyCommandToSpecialty) {
        this.notesCommandToNotes = notesCommandToNotes;
        this.specialtyCommandToSpecialty = specialtyCommandToSpecialty;
    }

    @Nullable
    @Synchronized
    @Override
    public Operator convert(OperatorCommand operatorCommand) {
        if(operatorCommand==null) {
            return null;
        }
        LocalDate date = LocalDate.parse(operatorCommand.getEmployementDate());

        Operator operator = new Operator();
        operator.setId(operatorCommand.getId());
        operator.setFirstName(operatorCommand.getFirstName());
        operator.setLastName(operatorCommand.getLastName());
        operator.setEmployementDate(date);
        operator.setNumber(operatorCommand.getNumber());
        operator.setShift(operatorCommand.getShift());
        operator.setNotes(notesCommandToNotes.convert(operatorCommand.getNotes()));

        if(operatorCommand!=null&&operatorCommand.getSpecialties().size()>0){
            operatorCommand.getSpecialties().
                    forEach(specialty -> operator.getSpecialties().add(specialtyCommandToSpecialty.convert(specialty)));
        }

        return operator;
    }
}
