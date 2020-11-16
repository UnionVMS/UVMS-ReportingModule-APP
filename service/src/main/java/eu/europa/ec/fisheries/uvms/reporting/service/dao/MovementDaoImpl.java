/*
 *
 *  Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2020.
 *
 *  This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
 *  and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
 *  the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 *  without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 *  details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 *
 */

/*
 *
 *  Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2020.
 *
 *  This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
 *  and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
 *  the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 *  without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 *  details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 *
 */

/*
 Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

 This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
 and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
 the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 */

package eu.europa.ec.fisheries.uvms.reporting.service.dao;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import eu.europa.ec.fisheries.uvms.reporting.service.entities.Area;
import eu.europa.ec.fisheries.uvms.reporting.service.exception.ApplicationException;
import eu.europa.ec.fisheries.uvms.reporting.service.exception.EntityDoesNotExistException;
import lombok.extern.slf4j.Slf4j;

/**
 * {@link MovementDao} JPA implementation.
 */
@ApplicationScoped
@Slf4j
class MovementDaoImpl implements MovementDao {

    @PersistenceContext(unitName = "reportingPUposgres")
    private EntityManager em;

    public MovementDaoImpl(EntityManager em) {
        this.em = em;
    }

    public MovementDaoImpl() {
    }

    @Override
    public <T> T createEntity(T entity) {
        em.persist(entity);
        return entity;
    }

    @Override
    public <T> T findById(Long id, Class<T> clazz) {
        return em.find(clazz, id);
    }

    @Override
    public <T> T update(T entity) {
        return em.merge(entity);
    }

    @Override
    public <T> void delete(Long id, Class<T> clazz) throws ApplicationException {
        T en = em.find(clazz, id);
        if (en == null) {
            throw new EntityDoesNotExistException(clazz + " with id " + id);
        }
        em.remove(en);
    }

    @Override
    public Area findAreaByTypeAndAreaCode(String areaType, String areaCode) {
        Query nativeQuery = em.createNativeQuery("select * from reporting.areas where area_type = :areaType and area_code = :areaCode", Area.class);
        nativeQuery.setParameter("areaType", areaType);
        nativeQuery.setParameter("areaCode", areaCode);
        try {
            return (Area) nativeQuery.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
