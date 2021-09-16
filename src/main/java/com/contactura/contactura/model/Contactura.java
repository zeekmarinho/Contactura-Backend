package com.contactura.contactura.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Contactura {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) //GEREÇÃO DE Pk AUTOMÁTICA
	private Long id;
	
	private String name;
	private String email;
	private String phone;
	

}
