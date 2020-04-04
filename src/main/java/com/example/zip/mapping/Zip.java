package com.example.zip.mapping;

import com.example.zip.entity.Archive;
import com.example.zip.entity.UploadFile;
import com.example.zip.service.DataService;
import com.example.zip.util.ZipArchiver;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Controller
public class Zip {

    @Autowired
    private DataService dataService;

    @Autowired
    private ZipArchiver zipArchiver;

    @GetMapping("/")
    public String home(){ return "index"; }

    private final String SIGNATURE = "Anta_";
    private final Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    @PostMapping("/result")
    public String onResult(Model model, @RequestParam("fileName")String fileName,
                           @RequestParam("files[]")MultipartFile[] files,
                           @RequestParam(value = "switch", required = false)String isChecked) throws Exception {
        String validatedFileName = fileName.replaceAll("[\\s/\\\\:*?\"<>|]", "");
        fileName = SIGNATURE + validatedFileName;
        logger.info("New file to be zipped: " + fileName);
        List<UploadFile> filesList = new ArrayList<>();
        ArrayList<File> allFiles = new ArrayList<>();
        for (MultipartFile file : files) {
            // UploadFile uploadFile = new UploadFile(file.getBytes());
            UploadFile uploadFile = new UploadFile(file.getOriginalFilename());
            allFiles.add(zipArchiver.multipartToFile(file, file.getOriginalFilename()));
            logger.info("File : " + file.getOriginalFilename() + " saved to list");
            filesList.add(uploadFile);
            dataService.addFile(uploadFile);
        }
        Pair<String, byte[]> filePassAndBytes = zipArchiver.archiveByZip(fileName, allFiles, isChecked != null);
        Archive archive = new Archive(fileName, filePassAndBytes.getKey(), filesList, filePassAndBytes.getValue());
        allFiles.parallelStream().forEach(i -> i.delete());
        long start = System.currentTimeMillis();
        dataService.addArchive(archive);
        logger.info("Added to DB: " + fileName);
        logger.info("This took " + (System.currentTimeMillis() - start)/1000.0 + "sec");
        model.addAttribute("archives", dataService.getListArchive());
        return "result";
    }
    @GetMapping("/result")
        public String getList(Model model){
        model.addAttribute("archives", dataService.getListArchive());
        return "result";
    }
    @GetMapping(value = "/result/{id}", produces = "application/zip")
    public ResponseEntity<Resource> getFile(@PathVariable Integer id, Model model) throws Exception {
        long start = System.currentTimeMillis();
        Archive archive = dataService.getListArchive().get(id-1);
        ByteArrayResource resource = new ByteArrayResource(archive.getBlob());
        ContentDisposition contentDisposition = ContentDisposition.builder("inline")
                .filename(archive.getName()+ ".zip")
                .build();
        HttpHeaders headers = new HttpHeaders();
        if (!archive.getName().endsWith(".zip")) headers.setContentDisposition(contentDisposition);
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        logger.info("Getting zip took: " + (System.currentTimeMillis() - start)/1000.0 + "sec");
        return ResponseEntity.ok()
                .headers(headers)
                .body(resource);
    }
}
