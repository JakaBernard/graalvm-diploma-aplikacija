#!/bin/bash
mvn clean package
native-image --language:js --language:R -H:ReflectionConfigurationFiles=reflectionConfig.json --report-unsupported-elements-at-runtime -cp api/target/classes:api/target/dependency/* com.kumuluz.ee.EeApplication apiExecutable
# native-image -cp entities/target/classes:entities/target/dependency/* com.kumuluz.ee.EeApplication entitiesExecutable
# native-image -cp services/target/classes:services/target/dependency/* com.kumuluz.ee.EeApplication servicesExecutable
