package org.gdf.form;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class UserAddressForm {
	
	@NotNull
	@Size(min=2, max=45)
	private String addressLine;
	@NotBlank
	@Size(min=6, max=8)
	private String postCode;
	@NotBlank
	@Size(min=2, max=45)
	private String city;
	@NotBlank
	@Size(min=2, max=45)
	private String state;
	@NotBlank
	@Size(min = 2, max = 2)
	private String countryCode;
	
	
	public String getAddressLine() {
		return addressLine;
	}
	public void setAddressLine(String addressLine) {
		this.addressLine = addressLine;
	}
	public String getPostCode() {
		return postCode;
	}
	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getCountryCode() {
		return countryCode;
	}
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
	
	
	

}
