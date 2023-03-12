package com.camohealth.Controller;

import com.camohealth.Entity.EducationHubEntity;
import com.camohealth.Repository.EducationHubRepository;
import com.camohealth.Service.EducationHubService;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/hub")
@RestController
public class EducationHubController {

    @Autowired
    EducationHubService educationhubservice;

    @Autowired
    EducationHubRepository educationhubrepository;



    private static final Logger LOG = LoggerFactory.getLogger(EducationHubController.class);

    @PostMapping("/insertVideo")
    public ResponseEntity<Object> AddVideo(@RequestBody @NotNull EducationHubEntity req){

        boolean flag = educationhubservice.saveVideo(new EducationHubEntity(req.getVideoName(), req.getUrl(),
                req.getCreatedBy(),req.getCreatedOn(),req.getModifiedBy(),req.getModifiedOn()));

        return new ResponseEntity<>("Your Video Has Been Saved", HttpStatus.OK);
    }

    @GetMapping("/videos/getAllVideos")
    public ResponseEntity<List<EducationHubEntity>> getAllVideos() {
        try {
            List<EducationHubEntity> videos = this.educationhubrepository.findAll();
            return ResponseEntity.ok(videos);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}