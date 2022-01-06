package org.gdf.form;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class GovernmentAddressForm {
	
	@NotBlank
	@Size(min=2, max=45)
	private String addressLine;
	
	@NotBlank
	@Size(min=2, max=45)
	private String city;
	
	@NotBlank
	@Size(min=2, max=8)
	private String postcode;
	
	@NotBlank
	@Size(min=2, max=45)
	private String state;
	
	@NotBlank
	@Pattern(regexp = "\\\\d{11}")
	private String phone;
	
	@NotBlank
	@Size(min = 2, max = 2)
	private String countryCode;

	public String getAddressLine() {
		return addressLine;
	}

	public void setAddressLine(String addressLine) {
		this.addressLine = addressLine;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getPostcode() {
		return postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getPhone() {
		return phone;
	}
	

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
	

}
