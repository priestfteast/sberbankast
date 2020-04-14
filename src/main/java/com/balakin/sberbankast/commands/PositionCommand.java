package com.balakin.sberbankast.commands;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Pattern;


@Getter
@Setter
@NoArgsConstructor
public class PositionCommand {

    private Long id;

    private String year;
    @Pattern(regexp = "^[0-9]{2}$")
    private String january;
    @Pattern(regexp = "^[0-9]{2}$")
    private String february;
    @Pattern(regexp = "^[0-9]{2}$")
    private String march;
    @Pattern(regexp = "^[0-9]{2}$")
    private String april;
    @Pattern(regexp = "^[0-9]{2}$")
    private String may;
    @Pattern(regexp = "^[0-9]{2}$")
    private String june;
    @Pattern(regexp = "^[0-9]{2}$")
    private String july;
    @Pattern(regexp = "^[0-9]{2}$")
    private String august;
    @Pattern(regexp = "^[0-9]{2}$")
    private String september;
    @Pattern(regexp = "^[0-9]{2}$")
    private String october;
    @Pattern(regexp = "^[0-9]{2}$")
    private String november;
    @Pattern(regexp = "^[0-9]{2}$")
    private String december;
}
