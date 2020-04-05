package com.balakin.sberbankast.converters;

import com.balakin.sberbankast.commands.OutgoingCommand;
import com.balakin.sberbankast.domain.Outgoing;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class OutgoingToOutgoingCommand implements Converter<Outgoing, OutgoingCommand> {
    @Nullable
    @Synchronized
    @Override
    public OutgoingCommand convert(Outgoing outgoing) {

        if(outgoing==null){
            return null;
        }
        final OutgoingCommand outgoingCommand = new OutgoingCommand();
        outgoingCommand.setId(outgoing.getId());

        outgoingCommand.setNumber(outgoing.getNumber());

        return outgoingCommand;
    }
}
