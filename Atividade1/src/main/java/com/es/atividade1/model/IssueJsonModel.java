
package com.es.atividade1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IssueJsonModel {
    
    @JsonProperty("author")
    private AuthorModel author;
    @JsonProperty("body")
    private String body;
    @JsonProperty("closedAt")
    private Date closedAt;
    @JsonProperty("comments")
    private Object[] comments;
    @JsonProperty("createdAt")
    private Date createdAt;
    @JsonProperty("milestone")
    private MilestoneModel milestone;
    @JsonProperty("title")
    private String title;
    @JsonProperty("labels")
    private Object[] labels;
}
    
