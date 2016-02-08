package eu.europa.ec.fisheries.uvms.reporting.service.interceptor;


import eu.europa.ec.fisheries.uvms.reporting.model.VisibilityEnum;
import eu.europa.ec.fisheries.uvms.reporting.service.bean.ReportServiceBean;
import eu.europa.ec.fisheries.uvms.reporting.service.bean.VmsServiceBean;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.MapConfigurationDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.dto.ReportDTO;
import lombok.SneakyThrows;
import org.junit.Test;

public class AuditInterceptorTest {

    private ReportServiceBean reportServiceBean = new ReportServiceBean();
    private VmsServiceBean vmsServiceBean = new VmsServiceBean();

    @Test
    @SneakyThrows
    public void parametersShouldNotChange(){
        // strict parameters used by audit interceptor
        reportServiceBean.getClass().getMethod("delete", Long.class, String.class, String.class);
        reportServiceBean.getClass().getMethod("create", ReportDTO.class);
        reportServiceBean.getClass().getMethod("update", ReportDTO.class, Boolean.class, MapConfigurationDTO.class);
        reportServiceBean.getClass().getMethod("share", Long.class, VisibilityEnum.class, String.class, String.class);
        vmsServiceBean.getClass().getMethod("getVmsDataByReportId", String.class, String.class, Long.class);
    }
}
