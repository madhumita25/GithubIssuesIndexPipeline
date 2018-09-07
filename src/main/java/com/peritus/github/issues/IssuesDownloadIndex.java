//package io.github.psgs.issuesdownload;
package com.peritus.github.issues;

import org.kohsuke.github.GHIssue;

import org.kohsuke.github.GHIssueState;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;

import java.util.List;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
//import java.io.OutputStream;
import org.apache.commons.io.FileUtils;

public class IssuesDownloadIndex {

    public static void main(String[] args) {
    	try {
            Config.loadConfiguration();
        } catch (IOException ex) {
            System.out.println("An IOException occurred while loading the configuration!");
            ex.printStackTrace();
        }
    	
    	try {    
        	System.out.println(Config.githubtoken);
            GitHub github = GitHub.connectUsingOAuth(Config.githubtoken);
            GHRepository repository = github.getUser(args[0]).getRepository(args[1]);
                        
            List<GHIssue> Issues = repository.getIssues(GHIssueState.CLOSED);
                        
            for (GHIssue issue : Issues) {
            	 System.out.println("Getting next issue..");
            	 int id = issue.getNumber();
            	 String body = issue.getBody();
            
                 String filename = "Issue" + String.valueOf(id);
                 
                 FileUtils.writeStringToFile(new File(filename), issue.getBody());
                 IssueIndexer.IndexIssue(id, body);
            }
            IssueIndexer.persistIndex("keywordMap.ser");
            System.out.println( "Download and Indexing Complete!");
        } catch (IOException ex) {
            System.out.println("An IOException has occurred!");
            ex.printStackTrace();
            
            if (ex.getMessage().equalsIgnoreCase("api.github.com")) {
            	System.out.println("An error has occurred reaching " + ex.getMessage() + "! Please check your network connection.");
            }
                           
        }
     }
}


