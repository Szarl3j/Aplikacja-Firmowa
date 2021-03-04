package com.aplikacja.Aplikacja.firmowa.Controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@RestController
public class DocumentUploadController {
    @RequestMapping(value = "/admin/upload", method = RequestMethod.POST,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)

    public String uploadFile(@RequestParam("file")MultipartFile file )throws IOException{
        File convertFile= new File("/var/tmp"+file.getOriginalFilename());
        convertFile.createNewFile();
        FileOutputStream fileOutput= new FileOutputStream(convertFile);
        fileOutput.write(file.getBytes());
        fileOutput.close();
        return "Successfuly uploaded file ";
    }
}
