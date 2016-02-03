package eu.europa.ec.fisheries.uvms.reporting.service.mapper;

import eu.europa.ec.fisheries.uvms.reporting.service.dto.AuditDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.Audit;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(uses = ObjectFactory.class)
public interface AuditMapper {

    AuditMapper INSTANCE = Mappers.getMapper(AuditMapper.class);

    AuditDTO auditToAuditDTO(Audit audit);

}
