
package com.peritus.github.issues;

import java.util.List;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;


public class QueryKeyword {
	    public static void main(String[] args) {
	    	System.out.println(queryKeyword(args[0]));
	    }

	    public static List<String> queryKeyword(String keyword) {
	        //String[] keywords = keyword.split(",");
	    	List<String> issueList = new ArrayList<String>();
	    	
	        try {
	        	//File toRead=new File(this.getClass().getResource("keywordMap.txt").getPath());
	        	File toRead=new File("keywordMap.ser");
	        	
	            FileInputStream fis=new FileInputStream(toRead);
	            ObjectInputStream ois=new ObjectInputStream(fis);

	            HashMap<String,HashMap<String, Integer>> mapInFile=(HashMap<String,HashMap<String, Integer>>) ois.readObject();

	            ois.close();
	            fis.close();
	            
	            if (mapInFile.containsKey(keyword)) {
					HashMap<String, Integer> innerMap = mapInFile.get(keyword);
	                Map<String, Integer> sortedMapDesc = sortByComparator(innerMap, false);
	                for(Entry<String, Integer> en: sortedMapDesc.entrySet()) {
	                    System.out.println("https://github.com/Microsoft/vscode/issues/" + en.getKey());
	                    issueList.add("https://github.com/Microsoft/vscode/issues/" + en.getKey());
	                }
	            }
	        } catch ( IOException ex) {
	            System.out.println("An IOException occurred while reading the Index!");
	            ex.printStackTrace();
	        } catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	  
	        return issueList;
	        
	    }
	   
		public static Map<String, Integer> sortByComparator(Map<String, Integer> unsortMap, final boolean order)
	        {

	            List<Entry<String, Integer>> list = new LinkedList<Entry<String, Integer>>(unsortMap.entrySet());

	            // Sorting the list based on values
	            Collections.sort(list, new Comparator<Entry<String, Integer>>()
	            {
	                public int compare(Entry<String, Integer> o1,
	                        Entry<String, Integer> o2)
	                {
	                    if (order)
	                    {
	                        return o1.getValue().compareTo(o2.getValue());
	                    }
	                    else
	                    {
	                        return o2.getValue().compareTo(o1.getValue());

	                    }
	                }
	            });

	            // Maintaining insertion order with the help of LinkedList
	            Map<String, Integer> sortedMap = new LinkedHashMap<String, Integer>();
	            for (Entry<String, Integer> entry : list)
	            {
	                sortedMap.put(entry.getKey(), entry.getValue());
	            }

	            return sortedMap;
	        }
}
