package com.balakin.sberbankast.services;

import com.balakin.sberbankast.domain.Operator;
import com.balakin.sberbankast.repositories.OperatorRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@Slf4j
public class ImageServiceImpl implements ImageService{

    private final OperatorRepository operatorRepository;

    public ImageServiceImpl(OperatorRepository operatorRepository) {
        this.operatorRepository = operatorRepository;
    }


    @Override
    public void saveImageFile(Long operatorId, MultipartFile file) {
        log.debug("Received a file");
        System.out.println(file.getSize());
        if(file.getSize()>5000000) {

            throw new IllegalStateException("Размер изображения превышает 5 мб");
        }

        if(!file.getOriginalFilename().endsWith(".jpg")) {

            throw new IllegalStateException("Формат изображения должен быть .jpg");


        }

        try{
            Operator operator = operatorRepository.findById(operatorId).get();
            Byte[] byteObjects = new Byte[file.getBytes().length];

            int counter = 0;

            for (Byte b: file.getBytes()
            ) {
                byteObjects[counter++] =b;
            }
            operator.setImage(byteObjects);
            operatorRepository.save(operator);
        }
        catch (IOException e){
            log.error("Error occured "+e);
            e.printStackTrace();
        }
    }
}


