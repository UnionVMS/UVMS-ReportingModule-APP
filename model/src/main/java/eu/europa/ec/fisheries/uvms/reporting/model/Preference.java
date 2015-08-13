package eu.europa.ec.fisheries.uvms.reporting.model;

import java.io.Serializable;

/**
 * Holds a preference
 */
public class Preference implements Serializable {
	private static final long serialVersionUID = 1L;
  private String applicationName;
  private String optionName;
  private String optionValue;

  /**
   * Creates a new instance.
   */
  public Preference() {
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
   * Get the value of optionName
   *
   * @return the value of optionName
   */
  public String getOptionName() {
    return optionName;
  }

  /**
   * Set the value of optionName
   *
   * @param optionName new value of optionName
   */
  public void setOptionName(String optionName) {
    this.optionName = optionName;
  }


  /**
   * Get the value of optionValue
   *
   * @return the value of optionValue
   */
  public String getOptionValue() {
    return optionValue;
  }

  /**
   * Set the value of optionValue
   *
   * @param optionValue new value of optionValue
   */
  public void setOptionValue(String optionValue) {
    this.optionValue = optionValue;
  }

  /**
   * Formats a human-readable view of this instance.
   * 
   * @return a human-readable view
   */
  @Override
  public String toString() {
    return "Preference{" + 
            "applicationName=" + applicationName + 
            ", optionName=" + optionName + 
            ", optionValue=" + optionValue + 
            '}';
  }

  
}
