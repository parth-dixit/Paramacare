package com.camohealth.Controller;

import com.camohealth.Entity.AvailabilityEntity;
import com.camohealth.Repository.AvailabilityRepository;
import com.camohealth.Service.AvailabilityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RequestMapping("/availability")
@RestController
public class AvailabilityController {

    @Autowired
    AvailabilityService availabilityService;

    @Autowired
    AvailabilityRepository availabilityRepository;

    private static final Logger LOG = LoggerFactory.getLogger(AvailabilityController.class);

    @PostMapping("/checkavailability")
    public ResponseEntity<Object> checkAvailability(@RequestBody AvailabilityEntity req) {

        try {
            LOG.info("Inside availability page!");
            String timeID = req.getTimeId();
            String date_of_availability = req.getDateOfAvailability();

            if (timeID.equals("")) {
                return new ResponseEntity<>("FAILURE-EMPTY_Description", HttpStatus.BAD_REQUEST);
            }

            if (date_of_availability.equals("")) {
                return new ResponseEntity<>("FAILURE-EMPTY_Description", HttpStatus.BAD_REQUEST);
            }

            return new ResponseEntity<>(date_of_availability, HttpStatus.OK);
        }
        catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<Object>("Bad Request", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/addavailability")
    public ResponseEntity <Object> addAvailability(@RequestBody AvailabilityEntity req){
        boolean flag = availabilityService.saveAvailability(new AvailabilityEntity(req.getPractitionerId(), req.getTimeId(), req.getDateOfAvailability(), req.getCreatedBy()
                ,req.getCreatedAt(),req.getModifiedBy(),req.getModifiedAt(),req.getIsDeleted()));


        return new ResponseEntity<>("Your availability has been saved", HttpStatus.OK);
    }

    @PutMapping("/updateavailability/{practitioner_id}")
    public ResponseEntity <Object> updateAvailability(@PathVariable String practitioner_id, @RequestBody AvailabilityEntity avail){

        try {
            /*List<AvailabilityEntity> fetch_avail = availabilityRepository.getAvailability(practitioner_id);
            if (fetch_avail.size() == 0) {
                return new ResponseEntity<>("FAILURE-Email does not Exists", HttpStatus.BAD_REQUEST);
            }
            AvailabilityEntity fetchedAvailability = fetch_avail.get(0);
            fetchedAvailability.setTimeId(avail.getTimeId());
            fetchedAvailability.setModifiedBy(avail.getModifiedBy());
            fetchedAvailability.setModifiedAt(avail.getModifiedAt());
            this.availabilityService.updateAvailability(fetchedAvailability);*/
            return ResponseEntity.ok().build();
        }

        catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }

    }


    @RequestMapping("/getavailability")
    public ResponseEntity<List<AvailabilityEntity>> getAllComplaints() {
        try {

            LOG.info("GetAllAvailability!");
            List<AvailabilityEntity> avail = availabilityService.getPractitionerAvailability();
            return ResponseEntity.ok(avail);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

}