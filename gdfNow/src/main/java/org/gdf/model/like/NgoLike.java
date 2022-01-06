/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gdf.model.like;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import org.gdf.model.Ngo;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.gdf.model.EntityType;

/**
 *
 * @author root
 */
@Entity
@Table(name = "NGO_LIKE")
public class NgoLike implements Serializable{
    
    private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected int id;
    
    @ManyToOne
    @JoinColumn(name = "NGO_ID")
    private Ngo ngo;
    
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
    
    public Ngo getNgo() {
        return ngo;
    }

    public void setNgo(Ngo ngo) {
        this.ngo = ngo;
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
