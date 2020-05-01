include ../../.env
export $(shell sed 's/=.*//' ../../.env)

prepare-db:
	dropdb hs-dev --if-exists
	createdb hs-dev
	dropdb hs-test --if-exists
	createdb hs-test

setup:
	lein deps
	lein migratus migrate

test:
	dropdb hs-test --if-exists
	createdb hs-test
	clj -Atest

start:
	lein run

run:
	lein figwheel

repl:
	lein repl

