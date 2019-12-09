package com.balakin.sberbankast.services;

import com.balakin.sberbankast.commands.FineCommand;

import java.util.Optional;

public interface FineService {

    FineCommand findByOperatorIdAndFineId(Long operatorId, Long fineId);
    FineCommand saveFineCommand(FineCommand command);
}
