ftp -v -n 192.168.0.2 22 <<EOF
user deipss deipss
quote pasv
passive
binary
cd /opt/app
prompt
put /Users/deipss/IdeaProjects/jvm-sandbox-inspector/jvm-sandbox-inspector-web/target/jvm-sandbox-inspector-web-1.0.1-SNAPSHOT.jar
put /Users/deipss/IdeaProjects/jvm-sandbox-inspector/jvm-sandbox-inspector-debug-provider/target/jvm-sandbox-inspector-debug-provider-1.0.1-SNAPSHOT.jar
put /Users/deipss/IdeaProjects/jvm-sandbox-inspector/jvm-sandbox-inspector-debug-consumer/target/jvm-sandbox-inspector-debug-consumer-1.0.1-SNAPSHOT.jar
cd /opt/sandbox/sandbox-module
put /Users/deipss/IdeaProjects/jvm-sandbox-inspector/jvm-sandbox-inspector-agent/jvm-sandbox-inspector-agent-core/target/inspector-module-jar-with-dependencies.jar
bye
EOF
echo "commit to ftp successfully"