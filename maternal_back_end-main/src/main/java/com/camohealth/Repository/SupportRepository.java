package com.camohealth.Repository;

import com.camohealth.Entity.SupportEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SupportRepository extends JpaRepository<SupportEntity, Integer> {

    //ArrayList<SupportEntity> getAllComplaint(String patient_id);

    @Query(value= "SELECT * FROM complaint", nativeQuery = true)
    List<SupportEntity> getAllComplaint();

}

