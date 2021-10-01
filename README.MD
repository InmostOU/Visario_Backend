# BACKEND SERVICES FOR THE VISARIO PROJECT 

## About Visario

Visario — is an open-source project, that uses [Amazon Chime API](https://docs.aws.amazon.com/chime/index.html/) and aimed 
to develop messaging and meeting features.

The Visario project is an example of the implementation of a communication platform based on Amazon Chime SDK by the engineers of the Inmost team. 
AWS microservices make it easy to create chats, appointments, calendars, and other functions within an existing client app or website. 
The received communication features are distinguished by the high quality of communication, data transmission security, and minimal implementation time.

## To run these you should:

In application.yml files, for each service, put your credentials for AWS account (IAM role), AWS Chime, database, etc. 
Also, if you don't want your config files to be hosted by Spring Cloud Config server, you must delete spring-cloud-config dependency
from all pom.xml.

