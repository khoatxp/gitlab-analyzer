# REST API
## Project controller
### Start analyzing projects
#### Request
`/POST /api/v1/projects/analytics?serverUrl${serverUrl}&accessToken=${accessToken}`
 * Request body: expects a JSON array of project IDs
#### Response

    Status: 200 OK

### Get projects
#### Request
`/GET /api/v1/projects/`
#### Response
        [
            {
                "id": 25514,
                "name": "GitLabAnalyzer",
                "nameWithNamespace": "373-2021-1-Eris / GitLabAnalyzer",
                "webUrl": "https://csil-git1.cs.surrey.sfu.ca/",
                "serverUrl": "https://csil-git1.cs.surrey.sfu.ca/373-2021-1-Eris/gitlabanalyzer"
            },
            {
                "id": 24694,
                "name": "ResumeParser",
                "nameWithNamespace": "rthanki / ResumeParser",
                "webUrl": "https://csil-git1.cs.surrey.sfu.ca/",
                "serverUrl": "https://csil-git1.cs.surrey.sfu.ca/rthanki/resumeparser"
            }
        
        ]

## Member controller
### Get members by project ID
#### Request
`/GET /api/v1/members?projectId=${projectId}`
#### Response
        [
            {
                "id": 2076,
                "username": "rthanki",
                "name": "rthanki",
                "accessLevel": 40
            },
            {
                "id": 2907,
                "username": "imorton",
                "name": "imorton",
                "accessLevel": 40
            }
        ]
    