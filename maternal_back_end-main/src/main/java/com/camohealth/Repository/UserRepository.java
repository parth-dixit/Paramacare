package com.camohealth.Repository;

import com.camohealth.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {

    ArrayList<UserEntity> findUserByEmail(String email);

    @Query(value= "SELECT * FROM users where user_type='Practitioner'", nativeQuery = true)
    List<UserEntity> getAllPractitionerDetails();

    @Query(value= "SELECT * FROM users where user_type='Patient'", nativeQuery = true)
    List<UserEntity> getAllPatientDetails();

    @Query(value= "SELECT * FROM users where email_id= :email", nativeQuery = true)
    UserEntity getPatientViewDetails(@Param("email") String email);

    @Query(value= "SELECT * FROM users where email_id= :email", nativeQuery = true)
    UserEntity getPractitionerViewDetails(@Param("email") String email);

}
