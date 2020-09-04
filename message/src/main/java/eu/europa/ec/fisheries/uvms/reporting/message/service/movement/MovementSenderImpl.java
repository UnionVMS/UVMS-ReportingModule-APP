/*
 Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2020.

 This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
 and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
 the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 */
package eu.europa.ec.fisheries.uvms.reporting.message.service.movement;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;

import eu.europa.ec.fisheries.schema.movement.v1.SegmentAndTrackList;
import eu.europa.ec.fisheries.schema.movement.v1.SegmentIds;

/**
 * Implementation of {@link MovementSender}
 */
@ApplicationScoped
public class MovementSenderImpl implements MovementSender {

    private MovementClient movementClient;

    /**
     * Injection constructor.
     *
     * @param movementClient The asset client
     */
    @Inject
    public MovementSenderImpl(MovementClient movementClient) {
        this.movementClient = movementClient;
    }

    /**
     * Constructor for frameworks.
     */
    @SuppressWarnings("unused")
    MovementSenderImpl() {
        // NOOP
    }

    @Override
    public List<SegmentAndTrackList> getSegmentsAndTrackBySegmentIds(List<SegmentIds> segmentIds) {
        return movementClient.getSegmentsAndTrackBySegmentIds(segmentIds);
    }
}
