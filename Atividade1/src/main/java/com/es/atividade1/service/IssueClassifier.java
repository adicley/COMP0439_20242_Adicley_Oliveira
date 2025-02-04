
package com.es.atividade1.service;

import com.es.atividade1.dtos.IssueClassificationAndPriorityDTO;
import com.es.atividade1.dtos.IssueClassificationDTO;
import com.es.atividade1.dtos.IssuePriorityDTO;
import com.es.atividade1.model.AuthorModel;
import com.es.atividade1.model.CommentsModel;
import com.es.atividade1.model.IssueJsonModel;
import com.es.atividade1.model.IssueModel;
import com.es.atividade1.model.LabelsModel;
import com.es.atividade1.model.MilestoneModel;
import com.es.atividade1.util.Utils;
import static com.es.atividade1.util.Utils.CMD;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.ChatOptionsBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IssueClassifier {
    private static final String JSON_ISSUES = Utils.PYTORCH_DIRECTORY + "\\issues.json";
    private static final String STOP_MISTRAL = "cmd /c ollama stop mistral";
    private static final String classify = """
                Classify text into one or more of the listed classes:
                Software Architecture, Patterns And Architectural Styles, Project Patterns, OTHERS.
                """;
    private static final String HIGH_PRIORITY = "high priority"; //Alta prioridade pra solucao da issue
    private static final String TRIAGED = "triaged"; //Priozado em outro modulo
    private static final String LOW_PRIORITY = "low priority"; //Baixa prioridade
    private static final String PRIORIZED = "priorized"; //Priorizado
    
    @Autowired
    private IssueDBService issueDBService;
    private final ChatClient chatClient;
    private final ObjectMapper mapper;
    private final ProcessBuilder pb;
    private List<IssueJsonModel> issues;
    
    public IssueClassifier(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder
                .defaultOptions(ChatOptionsBuilder.builder()
                        .withTemperature(0.0)
                        .build())
                .build();
        
        this.mapper = new ObjectMapper();
        this.pb = new ProcessBuilder();
    }

    public String exportIssuesToDataBase() {
        //Objeto IssueModel para ser salvo no banco;
        IssueModel model = new IssueModel();
        //Template para a issue armazenada no json;
        IssueJsonModel issueJsonModel = null;
        
        try {
            //Read issues in json file;
            this.issues = getJsonIssuesList(issues);
            
            AuthorModel authorModel;
            MilestoneModel  milestoneModel;
            //Save issues in data base;
            System.out.println("Export Issues Progress:");
            for (int i = 0; i < issues.size(); i++){
                showProgress(i, issues.size());
                
                issueJsonModel = issues.get(i);
                
                model.setTitle(issueJsonModel.getTitle());
                model.setBody(issueJsonModel.getBody());
                model.setCreatedAt(convertDateToLocalDate(issueJsonModel.getCreatedAt()));
                model.setClosedAt(convertDateToLocalDate(issueJsonModel.getClosedAt()));
                model.setDaysToClose(getDaysTwoDates(model.getCreatedAt(), model.getClosedAt()));
                model.setIssuePriority(getPriority(mapper, issueJsonModel.getLabels()));
                model.setMilestone((milestoneModel = issueJsonModel.getMilestone()) != null ?
                         milestoneModel.getTitle(): null);
                
                model.setAuthorName((authorModel = issueJsonModel.getAuthor()).getName().isEmpty() ? 
                         authorModel.getLogin():
                         authorModel.getName());
                
                model.setUserAtributeToResolveIssue(
                     issueJsonModel.getComments().length != 0? 
                     getUserAtributeToResolveIssue(mapper, issueJsonModel.getComments()):
                     model.getAuthorName()
                );
                //Campo vazio para a classificacao, sera classificada usando o MistralAI;
                model.setIssueClassification("");
                
                issueDBService.save(model);
                
                showProgress(i+1, issues.size());
            }
            
            System.out.println("\n*Success!\n"
                    + "*Total Issues Exported: " + issues.size());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return "Successfully Exported";
    }
    
    public String classifyIssues(){
        try {
            this.issues = getJsonIssuesList(issues);
            
            System.out.println("Issues Classification Progress:");
            
            String title;
            for (int i = 106; i < issues.size(); i++){
                showProgress(i, issues.size());
                
                title = issues.get(i).getTitle();
                issueDBService.setIssueClassification(title, mistralAI(title));
                
                showProgress(i+1, issues.size());
            }
            
            System.out.println("\n*Success!");
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            //Stop Mistral;
            mistral(STOP_MISTRAL);
        }
        
        return "Success!";
    }
    
    public String mistralAI(String text){
        String mistralResp = chatClient
               .prompt()
               .system(classify)
               .user(text)
               .call()
               .content();
        
        if (mistralResp.contains("(")){
            mistralResp = mistralResp.split("[(]")[0].toUpperCase().replaceAll("[.\\d]", "").trim();
            
            if (mistralResp.length() < 100)
                return mistralResp;
        }
        return "OTHERS";
    }
    
    private long getDaysTwoDates(LocalDate start, LocalDate end){
        return ChronoUnit.DAYS.between(start, end);
    }
    
    private LocalDate convertDateToLocalDate(Date date){
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }
    
    private String getUserAtributeToResolveIssue(ObjectMapper mapper, Object[] commentsObj) throws IOException{
        int len = commentsObj.length;
        
        CommentsModel commentsModel = null;
        for (int i = 0; i < len; i++){
             commentsModel = mapper.readValue(
                  mapper.writeValueAsString(commentsObj[i]), CommentsModel.class);
             if (commentsModel.getAuthorAssociation().equals("COLLABORATOR"))
                 return commentsModel.getAuthor().getLogin();
        }
        
        //Caso nao encontre o colaborador, pega o ultimo comentario que contem o usuario atributo que fechou a issue;
        return commentsModel.getAuthor().getLogin();
    }
    
    private String getPriority(ObjectMapper mapper, Object[] labelsObj) throws IOException{
        List<LabelsModel> labels = mapper.readValue(
                             mapper.writeValueAsString(labelsObj), new TypeReference<List<LabelsModel>>(){});
        
        String labelName;
        for (int i = 0; i < labels.size(); i++){
            labelName = labels.get(i).getName();
            
            if (labelName.equals(HIGH_PRIORITY))
                return HIGH_PRIORITY;
            else if (labelName.equals(TRIAGED))
                return PRIORIZED;
        }
        
        return LOW_PRIORITY;
    }
    
    public List<IssueClassificationAndPriorityDTO> getIssuesByClassificationAndPriority(){
        List<Object[]> results = issueDBService.countAllByIssueClassificationAndPriority();
        
        return results.stream().map(result -> new IssueClassificationAndPriorityDTO((String)result[0], (String)result[1], 
                (long)result[2])).collect(Collectors.toList());
    }

    public List<IssueClassificationDTO> getIssuesByClassification(){
        List<Object[]> results = issueDBService.countAllByIssueClassification();
        
        return results.stream().map(result -> new IssueClassificationDTO((String)result[0], (long)result[1]))
                .collect(Collectors.toList());
    }
    
    public List<IssuePriorityDTO> getIssuesByPriority(){
        List<Object[]> results = issueDBService.countAllByPriority();
        
        return results.stream().map(result -> new IssuePriorityDTO((String)result[0], (long)result[1]))
                .collect(Collectors.toList());
    }
    
    private void mistral(String command){
        try {
            pb.command(CMD, command).start().waitFor();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    private void showProgress(int start, int end) throws IOException{
        if (start >= end){
            System.out.print("\b\b\b100%");
            return;
        }
        
        float progress = (start * 100.0f/end);
     
        if (progress < 10)
            System.out.print("\b\b");
        else
            System.out.print("\b\b\b");
        
        System.out.print((int)progress + "%");
    }
    
    private List<IssueJsonModel> getJsonIssuesList(List<IssueJsonModel> list) throws IOException{
        return list == null? mapper.readValue(new File(JSON_ISSUES), new TypeReference<List<IssueJsonModel>>(){}): list;
    }
    
    public IssueDBService getDBService(){
        return this.issueDBService;
    }
}
