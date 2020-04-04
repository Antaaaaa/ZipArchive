package com.example.zip.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "my_file")
public class UploadFile implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "my_file_id")
    private Integer id;

    @Column(name = "file_name")
    private String name;
//    @Column(name = "my_file")
//    @Lob
//    private byte[] blob;

    @ManyToOne
    @JoinColumn(name = "archive_id", foreignKey=@ForeignKey(name="archive_id_fk"))
    private Archive archive;

    public UploadFile() {}

    //public UploadFile(byte[] blob){
//        this.blob = blob;
//    }

    public UploadFile(String name){
        this.name = name;
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
    //    public byte[] getBlob() {
//        return blob;
//    }
//
//    public void setBlob(byte[] blob) {
//        this.blob = blob;
//    }
}
