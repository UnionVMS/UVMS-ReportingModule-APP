/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */


package eu.europa.ec.fisheries.uvms.reporting.rest.filter;

import eu.europa.ec.fisheries.uvms.reporting.dto.report.ReportFeatureEnum;
import eu.europa.ec.fisheries.uvms.reporting.rest.utils.CrossOriginFilter;
import eu.europa.ec.fisheries.uvms.rest.security.UserRoleRequestWrapper;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class TempSecurityFilter extends CrossOriginFilter {

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

            ((HttpServletRequest) request).getSession().setAttribute("servletContextUserFeatures", allFeatures);
            ((HttpServletRequest) request).getSession().setAttribute("roleName", "rep_power_role");
            ((HttpServletRequest) request).getSession().setAttribute("scopeName", "EC");
            super.doFilter(wrapper, response, chain);
        }

        @Override
        public void destroy() {

        }
    }