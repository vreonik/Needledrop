NeedleDrop API Documentation

Base URL:
`http://localhost:8080/api`

Authentication Endpoints"

Register User:
- URL: `/auth/register`
- Method: POST
- Body:
```json
{
    "username": "string (3-20 chars)",
    "password": "string (min 6 chars)"
}

Login:
-URL: /auth/login
-Method: POST
-Body:
```json
{
    "username": "string",
    "password": "string"
}

Album Endpoints (Require JWT Token)
Get All Albums
URL: /albums

Method: GET

Headers: Authorization: Bearer {token}

Get Album by ID
URL: /albums/{id}

Method: GET

Create Album
URL: /albums

Method: POST

Body:

json
{
    "title": "string",
    "artist": "string",
    "releaseDate": "yyyy-mm-dd"
}
Update Album
URL: /albums/{id}

Method: PUT

Body: Same as Create

Delete Album
URL: /albums/{id}

Method: DELETE

Song Endpoints (Require JWT Token)
Get Songs by Album
URL: /albums/{albumId}/songs

Method: GET

Create Song
URL: /albums/{albumId}/songs

Method: POST

Body:

json
{
    "title": "string",
    "duration": "number (seconds)",
    "trackNumber": "number"
}
