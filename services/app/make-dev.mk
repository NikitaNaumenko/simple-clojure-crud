-include ../../.env
export $(shell sed 's/=.*//' ../../.env)

prepare-db:
	dropdb dev-db --if-exists
	createdb dev-db
	CLJ_ENV=development clojure -Amigratus

run-migrations:
	clojure -Amigratus

repl:
	iced repl
