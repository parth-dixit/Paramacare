package com.camohealth.Repository;

import com.camohealth.Entity.AppointmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
@Repository
public interface AppointmentRepository extends JpaRepository<AppointmentEntity, Integer> {

    @Query(value= "SELECT * FROM appointment where appointment_id= :appointmentId", nativeQuery = true)
    AppointmentEntity findAppointmentEntityByAppointment(Integer appointmentId);

    @Query(value= "SELECT * FROM appointment where patient_id= :patient_id and status= :status", nativeQuery = true)
    ArrayList<AppointmentEntity> getPatientAppointments(String patient_id, String status);

    @Query(value= "SELECT * FROM appointment where patient_id= :practitioner_id and status= :status", nativeQuery = true)
    ArrayList<AppointmentEntity> getPractitionerAppointments(String practitioner_id, String status);

//    ArrayList<AppointmentEntity> getPractitionerReview(String practitioner_id);
}
