package com.balakin.sberbankast.converters;

import com.balakin.sberbankast.commands.FineCommand;
import com.balakin.sberbankast.domain.Fine;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class FineToFineCommand implements Converter<Fine, FineCommand> {

    @Nullable
    @Synchronized
    @Override
    public FineCommand convert(Fine fine) {

        if(fine==null){
            return null;
        }
        final FineCommand fineCommand = new FineCommand();
        fineCommand.setId(fine.getId());
        if(fine.getOperator()!=null){
            fineCommand.setOperatorId(fine.getOperator().getId());
        }
        fineCommand.setSize(fine.getSize());
        fineCommand.setDescription(fine.getDescription());
        fineCommand.setDate(fine.getDate());
        return fineCommand;
    }
}
