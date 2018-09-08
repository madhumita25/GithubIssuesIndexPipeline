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
	/**
	 * Query a keyword on an existing Issues index
	 * @param keyword: the input keyword being queried
	 * @return list of URL strings containing the keyword
	 */
	public static void main(String[] args) {
		if ( args.length < 2) {
			System.out.println("Usage: IssueIndexer <keyword> <INDEX_FILE>"); 
			System.exit(1);
		}
		System.out.println(queryKeyword(args[0], args[1]));
	}

	public static List<String> queryKeyword(String keyword, String index_file) {
		//String[] keywords = keyword.split(",");
		List<String> issueList = new ArrayList<String>();

		try {
			//File toRead=new File(this.getClass().getResource("keywordMap.txt").getPath());
			File toRead=new File(index_file);

			FileInputStream fis=new FileInputStream(toRead);
			ObjectInputStream ois=new ObjectInputStream(fis);

			HashMap<String,HashMap<String, Integer>> mapInFile=(HashMap<String,HashMap<String, Integer>>) ois.readObject();

			ois.close();
			fis.close();

			keyword = keyword.toLowerCase();

			if (mapInFile.containsKey(keyword)) {
				HashMap<String, Integer> innerMap = mapInFile.get(keyword);
				Map<String, Integer> sortedMapDesc = sortByComparator(innerMap, false);
				for(Entry<String, Integer> en: sortedMapDesc.entrySet()) {
					// TODO: Use RepoOwner and RepoName from Config
					// TODO: URL ideally should be queried from kohsuke's github API
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
	/**
	 * This method sorts the index entries containing keyword
	 * @param unsortMap : the unsorted input map
	 * @param order : ASCENDING(1)/DESCENDING(0)
	 * @return map sorted by values
	 */
	// Borrowed from https://stackoverflow.com/questions/8119366/sorting-hashmap-by-values

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
