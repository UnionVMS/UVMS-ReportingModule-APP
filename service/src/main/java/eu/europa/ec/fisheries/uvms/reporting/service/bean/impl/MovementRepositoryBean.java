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

package eu.europa.ec.fisheries.uvms.reporting.service.bean.impl;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import eu.europa.ec.fisheries.uvms.reporting.service.bean.MovementRepository;
import eu.europa.ec.fisheries.uvms.reporting.service.dao.MovementDao;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Area;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Movement;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Segment;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Track;
import eu.europa.ec.fisheries.uvms.reporting.service.exception.ApplicationException;
import lombok.extern.slf4j.Slf4j;

@ApplicationScoped
@Slf4j
public class MovementRepositoryBean implements MovementRepository {

    @Inject
    private MovementDao movementDao;

    @Override
    public Movement createMovementEntity(Movement entity) {
        return movementDao.createEntity(entity);
    }

    @Override
    public Segment createSegmentEntity(Segment entity) {
        return movementDao.createEntity(entity);
    }

    @Override
    public Track createTrackEntity(Track entity) {
        return movementDao.createEntity(entity);
    }

    @Override
    public Movement findMovementById(Long id) {
        return movementDao.findById(id, Movement.class);
    }

    @Override
    public Movement updateMovement(Movement entity) {
        return movementDao.update(entity);
    }

    @Override
    public void deleteMovement(Long id) throws ApplicationException {
        movementDao.delete(id, Movement.class);
    }

    @Override
    public Area createArea(Area entity) {
        return movementDao.createEntity(entity);
    }

    @Override
    public Area findAreaByTypeAndAreaCode(String areaType, String areaCode) {
        return movementDao.findAreaByTypeAndAreaCode(areaType, areaCode);
    }
}
