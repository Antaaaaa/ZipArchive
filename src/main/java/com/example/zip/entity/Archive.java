package com.example.zip.entity;

import com.sun.deploy.security.ValidationState;
import org.hibernate.type.BlobType;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

@Entity
@Table(name = "archive")
public class Archive implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "archive_id")
    private Integer id;

    @Column(name = "name_file")
    private String name;
    @Column(name = "password_file")
    private String password;
    @Column(name = "archive_file", columnDefinition = "MEDIUMBLOB")
    private byte[] blob;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<UploadFile> fileList;

    public Archive() {}

    public Archive(String name, String password, List<UploadFile> fileList, byte[] blob){
        this.name = name;
        this.password = password;
        this.fileList = fileList;
        this.blob = blob;
    }

    public Archive(String fileName, List<UploadFile> filesList, byte[] blob) {
        this.name = fileName;
        this.fileList = filesList;
        this.blob = blob;
    }

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public List<UploadFile> getFileList() {
        return fileList;
    }
    public void setFileList(List<UploadFile> fileList) {
        this.fileList = fileList;
    }
    public int getSize(){
        return fileList.size();
    }
    public void setBlob(byte[] blob) {
        this.blob = blob;
    }
    public byte[] getBlob() {
        return blob;
    }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}