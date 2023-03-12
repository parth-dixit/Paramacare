package com.camohealth.Service;

import com.camohealth.Entity.AppointmentEntity;
import com.camohealth.Repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppointmentService {

    @Autowired
    AppointmentRepository appointmentRepository;
    public boolean saveAppointment(AppointmentEntity appointmentEntity) {
        appointmentRepository.save(appointmentEntity);
        return true;
    }

    public List<AppointmentEntity> getAppointmentByPatientId(String patient_id, String status) {
        return appointmentRepository.getPatientAppointments(patient_id,status);

    }

    public List<AppointmentEntity> getAppointmentByPractitionerId(String practitioner_id, String status) {
        return appointmentRepository.getPractitionerAppointments(practitioner_id,status);

    }

    public AppointmentEntity findAppointmentEntityByAppointment(Integer appointmentId) {
        return appointmentRepository.findAppointmentEntityByAppointment(appointmentId);
    }
}
