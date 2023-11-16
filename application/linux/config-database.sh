docker network create tracks_perform
sleep 10
docker run -d -p 3306:3306 --name mariadb --network tracks_perform -e MYSQL_ROOT_PASSWORD=password -d mariadb:lts
sleep 10
docker exec -i  mariadb mysql -u root -ppassword < ../sql/create-users.sql
docker exec -i  mariadb mysql -u root -ppassword < ../sql/init-tables.sql