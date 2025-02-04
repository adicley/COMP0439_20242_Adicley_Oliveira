
package com.es.atividade1.service;

import com.es.atividade1.model.IssueModel;
import com.es.atividade1.repository.IssueRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IssueDBService {
    
    @Autowired
    private IssueRepository issueRepository;
    
    public IssueModel save(IssueModel model){
        return issueRepository.save(model);
    }
    
    public boolean existsIssue(IssueModel model){
        return issueRepository.existsById(model.getTitle());
    }
    
    public Optional<IssueModel> read(IssueModel model){
        return issueRepository.findById(model.getTitle());
    }
    
    public List<IssueModel> realAll(){
        return issueRepository.findAll();
    }
    
    public void delete(IssueModel model){
        issueRepository.deleteById(model.getTitle());
    }
    
    public List<Object[]> countAllByIssueClassification(){
        return issueRepository.countAllByIssueClassification();
    }
    
    public List<Object[]> countAllByIssueClassificationAndPriority(){
        return issueRepository.countAllByIssueClassificationAndPriority();
    }
    
    public List<Object[]> countAllByPriority(){
        return issueRepository.countAllByPriority();
    }
    
    public int setIssueClassification(String title, String classification){
        return issueRepository.updateIssueClassification(title, classification);
    }
    
    public int getNumberOfIssues(){
        return issueRepository.getNumberOfIssues();
    }
}
