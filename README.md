# CMS-CCT
Content Manager System and Content Creator Tool developed to facilitate the presentation of information.

# Setting up
```
docker-compose up
docker-compose Down
docker-compose Down -v # down and delete volumes
```

# Errors
To fix errors such as the one below
```
Failed to compile
Error: Cannot find module [module]
```

Simply execute the following
```
docker-compose down -v
docker-compose build
docker-compose up
```