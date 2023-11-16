docker network create tracks_perform
timeout 50
docker run -d -p 3306:3306 --network tracks_perform --name mariadb -e MYSQL_ROOT_PASSWORD=password -d mariadb:lts
timeout 50
docker exec -i mariadb mysql -u root -ppassword < ..\sql\create-users.sql
docker exec -i mariadb mysql -u root -ppassword < ..\sql\init-tables.sql