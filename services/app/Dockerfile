FROM clojure
COPY . /app
WORKDIR /app
EXPOSE 3000
CMD ["lein", "ring", "server-headless", "3000"]
