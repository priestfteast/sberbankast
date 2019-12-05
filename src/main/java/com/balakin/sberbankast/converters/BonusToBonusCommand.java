package com.balakin.sberbankast.converters;

import com.balakin.sberbankast.commands.BonusCommand;
import com.balakin.sberbankast.commands.FineCommand;
import com.balakin.sberbankast.domain.Bonus;
import com.balakin.sberbankast.domain.Fine;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class BonusToBonusCommand implements Converter<Bonus, BonusCommand> {

    @Nullable
    @Synchronized
    @Override
    public BonusCommand convert(Bonus bonus) {

        if(bonus==null){
            return null;
        }
        final BonusCommand bonusCommand = new BonusCommand();
        bonusCommand.setId(bonus.getId());
        if(bonus.getOperator()!=null){
            bonusCommand.setOperatorId(bonus.getOperator().getId());
        }
        bonusCommand.setDescription(bonus.getDescription());
        bonusCommand.setSize(bonus.getSize());
        return bonusCommand;
    }
}