package com.balakin.sberbankast.services;

import com.balakin.sberbankast.commands.SpecialtyCommand;
import com.balakin.sberbankast.converters.SpecialtyToSpecialtyCommand;
import com.balakin.sberbankast.domain.Specialty;
import com.balakin.sberbankast.repositories.SpecialtyRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class SpecialtyServiceimpl implements SpecialtyService {

    private final SpecialtyRepository specialtyRepository;
    private final SpecialtyToSpecialtyCommand specialtyToSpecialtyCommand;

    public SpecialtyServiceimpl(SpecialtyRepository specialtyRepository, SpecialtyToSpecialtyCommand specialtyToSpecialtyCommand) {
        this.specialtyRepository = specialtyRepository;
        this.specialtyToSpecialtyCommand = specialtyToSpecialtyCommand;
    }


    @Override
    public List<SpecialtyCommand> listAllSpecialties() {
        List<SpecialtyCommand> list = new ArrayList<>();



        for (Specialty spec:specialtyRepository.findAll()
             ) {
            list.add(specialtyToSpecialtyCommand.convert(spec));
        }
        return list;
    }
}
