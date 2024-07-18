package com.example.ApiGen.entity;

import jakarta.persistence.*;

import java.util.Set;

@Entity
public class RoleRes {


    @Id
    private Long roleId;

    private String roleName;

    public Set<String> getAccess() {
        return access;
    }

    public void setAccess(Set<String> access) {
        this.access = access;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    @ElementCollection
    @CollectionTable(name = "role_res_access", joinColumns = @JoinColumn(name = "role_res_role_id"))
    @Column(name = "access")
    private Set<String> access;

    // Getters and Setters
}
