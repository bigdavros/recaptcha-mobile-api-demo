# recaptcha-mobile-api-demo
This demo will provision a Docker container to respond to requests from a mobile app. It runs as an API using the following spec:
* [API Spec](api.md) 

## Setup
`config.json` is the file that holds the variables.

### API Keys
The `api-keys` section is where the X-API-KEY header values that are used between the client and this docker container are stored.
```json
"api-keys":{
        "description":"API keys for use by each platform accessing this API. Set custom-api-keys to false to automatically generate API keys.",
        "use-custom-api-keys":true,
        "custom-api-keys":{
            "web":"",
            "android":"",
            "ios":""
        }        
    }
```
If you want the Docker container to create API keys for you, switch off `use-custom-api-keys` by setting it to false. The container will then create new API Keys for use. Otherwise set the values of web, android, and iOS to the X-API-KEY value you want to send from your application to the container.

### Site Keys
These are the site keys from reCAPTCHA Enterprise. You will need these from the [reCAPTCHA Enterprise Console](https://console.cloud.google.com/recaptcha). 

If the key types shown below are not available and greyed out then please contact your Google Cloud Sales representitive:
![Disabled Options](/res/disabled-drop-down.png?raw=true "Dropdown-list" =250x)

Start container with command:
```
sudo docker build -t rcemobapi . && sudo docker run -p 8080:8080 rcemobapi:latest
```
