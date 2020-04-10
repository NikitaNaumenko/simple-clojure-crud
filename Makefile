prepare-db:
	dropdb hs-dev --if-exists
	createdb hs-dev
	dropdb hs-test --if-exists
	createdb hs-test

setup: prepare-db
	lein deps
	lein migratus migrate

