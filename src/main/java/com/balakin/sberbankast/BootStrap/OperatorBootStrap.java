package com.balakin.sberbankast.BootStrap;

import com.balakin.sberbankast.domain.Notes;
import com.balakin.sberbankast.domain.Operator;
import com.balakin.sberbankast.domain.Shift;
import com.balakin.sberbankast.domain.Specialty;
import com.balakin.sberbankast.repositories.OperatorRepository;
import com.balakin.sberbankast.repositories.SpecialtyRepository;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Component
public class OperatorBootStrap implements ApplicationListener<ContextRefreshedEvent> {

    private final OperatorRepository operatorRepository;
    private final SpecialtyRepository specialtyRepository;

    public OperatorBootStrap(OperatorRepository operatorRepository, SpecialtyRepository specialtyRepository) {
        this.operatorRepository = operatorRepository;
        this.specialtyRepository = specialtyRepository;
    }

    private List<Operator> getOperators(){
        List<Operator> operators = new ArrayList<>(2);

        //get Specialties Optionals
        Optional<Specialty> gosOptional = specialtyRepository.findByDescription("GOS(44-FZ)");

        if(!gosOptional.isPresent()){
            throw new RuntimeException("Expected specialty Not Found");
        }
        Optional<Specialty> corpOptional = specialtyRepository.findByDescription("Corporate(223-FZ)");

        if(!corpOptional.isPresent()){
            throw new RuntimeException("Expected specialty Not Found");
        }
        Optional<Specialty> bankPrivOptional = specialtyRepository.findByDescription("Bankruptsy & Privatization");

        if(!bankPrivOptional.isPresent()){
            throw new RuntimeException("Expected specialty Not Found");
        }
        Optional<Specialty> commercialOptional = specialtyRepository.findByDescription("Commercial");

        if(!commercialOptional.isPresent()){
            throw new RuntimeException("Expected specialty Not Found");
        }
        Optional<Specialty> otherOptional = specialtyRepository.findByDescription("Other");

        if(!otherOptional.isPresent()){
            throw new RuntimeException("Expected specialty Not Found");
        }


        //get Specialties
        Specialty gos = gosOptional.get();
        Specialty corp = corpOptional.get();
        Specialty bankPriv = bankPrivOptional.get();
        Specialty com = commercialOptional.get();
        Specialty other = otherOptional.get();

//        Ivanov Ivan
        Operator ivanov = new Operator();
        ivanov.setFirstName("Ivan");
        ivanov.setLastName("Ivanov");
        ivanov.setNumber("user223");
        ivanov.setEmployementDate(LocalDate.of(2019, Month.SEPTEMBER,20));
        ivanov.setShift(Shift.NINE_AM);

        Notes ivanovNotes = new Notes();
        ivanovNotes.setDescription("Ivanov is a good guy");

        ivanov.setNotes(ivanovNotes);
        ivanovNotes.setOperator(ivanov);

        ivanov.getSpecialties().add(gos);
        ivanov.getSpecialties().add(bankPriv);


        //        Pereyaslova Maria
        Operator maria = new Operator();
        maria.setFirstName("Maria");
        maria.setLastName("Pereyaslova");
        maria.setNumber("user245");
        maria.setEmployementDate(LocalDate.of(2018, Month.MAY,1));
        maria.setShift(Shift.EIGHT_AM);

        Notes mariaNotes = new Notes();
        mariaNotes.setDescription("Maria is a good girl");

        maria.setNotes(mariaNotes);
        mariaNotes.setOperator(maria);

        maria.getSpecialties().add(other);
        maria.getSpecialties().add(gos);
        maria.getSpecialties().add(com);


        //add to return list
        operators.add(ivanov);
        operators.add(maria);

        return operators;
    }

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        operatorRepository.saveAll(getOperators());
        log.debug("Loading bootStrap data");
    }
}
