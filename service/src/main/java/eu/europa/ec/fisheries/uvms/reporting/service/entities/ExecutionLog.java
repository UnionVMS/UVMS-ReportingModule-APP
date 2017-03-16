/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package eu.europa.ec.fisheries.uvms.reporting.service.entities;

import eu.europa.ec.fisheries.uvms.domain.BaseEntity;
import java.util.Date;
import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.ToString;

@Entity
@Table(name = "execution_log", uniqueConstraints = @UniqueConstraint(columnNames = {"report_id", "executed_by"}))
@ToString(exclude = "report")
public class ExecutionLog extends BaseEntity {

	@Id
	@Column(name = "id")
	@SequenceGenerator(name="execution_log_seq", sequenceName="execution_log_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="execution_log_seq")
	private Long id;

    @Valid
    @JoinColumn(name = "report_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Report report;

    @NotNull
    @Column(name = "executed_by")
    private String executedBy;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "executed_on")
    private Date executedOn;

    public ExecutionLog() {
    }

    @Builder
    public ExecutionLog(Report report, String executedBy) {
        this.report = report;
        this.executedBy = executedBy;
        this.executedOn = new Date();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Report getReport() {
        return this.report;
    }

    public void setReport(Report report) {
        this.report = report;
    }

    public String getExecutedBy() {
        return this.executedBy;
    }

    public void setExecutedBy(String executedBy) {
        this.executedBy = executedBy;
    }

    public Date getExecutedOn() {
        return this.executedOn;
    }

    public void setExecutedOn(Date executedOn) {
        this.executedOn = executedOn;
    }

}