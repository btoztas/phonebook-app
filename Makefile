.PHONY: install-quick install system-tests stop start start-attached

install-quick:
	mvn clean install -DskipTests

install:
	mvn clean install

system-tests:
	mvn -Psystem-tests -f system-tests/pom.xml test

stop:
	docker-compose down

start: stop
	docker-compose up -d

start-attached: stop
	docker-compose up

frontend-dev:
	npm run start --prefix frontend

