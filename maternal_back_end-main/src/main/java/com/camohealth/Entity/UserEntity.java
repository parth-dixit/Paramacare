package com.camohealth.Entity;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "users")
public class UserEntity {
    @Id
    @SequenceGenerator(name="seq-gen",sequenceName="UserIdSequence", initialValue=2, allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="seq-gen")
    @Column(name = "user_id")
    private long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "address1")
    private String address1;

    @Column(name = "city")
    private String city;

    @Column(name = "state")
    private String state;

    @Column(name = "zipcode")
    private int zipcode;

    @Column(name = "password")
    private String password;

    @Column(name = "email_id")
    private String email;

    @Column(name = "phone_no")
    private int phoneNumber;

    @Column(name = "user_type")
    private String userType;

    @Column(name = "dob")
    private Date dateOfBirth;

    @Column(name = "speciality_id")
    private  int specialityId;

    @Lob
    @Type(type="org.hibernate.type.BinaryType")
    @Column(name = "license")
    private  byte[] license;

    @Lob
    @Type(type="org.hibernate.type.BinaryType")
    @Column(name = "certificate")
    private  byte[] certificate;

    @Column(name = "isLoggedIn")
    private boolean loggedIn=false;

    //from here
    @Column(name="insuranceplan")
    private String insurancePlan;

    @Lob
    @Type(type="org.hibernate.type.BinaryType")
    @Column(name="insurancecard")
    private byte[] insuranceCard;

    @Column(name="insurancenum")
    private int insuranceNum;

    @Column(name = "isPregnant")
    private boolean isPregnant;



    @Column(name = "triemester")
    private int trimester;

    @Column(name = "specialityconsultation")
    private String category;

    public UserEntity(String email, String password, String address1, String city, String state, String firstName, String lastName,
                      int phoneNumber, int zipcode, byte[] certificate, Date dateOfBirth, byte[] license
            , String userType, int specialityId, String insurancePlan, byte[] insuranceCard, int insuranceNum) {
        this.email=email;
        this.password=password;
        this.address1=address1;
        this.city=city;
        this.state=state;
        this.firstName=firstName;
        this.lastName= lastName;
        this.phoneNumber=phoneNumber;
        this.zipcode=zipcode;
        this.certificate=certificate;
        this.dateOfBirth=dateOfBirth;
        this.license=license;
        this.userType=userType;
        this.specialityId=specialityId;
        this.insuranceCard=insuranceCard;
        this.insuranceNum=insuranceNum;
        this.insurancePlan=insurancePlan;

    }



    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public String getInsurancePlan() {
        return insurancePlan;
    }

    public void setInsurancePlan(String insurancePlan) {
        this.insurancePlan = insurancePlan;
    }

    public byte[] getInsuranceCard() {
        return insuranceCard;
    }

    public void setInsuranceCard(byte[] insuranceCard) {
        this.insuranceCard = insuranceCard;
    }

    public int getInsuranceNum() {
        return insuranceNum;
    }

    public void setInsuranceNum(int insuranceNum) {
        this.insuranceNum = insuranceNum;
    }

    public UserEntity() {

    }

    public long getId() {
        return id;
    }

    public int getZipcode() {
        return zipcode;
    }

    public void setZipcode(int zipcode) {
        this.zipcode = zipcode;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public int getSpecialityId() {
        return specialityId;
    }

    public void setSpecialityId(int specialityId) {
        this.specialityId = specialityId;
    }

    public byte[] getLicense() {
        return license;
    }

    public void setLicense(byte[] license) {
        this.license = license;
    }

    public byte[] getCertificate() {
        return certificate;
    }

    public void setCertificate(byte[] certificate) {
        this.certificate = certificate;
    }

    public String getAddress2() {
        return city;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public void setAddress2(String city) {
        this.city = city;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(int phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public boolean getIsPregnant() {
        return isPregnant;
    }

    public void setIsPregnant(boolean isPregnant) {
        this.isPregnant = isPregnant;
    }

    public int getTrimester() {
        return trimester;
    }

    public void setTrimester(int trimester) {
        this.trimester = trimester;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }


}
