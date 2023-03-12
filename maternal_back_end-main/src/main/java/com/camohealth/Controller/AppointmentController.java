package com.camohealth.Controller;

import com.camohealth.Entity.AppointmentEntity;
import com.camohealth.Repository.AppointmentRepository;
import com.camohealth.Service.AppointmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequestMapping("/appointment")
@RestController
public class AppointmentController {
    @Autowired
    AppointmentService appointmentService;

    @Autowired
    AppointmentRepository appointmentRepository;

    private static final Logger LOG = LoggerFactory.getLogger(AppointmentController.class);

    /**
     * Helps user to sign up and register into the app.
     * This call should be done if you only want to register.
     */
    //To book a new appointment
    @PostMapping("/scheduleAppointment")
    public ResponseEntity<Object> bookAppointment(@RequestBody AppointmentEntity req){
        try {
            LOG.info("inside schedule: ");
            AppointmentEntity appointmentEntity= new AppointmentEntity(req.getPatientId(), req.getPractitionerId(), req.getSpeciality(), req.getTimeSlot(), req.getCreatedAt()
                    , req.getCreatedBy(), req.getModifiedBy(), req.getModifiedAt(), req.getIsDeleted());

            appointmentService.saveAppointment(appointmentEntity);

            return new ResponseEntity<>("Your appointment has been scheduled", HttpStatus.OK);
        }
        catch (Exception e){
            return ResponseEntity.badRequest().build();
        }

    }

    //to retrieve appointments for a particular patient the status can be either upcoming or previous.
    @GetMapping("/getAppointmentsByPatientId/{patient_id}/{status}")
    public ResponseEntity<List<AppointmentEntity>> getAppointmentByPatientId(@PathVariable String patient_id, @PathVariable String status) {
         try{
        List<AppointmentEntity> appointmentEntities= appointmentService.getAppointmentByPatientId(patient_id,status);
        LOG.info("inside getAppointmentsByPatientId: ");
             return ResponseEntity.ok(appointmentEntities);
         }
        catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    //To get upcoming or previous appointments of practitioners.
    @GetMapping("/getAppointmentsByPractitionerId/{practitioner_id}/{status}")
    public ResponseEntity<List<AppointmentEntity>> getAppointmentByPractitionerId(@PathVariable String practitioner_id, @PathVariable String status) {
        try{
            List<AppointmentEntity> appointmentEntities= appointmentService.getAppointmentByPractitionerId(practitioner_id,status);
            LOG.info("inside getAppointmentsByPatientId: ");
            return ResponseEntity.ok(appointmentEntities);
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    //to reschedule appointment for both patient and practitioner
    @PutMapping ("/rescheduleAppointment/{appointment_id}")
    public  ResponseEntity<Object> rescheduleAppointment(@Valid @RequestBody AppointmentEntity req, @PathVariable String appointment_id) {
        try{
            Integer appointmentId=req.getAppointmentId();
            AppointmentEntity appointmentEntity = appointmentService.findAppointmentEntityByAppointment(appointmentId);
            if (appointmentEntity==null) {
                return new ResponseEntity<>("FAILURE-Email does not Exists", HttpStatus.BAD_REQUEST);
            }

            appointmentEntity.setSpeciality(req.getSpeciality());
            appointmentEntity.setPractitioner_id(req.getPractitionerId());
            appointmentEntity.setPatient_id(req.getPatientId());
            appointmentEntity.setTimeSlot(req.getTimeSlot());
            LOG.info("inside getAppointmentsByPatientId: ");

            boolean flag= appointmentService.saveAppointment(appointmentEntity);
            return new ResponseEntity<>("Saved Appointment", HttpStatus.OK);
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }

    }

    //to update the appointment status to
    @PutMapping ("/updateAppointmentStatus/{appointment_id}/{status}")
    public  ResponseEntity <Object> updateAppointmentStatus(@Valid @RequestBody AppointmentEntity req, @PathVariable String appointment_id,
                                                            @PathVariable String status) {
        try{
            Integer appointmentId=req.getAppointmentId();
            AppointmentEntity appointmentEntity = appointmentService.findAppointmentEntityByAppointment(appointmentId);
            if (appointmentEntity==null) {
                return new ResponseEntity<>("FAILURE-Email does not Exists", HttpStatus.BAD_REQUEST);
            }

            appointmentEntity.setStatus(req.getStatus());
            LOG.info("inside getAppointmentsByPatientId: ");
            boolean flag= appointmentService.saveAppointment(appointmentEntity);
            return new ResponseEntity<>("Saved Appointment", HttpStatus.OK);
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }


    //to cancel the appointment
    @PutMapping ("/cancelAppointmentStatus/{appointment_id}/{status}")
    public  ResponseEntity <Object> cancelAppointment(@Valid @RequestBody AppointmentEntity req, @PathVariable String appointment_id,

                                                            @PathVariable String status) {
        try{
            Integer appointmentId=req.getAppointmentId();
            AppointmentEntity appointmentEntity = appointmentService.findAppointmentEntityByAppointment(appointmentId);
            if (appointmentEntity==null) {
                return new ResponseEntity<>("FAILURE-Email does not Exists", HttpStatus.BAD_REQUEST);
            }

            appointmentEntity.setStatus(req.getStatus());
            appointmentEntity.setIsDeleted(true);
            LOG.info("inside getAppointmentsByPatientId: ");
            boolean flag= appointmentService.saveAppointment(appointmentEntity);
            return new ResponseEntity<>("You Have Been Registered", HttpStatus.OK);
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
        }
//        @PostMapping("/addRating")
//        public ResponseEntity <Object> addRating(@RequestBody AppointmentEntity req){
//            try{
//                String appointmentId=req.getAppointmentId();
//                AppointmentEntity appointmentEntity = appointmentService.findAppointmentEntityByAppointment(appointmentId);
//                if (appointmentEntity==null) {
//                    return new ResponseEntity<>("FAILURE-Email does not Exists", HttpStatus.BAD_REQUEST);
//                }
//                appointmentEntity.setRating(req.getStatus());
//                LOG.info("inside getAppointmentsByPatientId: ");
//                AppointmentEntity appointmentEntities= appointmentService.saveAppointment(appointmentEntity);
//
//                return ResponseEntity.ok(appointmentEntities);
//            }
//            catch (Exception e) {
//                return ResponseEntity.badRequest().build();
//            }

    }
