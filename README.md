# recipe-api
Stateless REST api service for writing and store recipes <br>
Demo version: ...
## How to install? 
### linux:
First one, download and move into project directory. <br>
After that, use list of commands: 
1. Create docker container: `docker-compose build` <br>
2. Start that one: `docker-compose up` <br>
Check follow endpoint: http://localhost:8080/api/v1/swagger-ui.html#
## brief documentation
#### Core stack
The project has been inplemented using the spring framework.
Technology: Spring boot, Spring Data Jpa (Hibbernate), dozer
#### Cache
Project architecture have the additional cache layer. 
Every request first looking for data in cache, and if it empty go to database, pulls out and save data in cache. 
This way we unload the database. 
Technology: Guava cache
#### Database and migrations 
The project has been configured for database postgresql. <br>
Also was realized database migration engine. <br>
Technology: liqubase, hibernate, postgresql
#### Docker 
Docker deploy project and database postgresql.
Everything should start successfully if you haven't any busy ports from follow list: 
- `5432`
- `8080` <br>
#### API
Any endpoint starts from `api/v1`
