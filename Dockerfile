
FROM gradle:8.6.0-jdk17 AS build
WORKDIR /app


COPY build.gradle settings.gradle ./
# Faz o download das dependências para cache
RUN gradle dependencies

# Copia o restante do código
COPY . .

# Roda o build e gera o JAR
# O comando 'bootJar' do Spring Boot gera o JAR executável
RUN gradle bootJar

# Stage 2: Runtime (Execução)
# Usando uma imagem mais leve para produção
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# A sua aplicação está configurada para usar a porta 8081
EXPOSE 8080

# Copia o JAR do estágio de build. Ajuste o nome do arquivo se o seu for diferente.
# Exemplo: build/libs/code_crafters.jar
COPY --from=build /app/build/libs/*.jar app.jar

# Comando de entrada: Inicia o JAR ativando o perfil 'render'
ENTRYPOINT ["java", "-Dspring.profiles.active=render", "-jar", "app.jar"]