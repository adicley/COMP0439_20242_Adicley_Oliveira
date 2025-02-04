
package com.es.atividade1.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class IssuePriorityDTO {
    private String priority;
    private long count;
}
