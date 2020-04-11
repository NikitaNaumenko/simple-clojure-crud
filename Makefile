prepare-db:
	dropdb hs-dev --if-exists
	createdb hs-dev
	dropdb hs-test --if-exists
	createdb hs-test

setup: prepare-db
	lein deps
	lein migratus migrate

test:
	dropdb hs-test --if-exists
	createdb hs-test
	lein test

test-ci:
	docker-compose --file ./docker-compose.test.yml run test

compose-build:
	docker-compose build

compose:
	docker-compose up

compose-down:
	docker-compose down -v || true

compose-migrate:
	docker-compose run app lein migratus migrate

compose-setup: compose-down compose-build compose-migrate
