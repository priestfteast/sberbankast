package com.balakin.sberbankast.commands;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Pattern;

@Getter
@Setter
@NoArgsConstructor
public class OutgoingCommand {
    private Long id;

    @Pattern(regexp = "^[0-9]{3}$")
    private String number;
}
