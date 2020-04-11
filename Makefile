VERSION := latest
REGISTRY := docker.pkg.github.com

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
	docker-compose --file ./services/app/docker-compose.test.yml run test

compose-build:
	docker-compose build

compose:
	docker-compose up

compose-down:
	docker-compose down -v || true

compose-migrate:
	docker-compose run app lein migratus migrate

compose-setup: compose-down compose-build compose-migrate

project-files-touch:
	mkdir -p tmp
	if [ ! -f tmp/ansible-vault-password ]; then echo 'jopa' > tmp/ansible-vault-password; fi;

project-env-generate:
	docker run --rm -e RUNNER_PLAYBOOK=ansible/development.yml \
		-v $(CURDIR)/ansible/development:/runner/inventory \
		-v $(CURDIR):/runner/project \
		ansible/ansible-runner

app-docker-build-production:
	docker build --cache-from=$(REGISTRY)/simple-clojure-crud/app --tag $(REGISTRY)/simple-clojure-crud/app:$(VERSION) services/app

app-docker-push:
	docker push $(REGISTRY)/NikitaNaumenko/simple-clojure-crud/app:$(VERSION)

# caddy-docker-build-production:
# 	docker build --file services/caddy/Dockerfile.production --tag $(REGISTRY)/sql-masters/sql-masters/services-caddy:$(VERSION) services/web-caddy

# caddy-docker-push:
# 	docker push docker.pkg.github.com/sql-masters/sql-masters/services-caddy:$(VERSION)

