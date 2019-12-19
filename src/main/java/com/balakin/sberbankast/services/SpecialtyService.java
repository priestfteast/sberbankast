package com.balakin.sberbankast.services;

import com.balakin.sberbankast.commands.SpecialtyCommand;

import java.util.Set;

public interface SpecialtyService {
    Set<SpecialtyCommand> listAllSpecialties();
}
