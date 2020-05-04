include ../../.env
# export $(shell sed 's/=.*//' ../../.env)

prepare-db:
	dropdb hs-dev --if-exists
	createdb hs-dev
	dropdb hs-test --if-exists
	createdb hs-test

test-t:
	dropdb test-db --if-exists
	createdb test-db
	CLJ_ENV=test clojure -Amigratus
	CLJ_ENV=test clojure -Atest

repl:
	iced repl

