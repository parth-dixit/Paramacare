package com.camohealth.Repository;

import com.camohealth.Entity.EducationHubEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface EducationHubRepository extends JpaRepository<EducationHubEntity, Integer> {


}
