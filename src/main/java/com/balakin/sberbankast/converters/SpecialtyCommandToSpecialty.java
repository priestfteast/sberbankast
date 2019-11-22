package com.balakin.sberbankast.converters;

import com.balakin.sberbankast.commands.SpecialtyCommand;
import com.balakin.sberbankast.domain.Specialty;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class SpecialtyCommandToSpecialty implements Converter<SpecialtyCommand, Specialty> {

    @Nullable
    @Synchronized
    @Override
    public Specialty convert(SpecialtyCommand specialtyCommand) {

        if(specialtyCommand==null) {
            return null;
        }

        Specialty specialty = new Specialty();
        specialty.setId(specialtyCommand.getId());
        specialty.setDescription(specialtyCommand.getDescription());

        return specialty;
    }
}
