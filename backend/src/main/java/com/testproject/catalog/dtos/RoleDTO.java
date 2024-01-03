package com.testproject.catalog.dtos;

import com.testproject.catalog.entities.Role;

import java.io.Serializable;

public class RoleDTO implements Serializable {

    private Long id;
    private String authority;

    private RoleDTO(){}

    public RoleDTO(Long id, String authority) {
        this.id = id;
        this.authority = authority;
    }

    public RoleDTO(Role entity) {
        id = entity.getId();
        authority = entity.getAuthority();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }
}
