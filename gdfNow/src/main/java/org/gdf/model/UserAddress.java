/**
 *
 */
package org.gdf.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * @author satindersingh
 *
 */
@Entity
@Table(name = "USER_ADDRESS", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"id"})})
public class UserAddress implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "ADDRESS_LINE")
    private String addressLine;

    @Column(name = "POSTCODE")
    private String postCode;

    @Column(name = "CITY")
    private String city;

    @Column(name = "STATE")
    private String state;

    @OneToOne(targetEntity = Country.class, cascade = CascadeType.ALL)
    private Country country;

    @Column(name = "CREATED_ON")
    private LocalDateTime createdOn;

    @Column(name = "UPDATED_ON")
    private LocalDateTime updatedOn;

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
		this.id = id;
	}
    
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

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }

    public LocalDateTime getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(LocalDateTime updatedOn) {
        this.updatedOn = updatedOn;
    }
	@Override
	public String toString() {
		return "UserAddress [id=" + id + ", addressLine=" + addressLine + ", postCode=" + postCode + ", city=" + city
				+ ", state=" + state + ", country=" + country + "]";
	}

    

}
