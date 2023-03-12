package com.camohealth.Controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import com.camohealth.Entity.UserEntity;
import com.camohealth.Repository.UserRepository;
import com.camohealth.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/users")
public class SignUpController {

    ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;
    @RequestMapping(value="/signUp", method=RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    //@PostMapping(value = "/upload", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}, produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<Object> uploadFile(@RequestParam("insuranceCard") MultipartFile insurancecard, @RequestParam("license") MultipartFile license,
                                             @RequestParam("certificate") MultipartFile certificate,
                                             @RequestParam(required=true, value="jsondata")String jsondata) throws IOException  {
        System.out.println("Here");
//        File convertFile = new File(file.getOriginalFilename());
//        convertFile.createNewFile();
//        FileOutputStream fout = new FileOutputStream(convertFile);
//        fout.write(file.getBytes());
//        fout.close();
        InputStream initialStream = insurancecard.getInputStream();
        byte[] insuranceCard = new byte[initialStream.available()];
        initialStream.read(insuranceCard);

        UserEntity userData = objectMapper.readValue(jsondata, UserEntity.class);
        System.out.println(userData.getFirstName());
        System.out.println(userData.getLastName());

        InputStream initialStream1 = license.getInputStream();
        byte[] license1 = new byte[initialStream1.available()];
        initialStream.read(license1);

        InputStream initialStream2 = certificate.getInputStream();
        byte[] certificate1 = new byte[initialStream2.available()];
        initialStream.read(certificate1);


        boolean flag = userService.registerUser(new UserEntity(userData.getEmail(),userData.getPassword(),userData.getAddress1()
                ,userData.getCity(),userData.getState(),userData.getFirstName(),userData.getLastName(),userData.getPhoneNumber(),userData.getZipcode(),certificate1
                ,userData.getDateOfBirth(),license1,userData.getUserType(),userData.getSpecialityId(), userData.getInsurancePlan(), insuranceCard, userData.getInsuranceNum()));

        return new ResponseEntity<>("You Have Been Registered", HttpStatus.OK);
    }

    //@PutMapping("/editProfile/{email}")
    @RequestMapping(value="/editProfile/{email}", method=RequestMethod.PUT, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> editUserProfile(@PathVariable String email, @RequestParam("insuranceCard") MultipartFile insurancecard, @RequestParam("license") MultipartFile license,
                                                  @RequestParam("certificate") MultipartFile certificate,
                                                  @RequestParam("jsondata")String jsondata) throws IOException{
        try {
            System.out.println("Inside edit");
            InputStream initialStream = insurancecard.getInputStream();
            byte[] insuranceCard = new byte[initialStream.available()];
            initialStream.read(insuranceCard);

            UserEntity user = objectMapper.readValue(jsondata, UserEntity.class);

            InputStream initialStream1 = license.getInputStream();
            byte[] license1 = new byte[initialStream1.available()];
            initialStream.read(license1);

            InputStream initialStream2 = certificate.getInputStream();
            byte[] certificate1 = new byte[initialStream2.available()];
            initialStream.read(certificate1);

            List<UserEntity> users = userRepository.findUserByEmail(email);
            if (users.size() == 0) {
                return new ResponseEntity<>("FAILURE-Email does not Exists", HttpStatus.BAD_REQUEST);
            }
            System.out.println("Inside edit");
            UserEntity fetchedUser = users.get(0);
            fetchedUser.setEmail(user.getEmail());
            fetchedUser.setPassword(user.getPassword());
            fetchedUser.setFirstName(user.getFirstName());
            fetchedUser.setLastName(user.getLastName());
            fetchedUser.setAddress1(user.getAddress1());
            fetchedUser.setCity(user.getCity());
            fetchedUser.setState(user.getState());
            fetchedUser.setPhoneNumber(user.getPhoneNumber());
            fetchedUser.setZipcode(user.getZipcode());
            fetchedUser.setCertificate(certificate1);
            fetchedUser.setDateOfBirth(user.getDateOfBirth());
            fetchedUser.setLicense(license1);
            fetchedUser.setUserType(user.getUserType());
            fetchedUser.setSpecialityId(user.getSpecialityId());
            fetchedUser.setInsuranceNum(user.getInsuranceNum());
            fetchedUser.setInsurancePlan(user.getInsurancePlan());
            fetchedUser.setInsuranceCard(insuranceCard);
            fetchedUser.setIsPregnant(user.getIsPregnant());
            fetchedUser.setTrimester(user.getTrimester());
            fetchedUser.setCategory(user.getCategory());

            this.userService.editUserProfile(fetchedUser);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}