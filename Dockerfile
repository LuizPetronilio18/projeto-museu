# ----- Estágio 1: O "Builder" (Construtor) -----
# Usamos uma imagem que já tem Maven e Java 11 (o mesmo do seu pom.xml)
FROM maven:3.8.5-jdk-11 AS builder

# Criamos uma pasta de trabalho dentro do container
WORKDIR /app

# Copiamos o pom.xml e a pasta src para dentro do container
COPY pom.xml .
COPY src ./src

# Rodamos o comando do Maven para compilar e criar o .war
RUN mvn clean package -DskipTests

# ----- Estágio 2: O "Servidor" Final -----
# Usamos uma imagem do Tomcat com Java 11 (para bater com o build)
FROM tomcat:10.1-jdk11

# --- A GRANDE MUDANÇA ESTÁ AQUI ---
# Mudar para o usuário ROOT para poder instalar pacotes
USER root

# Atualizar o 'apt' (gerenciador de pacotes) e instalar o 'fontconfig' (gerenciador de fontes)
# O 'debconf-set-selections' é um truque para aceitar o "Contrato da Microsoft" automaticamente
# O 'ttf-mscorefonts-installer' é o pacote que instala Arial, Times New Roman, etc.
RUN apt-get update && \
    apt-get install -y --no-install-recommends fontconfig && \
    echo "ttf-mscorefonts-installer msttcorefonts/accepted-mscorefonts-eula select true" | debconf-set-selections && \
    apt-get install -y --no-install-recommends ttf-mscorefonts-installer && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

# Voltar para o usuário padrão do Tomcat (por segurança)
USER tomcat
# --- FIM DA MUDANÇA ---

# Copiamos o .war que foi criado no Estágio 1 (o "builder") 
# para a pasta de apps do Tomcat
COPY --from=builder /app/target/luiz_angelina_museu-1.0-SNAPSHOT.war /usr/local/tomcat/webapps/ROOT.war
