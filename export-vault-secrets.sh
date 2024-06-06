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

if test -f /var/run/secrets/nais.io/foerstesidegeneratordb_creds/username;
then
    echo "Setting SPRING_DATASOURCE_USERNAME"
    export SPRING_DATASOURCE_USERNAME=$(cat /var/run/secrets/nais.io/foerstesidegeneratordb_creds/username)
fi

if test -f /var/run/secrets/nais.io/foerstesidegeneratordb_creds/password;
then
    echo "Setting SPRING_DATASOURCE_PASSWORD"
    export SPRING_DATASOURCE_PASSWORD=$(cat /var/run/secrets/nais.io/foerstesidegeneratordb_creds/password)
fi

if test -f /var/run/secrets/nais.io/foerstesidegeneratordb_config/jdbc_url;
then
    export SPRING_DATASOURCE_URL=$(cat /var/run/secrets/nais.io/foerstesidegeneratordb_config/jdbc_url)
    echo "Setting SPRING_DATASOURCE_URL=$SPRING_DATASOURCE_URL"
fi

if test -f /var/run/secrets/nais.io/foerstesidegeneratordb_config/ons_host;
then
    export DATABASE_ONSHOSTS=$(cat /var/run/secrets/nais.io/foerstesidegeneratordb_config/ons_host)
    echo "Setting DATABASE_ONSHOSTS=$DATABASE_ONSHOSTS"
fi

echo "Exporting appdynamics environment variables"
if test -f /var/run/secrets/nais.io/appdynamics/appdynamics.env;
then
    export $(cat /var/run/secrets/nais.io/appdynamics/appdynamics.env)
    echo "Appdynamics environment variables exported"
else
    echo "No such file or directory found at /var/run/secrets/nais.io/appdynamics/appdynamics.env"
fi
