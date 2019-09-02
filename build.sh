#!/bin/bash
mvn clean package
native-image -cp api/target/classes:api/target/dependency/* com.kumuluz.ee.EeApplication apiExecutable
# native-image -cp entities/target/classes:entities/target/dependency/* com.kumuluz.ee.EeApplication entitiesExecutable
# native-image -cp services/target/classes:services/target/dependency/* com.kumuluz.ee.EeApplication servicesExecutable
