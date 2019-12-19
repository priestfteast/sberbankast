package com.balakin.sberbankast.commands;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SpecialtyCommand {
    private Long id;
    private String description;

    @Override
    public String toString() {
        return id+"="+description;
    }
}
