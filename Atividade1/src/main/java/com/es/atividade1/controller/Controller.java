
package com.es.atividade1.controller;

import com.es.atividade1.service.ExtractIssues;
import com.es.atividade1.service.IssueClassifier;
import org.springframework.ai.chat.client.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/issues")
public class Controller {
    
    @Autowired
    private final IssueClassifier issueClassifier;
    
    public Controller(IssueClassifier issueClassifier) {
        this.issueClassifier = issueClassifier;
    }

    @PostMapping("/extractAndExport")
    public ResponseEntity extractAndExportIssueToDataBase(){
        ExtractIssues.startExtractIssues();
        return new ResponseEntity<>(issueClassifier.exportIssuesToDataBase(), 
                HttpStatus.CREATED);
    }
    
    @PostMapping("/classify")
    public ResponseEntity classify() {
        return new ResponseEntity<>(issueClassifier.classifyIssues(), 
                HttpStatus.CREATED);
    }
    
    @GetMapping("/classification")
    public ResponseEntity getIssuesClassification(){
        return new ResponseEntity<>(issueClassifier.getIssuesByClassification(), 
                HttpStatus.OK);
    }

    @GetMapping("/classificationAndPriority")
    public ResponseEntity getIssuesClassificationAndEntity(){
        return new ResponseEntity<>(issueClassifier.getIssuesByClassificationAndPriority(), 
                HttpStatus.OK);
    }
    
    @GetMapping("/priority")
    public ResponseEntity getIssuePriority(){
        return new ResponseEntity<>(issueClassifier.getIssuesByPriority(), 
                HttpStatus.OK);
    }
    
    @GetMapping("/countIssues")
    public ResponseEntity getNumberOfIssues(){
        return new ResponseEntity<>(issueClassifier.getDBService().getNumberOfIssues(), 
                HttpStatus.OK);
    }
}
