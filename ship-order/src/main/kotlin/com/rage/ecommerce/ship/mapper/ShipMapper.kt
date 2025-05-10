package com.rage.ecommerce.ship.mapper

import com.rage.ecommerce.ship.dto.StageForDeliverRequestDTO
import com.rage.ecommerce.ship.dto.StageForDeliverResponseDTO
import org.mapstruct.Mapper
import org.mapstruct.ReportingPolicy

@Mapper(componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
interface ShipMapper {
    fun toStageForDeliverResponseDTO(dto: StageForDeliverRequestDTO?): StageForDeliverResponseDTO
}