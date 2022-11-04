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
<img src="/res/disabled-drop-down.png?raw=true" width="200">

Create a web, Android App, and iOS App site key and record the values here.

```json
"site-keys": { 
        "description":"reCAPTCHA Enterprise site keys",
        "web":{
            "description":"Website version 3 site key",
            "value":""
        },
        "android":{
            "description":"Android site key",
            "value":""
        },
        "ios":{
            "description":"iOS site key",
            "value":""
        }
    }
```

### Project Info
The reCAPTCHA Enterprise call from the Docker container to Google Cloud needs to know details about your project, enter the project-number and project name.

```json
"project-info":{
        "description":"Variables for the Project",
        "project-number":"",
        "project-id":""
    }
```

### Services and APIs
This is the API credentials needed by the Docker container to access the reCAPTCHA Enterprise sevice on Google Cloud. You must create a new credential in `APIs and Services`, and then limit the scope to reCAPTCHA Enterprise. Create this credential and use the API Key value as `api-access-key-for-recaptcha`.
<img src="/res/api-credential-settings.png?raw=true" width="400">

```json
"services-and-apis":{
        "description":"Credentials from the Services and APIs Console",
        "api-access-key-for-recaptcha":""
    }
```

## Starting the container

From inside the directory with the `Dockerfile` file, start the container with command:
```shell
sudo docker build -t rcemobapi . && sudo docker run -p 8080:8080 rcemobapi:latest
```

Navigate to http://localhost:8080 to verify the installation.
