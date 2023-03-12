package com.camohealth.Entity;
import javax.persistence.*;

@Entity
@Table(name = "availability")
public class AvailabilityEntity {
    @Id
    @SequenceGenerator(name="seq-gen",sequenceName="availabilityidsequence", initialValue=2, allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="seq-gen")
    @Column(name = "availability_id")
    private int availability_id;

    @Column(name = "practitionerId")
    private String practitioner_id;

    @Column(name = "timeId")
    private String time_id;

    @Column(name = "dateOfAvailability")
    private String date_of_availablity;

    @Column(name = "createdBy")
    private String created_by;

    @Column(name = "createdAt")
    private String created_at;

    @Column(name = "modifiedBy")
    private String modified_by;

    @Column(name = "modifiedAt")
    private String modified_at;

    @Column(name = "isDeleted")
    private String isDeleted;

    public AvailabilityEntity() {

    }

    public AvailabilityEntity(String practitioner_id, String time_id,

                              String date_of_availability, String created_by, String created_at, String modified_by, String modified_at, String isDeleted)
    {
        this.practitioner_id = practitioner_id;
        this.time_id = time_id;
        this.date_of_availablity = date_of_availability;
        this.created_by = created_by;
        this.created_at = created_at;
        this.modified_by = modified_by;
        this.modified_at = modified_at;
        this.isDeleted = isDeleted;
    }

    public int getAvailabilityId() {
        return availability_id;
    }

    public String getPractitionerId() {
        return practitioner_id;
    }

    public void setPractitionerId(String practitioner_id) {
        this.practitioner_id = practitioner_id;
    }

    public String getTimeId() {
        return time_id;
    }

    public void setTimeId(String time_id) {
        this.time_id = time_id;
    }

    public String getDateOfAvailability() {
        return date_of_availablity;
    }

    public void setDateOfAvailability(String date_of_availablity) {
        this.date_of_availablity = date_of_availablity;
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

}