package com.testproject.catalog.entities.enums;

public enum UserRole {

    ADMIN("admin"),
    EMPLOYEE("employee");

    private String role;

    UserRole(String role){
        this.role = role;
    }

    public String getRole(){
        return role;
    }

}
