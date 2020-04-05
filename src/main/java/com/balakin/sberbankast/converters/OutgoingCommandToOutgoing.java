package com.balakin.sberbankast.converters;

import com.balakin.sberbankast.commands.FineCommand;
import com.balakin.sberbankast.commands.OutgoingCommand;
import com.balakin.sberbankast.domain.Fine;
import com.balakin.sberbankast.domain.Outgoing;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class OutgoingCommandToOutgoing implements Converter<OutgoingCommand, Outgoing> {
    @Nullable
    @Synchronized
    @Override
    public Outgoing convert(OutgoingCommand outgoingCommand) {

        if(outgoingCommand==null){
            return null;
        }
        final Outgoing outgoing = new Outgoing();
        outgoing.setId(outgoingCommand.getId());

        outgoing.setNumber(outgoingCommand.getNumber());

        return outgoing;
    }
}
