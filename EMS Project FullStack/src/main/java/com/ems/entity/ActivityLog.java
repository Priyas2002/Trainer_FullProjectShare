package com.ems.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "activity_log")
public class ActivityLog {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String description;
	private LocalDateTime timestamp;

	public ActivityLog() {
		this.timestamp = LocalDateTime.now();
	}

	public ActivityLog(String description) {
		this.description = description;
		this.timestamp = LocalDateTime.now();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}
}
