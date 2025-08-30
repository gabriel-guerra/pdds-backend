package com.pdds.dto;

import com.pdds.domain.Product;

public record UpdateCartDTO(
        Long productId,
        int quantity
) {
}
