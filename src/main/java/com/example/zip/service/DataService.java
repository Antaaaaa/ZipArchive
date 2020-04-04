package com.example.zip.service;

import com.example.zip.entity.Archive;
import com.example.zip.entity.UploadFile;
import com.example.zip.repository.ArchiveRepos;
import com.example.zip.repository.FileRepos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
@Transactional
public class DataService{

    @Autowired
    FileRepos fileRepos;

    @Autowired
    ArchiveRepos archiveRepos;


    public List<Archive> getListArchive(){
        return archiveRepos.findAll();
    }
    public List<UploadFile> getListFile(){
        return fileRepos.findAll();
    }

    public void addArchive(Archive archive){
        archiveRepos.save(archive);
    }
    public void addFile(UploadFile file){ fileRepos.save(file);}
}
