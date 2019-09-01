# sample-openlayer

OpenLayer + Tomcat + Jersey + Guice

##Crear un war

> mvn -e war:war

##Debug Integration Tests

> mvn -e -Dmaven.failsafe.debug="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=8000 -Xnoagent -Djava.compiler=NONE" verify

##Debug Cargo

> mvn -e org.codehaus.cargo:cargo-maven2-plugin:run

> mvn -e -Dcargo.start.jvmargs="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=8000 -Xnoagent -Djava.compiler=NONE" cargo:run


##Grenerating archetypes

> mvn -e archetype:generate -DarchetypeGroupId=org.codehaus.cargo -DarchetypeArtifactId=cargo-archetype-webapp-single-module -DarchetypeVersion=1.7.7

> mvn -e archetype:generate -DarchetypeGroupId=org.codehaus.cargo -DarchetypeArtifactId=cargo-archetype-webapp-with-datasource -DarchetypeVersion=1.7.7

