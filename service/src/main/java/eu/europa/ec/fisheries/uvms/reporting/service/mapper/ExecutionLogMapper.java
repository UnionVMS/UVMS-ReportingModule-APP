package eu.europa.ec.fisheries.uvms.reporting.service.mapper;

import eu.europa.ec.fisheries.uvms.reporting.service.dto.ExecutionLogDTO;
import eu.europa.ec.fisheries.uvms.reporting.service.entities.ExecutionLog;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ExecutionLogMapper {

    ExecutionLogMapper INSTANCE = Mappers.getMapper(ExecutionLogMapper.class);

    ExecutionLogDTO executionLogFilterToExecutionLogFilterDTO(ExecutionLog executionLog);

}
