package com.property.db.hibernate;

import java.util.Date;
import java.util.Set;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.EntityEntry;
import org.hibernate.event.internal.DefaultDeleteEventListener;
import org.hibernate.event.spi.DeleteEvent;
import org.hibernate.persister.entity.EntityPersister;

import com.property.db.entities.Entity;


//persistence.xml - <property name = "hibernate.ejb.event.delete" value = "org.something.SoftDeleteEventListener"/> 
public class SoftDeleteEventListener extends DefaultDeleteEventListener {

	private static final long serialVersionUID = 1L;
	
	@SuppressWarnings("rawtypes")
	@Override
	public void onDelete(DeleteEvent event, Set transientEntities) throws HibernateException {
	    Object dbEntity = event.getObject();
	    
	    if (dbEntity instanceof Entity) 
	    {
	    	((Entity)dbEntity).setDeleted(true);
	        ((Entity)dbEntity).setDeletedOn(new Date());

	        EntityPersister persister = event.getSession().getEntityPersister( event.getEntityName(), dbEntity);
	        EntityEntry entityEntry = event.getSession().getPersistenceContext().getEntry(dbEntity);
	        
	        cascadeBeforeDelete(event.getSession(), persister, dbEntity, entityEntry, transientEntities);
	        cascadeAfterDelete(event.getSession(), persister, dbEntity, transientEntities);
	    } else {
	        super.onDelete(event, transientEntities);
	    }
	}
}
