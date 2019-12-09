package com.balakin.sberbankast.services;

import com.balakin.sberbankast.commands.FineCommand;
import com.balakin.sberbankast.domain.Fine;

import java.util.Optional;
import java.util.Set;

public interface FineService {



    FineCommand findByOperatorIdAndFineId(Long operatorId, Long fineId);

    FineCommand saveFineCommand(FineCommand command);

    void deleteById(Long operatorId, Long idToDelete);
}
