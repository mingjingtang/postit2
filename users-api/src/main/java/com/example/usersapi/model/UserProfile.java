package com.example.usersapi.model;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "profile")
public class UserProfile {

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "mobile")
  @NotBlank(message = "invalid mobile")
  private String mobile;

  @Column(name = "address")
  @NotBlank(message = "invalid address")
  private String address;

  @Column(name = "email")
  @NotBlank(message = "invalid email")
  @Email(message = "invalid email")
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
