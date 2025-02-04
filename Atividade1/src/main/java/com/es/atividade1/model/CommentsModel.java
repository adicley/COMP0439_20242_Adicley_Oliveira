
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
public class CommentsModel {
    
    @JsonProperty("id")
    private String id;
    @JsonProperty("author")
    private AuthorModel author;
    @JsonProperty("authorAssociation")
    private String authorAssociation;
    @JsonProperty("body")
    private String body;
    @JsonProperty("createdAt")
    private Date createdAt;
    @JsonProperty("includesCreatedEdit")
    private boolean includesCreatedEdit;
    @JsonProperty("isMinimized")
    private boolean isMinimized;
    @JsonProperty("minimizedReason")
    private String minimizedReason;
    @JsonProperty("reactionGroups")
    private Object[] reactionGroups;
    @JsonProperty("url")
    private String url;
    @JsonProperty("viewerDidAuthor")
    private boolean viewerDidAuthor; 
}
