
package com.es.atividade1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthorModel {
    
    @JsonProperty("id")
    private String id;
    @JsonProperty("is_bot")
    private boolean is_bot;
    @JsonProperty("login")
    private String login;
    @JsonProperty("name")
    private String name;
}
