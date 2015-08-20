package eu.europa.ec.fisheries.uvms.reporting.rest.temp;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;


import eu.europa.ec.fisheries.uvms.reporting.model.Context;
import eu.europa.ec.fisheries.uvms.reporting.model.Feature;
import eu.europa.ec.fisheries.uvms.reporting.model.Preference;
import eu.europa.ec.fisheries.uvms.reporting.model.Preferences;
import eu.europa.ec.fisheries.uvms.reporting.model.Role;
import eu.europa.ec.fisheries.uvms.reporting.model.Scope;
import eu.europa.ec.fisheries.uvms.reporting.model.UserContext;

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

	public static Context createContext(String scopeName, String ...userFeatures) {
		Context context = createContext(scopeName);

		context.getRole().setFeatures(new HashSet(Arrays.asList(userFeatures)));
		return context;
	}

    public static int randInt(int min, int max) {

        // NOTE: Usually this should be a field rather than a method
        // variable so that it is not re-seeded every call.
        Random rand = new Random();

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }
}
