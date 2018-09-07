//package io.github.psgs.issuesdownload;
package com.peritus.github.issues;

import org.kohsuke.github.GHIssue;

import org.kohsuke.github.GHIssueState;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;

import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.List;
import java.io.IOException;
import java.io.EOFException;
//import java.io.OutputStream;
import org.apache.commons.io.FileUtils;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

/**
 * Issue Indexing Pipeline 
 * @author bharde
 *
 */
public class IssuesIngest {

	/**
	 * Load Index, index newer issues and persist the index
	 * @param args RepoOwner & RepoName
	 * 
	 */
    public static void main(String[] args) {
    	try {
            Config.loadConfiguration();
        } catch (IOException ex) {
            System.out.println("An IOException occurred while loading the configuration!");
            ex.printStackTrace();
        }
    	
    	
        LocalDateTime start= LocalDateTime.now();
    	int lastProcessedIssue = 0;
    	int count = 0;
        try {
            GitHub github = GitHub.connectUsingOAuth(Config.githubtoken);
            GHRepository repository = github.getUser(args[0]).getRepository(args[1]);
            System.out.println("Ingesting issues from "+ args[0] + "/" + repository.getName());
            
            IssueIndexer.loadIndex(args[3]);
            BufferedReader br = new BufferedReader(new FileReader(args[4]));
              
            String str = br.readLine();
            if ( str != null) {
            	lastProcessedIssue = Integer.parseInt(str);             
            	while (true) {
            		lastProcessedIssue = lastProcessedIssue + 1;
            		GHIssue Issue = repository.getIssue(lastProcessedIssue);
               		String filename = args[2]+ File.separator + "Issue" + String.valueOf(lastProcessedIssue);
               		
               		String body = Issue.getBody();
               
                 	FileUtils.writeStringToFile(new File(filename), body);						
            		FileUtils.writeStringToFile(new File(args[4]), Integer.toString(lastProcessedIssue));
            		
            		IssueIndexer.IndexIssue(lastProcessedIssue, body);
            		count = count + 1;
            	}
            }
            
        } catch (EOFException ex) {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        	System.out.println("Processed " + count + " new issues @ " + dtf.format(LocalDateTime.now()));
        	IssueIndexer.persistIndex(args[3]);
            System.exit(0);
        } catch (IOException ex) {
        	if (ex.getMessage().equalsIgnoreCase("api.github.com")) {
                System.out.println("An error has occurred reaching " + ex.getMessage() + "! Please check your network connection.");
                System.exit(1);
        	}
        	DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        	LocalDateTime end= LocalDateTime.now();
        	long diffInSeconds = java.time.Duration.between(start, end).getSeconds();
        	System.out.println("Processed " + count + " new issues @ " + dtf.format(end) + " in " + diffInSeconds + " seconds.." );
        	IssueIndexer.persistIndex(args[3]);
            System.exit(0);
        } 
    }
}
