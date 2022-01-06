/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gdf.model.comment;

import org.gdf.model.Deed;
import org.gdf.model.EntityType;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 *
 * @author satindersingh
 */
@Entity
@Table(name = "DEED_COMMENT")
public class DeedComment implements Serializable {

    private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    private String text;
    
    private LocalDateTime date;
    
    @Column(name = "ENTITY_TYPE")
    private EntityType entityType;
    
    @Column(name = "ACCESS_ID")
    private int accessId;
    
    @Column(name = "POSTED_BY")
    private String PostedBy;
    
    @ManyToOne
    @JoinColumn(name = "DEED_ID")
    private Deed deed;
    
    @Transient
    private String dateStr;
    
    private int likes;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
        

    public EntityType getEntityType() {
		return entityType;
	}

	public void setEntityType(EntityType entityType) {
		this.entityType = entityType;
	}

	public int getAccessId() {
        return accessId;
    }

    public void setAccessId(int accessId) {
        this.accessId = accessId;
    }

    
    
    

    public String getPostedBy() {
        return PostedBy;
    }

    public void setPostedBy(String PostedBy) {
        this.PostedBy = PostedBy;
    }
    
    public Deed getDeed() {
        return deed;
    }

    public void setDeed(Deed deed) {
        this.deed = deed;
    }

    public String getDateStr() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        dateStr=date.format(formatter);
        return dateStr;
    }

    public void setDateStr(String dateStr) {
        this.dateStr = dateStr;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }
    
    
    
    
}
