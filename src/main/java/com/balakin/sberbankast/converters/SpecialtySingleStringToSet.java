package com.balakin.sberbankast.converters;

import com.balakin.sberbankast.commands.SpecialtyCommand;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class SpecialtySingleStringToSet implements Converter<String, Set<SpecialtyCommand>> {
    @Override
    public Set<SpecialtyCommand> convert(String s) {
        Set<SpecialtyCommand> setToReturn = new HashSet<>();

        int indexOfEquals = s.indexOf('=');

        Long id = Long.parseLong(s.substring(0, indexOfEquals));
        String description = s.substring(indexOfEquals + 1);

        SpecialtyCommand specialtyCommand = new SpecialtyCommand();
        specialtyCommand.setId(id);
        specialtyCommand.setDescription(description);

        setToReturn.add(specialtyCommand);

        return setToReturn;
    }


}