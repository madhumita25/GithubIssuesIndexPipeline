#!/bin/bash
java -cp /home/ubuntu/GithubIssuesIndexPipeline-1.0-RELEASE.jar com.peritus.github.issues.IssuesIngest Microsoft vscode /home/ubuntu/vscode /home/ubuntu/keywordMap.ser /home/ubuntu/last-processed.txt >> /home/ubuntu/issues_ingest_log
sudo chmod 777 /home/ubuntu/keywordMap.ser
sudo cp  -f /home/ubuntu/keywordMap.ser /

