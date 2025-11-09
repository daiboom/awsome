package org.startlight.awsome.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageResponse<T> {
    private Integer totalCount;
    private Integer page;
    private Integer offset;
    private List<T> data;
    private String cursor;
    private Boolean hasPrev;
    private Boolean hasNext;
}

