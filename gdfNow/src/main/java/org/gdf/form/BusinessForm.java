package org.gdf.form;

import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.springframework.web.multipart.MultipartFile;

public class BusinessForm {
	
	@NotBlank
	@Size(min=2, max=45)
	private String name;
	
	@NotBlank
	@Pattern(regexp = "^(.+)@(.+)$")
	private String email;
	
	@NotBlank
	@Size(min=2, max=450)
	private String description;
	
	@NotBlank
	@Pattern(regexp = "^((https?|ftp|smtp):\\/\\/)?(www.)?[a-z0-9]+\\.[a-z]+(\\/[a-zA-Z0-9#]+\\/?)*$")
	private String website;
	
	private String businessCategory;
	private String businessSubCategory;
	
	private List<String> catType=new ArrayList<String>();
	
	private List<String> catSubType=new ArrayList<String>();
	
	private MultipartFile profileImage;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public List<String> getCatType() {
		return catType;
	}

	//IMP: READ THIS
	//https://coderedirect.com/questions/579479/dynamic-dropdowns-using-thymeleaf-spring-boot
	//https://www.w3schools.com/jquery/jquery_ref_ajax.asp
	public void setCatType(List<String> catType) {
		this.catType = catType;
	}

	public List<String> getCatSubType() {
		return catSubType;
	}

	public void setCatSubType(List<String> catSubType) {
		this.catSubType = catSubType;
	}

	public MultipartFile getProfileImage() {
		return profileImage;
	}

	public void setProfileImage(MultipartFile profileImage) {
		this.profileImage = profileImage;
	}

	public String getBusinessCategory() {
		return businessCategory;
	}

	public void setBusinessCategory(String businessCategory) {
		this.businessCategory = businessCategory;
	}

	public String getBusinessSubCategory() {
		return businessSubCategory;
	}

	public void setBusinessSubCategory(String businessSubCategory) {
		this.businessSubCategory = businessSubCategory;
	}
	
	
	

}
