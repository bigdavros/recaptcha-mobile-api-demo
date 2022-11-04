# recaptcha-mobile-api-demo
This demo will provision a Docker container to respond to requests from a mobile app. It runs as an API that takes the following two inputs:
```
{
  "type":"[REQUEST_TYPE]"
}
```
or with a reCAPTCHA Enterprise Toke:
```
{
  "type":"[REQUEST_TYPE]",
  "recapToken":"[TOKEN_FROM_RECAPTCHA]"
}
```

## Setup
`config.json` is the file that holds the variables.

The `api-keys` section is where the 
`api-keys`

Start container with command:
```
sudo docker build -t rcemobapi . && sudo docker run -p 8080:8080 rcemobapi:latest
```
