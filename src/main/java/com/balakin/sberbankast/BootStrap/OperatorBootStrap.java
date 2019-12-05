package com.balakin.sberbankast.BootStrap;

import com.balakin.sberbankast.domain.*;
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

//        Bonuses for Ivanov
        Bonus bonusIvanov1 = new Bonus();
        bonusIvanov1.setDescription("4 years in company");
        bonusIvanov1.setSize(1500L);
        bonusIvanov1.setOperator(ivanov);
        ivanov.getBonuses().add(bonusIvanov1);


        Bonus bonusIvanov2 = new Bonus();
        bonusIvanov2.setDescription("note of appreciation");
        bonusIvanov2.setSize(1000L);
        bonusIvanov2.setOperator(ivanov);
        ivanov.getBonuses().add(bonusIvanov2);

        //        Fines for Ivanov
        Fine fineIvanov1 = new Fine();
        fineIvanov1.setDescription("Not ready failed");
        fineIvanov1.setSize(500L);
        fineIvanov1.setOperator(ivanov);
        ivanov.getFines().add(fineIvanov1);

        Fine fineIvanov2 = new Fine();
        fineIvanov2.setDescription("complaint note");
        fineIvanov2.setSize(1000L);
        fineIvanov2.setOperator(ivanov);
        ivanov.getFines().add(fineIvanov2);

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

        //        Bonuses for Maria
        Bonus bonusMaria1 = new Bonus();
        bonusMaria1.setDescription("4 years in company");
        bonusMaria1.setSize(1500L);
        bonusMaria1.setOperator(maria);
        maria.getBonuses().add(bonusMaria1);


        Bonus bonusMaria2 = new Bonus();
        bonusMaria2.setDescription("note of appreciation");
        bonusMaria2.setSize(1000L);
        bonusMaria2.setOperator(maria);
        maria.getBonuses().add(bonusMaria2);

        //        Fines for Maria
        Fine fineMaria1 = new Fine();
        fineMaria1.setDescription("Not ready failed");
        fineMaria1.setSize(500L);
        fineMaria1.setOperator(maria);
        maria.getFines().add(fineMaria1);

        Fine fineMaria2 = new Fine();
        fineMaria2.setDescription("complaint note");
        fineMaria2.setSize(1000L);
        fineMaria2.setOperator(maria);
        maria.getFines().add(fineMaria2);

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
