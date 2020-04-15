package com.balakin.sberbankast.commands;

import com.balakin.sberbankast.domain.Bonus;
import com.balakin.sberbankast.domain.Fine;
import com.balakin.sberbankast.domain.Shift;

import com.balakin.sberbankast.validators.OperatorImageConstraint;
import com.balakin.sberbankast.validators.OperatorLastNameConstraint;
import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.hibernate.validator.constraints.URL;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class OperatorCommand {
    private Long id;

    @NotBlank
    @Size(min = 2, max = 20)
    private String firstName;

    @NotBlank
    @Size(min = 2, max = 20)
//    @OperatorLastNameConstraint
    private String lastName;

    @Pattern(regexp = "^[0-9]{3}$")
    private String number;

    @Pattern(regexp = "(^[0-9]{3}$)|(^$)")
    private String additionalNumber;

@javax.validation.constraints.NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @PastOrPresent
    private LocalDate employementDate;


    private boolean incoming;

    private boolean outgoing;

    private boolean stake;


    private NotesCommand notes;

//    @OperatorImageConstraint
    private Byte[] image;

    @NotNull
    private Set<SpecialtyCommand> specialties = new HashSet<>();

    private Set<FineCommand> fines = new HashSet<>();

    private Set<BonusCommand> bonuses = new HashSet<>();

    private Shift shift;



}
