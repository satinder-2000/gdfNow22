/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gdf.model.like;

import org.gdf.model.BusinessOffer;
import org.gdf.model.EntityType;

import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author satindersingh
 */
@Entity
@Table(name = "BUSINESS_OFFER_LIKE")
public class BusinessOfferLike implements Serializable {
	
	   
    private static final long serialVersionUID = 1L;
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="ID")
	private int id;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BUSINESS_OFFER_ID")
    private BusinessOffer businessOffer;

    @Column(name = "TIME")
	private LocalDateTime time;
    
    @Column(name = "ENTITY_ID")
    private int entityId;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "ENTITY_TYPE")
    private EntityType entityType;
    
    @Column(name = "LIKE_BY_NAME")
    private String likeByName;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public BusinessOffer getBusinessOffer() {
		return businessOffer;
	}

	public void setBusinessOffer(BusinessOffer businessOffer) {
		this.businessOffer = businessOffer;
	}

	public LocalDateTime getTime() {
		return time;
	}

	public void setTime(LocalDateTime time) {
		this.time = time;
	}

	public int getEntityId() {
		return entityId;
	}

	public void setEntityId(int entityId) {
		this.entityId = entityId;
	}

	public EntityType getEntityType() {
		return entityType;
	}

	public void setEntityType(EntityType entityType) {
		this.entityType = entityType;
	}

	public String getLikeByName() {
		return likeByName;
	}

	public void setLikeByName(String likeByName) {
		this.likeByName = likeByName;
	}
    
    
    
    
    
    
    
}
