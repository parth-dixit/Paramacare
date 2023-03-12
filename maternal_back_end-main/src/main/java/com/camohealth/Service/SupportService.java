package com.camohealth.Service;

import com.camohealth.Entity.SupportEntity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Hashtable;


@Service
public class SupportService {
    @Autowired
    com.camohealth.Repository.SupportRepository supportRepository;

    SupportEntity support;

    SupportEntity supportentity;
    private static final Logger LOG = LoggerFactory.getLogger(SupportService.class); 

    Hashtable<String,SupportEntity> userComplaints = new Hashtable<String, SupportEntity>();

    public boolean ifComplaintNotNull(@NotNull String description, @NotNull String summary){

        support.setPatientId("2");
        support.setPractitionerId("2");
        support.setAppointmentId("3");
        support.setDescription(supportentity.getDescription());
        support.setAdminId("1");
        support.setStatus("New");
        support.setCreatedBy("2");
        support.setCreatedAt("2022-17-3:19:42:05:24:15");
        support.setModifiedBy("2");
        support.setModifiedAt("2022-17-3:19:42:05:24:15");
        support.setIsDeleted("No");
        support.setSummary(supportentity.getSummary());
        support = supportRepository.save(support);
        LOG.info("Complaint saved!");

        return false;
    }

    public boolean saveComplaint(SupportEntity supportentity){


      supportRepository.save(supportentity);

        return true;
    }

    public List<SupportEntity> getAllUserComplaints() {

		return supportRepository.getAllComplaint();
	}

}
