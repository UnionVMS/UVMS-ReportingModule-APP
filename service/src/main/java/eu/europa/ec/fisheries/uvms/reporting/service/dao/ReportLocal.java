package eu.europa.ec.fisheries.uvms.reporting.service.dao;


import java.util.Collection;

import javax.ejb.Local;

import eu.europa.ec.fisheries.uvms.reporting.service.entities.Report;

@Local
public interface ReportLocal {

	Report create();
	
	Report findById(long id);
	
	void update();
	
	void delete();
	
	Collection<Report> findAvailable(int userID, int scopeID);
	
}
