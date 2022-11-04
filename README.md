# recaptcha-mobile-api-demo
This demo will provision a Docker container to respond to requests from a mobile app.

`config.json` is the file that holds the variables.

Start container with command:
```
sudo docker build -t rcemobapi . && sudo docker run -p 8080:8080 rcemobapi:latest
```
