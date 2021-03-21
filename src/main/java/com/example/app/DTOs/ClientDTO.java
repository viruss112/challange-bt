package com.example.app.DTOs;

import lombok.Data;

import java.util.Date;

/**
 * This class represents the client's information
 */

@Data
public class ClientDTO {

    private String address;
    private String firstName;
    private String lastName;
    private String number;
    private String personalIdentityCode;
    private String placeOfBirth;
    private String series;
    private Date validFrom;
    private Date validTo;

}
