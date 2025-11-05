# Use uma imagem base oficial do Tomcat (que roda Java)
FROM tomcat:10.1-jdk17-temurin

# Copie o .war para a pasta de apps do Tomcat e renomeie para ROOT.war
COPY target/luiz_angelina_museu-1.0-SNAPSHOT.war /usr/local/tomcat/webapps/ROOT.war