package com.agri.backend.Exceptions;

import org.springframework.http.HttpStatusCode;

public class UserException extends Exception{
	private static final long serialVersionUID = -9121980966131405437L;
	private String message;
	private HttpStatusCode status;
	
	public UserException(String message,HttpStatusCode status) {
		this.setMessage(message);
		this.setStatus(status);
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public HttpStatusCode getStatus() {
		return status;
	}

	public void setStatus(HttpStatusCode status) {
		this.status = status;
	}

	
}
