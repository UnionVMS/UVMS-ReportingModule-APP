package eu.europa.ec.fisheries.uvms.reporting.model;

import java.io.Serializable;
import java.util.List;

/**
 * User profile (or preferences) for a specific application.
 */
public class UserProfile implements Serializable {
	private static final long serialVersionUID = 1L;
	private String userName;
	private String applicationName;
	private List<UserPreference> preferences;

  /**
   * Creates a new instance.
   */
  public UserProfile() {
  }

  
  /**
   * Get the value of userName
   *
   * @return the value of userName
   */
  public String getUserName() {
    return userName;
  }

  /**
   * Set the value of userName
   *
   * @param userName new value of userName
   */
  public void setUserName(String userName) {
    this.userName = userName;
  }


  /**
   * Get the value of applicationName
   *
   * @return the value of applicationName
   */
  public String getApplicationName() {
    return applicationName;
  }

  /**
   * Set the value of applicationName
   *
   * @param applicationName new value of applicationName
   */
  public void setApplicationName(String applicationName) {
    this.applicationName = applicationName;
  }


  /**
   * Get the value of preferences
   *
   * @return the value of preferences
   */
  public List<UserPreference> getPreferences() {
    return preferences;
  }

  /**
   * Set the value of preferences
   *
   * @param preferences new value of preferences
   */
  public void setPreferences(List<UserPreference> preferences) {
    this.preferences = preferences;
  }

  /**
   * Formats a human-readable view of this instance.
   * 
   * @return a human-readable view
   */
  @Override
  public String toString() 
  {
    return "UserProfile{" + 
            "userName=" + userName + 
            ", applicationName=" + applicationName + 
            ", preferences=" + preferences + 
            '}';
  }
}
