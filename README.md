foerstesidegenerator
================


### Swagger

1. Finn en service-bruker fra denne `NO_NAV_SECURITY_JWT_ISSUER_RESTSTS_ACCEPTED_AUDIENCE` variablen i `nais/<namespace>-config.json` som kan brukes til å lagge `OIDC` token.
2. Bruk service-brukeren for å `autherize` på swagger siden [security-token-service](https://security-token-service.nais.preprod.local/swagger-ui/index.html?configUrl=/api/api-doc/swagger-config#/) for å opprette `OIDC` token.
3. Bruk denne `OIDC` for å `autherize` på swagger for `foerstesidegenerator`. Hvis du får 401 response, prøv å kjøre manuelt mot `postman` etc.