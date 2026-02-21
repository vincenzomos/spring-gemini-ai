# setting your project
gcloud config set project ai-mcp-vmos

# Authentication via google cloud cli
gcloud auth login

#select the account for this project

    https://accounts.google.com/o/oauth2/auth?response_type=code&client_id=32555940559.apps.googleusercontent.com&redirect_uri=http%3A%2F%2Flocalhost%3A8085%2F&scope=openid+https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.email+https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fcloud-platform+https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fappengine.admin+https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fsqlservice.login+https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fcompute+https%3A%2F%2Fwww.googleapis.com%2Fauth%2Faccounts.reauth&state=xb1S2PLhZvBNCv6pZWYMKuPf62OmdB&access_type=offline&code_challenge=9orNLQYobx7lQ43xZRHpFSP7m9zm16qdwyAmAOSyqQ4&code_challenge_method=S256

You are now logged in as [vmos@qualogy.com].
Your current project is [ai-mcp-vmos].  You can change this setting by running:
$ gcloud config set project PROJECT_ID

Credentials saved to file: [C:\Users\vmos\AppData\Roaming\gcloud\application_default_credentials.json]

These credentials will be used by any library that requests Application Default Credentials (ADC).

Quota project "ai-mcp-vmos" was added to ADC which can be used by Google client libraries for billing and quota. Note that some services may still bill the project owning the resource.

## Demos

1. Showing Code in Intellij

2. Showing the MCP Inspector 
 Start
 npx @modelcontextprotocol/inspector
 
URL: http://localhost:8085/mcp
3. startup sequence application spring AI 
   - Start Docker desktop
   - in git bash folder of spring gemini : 
      docker compose -f docker-compose-postgres.yaml up
   - in intellij run the applications:
     
Spotify device ID: "id": "c9873d1378f7429a8e16b2cc20690193ad6694f8",

4. Eliciation demo : 
  Only in inspector 
  npx @modelcontextprotocol/inspector
  URL : http://localhost:8080/mcp

5. MCP Authentication demo :
   sdk use java 25.0.1-amzn 
    