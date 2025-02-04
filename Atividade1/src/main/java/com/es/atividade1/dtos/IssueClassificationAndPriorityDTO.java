
package com.es.atividade1.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class IssueClassificationAndPriorityDTO {
    private String issueClassification;
    private String priority;
    private long count;
}
