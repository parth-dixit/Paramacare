package com.camohealth.Service;

import com.camohealth.Entity.UserEntity;
import com.camohealth.Repository.UserRepository;
import com.camohealth.model.signUpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    UserEntity user;

    UserEntity userentity;
    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);


    public boolean ifUserExists(@NotNull String emailId){
        List<UserEntity> users = userRepository.findUserByEmail(emailId);
        LOG.info("users: "+ users);
        if (users.size() > 0) {
            return true;
        }
        user.setFirstName(userentity.getFirstName());
        user.setLastName(userentity.getLastName());
        user.setUserType(userentity.getUserType());
        user.setAddress2(userentity.getAddress1());
        user.setAddress1(userentity.getAddress2());
        user.setZipcode(userentity.getZipcode());
        user.setPassword(userentity.getPassword());
        user.setEmail(userentity.getEmail());
        user.setLicense(userentity.getLicense());
        user.setCertificate(userentity.getCertificate());
        user.setPhoneNumber(userentity.getPhoneNumber());
        user.setSpecialityId(userentity.getSpecialityId());
        user.setDateOfBirth(userentity.getDateOfBirth());
        user.setInsurancePlan(userentity.getInsurancePlan());
        user.setInsuranceNum(userentity.getInsuranceNum());
        user.setInsuranceCard(userentity.getInsuranceCard());
        user = userRepository.save(user);
        LOG.info("User saved!");

        return false;
    }

    public boolean registerUser(UserEntity userentity){
        userRepository.save(userentity);
        return true;
    }

    public signUpStatus getUserLoggedIn(@Valid UserEntity userentity){

        userentity.setLoggedIn(true);
        userRepository.save(userentity);
        return signUpStatus.SUCCESS;

    }

    public boolean editUserProfile(UserEntity user) {
        userRepository.save(user);
        return true;
    }

    public List<UserEntity> getAllPractitionerDetails() {

        return userRepository.getAllPractitionerDetails();
    }

    public List<UserEntity> getAllPatientDetails() {
        return userRepository.getAllPatientDetails();
    }

    public UserEntity getPatientViewDetails(String email) {
        return userRepository.getPatientViewDetails(email);
    }

    public UserEntity getPractitionerViewDetails(String email) {
        return userRepository.getPractitionerViewDetails(email);
    }
}
