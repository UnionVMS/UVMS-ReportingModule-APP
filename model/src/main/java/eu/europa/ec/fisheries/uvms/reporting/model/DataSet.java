package eu.europa.ec.fisheries.uvms.reporting.model;

import java.io.Serializable;

/**
 * Holds a Filter/dataset<br/>.
 *  
 * Definition: a filter is a value for a given criteria: Ex: criteria for the 
 * vessel application: vessel_group, filter: Group A 
 * It contains the criteria (or type of filter) (ex: vessel_group) is on which 
 * the filter should be applied
 * it is attached to the application in which it has been defined;
 * it is defined through the user interface of an application;
 * Datasets can also be defined in the configuration file of that application 
 * and  delivered to USM at deployment time;
 * A filter is not managed (CRUD) by USM but by the application exposing it;
 */
public class DataSet implements Serializable {
	private static final long serialVersionUID = 1L;
  private String applicationName;
  private String dataSetname;
  private String dataCategory;
  private String dataDiscriminator;

  /**
   * Creates a new instance
   */
  public DataSet() {
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
   * Get the value of dataSetname
   *
   * @return the value of dataSetname
   */
  public String getDataSetname() {
    return dataSetname;
  }

  /**
   * Set the value of dataSetname
   *
   * @param dataSetname new value of dataSetname
   */
  public void setDataSetname(String dataSetname) {
    this.dataSetname = dataSetname;
  }

  /**
   * Get the value of dataCategory
   *
   * @return the value of dataCategory
   */
  public String getDataCategory() {
    return dataCategory;
  }

  /**
   * Set the value of dataCategory
   *
   * @param dataCategory new value of dataCategory
   */
  public void setDataCategory(String dataCategory) {
    this.dataCategory = dataCategory;
  }

  /**
   * Get the value of dataDiscriminator
   *
   * @return the value of dataDiscriminator
   */
  public String getDataDiscriminator() {
    return dataDiscriminator;
  }

  /**
   * Set the value of dataDiscriminator
   *
   * @param dataDiscriminator new value of dataDiscriminator
   */
  public void setDataDiscriminator(String dataDiscriminator) {
    this.dataDiscriminator = dataDiscriminator;
  }

  /**
   * Formats a human-readable view of this instance.
   * 
   * @return a human-readable view
   */
  @Override
  public String toString() {
    return "DataSet{" + 
            "applicationName=" + applicationName + 
            ", dataSetname=" + dataSetname + 
            ", dataCategory=" + dataCategory + 
            ", dataDiscriminator=" + dataDiscriminator + 
            '}';
  }

}
