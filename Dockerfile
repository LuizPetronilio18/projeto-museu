# ----- Estágio 1: O "Builder" (Construtor) -----
# Usamos uma imagem que já tem Maven e Java 11 (o mesmo do seu pom.xml)
FROM maven:3.8.5-jdk-11 AS builder

# Criamos uma pasta de trabalho dentro do container
WORKDIR /app

# Copiamos o pom.xml e a pasta src para dentro do container
COPY pom.xml .
COPY src ./src

# Rodamos o comando do Maven para compilar e criar o .war
# O -DskipTests faz ir mais rápido
RUN mvn clean package -DskipTests

# ----- Estágio 2: O "Servidor" Final -----
# Usamos uma imagem do Tomcat com Java 11 (para bater com o build)
FROM tomcat:10.1-jdk11

# Copiamos o .war que foi criado no Estágio 1 (o "builder") 
# para a pasta de apps do Tomcat
COPY --from=builder /app/target/luiz_angelina_museu-1.0-SNAPSHOT.war /usr/local/tomcat/webapps/ROOT.war
