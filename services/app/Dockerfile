FROM clojure
COPY . /app
WORKDIR /app
RUN lein deps
EXPOSE 3000
CMD ["lein", "ring", "server-headless", "3000"]
