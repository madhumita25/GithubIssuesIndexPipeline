//package io.github.psgs.issuesdownload;
package com.peritus.github.issues;

import org.kohsuke.github.GHIssue;
import org.kohsuke.github.GHIssueState;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;
import org.omg.CORBA_2_3.portable.OutputStream;

import java.io.FileWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.io.IOException;
//import java.io.OutputStream;
import org.apache.commons.io.FileUtils;

public class IssuesDownload {

    public static void main(String[] args) {
        try {
            Config.loadConfiguration();
        } catch (IOException ex) {
            System.out.println("An IOException occurred while loading the configuration!");
            ex.printStackTrace();
        }
    }

    public static String saveIssues(String repoDetails, GHIssueState issueState) {

        String[] repoInfo = repoDetails.split("/");
        
        try {
        	
            GitHub github = GitHub.connectUsingOAuth(Config.githubtoken);
            GHRepository repository = github.getUser("Microsoft").getRepository("vscode");
            
            List<GHIssue> Issues = repository.getIssues(GHIssueState.CLOSED);
                        
            for (GHIssue issue : Issues) {
                
                String filename = "Issue" + String.valueOf(issue.getNumber());
                FileUtils.writeStringToFile(new File(filename), issue.getBody());
                            
            }
            return "Download Complete!";
        } catch (IOException ex) {
            System.out.println("An IOException has occurred!");
            ex.printStackTrace();
            
            if (ex.getMessage().equalsIgnoreCase("api.github.com")) {
                return "An error has occurred reaching " + ex.getMessage() + "! Please check your network connection.";
            }
                           
        }
        return "An error has occurred!";
    }
}
