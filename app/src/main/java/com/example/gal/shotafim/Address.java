package com.example.gal.shotafim;

public class Address {
    private String country;
    private String city;
    private String apartment;
    private String street;

    public Address(String country, String city, String apartment, String street) {
        this.country = country;
        this.city = city;
        this.apartment = apartment;
        this.street = street;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getApartment() {
        return apartment;
    }

    public void setApartment(String apartment) {
        this.apartment = apartment;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }
}
