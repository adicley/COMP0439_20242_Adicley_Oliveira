
package com.es.atividade1.repository;

import com.es.atividade1.model.IssueModel;
import com.es.atividade1.util.Utils;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface IssueRepository extends JpaRepository<IssueModel, String>{
    @Query(value = "SELECT issue_classification, COUNT(title) FROM api." + Utils.ISSUE_TABLE + " GROUP BY issue_classification",
            nativeQuery = true)     
    List<Object[]> countAllByIssueClassification();
    
    @Query(value = "SELECT issue_classification, issue_priority, COUNT(issue_priority) " +
            "FROM api." + Utils.ISSUE_TABLE + " GROUP BY issue_classification, issue_priority",
            nativeQuery = true)
    List<Object[]> countAllByIssueClassificationAndPriority();
    
    @Query(value = "SELECT issue_priority, COUNT(issue_priority) FROM api." + Utils.ISSUE_TABLE + " GROUP BY issue_priority", 
            nativeQuery = true)
    List<Object[]> countAllByPriority();
    
    @Query(value = "SELECT COUNT(*) FROM api." + Utils.ISSUE_TABLE, nativeQuery = true)
    int getNumberOfIssues();
    
    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(value = "UPDATE api." + Utils.ISSUE_TABLE + " SET issue_classification = ?2 WHERE title = ?1", nativeQuery = true)
    int updateIssueClassification(String title, String classification);
}
