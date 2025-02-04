
package com.es.atividade1.service;

import com.es.atividade1.util.Utils;
import static com.es.atividade1.util.Utils.CMD;
import java.io.File;
import java.io.IOException;
import org.springframework.stereotype.Service;

@Service
public class ExtractIssues {
    private static final String DEFAULT_COMMAND_FOR_GH_CLI = "cmd /c gh issue list --limit " + Utils.LIMIT + " --search \""
            + Utils.DEFAULT_SEARCH + "\" --json author,body,createdAt,closedAt,comments,milestone,title,labels"
            + " > issues.json";
    
    public static String startExtractIssues(){
        ProcessBuilder pb = new ProcessBuilder();
        pb.directory(new File(Utils.PYTORCH_DIRECTORY));
        
        pb.command(CMD, DEFAULT_COMMAND_FOR_GH_CLI);
        try {
            pb.start().waitFor();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        return "Success!";
    }
}
