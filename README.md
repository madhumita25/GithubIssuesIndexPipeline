# GithubIssuesIndexPipeline
Github Issues Processing Pipeline to Index Issues on Keywords.

## Deployment-only
### Prerequisites
* JRE 1.8+ 

### Deployment
1. Download and copy the jar 
2. Edit paths in scripts/clean_github_pipeline.sh and execute the script
3. Edit paths in scripts/github_pipeline.sh (correposnding to clean_gihub_peipleline.sh) and execute the script
4. For periodic exeuction, add an entry in crontab (crontab -e)

E.g. For hourly exeuction, add the line
```
0 * * * * /home/ubuntu/github_pipeline.sh >/dev/null 2>&1
```

## Build and Deploy
### Prerequisites

* JDK 1.8+ 
* Maven 3 (configure maven proxy if needed)

### Build
1. mvn package 
2. Follow Deployment steps..

### Execution Examples
* Ingest new issues on an existing index file
```
java -cp /home/ubuntu/GithubIssuesIndexPipeline-1.0-RELEASE.jar com.peritus.github.issues.IssuesIngest <RepoOwner> <RepoName> <PATH_TO_STORE_ISSUES> <PATH_TO_PREEXISTING_INDEX_FILE> <PATH_TO_LAST_PROCESSED_FILE>
```
* Index already downloaded issues
```
#java -cp GithubIssuesIndexPipeline-1.0-RELEASE.jar com.peritus.github.issues.IssueIndexer <ISSUES_FOLDER>
```
   Note: Creates index "keywordMap.ser"

* Query Keyword
```
#java -cp GithubIssuesIndexPipeline-1.0-RELEASE.jar com.peritus.github.issues.QueryKeyword <keyword> <INDEX_FILE>
```


## Enhancements TODO

### API: 
I found the functionality of kohsuke's github API to be limited. It was also slow and unreliable at times. Maybe some other option could be tried: https://developer.github.com/v3/libraries/

### Storage: 
Currently only bodies of the issues are being stored. Makes sense to store all relevant fields in JSON response, depending on use-case demands.

### Processing:
Would be ideal to use something like Spark for processing, start from Spark's signature wordcount and leverage map/reduce operations to process/order issues.

### Natural Language Processing:
Current functionality has only basic cleanup (stop word removal etc..) Varierty of external libraries can be used to enhance the extraction of keywords. (e.g. spark MLlib, Lucene)

### Index:
Currently the index is a nested Hashmap that is stored as a flat file.  Make sense to move to a more effcient index like Solr or Redis.

### Proximity heuristic & Backend DB:
If an absolute metric for relevance is desired (e.g. #occurrences of keyword - like in this usecase), even something like SQL (especially coupled with Spark processing) makes sense. On the other hand for an approximate match, TF-IDF libraries (again already part of Spark MLlib), K-V stores etc. can be used.  

### Streaming:
Although, not a perfectly real time use-case (we could live with newer issues not being indexed immediately after they get opened), maybe makes sense to leverage something like NiFi and customize the processsors to create a stream processing pipeline.

