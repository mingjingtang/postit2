package com.example.usersapi.model.wrapper;

import com.example.usersapi.model.User;
import com.example.usersapi.model.UserProfile;

public class UserProfileWithUser {

  private Long id;

  private String mobile;

  private String address;

  private String additionalEmail;

  private User user;

  public UserProfileWithUser(UserProfile userProfile, User user){
    this.id = userProfile.getId();
    this.mobile = userProfile.getMobile();
    this.address = userProfile.getAddress();
    this.additionalEmail = userProfile.getAdditionalEmail();
    this.user = user;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getMobile() {
    return mobile;
  }

  public void setMobile(String mobile) {
    this.mobile = mobile;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getAdditionalEmail() {
    return additionalEmail;
  }

  public void setAdditionalEmail(String additionalEmail) {
    this.additionalEmail = additionalEmail;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }
}
