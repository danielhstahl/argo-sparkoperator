FROM hseeberger/scala-sbt:graalvm-ce-20.0.0-java11_1.3.13_2.11.12 AS build
COPY src src
COPY build.sbt build.sbt
COPY project/plugins.sbt project/plugins.sbt
COPY project/metals.sbt project/metals.sbt
COPY project/build.properties project/build.properties
RUN sbt assembly

FROM gcr.io/spark-operator/spark-py:v2.4.5
COPY --from=build /root/target/scala-2.11/*.jar ./xgb.jar