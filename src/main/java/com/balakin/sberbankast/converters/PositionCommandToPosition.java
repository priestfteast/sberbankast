package com.balakin.sberbankast.converters;

import com.balakin.sberbankast.commands.PositionCommand;
import com.balakin.sberbankast.commands.SpecialtyCommand;
import com.balakin.sberbankast.domain.Position;
import com.balakin.sberbankast.domain.Specialty;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class PositionCommandToPosition implements Converter<PositionCommand,Position> {
    @Nullable
    @Synchronized
    @Override
    public Position convert(PositionCommand positionCommand) {

        if(positionCommand==null) {
            return null;
        }

        Position position = new Position();
        position.setId(positionCommand.getId());
        position.setYear(positionCommand.getYear());
        position.setJanuary(positionCommand.getJanuary());
        position.setFebruary(positionCommand.getFebruary());
        position.setMarch(positionCommand.getMarch());
        position.setApril(positionCommand.getApril());
        position.setMay(positionCommand.getMay());
        position.setJune(positionCommand.getJune());
        position.setJuly(positionCommand.getJuly());
        position.setAugust(positionCommand.getAugust());
        position.setSeptember(positionCommand.getSeptember());
        position.setOctober(positionCommand.getOctober());
        position.setNovember(positionCommand.getNovember());
        position.setDecember(positionCommand.getDecember());

        return position;
    }
}
