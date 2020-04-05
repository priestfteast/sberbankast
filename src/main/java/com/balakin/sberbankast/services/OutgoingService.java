package com.balakin.sberbankast.services;

import com.balakin.sberbankast.commands.FineCommand;
import com.balakin.sberbankast.commands.OutgoingCommand;
import com.balakin.sberbankast.domain.Outgoing;

import java.util.List;

public interface OutgoingService {
    OutgoingCommand saveOutgoingCommand(OutgoingCommand command);

    List<Outgoing> findAllOutgoing();

    void deleteById(Long idToDelete);
}
