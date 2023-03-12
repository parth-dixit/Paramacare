package com.camohealth.Entity;
import javax.persistence.*;

@Entity
@Table(name = "complaint")
public class SupportEntity {

    @Id
    @SequenceGenerator(name="seq-gen",sequenceName="complaintIdSequence", initialValue=2, allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="seq-gen")
    @Column(name = "complaint_id")
    private int complaint_id;

    @Column(name = "patientid")
    private String patient_id;

    @Column(name = "practitionerid")
    private String practitioner_id;

    @Column(name = "appointmentid")
    private String appointment_id;

    @Column(name = "description")
    private String description;

    @Column(name = "adminid")
    private String admin_id;

    @Column(name = "createdBy")
    private String created_by;

    @Column(name = "status")
    private String status;

    @Column(name = "createdAt")
    private String created_at;

    @Column(name = "modifiedBy")
    private String modified_by;

    @Column(name = "modifiedAt")
    private String modified_at;

    @Column(name = "isDeleted")
    private String isDeleted;

    @Column(name = "summary")
    private String summary;


    public SupportEntity(String description, String summary, String patient_id ,String practitioner_id, String appointment_id, String admin_id,

                         String status, String created_by, String created_at, String modified_by, String modified_at, String isDeleted)
    {
        this.description = description;
        this.summary = summary;
        this.patient_id = patient_id;
        this.practitioner_id = practitioner_id;
        this.appointment_id = appointment_id;
        this.admin_id = admin_id;
        this.status = status;
        this.created_by = created_by;
        this.created_at = created_at;
        this.modified_by = modified_by;
        this.modified_at = modified_at;
        this.isDeleted = isDeleted;
    }

    public SupportEntity() {

    }

    public int getComplaintId() {
        return complaint_id;
    }

    public String getPatientId() {
        return patient_id;
    }

    public void setPatientId(String patient_id) {
        this.patient_id = patient_id;
    }

    public String getPractitionerId() {
        return practitioner_id;
    }

    public void setPractitionerId(String practitioner_id) {
        this.practitioner_id = practitioner_id;
    }

    public String getAppointmentId() {
        return appointment_id;
    }

    public void setAppointmentId(String appointment_id) {
        this.appointment_id = appointment_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAdminId() {
        return admin_id;
    }

    public void setAdminId(String admin_id) {
        this.admin_id = admin_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreatedBy() {
        return created_by;
    }

    public void setCreatedBy(String created_by) {
        this.created_by = created_by;
    }

    public String getCreatedAt() {
        return created_at;
    }

    public void setCreatedAt(String created_at) {
        this.created_at = created_at;
    }

    public String getModifiedBy() {
        return modified_by;
    }

    public void setModifiedBy(String modified_by) {
        this.modified_by = modified_by;
    }

    public String getModifiedAt() {
        return modified_at;
    }

    public void setModifiedAt(String modified_at) {
        this.modified_at = modified_at;
    }

    public String getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(String isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }


}