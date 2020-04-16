package com.balakin.sberbankast.services;

import com.balakin.sberbankast.commands.PositionCommand;
import com.balakin.sberbankast.converters.PositionCommandToPosition;
import com.balakin.sberbankast.converters.PositionToPositionCommand;
import com.balakin.sberbankast.domain.Position;
import com.balakin.sberbankast.repositories.PositionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PositionServiceImpl implements PositionService {

    private final PositionRepository positionRepository;
    private final PositionCommandToPosition positionCommandToPosition;
    private final PositionToPositionCommand positionToPositionCommand;


    public PositionServiceImpl(PositionRepository positionRepository, PositionCommandToPosition positionCommandToPosition, PositionToPositionCommand positionToPositionCommand) {
        this.positionRepository = positionRepository;
        this.positionCommandToPosition = positionCommandToPosition;
        this.positionToPositionCommand = positionToPositionCommand;
    }


    @Override
    public PositionCommand savePositionCommand(PositionCommand positionCommand) {
        Position savedPosition = positionRepository.save(positionCommandToPosition.convert(positionCommand));
        return positionToPositionCommand.convert(savedPosition);
    }

    @Override
    public PositionCommand findPositionByYear(String year) {
        return positionToPositionCommand.convert(positionRepository.findPositionByYear(year));
    }

    @Override
    public Long getLabourdays(LocalDate start, LocalDate end) {
        List<LocalDate> dates = start.datesUntil(end)
                .collect(Collectors.toList());
        List<String> resultDates = new ArrayList<>();
        for (LocalDate date: dates
        ) {
            if(!resultDates.contains( date.toString().substring(0,date.toString().lastIndexOf("-"))))
                resultDates.add(date.toString().substring(0,date.toString().lastIndexOf("-")));
            System.out.println(date.toString().substring(0,date.toString().lastIndexOf("-")));
        }


        Long labourDays = 0L;

        for (String date: resultDates
             ) {


            String[] parts = date.split("-");
            String year = parts[0];
            String month = parts[1];
            Position position = positionRepository.findPositionByYear(year);

            switch (month) {
                case ("01"):
                    labourDays+= Long.valueOf(position.getJanuary());
                    break;
                case ("02"):
                    labourDays+= Long.valueOf(position.getFebruary());
                    break;
                case ("03"):
                    labourDays+= Long.valueOf(position.getMarch());
                    break;
                case ("04"):
                    labourDays+= Long.valueOf(position.getApril());
                    break;
                case ("05"):
                    labourDays+=Long.valueOf(position.getMay());
                    break;
                case ("06"):
                    labourDays+= Long.valueOf(position.getJune());
                    break;
                case ("07"):
                    labourDays+= Long.valueOf(position.getJuly());
                    break;
                case ("08"):
                    labourDays+= Long.valueOf(position.getAugust());
                    break;
                case ("09"):
                    labourDays+= Long.valueOf(position.getSeptember());
                    break;
                case ("10"):
                    labourDays+= Long.valueOf(position.getOctober());
                    break;
                case ("11"):
                    labourDays+= Long.valueOf(position.getNovember());
                    break;
                case ("12"):
                    labourDays+= Long.valueOf(position.getDecember());
                    break;

            }
        }
        return labourDays;
    }

}
