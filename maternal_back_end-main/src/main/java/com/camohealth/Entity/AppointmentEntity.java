package com.camohealth.Entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Appointment")
public class AppointmentEntity {
    @Id
    @SequenceGenerator(name = "seq-gen", sequenceName = "AppointmentIdSequence", initialValue = 2, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq-gen")
    @Column(name = " appointment_id")
    private Integer  appointmentId;

    @Column(name = "patient_id")
    private String patientId;

    @Column(name = "practitioner_id")
    private String practitionerId;

    @Column(name = "timeSlot")
    private LocalDateTime timeSlot;

    @Column(name = "appointment_mode")
    private String appointmentMode;

    @Column(name = "zoom_link")
    private String zoomLink;

    @Column(name = "speciality")
    private String speciality;

    @Column(name = "review")
    private String review;

    @Column(name = "rating")
    private String rating;

    @Column(name = "status")
    private String status;

    @Column(name = "createdBy")
    private String createdBy;

    @Column(name = "createdAt")
    private LocalDateTime createdAt;


    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    @Column(name = "modifiedBy")
    private String modifiedBy;

    @Column(name = "modifiedAt")
    private LocalDateTime modifiedAt;

    @Column(name = "isDeleted")
    private boolean isDeleted;

    public AppointmentEntity() {
    }

    public AppointmentEntity( String patientId, String practitionerId, String speciality, LocalDateTime timeSlot, LocalDateTime createdAt, String createdBy, String modifiedBy, LocalDateTime modifiedAt, boolean isDeleted) {
        this.patientId = patientId;
        this.practitionerId = practitionerId;
        this.speciality=speciality;
        this.timeSlot = timeSlot;
        this.createdAt = createdAt;
        this.createdBy= createdBy;
        this.modifiedBy = modifiedBy;
        this.modifiedAt = modifiedAt;
        this.isDeleted = isDeleted;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public Integer getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(Integer appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatient_id(String patient_id) {
        this.patientId = patientId;
    }

    public String getPractitionerId() {
        return practitionerId;
    }

    public void setPractitioner_id(String practitioner_id) {
        this.practitionerId = practitionerId;
    }

    public LocalDateTime getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(LocalDateTime timeSlot) {
        this.timeSlot = timeSlot;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public LocalDateTime getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(LocalDateTime modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    public boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(boolean deleted) {
        this.isDeleted = deleted;
    }
}
