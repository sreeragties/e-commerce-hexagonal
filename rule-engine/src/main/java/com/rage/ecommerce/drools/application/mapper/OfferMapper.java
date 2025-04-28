package com.rage.ecommerce.drools.application.mapper;

import com.rage.ecommerce.drools.application.dto.ApplyOfferRequestDTO;
import com.rage.ecommerce.drools.application.dto.CheckOfferResponseDTO;
import com.rage.ecommerce.drools.domain.model.Offer;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OfferMapper {

    ApplyOfferRequestDTO toApplyOfferRequestDTO(Offer offer);

    ApplyOfferRequestDTO toApplyOfferRequestDTOFromCheckOfferResponse(CheckOfferResponseDTO checkOfferResponseDTO);

    Offer toOffer(ApplyOfferRequestDTO applyOfferRequestDTO);
}