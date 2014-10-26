package com.property.db.hibernate;

import java.io.Serializable;
import java.util.Date;

import com.googlecode.genericdao.dao.hibernate.original.GenericDAO;
import com.googlecode.genericdao.dao.hibernate.original.GenericDAOImpl;
import com.property.db.entities.Entity;

public class FreeholdGenericDAOImpl<T, ID extends Serializable>
	extends GenericDAOImpl<T, ID>
	implements GenericDAO<T, ID>
{
	@Override
	public boolean deleteById(ID id) {
		T obj = this.fetch(id);
		return this.deleteEntity(obj);
	}

	@Override
	public boolean deleteEntity(T object) {
		if (!persistentClass.isInstance(object))
			throw new IllegalArgumentException("Object class does not match dao type.");
		
		if (object instanceof Entity)
		{
			Entity entity = (Entity)object;
			entity.setDeletedOn(new Date());
			entity.setDeleted(true);
			super.update(object);
			return true;
		}
		else
			return super.deleteEntity(object);
	}
	
	@Override
	public void update(T object) {
		if (!persistentClass.isInstance(object))
			throw new IllegalArgumentException("Object class does not match dao type.");
		
		if (object instanceof Entity)
		{
			Entity entity = (Entity)object;
			entity.setUpdatedOn(new Date());
			super.update(object);
		}
		else
			super.update(object);
	}
	
	
}
