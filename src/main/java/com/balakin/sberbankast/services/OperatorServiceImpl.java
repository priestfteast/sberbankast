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

        if (parsedRequest.length == 3) {
            filteredList=filterByExperience(filteredList,parsedRequest);
           filteredList=filterByIncoming(filteredList,parsedRequest);
            }

        if (parsedRequest.length == 4) {

            filteredList=filterByExperience(filteredList,parsedRequest);

            filteredList=filterByIncoming(filteredList,parsedRequest);

                for (Operator op : operators
                ) {
//                    System.out.println(parseSpecs(op.getSpecialties()));
                    if (!checkSpecs(parsedRequest[3],parseSpecs(op.getSpecialties())))
                        filteredList.remove(op);
                }
        }
//        GOS(44-FZ)Corporate(223-FZ)Bankruptsy&PrivatizationCommercialOther
//        GOS(44-FZ)Corporate(223-FZ)CommercialBankruptsy&PrivatizationOther

        return filteredList;
    }

    @Transactional
    @Override
    public List<Specialty> getSpecialties(Long id) {

        return findById(id).getSpecialties();
    }

    @Override
    public List<Operator> getOperatorsBySpecialties() {
        log.debug("we are in service");
        List<Operator> operators = new ArrayList<>();
        operatorRepository.findAll().forEach(operators::add);
        operators.sort((o1, o2) -> {
            if(o1.getSpecialties().size()>o2.getSpecialties().size())
            return -1;
            else if(o1.getSpecialties().size()<o2.getSpecialties().size())
                return 1;
            else
                return 0;
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
            if(i<3) {
                massive[i] = massive[i].replaceAll("\\[|}|.+=|,|\\s", "").replaceAll("year", "").replaceAll("everyspecialty", "");
                System.out.println(massive[i]);
            }
            if(i==3) {
                massive[i] = massive[i].replaceAll("\\[|}|.+=|,", "").replaceAll("year", "").replaceAll("everyspecialty", "");
                System.out.println(massive[i]);
            }
        }
        return massive;
    }

    public List<Operator> filterByIncoming(List<Operator> operators, String[] data){

        List<Operator> filteredList = new ArrayList<>();
        filteredList.addAll(operators);

        for (Operator op : operators
        ) {
            if(data[2].equals("every"))
                break;
            if(!op.isIncoming()&&!op.isOutgoing())
                filteredList.remove(op);
            if (data[2].equals("inc")&&!(op.isIncoming())||data[2].equals("incom")&&op.isOutgoing())
                filteredList.remove(op);
            if (data[2].equals("outg")&&!(op.isOutgoing())||data[2].equals("outg")&&op.isIncoming())
                filteredList.remove(op);
            if(data[2].equals("inc&out")&&!(op.isIncoming())|| data[2].equals("inc&out")&&!(op.isOutgoing())) {
                System.out.println(op.getLastName()+" "+ data[2]+" outgoing - "+op.isOutgoing()+" incoming - "+op.isIncoming());
                filteredList.remove(op);
            }
        }
        return filteredList;
    }

    public List<Operator> filterByExperience(List<Operator> operators, String[] data){

        List<Operator> filteredList = new ArrayList<>();
        filteredList.addAll(operators);

        if(data[1].equals("all")&&data[2].equals("every"))
            return  filteredList;

        for (Operator op : operators
        ) {
            if(data[1].equals("all"))
                break;
//                        System.out.println(parsedRequest[1]+"  "+op.getYears());
            if (!data[1].equals(String.valueOf(op.getYears()))&&!data[1].equals("4"))
                filteredList.remove(op);

            if (data[1].equals("4")&&op.getYears()<4)
                filteredList.remove(op);
        }

        return filteredList;
    }

    public boolean checkSpecs(String request, String specs){

        String[] parsedRequest = request.split(" ");

        for (String  spec : parsedRequest){

            if(!specs.contains(spec)) {
                System.out.println("!!!!!"+ specs+" doesnt contain "+spec);
                return false;
            }
        }
        return true;
    }


    public int countAllByIdNotNull(){
        return operatorRepository.countAllByIdNotNull();
    }
    public int countAllByOutgoingTrue(){
        return operatorRepository.countAllByOutgoingTrue();
    }

    public int countAllByIncomingTrue(){
        return operatorRepository.countAllByIncomingTrue();
    }

    public int countAllByStakeTrue(){
        return operatorRepository.countAllByStakeTrue();
        }



}
