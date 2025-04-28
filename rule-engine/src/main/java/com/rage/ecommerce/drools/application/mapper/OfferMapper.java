package com.rage.ecommerce.drools.application.mapper;

import com.rage.ecommerce.drools.application.dto.OfferEvaluationRequestDTO;
import com.rage.ecommerce.drools.application.dto.CheckOfferResponseDTO;
import com.rage.ecommerce.drools.domain.model.Offer;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OfferMapper {

    OfferEvaluationRequestDTO toApplyOfferRequestDTO(Offer offer);

    OfferEvaluationRequestDTO toApplyOfferRequestDTOFromCheckOfferResponse(CheckOfferResponseDTO checkOfferResponseDTO);

    Offer toOffer(OfferEvaluationRequestDTO offerEvaluationRequestDTO);
}