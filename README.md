## Editor setup
1. Import google code formatting style as default code formatter.
https://github.com/google/styleguide/blob/gh-pages/eclipse-java-google-style.xml

## Setup environment
1. Install docker and pull postgresql image.
2. `docker-compose up -d`  (change port and password as you needed in docker-compose file)
3. database default user is `postgres`
4. connect to db container `docker-compose exec postgres sh`
5. login to the container `psql -U postgres` 
6. Create database according to dev and prod environment as needed.
7. Test run using H2 database

## postgress setup

1. CREATE SEQUENCE student_id_seq START WITH 1 INCREMENT BY 1;

## Swagger

1. http://localhost:8080/swagger-ui/index.html

## Generate RSA Keys
- openssl genpkey -algorithm RSA -out private.pem -pkeyopt rsa_keygen_bits:2048
- openssl rsa -in private.pem -pubout -out public.pem


