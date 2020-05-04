FROM clojure
COPY . /app
WORKDIR /app
RUN apt-get update && apt-get install make
EXPOSE 3000
CMD ["lein", "ring", "server-headless", "3000"]
