package com.balakin.sberbankast.services;


import com.balakin.sberbankast.commands.OperatorCommand;
import com.balakin.sberbankast.converters.OperatorCommandToOperator;
import com.balakin.sberbankast.converters.OperatorToOperatorCommand;
import com.balakin.sberbankast.domain.Operator;
import com.balakin.sberbankast.domain.Specialty;
import com.balakin.sberbankast.exceptions.NotFoundException;
import com.balakin.sberbankast.repositories.OperatorRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OperatorServiceImpl implements OperatorService {
    private final OperatorRepository operatorRepository;
    private final OperatorCommandToOperator operatorCommandToOperator;
    private final OperatorToOperatorCommand operatorToOperatorCommand;

    public OperatorServiceImpl(OperatorRepository operatorRepository, OperatorCommandToOperator operatorCommandToOperator, OperatorToOperatorCommand operatorToOperatorCommand) {
        this.operatorRepository = operatorRepository;
        this.operatorCommandToOperator = operatorCommandToOperator;
        this.operatorToOperatorCommand = operatorToOperatorCommand;
    }

    @Override
    public List<Operator> getOperators(String request) {
        log.debug("we are in service");
        List<Operator> operators = new ArrayList<>();
        String[] parsedRequest = parseRequest(request);
//        System.out.println(request);
//        System.out.println(parsedRequest.length);
        switch (parseRequest(request)[0]) {
            case "name":
                operatorRepository.findAllByOrderByLastName().forEach(operators::add);
                break;
            case "dateofemployment":
                operatorRepository.findAllByOrderByEmployementDate().forEach(operators::add);
                break;
            case "specialties":
                operators.addAll(getOperatorsBySpecialties());
                break;
        }

        List<Operator> filteredList = new ArrayList<>();
        filteredList.addAll(operators);
        if (parsedRequest.length == 2) {
            if (request.contains("specialty")) {
                String specsRequest = parsedRequest[1];


                for (Operator op : operators
                ) {

                    if (!parseSpecs(op.getSpecialties()).contains(specsRequest))
                        filteredList.remove(op);
                }

            }

                if (request.contains("experience")) {

                    if(parsedRequest[1].equals("all"))
                        return  filteredList;

                    for (Operator op : operators
                    ) {
//                        System.out.println(parsedRequest[1]+"  "+op.getYears());
                        if (!parsedRequest[1].equals(String.valueOf(op.getYears())))
                            filteredList.remove(op);
                    }

                }

            return filteredList;

            }

        if (parsedRequest.length == 3) {

                for (Operator op : operators
                ) {

                    if (!parseSpecs(op.getSpecialties()).contains(parsedRequest[1]))
                        filteredList.remove(op);
                }

                if(parsedRequest[2].equals("all"))
                    return filteredList;


                for (Operator op : operators
                ) {
//                    System.out.println(parsedRequest[1]+"  "+op.getYears());
                    if (Integer.valueOf(parsedRequest[2])!=op.getYears())
                        filteredList.remove(op);
                }



            return filteredList;

        }

        return operators;
    }


    @Override
    public List<Operator> getOperatorsBySpecialties() {
        log.debug("we are in service");
        List<Operator> operators = new ArrayList<>();
        operatorRepository.findAll().forEach(operators::add);
        operators.sort(new Comparator<Operator>() {
            @Override
            public int compare(Operator o1, Operator o2) {
                if(o1.getSpecialties().size()>o2.getSpecialties().size())
                return -1;
                else
                    return 1;
            }
        });

        return operators;
    }



    @Override
    public Operator findById(Long l) {

        Optional<Operator> operatorOptional = operatorRepository.findById(l);
        if(!operatorOptional.isPresent())
//            throw new RuntimeException(String.format("There is no operator wit id %f",l));
            throw new NotFoundException("There is no operator with ID: "+l.toString());

        return operatorOptional.get();
    }

    @Override
    public OperatorCommand findCommandById(Long l) {
        return operatorToOperatorCommand.convert(findById(l));
    }

    @Override
    @Transactional
    public OperatorCommand saveOperatorCommand(OperatorCommand operatorCommand){
        Operator detachedOperator = operatorCommandToOperator.convert(operatorCommand);
        Operator savedOperator = operatorRepository.save(detachedOperator);

        return operatorToOperatorCommand.convert(savedOperator);
    }

    @Override
    public void deleteById(Long idToDelete) {
        operatorRepository.deleteById(idToDelete);
    }

    public String parseSpecs(List<Specialty> specs){
        String result ="";
        for (Specialty spec: specs
             ) {
            result+=spec.getDescription();
        }
        return result.replaceAll("\\s","");
    }

    public String[] parseRequest(String request){
       request=request.replaceAll("\\d{1}=","").replaceAll("}","");
        String [] massive = request.split("]");
        for (int i = 0; i < massive.length; i++) {
            massive[i]=massive[i].replaceAll("\\[|}|.+=|,|\\s","").replaceAll("year","").replaceAll("everyspecialty","");
            System.out.println(massive[i]);
        }
        return massive;
    }

    public static void main(String[] args) {
        String request = "{sort by=[name], experience=[1 year]}";
        request=request.replaceAll("\\d{1}=","").replaceAll("}","");
        String [] massive = request.split("]");
        for (int i = 0; i < massive.length; i++) {
            massive[i]=massive[i].replaceAll("\\[|}|.+=|,|\\s","").replaceAll("year","").replaceAll("allcategories","");
            System.out.println("! "+massive[i]);
        }
    }
}
