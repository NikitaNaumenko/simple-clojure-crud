VERSION := latest
REGISTRY := docker.pkg.github.com

run-db:
	docker run --rm --name pg-docker -d \
		-e POSTGRES_PASSWORD=docker \
		-e POSTGRES_USER=docker \
		-e POSTGRES_DB=hs-dev \
		-p 5432:5432 \
		-v $(HOME)/docker/volumes/postgres:/var/lib/postgresql/data postgres

test-ci:
	docker-compose --file ./services/app/docker-compose.test.yml run test

build-jar:
	cd services/app && mkdir classes
	cd services/app && CLJ_ENV=production clojure -e "(compile 'backend.core)"
	cd services/app && shadow-cljs release app
	cd services/app && CLJ_ENV=production clojure -A:uberjar --main-class backend.core

project-files-touch:
	mkdir -p tmp
	if [ ! -f tmp/ansible-vault-password ]; then echo 'jopa' > tmp/ansible-vault-password; fi;

project-env-generate: project-files-touch
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
