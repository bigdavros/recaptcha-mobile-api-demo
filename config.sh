echo "#!/usr/bin/env bash" > /newcatalina.sh

export CUSTOMKEYS=$(cat config.json | jq '."api-keys" ."use-custom-api-keys"')
if [ "$CUSTOMKEYS" = true ]; then
    echo "Use custom API keys"
    cat config.json | jq '."api-keys" ."custom-api-keys" .web' | awk '{print "export APPXAPIKEYWEB="$0}'>> /newcatalina.sh
    cat config.json | jq '."api-keys" ."custom-api-keys" .android' | awk '{print "export APPXAPIKEYANDROID="$0}'>> /newcatalina.sh
    cat config.json | jq '."api-keys" ."custom-api-keys" .ios' | awk '{print "export APPXAPIKEYIOS="$0}'>> /newcatalina.sh
else
    echo "Generate API keys"
    export WEBRAND=$(cat /dev/urandom | tr -dc '[:graph:]' | fold -w ${1:-40} | head -n 1 | base64)
    export ANDROIDRAND=$(cat /dev/urandom | tr -dc '[:graph:]' | fold -w ${1:-40} | head -n 1 | base64)
    export IOSRAND=$(cat /dev/urandom | tr -dc '[:graph:]' | fold -w ${1:-40} | head -n 1 | base64)
    echo "export APPXAPIKEYWEB=\"$WEBRAND\"" >> /newcatalina.sh
    echo "export APPXAPIKEYANDROID=\"$ANDROIDRAND\"" >> /newcatalina.sh
    echo "export APPXAPIKEYIOS=\"$IOSRAND\"" >> /newcatalina.sh
    echo APPXAPIKEYWEB - $WEBRAND
    echo APPXAPIKEYANDROID - $ANDROIDKEY
    echo APPXAPIKEYIOS - $IOSKEY
fi

cat config.json | jq '."project-info" ."project-number"' | awk '{print "export PROJECTID="$0}'>> newcatalina.sh
cat config.json | jq '."services-and-apis" ."api-access-key-for-recaptcha"' | awk '{print "export RECAPTCHAAPIKEY="$0}'>> /newcatalina.sh
cat config.json | jq '."site-keys" .web .value' | awk '{print "export WEBV3KEY="$0}'>> /newcatalina.sh
cat config.json | jq '."site-keys" .android .value' | awk '{print "export ANDROIDKEY="$0}'>> /newcatalina.sh
cat config.json | jq '."site-keys" .ios .value' | awk '{print "export IOSKEY="$0}'>> /newcatalina.sh
export MYDATE=$(date +"%d-%b-%Y_%H:%M:%S") && echo export LASTBUILD="\"$MYDATE\"" >> /newcatalina.sh
