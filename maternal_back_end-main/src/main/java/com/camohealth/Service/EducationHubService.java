package com.camohealth.Service;

import com.camohealth.Entity.EducationHubEntity;
import com.camohealth.Repository.EducationHubRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EducationHubService {
    private static final Logger LOG = LoggerFactory.getLogger(EducationHubService.class);

    @Autowired
    EducationHubRepository educationhubrepository;

    public boolean saveVideo(EducationHubEntity educationhubentity){
        educationhubrepository.save(educationhubentity);
        return true;
    }


}
