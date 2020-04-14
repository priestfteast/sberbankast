package com.balakin.sberbankast.services;

import com.balakin.sberbankast.commands.PositionCommand;

import java.time.LocalDate;

public interface PositionService {

    PositionCommand savePositionCommand(PositionCommand positionCommand);
    PositionCommand findPositionByYear(String year);
    Long getLabourdays(LocalDate start, LocalDate end);
}
