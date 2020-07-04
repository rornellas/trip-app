## Stack
* Kotlin, AWS Sam, Dynamodb, Lambda, Serverless

## AWS SAM Application for Managing Trip

This is a sample application to demonstrate how to build an application on AWS Serverless Envinronment using the
AWS SAM, Amazon API Gateway, AWS Lambda and Amazon DynamoDB.
It also uses the DynamoDBMapper ORM structure to map Trip items in a DynamoDB table to a RESTful API for managing Studies.

## Requirements

* AWS CLI already configured with at least PowerUser permission
* [Java SE Development Kit 8 installed](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
* [Docker installed](https://www.docker.com/community-edition)
* [Maven](https://maven.apache.org/install.html)
* [SAM CLI](https://github.com/awslabs/aws-sam-cli)
* [Python 3](https://docs.python.org/3/)

## Setup process

### Installing dependencies

We use `maven` to install our dependencies and package our application into a JAR file:

```bash
mvn install
```

### Local development

**Invoking function locally through local API Gateway**
1. Start DynamoDB Local in a Docker container using docker-compose at the root of the project. You can also run `docker run -p 8000:8000 -v $(pwd)/local/dynamodb:/data/ amazon/dynamodb-local -jar DynamoDBLocal.jar -sharedDb -dbPath /data`
2. Create the DynamoDB table. `aws dynamodb create-table --table-name trip --attribute-definitions AttributeName=country,AttributeType=S AttributeName=date,AttributeType=S AttributeName=city,AttributeType=S --key-schema AttributeName=country,KeyType=HASH AttributeName=date,KeyType=RANGE --local-secondary-indexes 'IndexName=cityIndex,KeySchema=[{AttributeName=country,KeyType=HASH},{AttributeName=city,KeyType=RANGE}],Projection={ProjectionType=ALL}' --billing-mode PAY_PER_REQUEST --endpoint-url http://localhost:8000`

If the table already exist, you can delete: `aws dynamodb delete-table --table-name trip --endpoint-url http://localhost:8000`

3. Start the SAM local API.
 - On a Mac: `sam local start-api --env-vars src/test/resources/test_environment_mac.json`
 - On Windows: `sam local start-api --env-vars src/test/resources/test_environment_windows.json`
 - On Linux: `sam local start-api --env-vars src/test/resources/test_environment_linux.json`

* If you have any issues using 'localhost', try using your network ip instead

* You may need to change host and port according to your configurations

If the previous command ran successfully you should now be able to hit the following local endpoint to
invoke the functions rooted at `http://localhost:3000/trips/?starts=2020-01-02&ends=2020-02-02`.

It shoud return empty list. You can try to start posting trips to `http://localhost:3000/trips` using the example json below:

{
	"country": "Brazil",
	"city": "Salvador",
	"date": "2020-07-03",
	"reason": "Cidade do axé!"
}

**SAM CLI** is used to emulate both Lambda and API Gateway locally and uses our `template.yaml` to
understand how to bootstrap this environment (runtime, where the source code is, etc.) - The
following excerpt is what the CLI will read in order to initialize an API and its routes:

## Packaging and deployment

AWS Lambda Java runtime accepts either a zip file or a standalone JAR file - We use the latter in
this example. SAM will use `CodeUri` property to know where to look up for both application and
dependencies:

Firstly, we need a `S3 bucket` where we can upload our Lambda functions packaged as ZIP before we
deploy anything - If you don't have a S3 bucket to store code artifacts then this is a good time to
create one:
* After created, you must to change s3 host address at file "packaged.yaml" in order to deploy to your own s3 bucket

- Windows (cmd)
```
set BUCKET_NAME="my_cool_new_bucket"
aws s3 mb s3://%BUCKET_NAME%

``` 

- Linux
```
export BUCKET_NAME=my_cool_new_bucket
aws s3 mb s3://$BUCKET_NAME
```

Next, from the project root directory, run the following command to package our Lambda function to S3:

- Windows (cmd)
```
sam package --template-file template.yaml --output-template-file packaged.yaml --s3-bucket %BUCKET_NAME%
```

- Linux
```
sam package \
    --template-file template.yaml \
    --output-template-file packaged.yaml \
    --s3-bucket $BUCKET_NAME
```

Next, the following command will create a Cloudformation Stack and deploy your SAM resources.

```
sam deploy --template-file packaged.yaml --stack-name trip-app --capabilities CAPABILITY_IAM
```

> **See [Serverless Application Model (SAM) HOWTO Guide](https://github.com/awslabs/serverless-application-model/blob/master/HOWTO.md) for more details in how to get started.**

After deployment is complete you can run the following command to retrieve the API Gateway Endpoint URL:

```
aws cloudformation describe-stacks --stack-name trip-app --query 'Stacks[].Outputs'
```

# Appendix

## AWS CLI commands

AWS CLI commands to package, deploy and describe outputs defined within the cloudformation stack:

```bash
sam package \
    --template-file template.yaml \
    --output-template-file packaged.yaml \
    --s3-bucket REPLACE_THIS_WITH_YOUR_S3_BUCKET_NAME

sam deploy \
    --template-file packaged.yaml \
    --stack-name sam-orderHandler \
    --capabilities CAPABILITY_IAM \
    --parameter-overrides MyParameterSample=MySampleValue

aws cloudformation describe-stacks \
    --stack-name sam-orderHandler --query 'Stacks[].Outputs'
```

Now you can start using your trip app at the cloud!
