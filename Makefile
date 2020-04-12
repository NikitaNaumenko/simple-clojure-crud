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
	docker build --cache-from=$(REGISTRY)/nikitanaumenko/simple-clojure-crud/app --tag $(REGISTRY)/nikitanaumenko/simple-clojure-crud/app:$(VERSION) services/app

app-docker-push:
	docker push $(REGISTRY)/nikitanaumenko/simple-clojure-crud/app:$(VERSION)

caddy-docker-build-production:
	docker build --file services/caddy/Dockerfile.production --tag $(REGISTRY)/nikitanaumenko/simple-clojure-crud/caddy:$(VERSION) services/caddy

caddy-docker-push:
	docker push docker.pkg.github.com/nikitanaumenko/simple-clojure-crud/caddy:$(VERSION)

ansible-vaults-edit:
	docker run -it --rm \
		-v $(CURDIR):/runner/project \
		ansible/ansible-runner ansible-vault edit project/ansible/production/group_vars/all/vault.yml

ansible-vaults-create:
	docker run -it --rm \
		-v $(CURDIR):/runner/project \
		ansible/ansible-runner ansible-vault create project/ansible/production/group_vars/all/vault.yml
