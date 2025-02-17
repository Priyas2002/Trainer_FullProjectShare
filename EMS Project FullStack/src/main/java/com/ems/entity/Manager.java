package com.ems.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "manager")
public class Manager {

	@Id
	@Column(name = "empid")
	private Long empid; 

	@Column(nullable = false)
	private String firstName;

	@Column(nullable = false)
	private String lastName;

	@Column(nullable = false, unique = true)
	private String email;

	@Column(nullable = false)
	private String password; 

	public Manager() {
	}

	public Manager(Long empid, String firstName, String lastName, String email, String password) {
		this.empid = empid;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
	}

	// Getters and Setters
	public Long getEmpid() {
		return empid;
	}

	public void setEmpid(Long empid) {
		this.empid = empid;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}