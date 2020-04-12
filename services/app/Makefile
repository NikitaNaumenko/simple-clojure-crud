prepare-db:
	dropdb hs-dev --if-exists
	createdb hs-dev
	dropdb hs-test --if-exists
	createdb hs-test

setup:
	env $(cat .env.test | xargs) lein deps
	env $(cat .env.test | xargs) lein migratus migrate

test:
	dropdb hs-test --if-exists
	createdb hs-test
	lein test

start:
	env $(cat .env.test | xargs) lein ring server-headless


