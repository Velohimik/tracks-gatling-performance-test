package com.tracks.models;

import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Builder(toBuilder = true)
public class User {

    private Integer id;
    private String login;
    private String cryptedPass;
    private String token;
    private Byte isAdmin;
    private String firstName;
    private String lastName;
    private String authType;
    private String openIdUrl;
    private String rememberToken;
    private Timestamp rememberTokenExpiresAt;
    private String email;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private Timestamp lastLoginAt;
}
