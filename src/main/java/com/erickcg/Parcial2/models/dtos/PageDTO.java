package com.erickcg.Parcial2.models.dtos;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageDTO<T> {
	private List<?> content;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
}
