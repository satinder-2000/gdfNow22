package org.gdf.form;

import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.springframework.web.multipart.MultipartFile;

public class GovernmentForm {
	
	@NotBlank
	@Size(min=2, max=45)
	private String name;
	
	@Pattern(regexp = "^(.+)@(.+)$")
	private String email;
	
	@NotBlank
	@Size(min=2, max=450)
	private String description;
	
	@NotBlank
	@Pattern(regexp = "^((https?|ftp|smtp):\\/\\/)?(www.)?[a-z0-9]+\\.[a-z]+(\\/[a-zA-Z0-9#]+\\/?)*$")
	private String website;
	
	@NotBlank
	@Size(min=2, max=45)
	private String contactName;
	
	private String ministry;
	
	private String department;
	
	private List<String> ministries=new ArrayList<String>();
	
	private List<String> deartments=new ArrayList<String>();
	
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

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getMinistry() {
		return ministry;
	}

	public void setMinistry(String ministry) {
		this.ministry = ministry;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public List<String> getMinistries() {
		return ministries;
	}

	public void setMinistries(List<String> ministries) {
		this.ministries = ministries;
	}

	public List<String> getDeartments() {
		return deartments;
	}

	public void setDeartments(List<String> deartments) {
		this.deartments = deartments;
	}

	public MultipartFile getProfileImage() {
		return profileImage;
	}

	public void setProfileImage(MultipartFile profileImage) {
		this.profileImage = profileImage;
	}
	
	


}
