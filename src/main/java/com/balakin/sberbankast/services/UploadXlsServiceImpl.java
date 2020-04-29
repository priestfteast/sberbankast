package com.balakin.sberbankast.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
@Slf4j
public class UploadXlsServiceImpl implements UploadXlsService {

    @Override
    public void uploadXls(MultipartFile file) throws Exception {
        log.debug("Received a file");
        System.out.println(file.getSize());
        if(file.getSize()>5000000) {

            throw new IllegalStateException("Размер файла превышает 5 мб");
        }





        try{

            byte[] byteObjects = new byte[file.getBytes().length];

            int counter = 0;

            for (byte b: file.getBytes()
            ) {
                byteObjects[counter++] =b;
            }

            File uploadedFile;

            if(Files.exists(Paths.get("src\\main\\resources\\dailystats\\"+file.getOriginalFilename()))) {
                throw new Exception("File " + file.getOriginalFilename() + " has been uploaded earlier");
            }
            else {
                uploadedFile = new File("src\\main\\resources\\dailystats\\" + file.getOriginalFilename());
            }

            OutputStream os = new FileOutputStream(uploadedFile);
            os.write(byteObjects);
            System.out.println("Write bytes to file.");

            os.close();
        }
        catch (IOException e){
            log.error("Error occured "+e);
            e.printStackTrace();
        }
    }
}
