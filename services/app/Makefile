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
	lein test

start:
	lein run

