package eu.europa.ec.fisheries.uvms.reporting.rest.filter;


import eu.europa.ec.fisheries.uvms.reporting.model.ReportFeatureEnum;
import eu.europa.ec.fisheries.uvms.rest.security.UserRoleRequestWrapper;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by georgige on 10/8/2015.
 */
public class TempSecurityFilter extends CrossOriginFilter{



        @Override
        public void init(FilterConfig filterConfig) throws ServletException {
        }


        @Override
        public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
            Set<String> allFeatures = new HashSet<>(ReportFeatureEnum.values().length);
            for(ReportFeatureEnum feature: ReportFeatureEnum.values()) {
                allFeatures.add(feature.toString());
            }

            UserRoleRequestWrapper wrapper = new UserRoleRequestWrapper("Hugo", allFeatures, (HttpServletRequest) request);

            super.doFilter(wrapper, response, chain);
        }



        @Override
        public void destroy() {

        }
    }
