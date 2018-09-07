//package io.github.psgs.issuesdownload;
package com.peritus.github.issues;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.util.List;
import java.util.SortedMap;
import java.util.Map;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.Map.Entry;
import java.util.StringTokenizer;
import java.io.ObjectOutputStream;
//import com.google.common.collect.TreeMultimap;
//import com.google.common.collect.SortedSetMultimap;
/**
 * Issue Indeing related operations
 * @author bharde
 *
 */
public class IssueIndexer {
	//SortedMap<String, String> keywordMap  = new TreeMap<String, String>();
	//public static HashMap<String, HashMap<String, Integer>> keywordMap = new HashMap<String, HashMap<String, Integer>>();
	public static HashMap<String, HashMap<String, Integer>> keywordMap;
	/**
	 * Index already Downloaded Issues
	 * Filenames are of the form "Issue<id>"
	 * @param args: Directory containing downloaded issue files
	 */
	public static void main(String[] args) {
		loadIndex("keywordMap.ser");
		File folder = new File(args[0]);
		try {
			for (File file : folder.listFiles()) {
				if (file.isFile()) {
					String filename = file.getName();
					int issue_id = Integer.parseInt(filename.split("Issue")[1]);
					System.out.println(issue_id);
					IndexIssue(issue_id, readFile(file.getAbsolutePath()));
				}
			}
			persistIndex("keywordMap.ser");
		} catch (IOException ex) {
            System.out.println("An IOException has occurred!");
            ex.printStackTrace();
		}
	}
	
	/**
	 * Read all of file content in a string
	 * @param fileName: input file
	 * @return: output string
	 * @throws IOException
	 */
	public static String readFile(String fileName) throws IOException {
		    BufferedReader br = new BufferedReader(new FileReader(fileName));
		    try {
		        StringBuilder sb = new StringBuilder();
		        String line = br.readLine();

		        while (line != null) {
		            sb.append(line);
		            sb.append(" ");
		            //sb.append("\n");
		            line = br.readLine();
		        }
		        return sb.toString();
		    } finally {
		        br.close();
		    }
    }
	
	/**
	 * Load Index in keywordMap
	 * @param index_file: input index file
	 */
	public static void loadIndex(String index_file) {
		keywordMap = new HashMap<String, HashMap<String, Integer>>();
		try {
			//File toRead=new File(this.getClass().getResource("keywordMap.txt").getPath());
			File toRead=new File(index_file);
    	
			FileInputStream fis=new FileInputStream(toRead);
			ObjectInputStream ois=new ObjectInputStream(fis);

			keywordMap=(HashMap<String,HashMap<String, Integer>>) ois.readObject();

			ois.close();
			fis.close();
             
		} catch ( IOException ex) {
        System.out.println("An IOException occurred while reading the Index!");
        ex.printStackTrace();
		} catch (ClassNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		}
	}

	/**
	 * Persist Index
	 * @param index_file: index file to persist
	 */
	public static void persistIndex(String index_file) {
		try {
			File fileMap=new File(index_file);
			FileOutputStream fos=new FileOutputStream(fileMap);
	
			ObjectOutputStream oos=new ObjectOutputStream(fos);

			oos.writeObject(keywordMap);
			oos.flush();
			oos.close();
		} catch (IOException ex) {
            System.out.println("An IOException has occurred!");
            ex.printStackTrace();
		}
	}
	
	/**
	 * Index an Issue
	 * @param id: issue number
	 * @param body: body of the issue
	 */
	public static void IndexIssue(int id, String body) {
			
		body = body.replaceAll("[^A-Za-z ]","").toLowerCase();
		List<String> stopWords = StopWords.getWords();
		for (String word : stopWords) {
	         body = body.replaceAll("\\b" + word + "\\b", "");
	    }
		
		String issue_id = Integer.toString(id);
		
		StringTokenizer st = new StringTokenizer(body); 
		while (st.hasMoreTokens()) {
			
			String keyword = st.nextToken();
			
			// TODO: Stem the words
			//String keyword = Stemmer.stem(keyword_unstemmed);
			
			if (keywordMap.containsKey(keyword)) {
				HashMap<String, Integer> innerMap = keywordMap.get(keyword);
				if (!innerMap.containsKey(issue_id)) {
			    	keywordMap.get(keyword).put(issue_id, 1);
				} else {
					innerMap.put(issue_id, innerMap.get(issue_id) + 1);
				}
		     } else {
		    	 keywordMap.put(keyword, new HashMap<String, Integer>());
				 keywordMap.get(keyword).put(issue_id, 1);
		     }
		}
	}
}
