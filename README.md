# Simple clojure CRUD
![My code](https://i.imgur.com/HmLYQg1.jpeg)

### Requirements

* PostgreSQL
* Clojure
* ClojureTools
* Yarn
* ShadowCLJs
* make

### Install

```
    make project-env-generate
    cd services/app
    make prepare-db
    make prepare-test-db
    make install-deps
    make test
```

### Run REPL(for Vim Iced Repl users)

```
   make repl
```

### Start jetty

Go to backend.core.clj and eval main

### Start ShadowCLJs

```
  yarn start
```

Open in browser localhost:3000
