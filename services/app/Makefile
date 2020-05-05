# include ../../.env
# export $(shell sed 's/=.*//' ../../.env)

prepare-db:
	dropdb dev-db --if-exists
	createdb dev-db
	CLJ_ENV=development clojure -Amigratus

prepare-test-db:
	dropdb test-db --if-exists
	createdb test-db
	CLJ_ENV=test clojure -Amigratus

test-t:
	CLJ_ENV=test clojure -Amigratus
	CLJ_ENV=test clojure -Atest

repl:
	iced repl

