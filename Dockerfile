# Alpine Linux with OpenJDK JRE
FROM openjdk:8-jre-alpine
# Copy JAR into image
COPY target/OMDb-1.0.1-jar-with-dependencies.jar /home/OMDB/OMDb.jar
COPY src/run.sh /home/OMDB/run.sh
# Run application with this command line
CMD ["/home/OMDB/run.sh"]

#
# Build docker image with:
#  docker build -t omdb .
#
#
# Create docker container and run application inside with:
#  docker run omdb /home/OMDB/run.sh <arguments>
#
#  -or-
#
#  docker run omdb java -jar /home/OMDB/OMDb.jar <arguments>
#
#
# Run the image with shell access using:
#  docker run -it omdb /bin/sh
# 
# You can find the logs in the /home/OMDB/logs directory
#
# To see "old" docker processes use:
#
# docker ps -a|head
# CONTAINER ID        IMAGE               COMMAND                  CREATED             STATUS                           PORTS               NAMES
# ea5ffe34f207        omdb                "/home/OMDB/run.sh..."   About an hour ago   Exited (0) 56 minutes ago                            compassionate_curie
# 59b79b9456f6        omdb                "/bin/sh"                About an hour ago   Up 56 minutes                                        optimistic_tesla
# c6085fd7fbf9        omdb                "/bin/sh"                About an hour ago   Exited (130) 25 seconds ago                          laughing_booth
# be615ee29b55        omdb                "/home/OMDB/run.sh a"    About an hour ago   Exited (0) About an hour ago                         infallible_tesla
# e4576d5f5ade        f6a8e27e1d2c        "/bin/sh"                About an hour ago   Up 4 seconds                                         blissful_babbage
#
# Then, you can re-interact with the shell using:
#  docker container start laughing_booth
#  docker container attach laughing_booth
#
# You can re-run the "java" command and see the output if you use (note that the commands need to follow each other and be quick):
#  docker container start ea5ffe34f207 ; docker container attach ea5ffe34f207
#
#
#
#
#
#
#
#
#
#
#
#
#
#
#
#
#
#
#
#
#
#
# 
#
#
