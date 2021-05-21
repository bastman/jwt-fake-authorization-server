# jwt-fake-authorization-server

- authorization server: https://github.com/bastman/jwt-fake-authorization-server
- resource server: https://github.com/bastman/spring-jwt-resource-server-example

## docker

```
# build image
$ ./gradlew bootBuildImage

# run 
$ docker-compose up


```

# endpoints

- swagger-ui: http://localhost:9000/swagger-ui.html
  
- GET /.well-known/jwks.json
    - expose public rsa key
- POST /oauth/sign-token 
    - to sign any jwt payload
- POST /oauth/generate-rsa-keys
    - generate rsa key pair (public, private)
    

# use case: sign an arbitrary jwt payload

```
jwt payload to be signed ...

{
  "https://example.com/claims/family_name": "my-family-name",
  "sub": "my-issuer-1|my-user-id-1",
  "aud": [
    "my-audience-1",
    "my-audience-2"
  ],
  "https://app.example.com/roles": [
    "my-role-1",
    "my-role-2"
  ],
  "scope": "openid profile email",
  "iss": "https://my-issuer-1.local",
  "https://example.com/claims/given_name": "my-given-name",
  "https://example.com/claims/userid": "my-issuer-1|my-user-id-1",
  "exp": 1936954919,
  "iat": 1604327123,
  "https://example.com/claims/email": "my.email@example.com"
}


$ curl -v -X POST "http://localhost:9000/oauth/sign-token" -H "accept: */*" -H "Content-Type: application/json" -d "{ \"https://example.com/claims/family_name\": \"my-family-name\", \"sub\": \"my-issuer-1|my-user-id-1\", \"aud\": [ \"my-audience-1\", \"my-audience-2\" ], \"https://app.example.com/roles\": [ \"my-role-1\", \"my-role-2\" ], \"scope\": \"openid profile email\", \"iss\": \"https://my-issuer-1.local\", \"https://example.com/claims/given_name\": \"my-given-name\", \"https://example.com/claims/userid\": \"my-issuer-1|my-user-id-1\", \"exp\": 1936954919, \"iat\": 1604327123, \"https://example.com/claims/email\": \"my.email@example.com\"}"

response:

{
  "token": "Bearer eyJraWQiOiJhNjYwNThiNC00MDY0LTRlZjYtOGMwYi05OGZjZDA3MWIyZWMiLCJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJodHRwczpcL1wvZXhhbXBsZS5jb21cL2NsYWltc1wvZmFtaWx5X25hbWUiOiJteS1mYW1pbHktbmFtZSIsInN1YiI6Im15LWlzc3Vlci0xfG15LXVzZXItaWQtMSIsImF1ZCI6WyJteS1hdWRpZW5jZS0xIiwibXktYXVkaWVuY2UtMiJdLCJodHRwczpcL1wvYXBwLmV4YW1wbGUuY29tXC9yb2xlcyI6WyJteS1yb2xlLTEiLCJteS1yb2xlLTIiXSwic2NvcGUiOiJvcGVuaWQgcHJvZmlsZSBlbWFpbCIsImlzcyI6Imh0dHBzOlwvXC9teS1pc3N1ZXItMS5sb2NhbCIsImh0dHBzOlwvXC9leGFtcGxlLmNvbVwvY2xhaW1zXC9naXZlbl9uYW1lIjoibXktZ2l2ZW4tbmFtZSIsImh0dHBzOlwvXC9leGFtcGxlLmNvbVwvY2xhaW1zXC91c2VyaWQiOiJteS1pc3N1ZXItMXxteS11c2VyLWlkLTEiLCJleHAiOjE5MzY5NjM5NzksImlhdCI6MTYwNDMyNzEyMywiaHR0cHM6XC9cL2V4YW1wbGUuY29tXC9jbGFpbXNcL2VtYWlsIjoibXkuZW1haWxAZXhhbXBsZS5jb20ifQ.UtdlTNRKAYVtwiJa1WjWNqR5WS1S-pujyxXa3OgoAGiBNdMoWUWk70gH9pN57gxWctmvIJlpDmooKvrEfjVPHGUp_ulQa0UbqLwvjcc1V9CkYkkb3_e2RXc9dCkImHzfktxldyDLI2std1gAGOOezNuOUkuOJwA3rj06QllOynheiY4CS_wep-9D4no1CW3wFkrPnpFyaThsi_ZJGC0KXmMWYXu3Ay4kWxje7FBHVo23miQ5UimqlKaiizZGbkjYQRK8dYLYR5PG0HEDJ7KGxsQYHN7MfbrNucgmkg2NhT6npeOyY4e0Cj8YegGtQhvI2CTP_t6GntVWtP3RPD0V9g"
}

```

  





