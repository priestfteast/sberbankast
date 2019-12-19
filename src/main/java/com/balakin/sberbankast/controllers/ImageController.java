package com.balakin.sberbankast.controllers;

import com.balakin.sberbankast.commands.OperatorCommand;
import com.balakin.sberbankast.services.ImageService;
import com.balakin.sberbankast.services.OperatorService;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@Controller
public class ImageController {

        private final ImageService imageService;
        private final OperatorService operatorService;

        public ImageController(ImageService imageService, OperatorService operatorService) {
            this.imageService = imageService;
            this.operatorService = operatorService;
        }

        @GetMapping("operator/{id}/image")
        public String showUploadForm(@PathVariable String id, Model model){
            model.addAttribute("operator", operatorService.findCommandById(Long.valueOf(id)));

            return "operator/imageuploadform";
        }

        @PostMapping("operator/{id}/image")
        public String handleImagePost(@PathVariable String id, @RequestParam("imagefile") MultipartFile file){

            imageService.saveImageFile(Long.valueOf(id), file);

            return "redirect:/operator/" + id + "/show";
        }

        @GetMapping("operator/{id}/operatorimage")
        public void renderImageFromDB(@PathVariable String id, HttpServletResponse response) throws IOException {
            OperatorCommand operatorCommand = operatorService.findCommandById(Long.valueOf(id));

            if (operatorCommand.getImage() != null) {
                byte[] byteArray = new byte[operatorCommand.getImage().length];
                int i = 0;

                for (Byte wrappedByte : operatorCommand.getImage()){
                    byteArray[i++] = wrappedByte; //auto unboxing
                }

                response.setContentType("image/jpeg");
                InputStream is = new ByteArrayInputStream(byteArray);
                IOUtils.copy(is, response.getOutputStream());
            }
        }



    }

