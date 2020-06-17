#!/usr/bin/env sh

if test -f /var/run/secrets/nais.io/srvfoerstesidegenerator/username;
then
    echo "Setting SERVICEUSER_USERNAME"
    export SERVICEUSER_USERNAME=$(cat /var/run/secrets/nais.io/srvfoerstesidegenerator/username)
fi

if test -f /var/run/secrets/nais.io/srvfoerstesidegenerator/password;
then
    echo "Setting SERVICEUSER_PASSWORD"
    export SERVICEUSER_PASSWORD=$(cat /var/run/secrets/nais.io/srvfoerstesidegenerator/password)
fi

if test -f /var/run/secrets/nais.io/foerstesidegeneratorDB/username;
then
    echo "Setting SPRING_DATASOURCE_USERNAME"
    export SPRING_DATASOURCE_USERNAME=$(cat /var/run/secrets/nais.io/foerstesidegeneratorDB/username)
fi

if test -f /var/run/secrets/nais.io/foerstesidegeneratorDB/password;
then
    echo "Setting SPRING_DATASOURCE_PASSWORD"
    export SPRING_DATASOURCE_PASSWORD=$(cat /var/run/secrets/nais.io/foerstesidegeneratorDB/password)
fi

