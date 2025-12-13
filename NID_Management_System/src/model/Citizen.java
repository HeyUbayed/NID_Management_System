package model;

import java.time.LocalDate;

public class Citizen {
    private int citizenId;
    private String fullName;
    private String fatherName;
    private String motherName;
    private LocalDate dateOfBirth;
    private String gender;
    private String bloodGroup;
    private String religion;
    private String phoneNumber;
    private String email;
    private String division;
    private String district;
    private String upazila;
    private String postalCode;
    private String nidNumber;
    private String password;
    private String status;
    private LocalDate registrationDate;


    public Citizen() {}

    public Citizen(String fullName, String fatherName, String motherName,
                   LocalDate dateOfBirth, String gender, String bloodGroup,
                   String religion, String phoneNumber, String email,
                   String division, String district, String upazila,
                   String postalCode, String password) {
        this.fullName = fullName;
        this.fatherName = fatherName;
        this.motherName = motherName;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.bloodGroup = bloodGroup;
        this.religion = religion;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.division = division;
        this.district = district;
        this.upazila = upazila;
        this.postalCode = postalCode;
        this.password = password;
        this.status = "Pending";
    }

    public int getCitizenId() { return citizenId; }
    public void setCitizenId(int citizenId) { this.citizenId = citizenId; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getFatherName() { return fatherName; }
    public void setFatherName(String fatherName) { this.fatherName = fatherName; }

    public String getMotherName() { return motherName; }
    public void setMotherName(String motherName) { this.motherName = motherName; }

    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getBloodGroup() { return bloodGroup; }
    public void setBloodGroup(String bloodGroup) { this.bloodGroup = bloodGroup; }

    public String getReligion() { return religion; }
    public void setReligion(String religion) { this.religion = religion; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getDivision() { return division; }
    public void setDivision(String division) { this.division = division; }

    public String getDistrict() { return district; }
    public void setDistrict(String district) { this.district = district; }

    public String getUpazila() { return upazila; }
    public void setUpazila(String upazila) { this.upazila = upazila; }

    public String getPostalCode() { return postalCode; }
    public void setPostalCode(String postalCode) { this.postalCode = postalCode; }

    public String getNidNumber() { return nidNumber; }
    public void setNidNumber(String nidNumber) { this.nidNumber = nidNumber; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDate getRegistrationDate() { return registrationDate; }
    public void setRegistrationDate(LocalDate registrationDate) { this.registrationDate = registrationDate; }

    @Override
    public String toString() {
        return "Citizen{" +
                "citizenId=" + citizenId +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", nidNumber='" + nidNumber + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}