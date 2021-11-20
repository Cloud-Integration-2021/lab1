# Cloud-Integration - Lab 1

Java API Rest for lab 3

## Swagger UI 

[http://localhost:8080/swagger-ui/#/](http://localhost:8080/swagger-ui/#/)

## 🛠️ Installation Steps

### 🐳 Option 1: Run from Docker run

```bash
# Run the container
$ docker run \
  -v /etc/localtime:/etc/localtime:ro \
  -e "DATASOURCE_DB_PASSWORD=password" \
  -e "DATASOURCE_DB_USERNAME=user" \
  -e "DATASOURCE_DB_URL=jdbc:postgresql://localhost:5432/databaseName" \
  --restart always \
  --name lab1 \
  -p 8080:8080 \
  thomaslacaze/lab1
```

### 🐳 Option 2: Run from Docker-compose

**See [here](https://github.com/Cloud-Integration-2021/lab1/blob/master/docker-compose.yml)** 


🌟 You are all set!

## Dockerfile
<a href="https://github.com/Cloud-Integration-2021/lab1/blob/master/Dockerfile">Dockerfile</a>

## License
<a href="https://github.com/Cloud-Integration-2021/lab1/blob/master/LICENSE">MIT</a>
