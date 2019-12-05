package com.balakin.sberbankast.commands;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BonusCommand {
    private Long id;
    private Long operatorId;
    private String description;
    private Long size;

}
