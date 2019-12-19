package com.balakin.sberbankast.converters;

import com.balakin.sberbankast.commands.SpecialtyCommand;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class SpecialtyStringArrayToSet implements Converter<String[], Set<SpecialtyCommand>> {
    @Override
    public Set<SpecialtyCommand> convert(String[] strings) {
        Set<SpecialtyCommand> setToReturn = new HashSet<>();

        for (String s: strings
             ) {
            int indexOfEquals = s.indexOf('=');
            SpecialtyCommand specialtyCommand = new SpecialtyCommand();
            specialtyCommand.setId(Long.valueOf(s.substring(0,indexOfEquals)));
            specialtyCommand.setDescription(s.substring(indexOfEquals+1));
            setToReturn.add(specialtyCommand);

        }
       return setToReturn;
    }
}
