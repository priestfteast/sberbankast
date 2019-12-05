package com.balakin.sberbankast.converters;

import com.balakin.sberbankast.commands.BonusCommand;
import com.balakin.sberbankast.domain.Bonus;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class BonusCommandToBonus implements Converter<BonusCommand, Bonus> {

    @Nullable
    @Synchronized
    @Override
    public Bonus convert(BonusCommand bonusCommand) {
        if(bonusCommand==null) {
            return null;
        }
        final Bonus bonus = new Bonus();
        bonus.setId(bonusCommand.getId());
        bonus.setSize(bonusCommand.getSize());
        bonus.setDescription(bonusCommand.getDescription());

        return bonus;
    }
}