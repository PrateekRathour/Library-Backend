package com.library.dto;

import com.library.entity.Role;
import com.library.entity.User;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class MemberResponse {
    private Long id;
    private String username;
    private String email;
    private String fullName;
    private String phone;
    private String address;
    private Role role;
    private String status;
    private LocalDate membershipDate;
    private LocalDate membershipExpiryDate;
    private LocalDateTime createdAt;
    private Long activeBorrowsCount = 0L;
    private Double pendingFinesAmount = 0.0;

    public MemberResponse() {}

    public static MemberResponse fromEntity(User user) {
        MemberResponse resp = new MemberResponse();
        resp.setId(user.getId());
        resp.setUsername(user.getUsername());
        resp.setEmail(user.getEmail());
        resp.setFullName(user.getFullName());
        resp.setPhone(user.getPhone());
        resp.setAddress(user.getAddress());
        resp.setRole(user.getRole());
        resp.setStatus(user.getStatus());
        resp.setMembershipDate(user.getMembershipDate());
        resp.setMembershipExpiryDate(user.getMembershipExpiryDate());
        resp.setCreatedAt(user.getCreatedAt());
        return resp;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getMembershipDate() {
        return membershipDate;
    }

    public void setMembershipDate(LocalDate membershipDate) {
        this.membershipDate = membershipDate;
    }

    public LocalDate getMembershipExpiryDate() {
        return membershipExpiryDate;
    }

    public void setMembershipExpiryDate(LocalDate membershipExpiryDate) {
        this.membershipExpiryDate = membershipExpiryDate;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Long getActiveBorrowsCount() {
        return activeBorrowsCount;
    }

    public void setActiveBorrowsCount(Long activeBorrowsCount) {
        this.activeBorrowsCount = activeBorrowsCount;
    }

    public Double getPendingFinesAmount() {
        return pendingFinesAmount;
    }

    public void setPendingFinesAmount(Double pendingFinesAmount) {
        this.pendingFinesAmount = pendingFinesAmount;
    }
}
