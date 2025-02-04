
package com.es.atividade1.model;

import com.es.atividade1.util.Utils;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = Utils.ISSUE_TABLE)
public class IssueModel {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id = 1;
    
    @Column(name = "title")
    private String title;
    
    @Column(name = "body")
    private String body;
    
    @Column(name = "created_at")
    private LocalDate createdAt;
    
    @Column(name = "closed_at")
    private LocalDate closedAt;
    
    @Column(name = "days_to_close")
    private long daysToClose;
    
    @Column(name = "issue_priority")
    private String issuePriority;
    
    @Column(name = "milestone")
    private String milestone;
    
    @Column(name = "author_name")
    private String authorName;
    
    @Column(name = "user_atribute_to_resolve_issue")
    private String userAtributeToResolveIssue;
    
    @Column(name = "issue_classification")
    private String issueClassification;
}
