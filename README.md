# recaptcha-mobile-api-demo
This demo will provision a project with the following resources:
 - App Engine
 - reCAPTCHA Enterprise Mobile keys

## Required setup steps
1) Have an exisiting project in Google Cloud, or create a new project with:
`gcloud projects create $PROJECT_ID`
2) Create an service account with the permissions contained in permissions.json
3) Contact reCAPTCHA Enterprise sales to enable Mobile reCAPTCHA Enterprise site keys
4) Use service account to run the setup.sh script
