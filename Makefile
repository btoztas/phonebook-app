.PHONY: install start

install-quick:
	mvn clean install -DskipTests

install:
	mvn clean install

stop:
	docker-compose down

start: stop
	docker-compose up -d

start-attached: stop
	docker-compose up

system-tests:
	mvn -pl system-tests test

