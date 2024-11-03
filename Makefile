.PHONY: build docker-up

build:
	mvn	clean package -DskipTests

serve: build
	docker-compose up --build