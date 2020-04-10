compose-build:
	docker-compose build

compose-stop:
	docker-compose stop

compose:
	docker-compose up -d

compose-down:
	docker-compose down -v || true

# setup: compose-down compose-build db-drop db-prepare

db-drop:
	docker-compose exec db dropdb hs-dev
db-create:
	docker-compose exec db createdb hs-dev
setup: compose-down compose-build compose db-drop db-create
