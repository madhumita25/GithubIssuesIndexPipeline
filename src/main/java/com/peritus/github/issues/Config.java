//package io.github.psgs.issuesdownload;
package com.peritus.github.issues;


import java.io.FileInputStream;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class Config {

    public static String githubtoken;
    private static Properties config;

    private Config() {
    	config = new Properties();
    	try {
			config.load(this.getClass().getResourceAsStream("/IssuesPipeline.properties"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public static void loadConfiguration() throws FileNotFoundException, IOException {

        //githubtoken = config.getProperty("GitHub-Token");
        githubtoken = "";

    }
}
