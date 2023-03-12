package com.camohealth.Service;

import com.camohealth.Entity.AvailabilityEntity;
import com.camohealth.Repository.AvailabilityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AvailabilityService {

    @Autowired
    AvailabilityRepository availabilityRepository;

    AvailabilityEntity availability;

    AvailabilityEntity availabilityentity;

    private static final Logger LOG = LoggerFactory.getLogger(AvailabilityService.class); 


    public boolean saveAvailability(AvailabilityEntity availabilityEntity){

        availabilityRepository.save(availabilityEntity);

        return true;
    }

    public boolean updateAvailability(AvailabilityEntity availabilityEntity){

        availabilityRepository.save(availabilityEntity);

        return true;

    }

    public List<AvailabilityEntity> getPractitionerAvailability() {

		return availabilityRepository.getAvailability();
	}

}
