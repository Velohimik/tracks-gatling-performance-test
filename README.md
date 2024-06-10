# tracks-gatling-performance-test

There are the performance tests using Gatling framework https://gatling.io/ of the Tracks application 
https://www.getontracks.org/

## Run application using Docker
### Prepare database
- For Windows: go to`application/windows` and run `config-database.bat`


- For Linux: go to`application/linux` and run `config-database.sh`

### Run application
- For Windows: go to`application/windows` and run `start-tracks.bat`


- For Linux: go to`application/linux` and run `start-tracks.sh`

## Run application using Docker Compose
- Run `docker compose up`

## Run performance test

- From Docker image:
    - build Docker image: ```docker build -t tracks-gatling-test .```<br>
    - run Docker container: ```docker run --rm -v ${PWD}/target:/perf-test/target --network=tracks_perform -e MAVEN_OPTS="-DpropertyName1=value1 -DpropertyName2=value2" tracks-gatling-test```<br><br>
- With Maven:
    - run the Maven command: ```mvn gatling:test -DruntimeEnv=maven```<br><br>
- the next system properties are available for test configuration:<br>

| Property name                   | Enable values                                                | Description                                                                                                                                                                                                                                                                                                                                                                                                               |
|---------------------------------|--------------------------------------------------------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| injectionProfile                | userPlato;<br><br>rpsSteps;<br><br>rpsPlato;<br><br>rpsPeaks | Injection profile configures the changes of virtual users number (or request per second value) during performance test.<br><br>userPlato(default value) - constant value of injected users per second;<br><br>rpsSteps - gradual increasing of requests per second values with plato stages;<br><br>rpsPlato - constant value of requests per second;<br><br>rpsPeaks - constant value of requests per second with spikes |
| numberOfUsersPerSecondOnPlato   | Integer                                                      | The number of users that are injected every second to server in *userPlato* injection profile, default value: 5 users/sec                                                                                                                                                                                                                                                                                                 |
| platoLength                     | Integer                                                      | The length of plato in *userPlato* and *rpsPlato* injection profiles, default value: 200 sec                                                                                                                                                                                                                                                                                                                              |
| stepLength                      | Integer                                                      | The length of every steps in *rpsSteps* injection profile, default value: 30 sec                                                                                                                                                                                                                                                                                                                                          |
| highestStepsRps                 | Integer                                                      | The rps value on the highest step in *rpsSteps* injection profile, default value: 20 rps                                                                                                                                                                                                                                                                                                                                  |
| rpsOnPlato                      | Integer                                                      | The rps value in *rpsPlato* injection profile, default value: 200 rps                                                                                                                                                                                                                                                                                                                                                     |
| peakRps                         | Integer                                                      | The rps value during spike in *rpsPeaks* injection profile, default value: 100 rps                                                                                                                                                                                                                                                                                                                                        |
| timeBetweenPeak                 | Integer                                                      | Time between spiked peak rps in *rpsPeaks* injection profile, default value: 20 sec                                                                                                                                                                                                                                                                                                                                       | 

## Stop application and clear database that was run with Docker
- For Windows: go to`application/windows`. Stop Tracks app with running `stop-tracks.bat`. 
Remove app database with running `remove-database.bat`.<br><br>
- For Linux: go to`application/linux`. Stop Tracks app with running `stop-tracks.sh`. 
Remove app database with running `remove-database.sh`.

## Stop application that was run with Docker Compose
- Run `docker compose down`

## Gatling report

Report folder is stored in `target/gatling/`. There is the name of folder with the last gatling
run report in `lastRun.txt`. Open in web browser file `index.html` in this folder.
