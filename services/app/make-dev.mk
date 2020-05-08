-include ../../.env.prod
export $(shell sed 's/=.*//' ../../.env.prod)

prepare-db:
	dropdb dev-db --if-exists
	createdb dev-db
	CLJ_ENV=development clojure -Amigratus

run-migrations:
	clojure -Amigratus

repl:
	iced repl
