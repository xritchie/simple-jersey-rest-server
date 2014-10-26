package com.property.db.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.Where;

//No instance of this class is created but is inputed into sub classes
@MappedSuperclass

//Filter added to retrieve only records that have not been soft deleted.
//@SQLDelete(sql = "UPDATE TABLE SET deleted = true, deleted_on = NOW() WHERE id = ?")
@Where(clause="deleted_on IS NULL")
public abstract class Entity {

	@Column(name = "deleted", nullable = false)
	@Type(type = "numeric_boolean")
	private boolean deleted;  
	
	@Column(name = "deleted_on", nullable = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date deletedOn = null;
	
	@Column(name = "updated_on", nullable = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date updatedOn = null;
	
	@Column(updatable = false, name = "created_on", nullable = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdOn = null;

	public Entity() {
		super();
		this.deleted = false;
		this.createdOn = new Date();
	}

	public Entity(boolean deleted, Date deletedOn, Date updatedOn, Date createdOn) {
		super();
		this.deleted = deleted;
		this.deletedOn = deletedOn;
		this.updatedOn = updatedOn;
		this.createdOn = createdOn;
	}
	
	public boolean isDeleted() {
		return deleted;
	}
	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
	
	public Date getDeletedOn() {
		return deletedOn;
	}
	public void setDeletedOn(Date deletedOn) {
		this.deletedOn = deletedOn;
	}
	
	public Date getUpdatedOn() {
		return updatedOn;
	}
	public void setUpdatedOn(Date updatedOn) {
		this.updatedOn = updatedOn;
	}
	
	public Date getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

}
