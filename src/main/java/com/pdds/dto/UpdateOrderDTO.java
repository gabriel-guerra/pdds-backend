package com.pdds.dto;

import com.pdds.domain.enums.OrderStatus;

import java.util.List;

public record UpdateOrderDTO(
        Long productId,
        int quantity
) {
}
