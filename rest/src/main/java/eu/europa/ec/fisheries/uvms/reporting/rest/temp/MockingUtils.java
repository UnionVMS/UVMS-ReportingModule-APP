package eu.europa.ec.fisheries.uvms.reporting.rest.temp;

import java.util.HashSet;
import java.util.Set;

import eu.europa.ec.fisheries.uvms.reporting.model.Context;
import eu.europa.ec.fisheries.uvms.reporting.model.Preference;
import eu.europa.ec.fisheries.uvms.reporting.model.Preferences;
import eu.europa.ec.fisheries.uvms.reporting.model.Role;
import eu.europa.ec.fisheries.uvms.reporting.model.Scope;

public class MockingUtils {
	
	public static Context createContext(String scopeName) {
		Context currentContext = new Context();
		
		Role fullReportsRole = new Role();
		Scope euScope = new Scope();
		euScope.setScopeName(scopeName);
		
		Preferences prefs = new Preferences();
		Preference pref = new Preference();
		pref.setApplicationName("reporting");
		pref.setOptionName("page_size");
		pref.setOptionValue("2");
		Set<Preference> prefSet = new HashSet<>();
		prefSet.add(pref);
		prefs.setPreferences(prefSet);

		currentContext.setRole(fullReportsRole);
		currentContext.setScope(euScope);
		currentContext.setPreferences(prefs);
		
		return currentContext;
	}

}
