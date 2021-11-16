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

import eu.europa.ec.fisheries.uvms.reporting.service.entities.Asset;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
public class AssetDaoImpl implements AssetDao {

    @PersistenceContext(unitName = "reporting")
    private EntityManager em;

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
    public Asset findAssetByAssetHistoryGuid(String guid) {
        Query nativeQuery = em.createNativeQuery("SELECT * FROM reporting.asset WHERE asset_hist_guid = :assetHistoryGuid", Asset.class);
        nativeQuery.setParameter("assetHistoryGuid", guid);
        try {
            return (Asset) nativeQuery.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public int updateHistoryRecordsAsInactiveForAssetGuid(String guid) {
        Query nativeQuery = em.createNativeQuery("UPDATE reporting.asset SET asset_hist_active = false WHERE asset_guid = :assetGuid");
        nativeQuery.setParameter("assetGuid", guid);
        try {
            return nativeQuery.executeUpdate();
        } catch (NoResultException e) {
            return 0;
        }
    }

    @Override
    public int makeOtherHistoryEntriesOfAssetInactiveExceptCurrentHistId(String guid, String eventId) {
        Query nativeQuery = em.createNativeQuery("UPDATE reporting.asset SET asset_hist_active = false WHERE asset_guid = :assetGuid AND asset_hist_guid <> :assetHistGuid");
        nativeQuery.setParameter("assetGuid", guid);
        nativeQuery.setParameter("assetHistGuid", eventId);
        try {
            return nativeQuery.executeUpdate();
        } catch (NoResultException e) {
            return 0;
        }    }
}