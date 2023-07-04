package com.platzi.pizza.service.dto;

import lombok.Data;

@Data
public class UpdatePizzaPriceDto {
    public int pizzaId;
    public double newPrice;
}
