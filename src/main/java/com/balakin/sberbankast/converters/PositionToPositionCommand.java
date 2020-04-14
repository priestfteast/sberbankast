package com.balakin.sberbankast.converters;

import com.balakin.sberbankast.commands.PositionCommand;
import com.balakin.sberbankast.domain.Position;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class PositionToPositionCommand implements Converter<Position, PositionCommand> {

    @Nullable
    @Synchronized
    @Override
    public PositionCommand convert(Position position) {
        if(position == null) {
            return null;
        }
        final PositionCommand positionCommand = new PositionCommand();
        positionCommand.setId(position.getId());
        positionCommand.setYear(position.getYear());
        positionCommand.setJanuary(position.getJanuary());
        positionCommand.setFebruary(position.getFebruary());
        positionCommand.setMarch(position.getMarch());
        positionCommand.setApril(position.getApril());
        positionCommand.setMay(position.getMay());
        positionCommand.setJune(position.getJune());
        positionCommand.setJuly(position.getJuly());
        positionCommand.setAugust(position.getAugust());
        positionCommand.setSeptember(position.getSeptember());
        positionCommand.setOctober(position.getOctober());
        positionCommand.setNovember(position.getNovember());
        positionCommand.setDecember(position.getDecember());

        return positionCommand;
    }
}
