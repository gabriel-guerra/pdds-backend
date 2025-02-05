package com.pdds.dto;

import com.pdds.domain.enums.OrderStatus;

import java.util.List;

public record OrderResponseDTO(
    long id,
    List<SelectedProductResponseDTO> orderProducts,
    double total,
    OrderStatus status
) {
}
