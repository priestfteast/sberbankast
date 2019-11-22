package com.balakin.sberbankast.commands;

import com.balakin.sberbankast.domain.Shift;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class OperatorCommand {
    private Long id;

    private String firstName;

    private String lastName;

    private String number;

    private String employementDate;


    private NotesCommand notes;


    private Set<SpecialtyCommand> specialties = new HashSet<>();


    private Shift shift;
}
