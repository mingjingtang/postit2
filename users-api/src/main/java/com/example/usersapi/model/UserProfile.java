package com.example.usersapi.model;

import javax.persistence.*;

@Entity
@Table(name = "profile")
public class UserProfile {

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "mobile")
  private String mobile;

  @Column(name = "address")
  private String address;

  @Column(name = "email")
  private String additionalEmail;

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
}
