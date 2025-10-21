package com.mvc.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class ContactModel {
	   private String name;
	    private String email;
	    private String subject;
	    private String message;

}
