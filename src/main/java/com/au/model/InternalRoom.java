package com.au.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "internal_room_assignment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InternalRoom {


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer internal_room_id;
	private Integer internal_session_id;
	
	@ManyToOne
	@JoinColumn(name = "room_id")
	private InfrastructureRooms rooms;

	
	 	@Column(name = "created_date", updatable = false)
		@Temporal(TemporalType.TIMESTAMP)
		@CreationTimestamp
		private Date created_date;

		@Column(name = "modified_date")
		@Temporal(TemporalType.TIMESTAMP)
		@UpdateTimestamp
		private Date modified_date;

		@Column(name = "created_by", updatable = false)
		private Integer created_by;
		private Integer modified_by;
		private Boolean active;
		@Column(name = "created_username", updatable = false)
		private String created_username;
		private String modified_username;
		
}
