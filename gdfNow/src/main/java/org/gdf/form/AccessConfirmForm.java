package org.gdf.form;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class AccessConfirmForm {
	
	@NotNull
	@Size(min = 5, max = 45)
	@Pattern(regexp = ".+\\@.+\\..+")
	private String email;
	
	@NotBlank
	@Pattern(regexp = "^(?=.*\\d).{8,14}$")
	private String password;
	
	@NotBlank
	@Pattern(regexp = "^(?=.*\\d).{8,14}$")
	private String passwordConfirm;

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

	public String getPasswordConfirm() {
		return passwordConfirm;
	}

	public void setPasswordConfirm(String passwordConfirm) {
		this.passwordConfirm = passwordConfirm;
	}
	
	
	
	

}
