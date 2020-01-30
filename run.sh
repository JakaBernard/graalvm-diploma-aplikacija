#!/bin/bash

# Zaganjanje z Graal
# /usr/bin/time -v java -cp api/target/classes:api/target/dependency/* com.kumuluz.ee.EeApplication

# Zaganjanje z Javo
# /usr/bin/time -v java -XX:-UseJVMCICompiler -cp api/target/classes:api/target/dependency/* com.kumuluz.ee.EeApplication

# Zaganjanje kot native image
/usr/bin/time -v ./apiExecutable
