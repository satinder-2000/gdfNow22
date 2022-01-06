/**
 * 
 */
package org.gdf.model;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

/**
 * @author satindersingh
 *
 */
@Entity(name="BUSINESS_ADDRESS")
public class BusinessAddress implements Serializable {
	
	private static final long serialVersionUID = 6893399628918125552L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	private String addressLine;
	
	private String city;
	
	private String postcode;
	
	private String state;
	
	private String phone;
	
	
	@OneToOne(targetEntity = Country.class, cascade = CascadeType.DETACH)
	private Country country;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	
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

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	@Override
    public String toString() {
        return "{" + "\"id\":"+ id +","+
        		 "\"addressLine\":\""+ addressLine +"\","+
        		  "\"postcode\":\""+ postcode +"\","+
        		 "\"city\":\""+ city +"\","+
        		 "\"state\":\""+ state +"\","+
        		 "\"country\":\""+ country.getName()+"\"}";
    }
	
	

}
