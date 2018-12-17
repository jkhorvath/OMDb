# OMDb Movie Lookup Example

This project illustrates the sample invocation of the OMDb online enterprise service to lookup a sample movie title.

## Getting Started

These instructions will demonstrate how to compile the code, create a runnable Java JAR file, package the JAR file into a docker image, and run the docker image.

### Prerequisites

This project assumes that you have Java JDK, Maven, and Docker available on your desktop / laptop.

Here is what my system is running.

```
[desktop] 13:04 root@jkh-7:~# java -version
java version "1.8.0_131"
Java(TM) SE Runtime Environment (build 1.8.0_131-b11)
Java HotSpot(TM) 64-Bit Server VM (build 25.131-b11, mixed mode)

[desktop] 13:04 root@jkh-7:~# mvn -version
Apache Maven 3.3.3 (7994120775791599e205a5524ec3e0dfe41d4a06; 2015-04-22T06:57:37-05:00)
Maven home: /usr/local/apache-maven-3.3.3
Java version: 1.8.0_131, vendor: Oracle Corporation
Java home: /usr/java/jdk1.8.0_131/jre
Default locale: en_US, platform encoding: ISO-8859-1
OS name: "linux", version: "3.10.0-862.14.4.el7.x86_64", arch: "amd64", family: "unix"

[desktop] 13:04 root@jkh-7:~# docker version
Client:
 Version:         1.13.1
 API version:     1.26
 Package version: docker-1.13.1-88.git07f3374.el7.centos.x86_64
 Go version:      go1.9.4
 Git commit:      07f3374/1.13.1
 Built:           Fri Dec  7 16:13:51 2018
 OS/Arch:         linux/amd64

Server:
 Version:         1.13.1
 API version:     1.26 (minimum version 1.12)
 Package version: docker-1.13.1-88.git07f3374.el7.centos.x86_64
 Go version:      go1.9.4
 Git commit:      07f3374/1.13.1
 Built:           Fri Dec  7 16:13:51 2018
 OS/Arch:         linux/amd64
 Experimental:    false
[desktop] 13:04 root@jkh-7:~#
```

### Installing

A step by step explanation of how to download, compile, and run this example.

Clone the GIT repository

```
# git clone https://github.com/jkhorvath/OMDb
```

Change directory into the project

```
# cd OMDb
```

Build the project and create runnable Java JAR file

```
# mvn clean package
```

Test the output of the build

```
# java -jar target/OMDb-1.0.1-jar-with-dependencies.jar batman
```

Create a docker image to hold this application. Remember that this is just an example of how to package an application into a Docker image. Docker images should really hold services that are meant to be distributed across a network for scalability and availability. Applications that use machine type of interfaces (like REST) are typically the types of application that become distributed and scaled service aross a network using Docker. This application is just a Docker example.

```
# docker build -t omdb .
```

Run the docker image once telling the docker image to execute the sample using the command line arguments given.

```
# docker run omdb /home/OMDB/run.sh batman
```

Or, run the docker image once telling the docker image to execute the sample using the raw `java` command using the command line arguments given.

```
# docker run omdb java -jar /home/OMDB/OMDb.jar batman
```

Run the docker image as an interactive shell so that you can re-attach to the container.
Note that the default logging level is set to `warn`. Other logging levels can be specified on the command line as in the example command invocations below.
Notice that the persistent logs for the application are stored in the `logs` subdirectory. The logs contained within the `omdb.log` file will also contain additional logging information (such as timestamp, code location, etc.). The log file `omdb.log` is appended to until it get larger than 50 MB or older than one day. After that it gets rolled into a timestamped ZIP file.

```
# docker run -it omdb /bin/sh
/ # cd /home/OMDB/
/home/OMDB # ls -lsF
total 3384
  3380 -rw-r--r--    1 root     root       3460393 Dec 14 17:20 OMDb.jar
     4 -rwxr-xr-x    1 root     root           115 Dec 14 17:24 run.sh*
/home/OMDB # java -jar OMDb.jar batman
Movie title "Batman" has rating of 72% from "Rotten Tomatoes"
/home/OMDB # LOG_LEVEL=error java -jar OMDb.jar batman
Movie title "Batman" has rating of 72% from "Rotten Tomatoes"
/home/OMDB # LOG_LEVEL=info java -jar OMDb.jar batman
Loading properties from file: omdb.properties
Module Resource file URL: jar:file:/home/OMDB/OMDb.jar!/omdb.properties
#
# Begin properties load from class: com.cisco.omdb.OMDBProperties
#
# Sorted properties from class: com.cisco.omdb.OMDBProperties on Fri Dec 14 20:52:23 GMT 2018
#Fri Dec 14 20:52:23 GMT 2018
com.cisco.omdb.key.api=bde60be5
#
# End properties from class: com.cisco.omdb.OMDBProperties
#
Located Title: "Batman", Year: "1989", Rated: "PG-13", Released: "23 Jun 1989", Runtime: "126 min", Genre: "Action, Adventure", Director: "Tim Burton", Writer: "Bob Kane (Batman characters), Sam Hamm (story), Sam Hamm (screenplay), Warren Skaaren (screenplay)", Actors: "Michael Keaton, Jack Nicholson, Kim Basinger, Robert Wuhl", Plot: "The Dark Knight of Gotham City begins his war on crime with his first major enemy being the clownishly homicidal Joker.", Language: "English, French, Spanish", Country: "USA, UK", Awards: "Won 1 Oscar. Another 8 wins & 26 nominations.", Poster: "https://m.media-amazon.com/images/M/MV5BMTYwNjAyODIyMF5BMl5BanBnXkFtZTYwNDMwMDk2._V1_SX300.jpg", Metascore: "69", imdbRating: "7.5", imdbVotes: "305,095", imdbID: "tt0096895", Type: "movie", DVD: "25 Mar 1997", BoxOffice: "N/A", Production: "Warner Bros. Pictures", Website: "N/A", Response: "True"
Movie rating source (Internet Movie Database) of "7.5/10" for Movie Title: Batman is not the desired rating source (Rotten Tomatoes)
Movie title "Batman" has rating of 72% from "Rotten Tomatoes"
Movie title "Batman" has rating of 72% from "Rotten Tomatoes"
Movie rating source (Metacritic) of "69/100" for Movie Title: Batman is not the desired rating source (Rotten Tomatoes)
/home/OMDB # ls -lsF
total 3388
  3380 -rw-r--r--    1 root     root       3460393 Dec 14 17:20 OMDb.jar
     4 drwxr-xr-x    3 root     root          4096 Dec 14 19:35 logs/
     4 -rwxr-xr-x    1 root     root           115 Dec 14 17:24 run.sh*
/home/OMDB # ls -lsF logs
total 16
     4 drwxr-xr-x    2 root     root          4096 Dec 14 19:35 2018-12-14/
    12 -rw-r--r--    1 root     root         10065 Dec 14 19:35 omdb.log
/home/OMDB #
```

## Running sample program

The sample application has a few options that you can use to tailor the search and result. The options can be viewed by executing the application without any arguments.

```
/home/OMDB # java -jar OMDb.jar
usage: java com.cisco.omdb.Main [-p short|full] [-t movie|series|episode] <movie-name> [movie-year]
/home/OMDB #
```

The `-p` option is used to tell OMDb whether to return the short or full plot of the movie.

The `-t` option is used to tell OMDb if you are specifying a movie, a series, or an episode.

After the movie name is given, an additional argument can be passed to denote the year the movie was released.


## Cleanup

Note that after running this sample application, you will have a few docker images and runnable containers left. You can see these with the following commands.

```
# [desktop] 15:48 root@jkh-7:~# docker ps -a
CONTAINER ID        IMAGE               COMMAND                  CREATED             STATUS                     PORTS               NAMES
22e5fa776430        omdb                "/bin/sh"                9 minutes ago       Exited (0) 6 seconds ago                       zen_chandrasekhar
bec0ac354021        omdb                "java -jar /home/O..."   9 minutes ago       Exited (0) 9 minutes ago                       trusting_lalande
51ce8072aba0        omdb                "java -jar /home/O..."   9 minutes ago       Exited (0) 9 minutes ago                       eloquent_darwin
dc7dd79d6036        omdb                "/home/OMDB/run.sh..."   9 minutes ago       Exited (0) 9 minutes ago                       objective_goldberg
[desktop] 15:48 root@jkh-7:~# docker image ls
REPOSITORY          TAG                 IMAGE ID            CREATED             SIZE
omdb                latest              a79347d3f4e5        10 minutes ago      86.5 MB
docker.io/openjdk   8-jre-alpine        2e01f547f003        7 weeks ago         83 MB
[desktop] 15:49 root@jkh-7:~#
```

If all of these are exited, then you can clean these up by using the following sample commands.

```
[desktop] 15:52 root@jkh-7:~# docker container rm 22e5fa776430 bec0ac354021 51ce8072aba0 dc7dd79d6036
22e5fa776430
bec0ac354021
51ce8072aba0
dc7dd79d6036
[desktop] 15:53 root@jkh-7:~# docker image rm a79347d3f4e5 2e01f547f003
Untagged: omdb:latest
Deleted: sha256:a79347d3f4e520040d0dbc7529b042e2a59b282eac6165327b85ae0410bd81e9
Deleted: sha256:981f457819cdaf38c04a29ba8608b35e1f5d4f8004a2d80e0381075ebf7115b4
Deleted: sha256:e37de409a9228fde0081fc2ff33997f6d4a3e5d81549f9080f8b8d98cdbbacdb
Deleted: sha256:2b974e77039dffd5a3a982adc6c8fb8c838ca17ef427c745e569b9de31c534fb
Deleted: sha256:2bd2816ba5050a30519010c1c15fb7e4179a4ec63a249b46130fdb5ac8084152
Untagged: docker.io/openjdk:8-jre-alpine
Untagged: docker.io/openjdk@sha256:e8a689c4b2913f07e401e5e9325d66cecc33d30738aadf1dbe3db5af70997742
Deleted: sha256:2e01f547f00384dbab90a4dd9bde29caf862a89c2356f58aeed38fa7b486c575
Deleted: sha256:5b98423ee68b954601a93be494cbf128034a3db73e9bba86dfbccb3487ca3758
Deleted: sha256:577174891c779d8b287b2132bfc1335f435f6b167716fdc410c6645a37b37ab2
Deleted: sha256:df64d3292fd6194b7865d7326af5255db6d81e9df29f48adde61a918fbd8c332
[desktop] 15:53 root@jkh-7:~# 
```

## Authors

* **Joe Horvath** - *Initial work* - (joshorva@cisco.com)


## Licenses

* Please note that the OMDb lookup service uses my personal (and free) OMDb key (contained within the omdb.properties file).


## Acknowledgments

* This sample application only uses publicly available 3rd party libraries available on most Maven repositories.
