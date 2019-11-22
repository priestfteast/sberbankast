package com.balakin.sberbankast.converters;

import com.balakin.sberbankast.commands.SpecialtyCommand;
import com.balakin.sberbankast.domain.Specialty;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class SpecialtyToSpecialtyCommand implements Converter<Specialty, SpecialtyCommand> {

    @Nullable
    @Synchronized
    @Override
    public SpecialtyCommand convert(Specialty specialty) {
        if(specialty == null) {
            return null;
        }
        final SpecialtyCommand specialtyCommand = new SpecialtyCommand();
        specialtyCommand.setId(specialty.getId());
        specialtyCommand.setDescription(specialty.getDescription());
        return specialtyCommand;
    }
}
