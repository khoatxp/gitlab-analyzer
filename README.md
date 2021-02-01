# Gitlab Analyzer

## Tech stack
- Next.js and React for the frontend
- PostgreSQL for the database
- Java + Spring Boot for the backend
- Docker to containerize components

## Getting Started
Note the following steps pertain to running the application through IntelliJ. 

- Travel to the terminal window and run the following commands:
```bash
docker-compose build
docker-compose up
```
- Running this will start up the front end next server along with the postgres db.
- Travel to [http://localhost:3000](http://localhost:3000) within your browser to view the front end.
- For the backend, travel to intellij and hit the green ▶️ button at the top right to start the Spring Boot Application. (You may need to open the backend directory first so IntelliJ can locate the application)
- Travel to [http://localhost:8080](http://localhost:8080) within your browser to view the back end.
