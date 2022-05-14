package guru.qa;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.lang.model.type.ArrayType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class User {

    @JsonProperty("firstName")
    private String firstName;

    @JsonProperty("lastName")
    private String lastName;

    @JsonProperty("")
    private String gender;

    @JsonProperty("")
    private int age;

    @JsonProperty("address")
    Address address;

    @JsonProperty("phoneNumbers")
    List<PhoneNumber> phoneNumber;


    // Getter Methods

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getGender() {
        return gender;
    }

    public int getAge() {
        return age;
    }

    public Address getAddress() {
        return address;
    }

    public List<PhoneNumber> getPhoneNumber() {
        return phoneNumber;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setAddress(Address addressObject) {
        this.address = addressObject;
    }

    public void setPhoneNumber(List<PhoneNumber> phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

}

class Address {

    @JsonProperty("streetAddress")
    private String streetAddress;

    @JsonProperty("city")
    private String city;

    @JsonProperty("state")
    private String state;

    public String getStreetAddress() {
        return streetAddress;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "Address{" +
                "streetAddress='" + streetAddress + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                '}';
    }

}

class PhoneNumber {

    @JsonProperty("type")
    private String type;

    @JsonProperty("number")
    private String number;


    public String getType() {
        return type;
    }

    public String getNumber() {
        return number;
    }

    public void setStreetAddress(String type) {
        this.type = type;
    }

    public void setCity(String number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return "phone{" +
                "type='" + type + '\'' +
                ", number='" + number + '\'' +
                '}';
    }

}