package com.balakin.sberbankast.converters;

import com.balakin.sberbankast.commands.FineCommand;
import com.balakin.sberbankast.domain.Fine;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class FineCommandToFine implements Converter<FineCommand, Fine> {

    @Nullable
    @Synchronized
    @Override
    public Fine convert(FineCommand fineCommand) {
        if(fineCommand==null) {
            return null;
        }
        final Fine fine = new Fine();
        fine.setId(fineCommand.getId());
        fine.setSize(fineCommand.getSize());
        fine.setDescription(fineCommand.getDescription());
        fine.setDate(fineCommand.getDate());

        return fine;
    }
}
