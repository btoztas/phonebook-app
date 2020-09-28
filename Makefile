.PHONY: install start

install:
	mvn clean install

start: install
	docker-compose up
