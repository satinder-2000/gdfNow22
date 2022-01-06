package org.gdf.form;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class LoginForm {
	
	@NotNull
	@Size(min = 5, max = 45)
	@Pattern(regexp = ".+\\@.+\\..+")
	private String email;
	
	@NotBlank
	@Pattern(regexp = "^(?=.*\\d).{8,14}$")
	private String password;

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
