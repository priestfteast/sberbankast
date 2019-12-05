package com.balakin.sberbankast.services;

import com.balakin.sberbankast.commands.BonusCommand;

public interface BonusService {

    BonusCommand findByOperatorIdAndBonusId(Long operatorId, Long bonusId);
}
