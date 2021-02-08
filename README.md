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
- For the backend, travel to intellij and hit the green ▶️ button at the top right to start the Spring Boot Application. (When you first open the application in IntelliJ, you should get a notification saying Maven build scripts have been found. Make sure to import them.)
- Travel to [http://localhost:8080](http://localhost:8080) within your browser to view the back end.

## TESTING

All test need to be placed into test folders for frontend test file format will
follow file naming *.test.tsx. Frontend Test data can be found in the `frontend/coverage`. 
Back End test data can be found in `backend/target/surefire-reports`.

To run tests can use `tests.bat` in root directory to run tests for back and front ends, docker must
be running to use back end tests i.e (http://localhost:8080) should be available.

To run just frontend tests ensure the current directory is `./frontend` then run the command `npm test`

To run just backend tests again ensure (http://localhost:8080) is available then change directory to 
`./backend` and run `mvnw clean test`

