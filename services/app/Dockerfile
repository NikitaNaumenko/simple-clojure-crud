FROM clojure:tools-deps
WORKDIR /app
RUN apt-get update && apt-get install make
COPY /target/app.jar /app/app.jar
EXPOSE 3000
CMD ["java", "-jar", "app.jar", "--main-class backend.core"]
