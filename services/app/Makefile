include make-dev.mk

prepare-test-db:
	dropdb test-db --if-exists
	createdb test-db
	CLJ_ENV=test clojure -Amigratus

run-test:
	CLJ_ENV=test clojure -Amigratus
	CLJ_ENV=test clojure -Atest

install-deps:
	yarn install
