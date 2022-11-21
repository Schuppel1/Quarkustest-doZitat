#!/usr/bin/env bash
mvn -U io.quarkus:quarkus-maven-plugin:2.14.1.Final:create \
        -DprojectGroupId=org.schuppel.quarkus.doZitat \
        -DprojectArtifactId=doZitat \
        -DclassName="org.schuppel.quarkus.doZitat.QuoteResource" \
        -Dpath="/api/quotes" \
        -Dextensions="resteasy-jsonb"
