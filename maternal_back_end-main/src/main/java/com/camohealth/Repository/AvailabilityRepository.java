package com.camohealth.Repository;

import com.camohealth.Entity.AvailabilityEntity;
import org.springframework.data.jpa.repository.Query;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;


@Repository
public interface AvailabilityRepository extends JpaRepository<AvailabilityEntity, Integer> {
    //ArrayList<AvailabilityEntity> getAvailabilityByPractitionerId(String practitioner_id);

    @Query(value= "SELECT * FROM availability where practitionerId = '1'", nativeQuery = true)
    List<AvailabilityEntity> getAvailability();

}
