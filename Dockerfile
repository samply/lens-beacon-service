FROM maven:3.9.5-eclipse-temurin-17 AS build

COPY ./ /workingdir/
WORKDIR /workingdir

RUN mvn clean install

FROM eclipse-temurin:17-focal

COPY --from=build /workingdir/target/*.jar ./lens_beacon_service.jar

CMD java -jar lens_beacon_service.jar

