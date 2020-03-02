package com.balakin.sberbankast.commands;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Positive;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class BonusCommand {
    private Long id;
    private Long operatorId;

    @NotBlank
    private String description;

    @javax.validation.constraints.NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @PastOrPresent
    private LocalDate date;

    @Positive
    private Long size;

}
