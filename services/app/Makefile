include make-dev.mk

prepare-test-db:
	dropdb test-db --if-exists
	createdb test-db
	CLJ_ENV=test clojure -Amigratus

run-test:
	CLJ_ENV=test clojure -Amigratus
	CLJ_ENV=test clojure -Atest

prod-start:
	clojure -Amigratus
	java -jar app.jar --main-class backend.core

install-deps:
	yarn install

test-repl:
	CLJ_ENV=test iced repl -A:test-path
