package com.balakin.sberbankast.services;

import com.balakin.sberbankast.commands.OutgoingCommand;
import com.balakin.sberbankast.converters.FineCommandToFine;
import com.balakin.sberbankast.converters.FineToFineCommand;
import com.balakin.sberbankast.converters.OutgoingCommandToOutgoing;
import com.balakin.sberbankast.converters.OutgoingToOutgoingCommand;
import com.balakin.sberbankast.domain.Outgoing;
import com.balakin.sberbankast.repositories.OutgoingRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class OutgoingServiceImpl implements OutgoingService {

    private final OutgoingRepository outgoingRepository;
    private final OutgoingCommandToOutgoing outgoingCommandToOutgoing;
    private final OutgoingToOutgoingCommand outgoingToOutgoingCommand;

    public OutgoingServiceImpl(OutgoingRepository outgoingRepository, FineToFineCommand fineToFineCommand, FineCommandToFine fineCommandToFine, OutgoingCommandToOutgoing outgoingCommandToOutgoing, OutgoingToOutgoingCommand outgoingToOutgoingCommand) {
        this.outgoingRepository = outgoingRepository;

        this.outgoingCommandToOutgoing = outgoingCommandToOutgoing;
        this.outgoingToOutgoingCommand = outgoingToOutgoingCommand;
    }

    @Override
    @Transactional
    public OutgoingCommand saveOutgoingCommand(OutgoingCommand command) {

        Outgoing savedOutgoing = outgoingRepository.save(outgoingCommandToOutgoing.convert(command));

        return outgoingToOutgoingCommand.convert(savedOutgoing);

    }

    @Override
    public List<Outgoing> findAllOutgoing() {
      return (List<Outgoing>) outgoingRepository.findAll();
    }

    @Override
    public void deleteById(Long idToDelete) {
        log.debug("Deleting outgoing: " + ":" + idToDelete);
        outgoingRepository.deleteById(idToDelete);
    }
}
