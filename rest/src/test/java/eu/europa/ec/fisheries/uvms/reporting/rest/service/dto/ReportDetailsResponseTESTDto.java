package eu.europa.ec.fisheries.uvms.reporting.rest.service.dto;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import eu.europa.ec.fisheries.uvms.reporting.rest.dto.ReportDetailsDTO;

@JsonInclude(Include.NON_NULL)
public class ReportDetailsResponseTESTDto {

    private ReportDetailsDTO data;
    private int code;
    private String msg;

    public ReportDetailsResponseTESTDto(ReportDetailsDTO data, int code) {
        this.data = data;
        this.code = code;
    }

    public ReportDetailsResponseTESTDto(int code) {
        this.code = code;
    }

    public ReportDetailsResponseTESTDto(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
    
    public ReportDetailsResponseTESTDto(ReportDetailsDTO data, int code, String msg) {
    	this.data = data;
        this.code = code;
        this.msg = msg;
    }
    
    public ReportDetailsResponseTESTDto() {
    }
    
    
    public ReportDetailsDTO getData() {
        return data;
    }

    public int getCode() {
        return code;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 23 * hash + Objects.hashCode(this.data);
        hash = 23 * hash + Objects.hashCode(this.code);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ReportDetailsResponseTESTDto other = (ReportDetailsResponseTESTDto) obj;
        if (!Objects.equals(this.data, other.data)) {
            return false;
        }
        if (!Objects.equals(this.code, other.code)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ResponseDto{" + "data=" + data + ", code=" + code + '}';
    }

	/**
	 * @return the msg
	 */
	public String getMsg() {
		return msg;
	}

	/**
	 * @param msg the msg to set
	 */
	public void setMsg(String msg) {
		this.msg = msg;
	}

}
