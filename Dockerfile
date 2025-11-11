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

# --- INÍCIO DA INSTALAÇÃO MANUAL E LEVE DAS FONTES ---
# Precisamos ser 'root' para instalar
USER root

# Etapa A: Instalar ferramentas LEVES (wget, cabextract, fontconfig)
# fontconfig é essencial para a JVM encontrar as fontes
RUN apt-get update && \
    apt-get install -y --no-install-recommends wget cabextract fontconfig && \
    \
    # Etapa B: Baixar e extrair as fontes manualmente
    # Nós só baixamos o que é essencial (Arial, Times) para economizar espaço
    mkdir -p /usr/share/fonts/truetype/msttcorefonts && \
    cd /tmp && \
    \
    # Baixa os .exe (Arial, Arial Bold, Times New Roman)
    wget -q http://downloads.sourceforge.net/corefonts/arial32.exe && \
    wget -q http://downloads.sourceforge.net/corefonts/arialb32.exe && \
    wget -q http://downloads.sourceforge.net/corefonts/times32.exe && \
    \
    # Extrai APENAS os .TTF para a pasta de fontes
    cabextract -L -F '*.TTF' arial32.exe -d /usr/share/fonts/truetype/msttcorefonts && \
    cabextract -L -F '*.TTF' arialb32.exe -d /usr/share/fonts/truetype/msttcorefonts && \
    cabextract -L -F '*.TTF' times32.exe -d /usr/share/fonts/truetype/msttcorefonts && \
    \
    # Etapa C: Atualizar o cache de fontes do sistema
    fc-cache -f -v && \
    \
    # Etapa D: Limpeza TOTAL
    # Remove as ferramentas que acabamos de usar (para a imagem ficar leve)
    apt-get purge -y --auto-remove wget cabextract && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/* /tmp/*

# Volta para o usuário padrão do Tomcat
USER tomcat
# --- FIM DA INSTALAÇÃO DAS FONTES ---

# Copiamos o .war que foi criado no Estágio 1 (o "builder") 
# para a pasta de apps do Tomcat
COPY --from=builder /app/target/luiz_angelina_museu-1.0-SNAPSHOT.war /usr/local/tomcat/webapps/ROOT.war
