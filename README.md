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

* Download and Index Issues
```
#java -cp GithubIssuesIndexPipeline-1.0-RELEASE.jar com.peritus.github.issues.IssuesDownloadIndex <RepoOwner> <RepoName>
```
Note: May need to be re-built based on closed/open issue state.

* Index issues
```
#java -cp GithubIssuesIndexPipeline-1.0-RELEASE.jar com.peritus.github.issues.IssueIndexer <ISSUES_FOLDER>
```
Note: Creates index "keywordMap.ser"

* Query Keyword
```
#java -cp GithubIssuesIndexPipeline-1.0-RELEASE.jar com.peritus.github.issues.QueryKeyword <keyword> <INDEX_FILE>
```
