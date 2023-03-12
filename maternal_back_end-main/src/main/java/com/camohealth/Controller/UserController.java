package com.camohealth.Controller;

import com.camohealth.Entity.UserEntity;
import com.camohealth.Repository.UserRepository;
import com.camohealth.Service.UserService;
import com.camohealth.model.signUpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

@RequestMapping(value= "/users/*", method=RequestMethod.OPTIONS)
//@RequestMapping("/users")
@RestController
public class UserController {
    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);

    /**
     * Helps user to sign up and register into the app.
     * This call should be done if you only want to register.
     */



   // need to remove this method as email validation is being done from frontend.
//    @PostMapping("/signUp/emailDoesExists")
//    public ResponseEntity<Object> emailDoesExists(@RequestBody UserEntity req) {
//        try {
//            LOG.info("Inside Sign Up!");
//            String email = req.getEmail();
//            String password = req.getPassword();
//            String emailCheck[] = email.split("@");
//
//            // To check if the email and password are not empty
//            if (email.equals("")) {
//                return new ResponseEntity<>("FAILURE-EMPTY_EMAIL", HttpStatus.BAD_REQUEST);
//            }
//            if (password.equals("")) {
//                return new ResponseEntity<>("FAILURE-EMPTY_PASSWORD", HttpStatus.BAD_REQUEST);
//            }
//
//            boolean userExists = userService.ifUserExists(email);
//            LOG.info("userExists: "+userExists);
//            if(userExists){
//                return new ResponseEntity<>("This Email Id Already Exists", HttpStatus.NOT_ACCEPTABLE);
//            }
//
//            return new ResponseEntity<>(email, HttpStatus.OK);
//        }
//
//        catch (Exception e) {
//            e.printStackTrace();
//            return new ResponseEntity<Object>("Bad Request", HttpStatus.BAD_REQUEST);
//        }
//
//
//    }

//    //To Register the user(this is common method for both patient and practitioner)
//    @PostMapping(value="/signUp",consumes = MediaType.MULTIPART_FORM_DATA_VALUE )
//    @CrossOrigin(origins = "*")
//    public ResponseEntity <Object> SignUpUser(@ModelAttribute UserEntity req){
//        LOG.info("Inside Sign Up!");
//        boolean flag = userService.registerUser(new UserEntity(req.getEmail(),req.getPassword(),req.getAddress1()
//                ,req.getAddress2(),req.getFirstName(),req.getLastName(),req.getPhoneNumber(),req.getZipcode(),req.getCertificate()
//                ,req.getDateOfBirth(),req.getLicense(),req.getUserType(),req.getSpecialityId(), req.getInsurancePlan(), req.getInsuranceCard(), req.getInsuranceNum()));
//
//        return new ResponseEntity<>("You Have Been Registered", HttpStatus.OK);
//    }

    public void corsHeaders(HttpServletResponse response) {
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.addHeader("Access-Control-Allow-Headers", "origin, content-type, accept, x-requested-with");
        response.addHeader("Access-Control-Max-Age", "3600");
    }



    //To login the user
    @PostMapping("/login")
    @CrossOrigin(origins = "*")
    public signUpStatus loginUser(@Valid @RequestBody UserEntity req) {
        LOG.info("login!");
        return userService.getUserLoggedIn(req);
    }

    public signUpStatus getUserLoggedIn(@Valid UserEntity userentity){
        LOG.info("getloggedin!");
        userentity.setLoggedIn(true);
        userService.getUserLoggedIn(userentity);
        return signUpStatus.SUCCESS;

    }

    //ADMIN-To retrieve all the practitioner details from Database, this is for admin only
    @GetMapping("/getAllPractitionerDetails")
    public ResponseEntity<List<UserEntity>> getAllPractitionerDetails() {
        try {
            LOG.info("getAllPractitionerDetails!");
            List<UserEntity> user = this.userService.getAllPractitionerDetails();
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    //ADMIN-To retrieve all the patient details from Database, this is for admin only
    @GetMapping("/getAllPatientDetails")
    public ResponseEntity<List<UserEntity>> getAllPatientDetails() {
        try {
            LOG.info("getAllPatientDetails");
            List<UserEntity> user = this.userService.getAllPatientDetails();
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    //ADMIN-To retrieve all the patient details from Database, this is for admin only
    @GetMapping("/getPatientViewDetails/{email}")
    public ResponseEntity<UserEntity> getPatientViewDetails(@PathVariable String email) {
        try {
            LOG.info("getAllPatientDetails-view");
            UserEntity user = this.userService.getPatientViewDetails(email);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    //ADMIN-To retrieve practitioner details of particular practitioner-ViewDeatils, this is for admin only
    @GetMapping("/getPractitionerViewDetails/{email}")
    public ResponseEntity<UserEntity> getPractitionerViewDetails(@PathVariable String email) {
        try {
            LOG.info("getAllPatiDetails-view");
            UserEntity user = this.userService.getPractitionerViewDetails(email);
            LOG.info(user.getFirstName());
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    //USER-for editing the user profile.
//    @PutMapping("/editProfile/{email}")
//    public ResponseEntity<Object> editUserProfile(@PathVariable String email, @RequestBody UserEntity user) {
//        try {
//
//            List<UserEntity> users = userRepository.findUserByEmail(email);
//            if (users.size() == 0) {
//                return new ResponseEntity<>("FAILURE-Email does not Exists", HttpStatus.BAD_REQUEST);
//            }
//            UserEntity fetchedUser = users.get(0);
//            fetchedUser.setEmail(user.getEmail());
//            fetchedUser.setPassword(user.getPassword());
//            fetchedUser.setFirstName(user.getFirstName());
//            fetchedUser.setLastName(user.getLastName());
//            fetchedUser.setAddress1(user.getAddress1());
//            fetchedUser.setCity(user.getCity());
//            fetchedUser.setPhoneNumber(user.getPhoneNumber());
//            fetchedUser.setZipcode(user.getZipcode());
//            fetchedUser.setCertificate(user.getCertificate());
//            fetchedUser.setDateOfBirth(user.getDateOfBirth());
//            fetchedUser.setLicense(user.getLicense());
//            fetchedUser.setUserType(user.getUserType());
//            fetchedUser.setSpecialityId(user.getSpecialityId());
//            fetchedUser.setInsuranceNum(user.getInsuranceNum());
//            fetchedUser.setInsurancePlan(user.getInsurancePlan());
//            fetchedUser.setInsuranceCard(user.getInsuranceCard());
//            fetchedUser.setIsPregnant(user.getIsPregnant());
//            fetchedUser.setTrimester(user.getTrimester());
//            fetchedUser.setCategory(user.getCategory());
//
//            this.userService.editUserProfile(fetchedUser);
//            return ResponseEntity.ok().build();
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().build();
//        }
//    }

}
