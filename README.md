# Getting Started

## Running application

### Pre-requisites
- JDK 17
- Docker

### Commands


```bash
./gradlew bootRun
```
Builds source code if necessary and starts spring boot server on 8080 port

```bash
./gradlew bootRun --args='--spring.profiles.active=local'
```
- Builds source code if necessary, sets the active profile and starts spring boot server on 8080 port
- Active profile local is for local development

```bash
./gradlew clean build
```
Cleans all build files and executes all build, lint, test tasks

```bash
./gradlew test
```
Executes project tests

### Database
Launching PostgreSQL database with Docker container:

- launch and init db

```bash
docker-compose up -d
```

- recreate db / cleanup

```bash
docker-compose down -v && docker-compose up -d
```

---

## Liquibase

### Automatically generating SQL from entities
Run:
```bash
./gradlew diffChangeLog
```

If there’s any difference between the entity classes' code and what is currently in your database, there will be new changesets generated in the file `db.changelog-diff.sql`. Run `./gradlew update` to update the database with newly written changesets.

#### Some things to remember:

* Please always check the automatically generated SQL;

* **Remove all the quotation marks yourself**. If not, the database objects will also be with quotation marks and you will have to query them using quotes.

* If you’re creating a new column in an entity class and its name consists of several words (like `longName`), you can mark the field with `@Column(name = "long_name")` to name the column tables consistently. Otherwise, the column will be named as `longname` in the database.

### Liquibase commands
To update the database after you have made a new changeset, run:
```bash
./gradlew update
```

You can 'undo' the latest changeset in the database by running:
```bash
./gradlew rollbackCount -PliquibaseCommandValue=1
```

If you have any problems with changeset checksums, run:
```bash
./gradlew clearChecksums
```

You can find other commands that can be run by running:
```bash
./gradlew tasks
```

---

## JWT
Openssl is preinstalled on MacOS. If you are using Windows/Linux you should install openssl yourself.
After having successfully installed it, make sure that the src/main/resources/certs/ folder is empty and run following 3 commands (or run the generate_certs.sh file):

- generating private and public keys

```bash
openssl genrsa -out src/main/resources/certs/keypair.pem 2048
```

```bash
openssl rsa -in src/main/resources/certs/keypair.pem -pubout -out src/main/resources/certs/public.pem
```

```bash
openssl pkcs8 -topk8 -inform PEM -outform PEM -nocrypt -in src/main/resources/certs/keypair.pem -out src/main/resources/certs/private.pem
```

After having run all these 3 commands make sure that there are 3 files in the
_src/main/resources/certs_ folder: **public.pem**, **private.pem**, and keypair.pem.
The keypair.pem can be deleted, we will no longer need this file.

---

## How to use API

- _No auth. required_:

**POST** `/login`. Required: JSON {email, password}

**POST** `/register/user`. Required: JSON {email, password, firstName, lastName, birthDate}

**POST** `/register/service_provider`. Required: JSON {email, password, companyName, phoneNumber, type}


- For _MANAGER_ or _ADMIN_:

**GET** `/service_providers/{serviceProviderId}`

**GET** `/service_providers/`

**DELETE** `/service_providers/{serviceProviderId}`

**PATCH** `/service_providers/{serviceProviderId}` + patch JSON: {email, password, companyName, phoneNumber, type}.

**GET** `/timeslots` - returns the list of all timeslots

**GET** `/timeslots/{timeslotId}` - get specific timeslot

**DELETE** `/timeslots/{timeslotId}` - deletes the timeslot with specified ID

**PATCH** `/timeslots/{timeslotId}` - modifies the timeslot with specified ID. Requires JSON object like this:

{
"serviceProviderId": "2",
"startTime": "2022-07-02T11:00:00.000+00:00",
"endTime": "2022-07-02T11:30:00.000+00:00"
}


- For _ADMIN_:

**GET** `/users/{user_id}`

**GET** `/users/`

**DELETE** `/users/{user_id}`

**PATCH** `/users/{user_id}` + patch JSON: {email, password, firstName, lastName, birthDate}. All fields are required

**POST** `/roles/add/{user_id}/{role}`

**POST** `/roles/remove/{user_id}/{role}`

- For _ADMIN_ and  _SERVICE_PROVIDER_:

**GET** `/procedures/{user_id}` - returns the specific procedure

**GET** [available for all users] `/procedures/` - returns the list of all procedures

**DELETE** `/procedures/{user_id}` - deletes the specific procedure

**PATCH** `/procedures/{user_id}` + patch JSON: {procedureName, description, price}. All fields are required. Updates the procedure

**POST** `/procedures/` - create a procedure. Required: JSON {procedureName, description, price}

- For _Everyone_:

**GET** `/appointments` - returns the JSON list of all appointments

**GET** `/appointments/{appointmentId}` - returns JSON object of a single appointment

**POST** `/appointments` - create appointment. Requires JSON like this:

{
  "timeslotId": "5",
  "comment": "no user in appointment check",
  "procedures": [
    {
      "id": "1"
    },
    {
      "id": "2"
    }
  ]
}

**DELETE** `/appointments/{appointmentId}` - deletes appointment with specified ID

**PATCH** `/appointments/{appointmentId}` - patch appointment. Requires JSON like this:

---

### Reference Documentation

* [Official Gradle documentation](https://docs.gradle.org)
* [Spring Boot Gradle Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.7.4/gradle-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/2.7.4/gradle-plugin/reference/html/#build-image)
* [Spring Data JPA](https://docs.spring.io/spring-boot/docs/2.7.4/reference/htmlsingle/#data.sql.jpa-and-spring-data)
* [Spring Web](https://docs.spring.io/spring-boot/docs/2.7.4/reference/htmlsingle/#web)
* [Liquibase Migration](https://docs.spring.io/spring-boot/docs/2.7.4/reference/htmlsingle/#howto.data-initialization.migration-tool.liquibase)
* [Spring Security](https://docs.spring.io/spring-boot/docs/2.7.4/reference/htmlsingle/#web.security)
* [Validation](https://docs.spring.io/spring-boot/docs/2.7.4/reference/htmlsingle/#io.validation)
* [Spring Boot DevTools](https://docs.spring.io/spring-boot/docs/2.7.4/reference/htmlsingle/#using.devtools)
