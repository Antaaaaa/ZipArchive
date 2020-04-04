package com.example.zip.util;

import javafx.util.Pair;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;
import org.apache.commons.text.RandomStringGenerator;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Component
public class ZipArchiver {

    public File multipartToFile(MultipartFile multipart, String fileName) {
        try {
        //File convFile = new File(System.getProperty("java.io.tmpdir")+"/"+fileName);
        File convFile = File.createTempFile(fileName.substring(0,fileName.indexOf(".")),
                fileName.substring(fileName.indexOf(".")), new File(System.getProperty("java.io.tmpdir")));
        multipart.transferTo(convFile);
        convFile.deleteOnExit();
        return convFile;
        } catch (IOException e){
            e.printStackTrace();
            return null;
        }
    }
    // Using net.lingala.zip4j
    public Pair<String,byte[]> archiveByZip(String name, ArrayList<File> lst, Boolean setPass) {
        try {
            String file = name + ".zip";
            ZipFile zipFile = new ZipFile(file);
            ZipParameters zipParameters = new ZipParameters();
            String password = "";
            if (setPass){
                password = generateRandomSpecialCharacters((int)(10+Math.random()*20));
                zipParameters.setPassword(password);
                zipParameters.setEncryptFiles(true);
                zipParameters.setEncryptionMethod(Zip4jConstants.ENC_METHOD_AES);
                zipParameters.setAesKeyStrength(Zip4jConstants.AES_STRENGTH_128);
            }
            zipParameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_ULTRA);
            zipFile.addFiles(lst, zipParameters);
            byte[] arr = Files.readAllBytes(Paths.get(file));
            File tempFile = new File(file);
            tempFile.delete();
            return new Pair(password,arr);
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
    public String generateRandomSpecialCharacters(int length) {
        RandomStringGenerator pwdGenerator = new RandomStringGenerator.Builder()
                .withinRange(65, 112)
                .build();
        return pwdGenerator.generate(length);
    }
    // Simple Method
    /*
    public byte[] archive(String name, List<File> lst) throws Exception {
        String tmp = name+".zip";
        byte[] buffer = new byte[102400];
        FileOutputStream fos = new FileOutputStream(tmp);
        ZipOutputStream zos = new ZipOutputStream(fos);
        zos.setLevel(0);
        for (File srcFiles : lst){
            File srcFile = srcFiles.getAbsoluteFile();
            FileInputStream fis = new FileInputStream(srcFile);
            zos.putNextEntry(new ZipEntry(srcFile.getName()));
            int length;
            while ((length = fis.read(buffer)) > 0) {
                zos.write(buffer, 0, length);
            }
            zos.closeEntry();
            fis.close();
        }
        zos.close();
        File file = new File(tmp);
        byte[] result = Files.readAllBytes(Paths.get(file.getAbsolutePath()));
        file.delete();
        return result;
        }*/
    }
