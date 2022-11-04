## API Spec
Get a random number, and if requested a reCAPTCHA Enterprise score

**URL** : `/api`

**Method** : `POST`

**Auth required** : YES

**Auth Method**: HTTP Header

**Auth value**: `X-API-KEY: KEY_VALUE`

## Success Response

**Code** : `200 OK`

**Content examples**

For a random number with no reCAPTCHA Enterprise score, use VALUE `web`, `android`, or `ios`.

```json
{
    "type": "VALUE"
}
```

For a random number with a reCAPTCHA Enterprise score, add a reCAPTCHA Enterprise token as TOKEN.

```json
{
    "type": "VALUE",
    "recapToken": "TOKEN"
}
```
