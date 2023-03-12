package com.camohealth.Controller;
import com.camohealth.Entity.SupportEntity;
import com.camohealth.Repository.SupportRepository;
import com.camohealth.Service.SupportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/step3")
@RestController
public class SupportController {
    @Autowired
    SupportService supportService;

    @Autowired
    SupportRepository supportRepository;

    private static final Logger LOG = LoggerFactory.getLogger(SupportController.class);

    @PostMapping("/complaint/checkComplaint")
    public ResponseEntity<Object> checkComplaint(@RequestBody SupportEntity req) {
        try {
            LOG.info("Inside Report a problem!");
            String description = req.getDescription();
            String summary = req.getSummary();

            if (description.equals("")) {
                return new ResponseEntity<>("FAILURE-EMPTY_Description", HttpStatus.BAD_REQUEST);
            }
            if (summary.equals("")) {
                return new ResponseEntity<>("FAILURE-EMPTY_SUMMARY", HttpStatus.BAD_REQUEST);
            }

            return new ResponseEntity<>("ok", HttpStatus.OK);
        }
        catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<Object>("Bad Request", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/addcomplaint")
    public ResponseEntity <Object> addComplaint(@RequestBody SupportEntity req){
        boolean flag = supportService.saveComplaint(new SupportEntity(req.getDescription(),req.getSummary(),req.getPatientId()
                ,req.getPractitionerId(),req.getAppointmentId(),req.getAdminId(),req.getStatus(),req.getCreatedBy()
                ,req.getCreatedAt(),req.getModifiedBy(),req.getModifiedAt(),req.getIsDeleted()));

        return new ResponseEntity<>("Your Complaint have been added", HttpStatus.OK);
    }


    @RequestMapping("/getcomplaint")

    public ResponseEntity<List<SupportEntity>> getAllComplaints() {
        try {
            LOG.info("GetAllComplaint!");
            List<SupportEntity> complaint = this.supportService.getAllUserComplaints();
            return ResponseEntity.ok(complaint);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }

    }

}