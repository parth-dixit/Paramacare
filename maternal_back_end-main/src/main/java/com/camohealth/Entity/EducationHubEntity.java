package com.camohealth.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;

@Entity
@Table(name = "videos")
public class EducationHubEntity {
    @Id
    @SequenceGenerator(name="seq-gen",sequenceName="VideoIdSequence", initialValue=1, allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="seq-gen")
    @Column(name = "video_id")
    private int id;

    @Column(name = "video_name")
    private String videoName;

    public EducationHubEntity(String videoName, String url,String createdBy,Timestamp createdOn, String modifiedBy,
                              Timestamp modifiedOn) {
        this.videoName = videoName;
        this.url = url;
        this.createdOn = createdOn;
        this.createdBy = createdBy;
        this.modifiedOn = modifiedOn;
        this.modifiedBy = modifiedBy;
    }

    @Column(name = "url")
    private String url;

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd-MM-yyyy")
    @Column(name = "createdOn")
    private Timestamp createdOn;

    public EducationHubEntity() {

    }

    @Column(name = "createdBy")
    private String createdBy;

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd-MM-yyyy")
    @Column(name = "modifiedOn")
    private Timestamp modifiedOn;

    @Column(name = "isActive")
    private boolean isActive=false;



    @Column(name = "modifiedBy")
    private String modifiedBy;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Timestamp getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Timestamp createdOn) {
        this.createdOn = createdOn;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Timestamp getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(Timestamp modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }
}