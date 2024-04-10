package com.testproject.catalog.dtos;

import com.testproject.catalog.projections.UserNameProjection;

public class UserNameProjectionDTO {

    private String email;

    public UserNameProjectionDTO() {}

    public UserNameProjectionDTO(String email){
        this.email = email;
    }

    public UserNameProjectionDTO(UserNameProjection projection){
        email = projection.getEmail();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
