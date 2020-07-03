package br.com.rornellas.fiap.dao

import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper

object DynamoDBManager {
    private var mapper: DynamoDBMapper? = null

    init {
        var ddb: AmazonDynamoDB? = null
        val endpoint = System.getenv("ENDPOINT_OVERRIDE")

        ddb = if (endpoint != null && !endpoint.isEmpty()) {
            val endpointConfiguration = EndpointConfiguration(endpoint, "us-east-1")
            AmazonDynamoDBClientBuilder.standard().withEndpointConfiguration(endpointConfiguration).build()
        } else {
            AmazonDynamoDBClientBuilder.defaultClient()
        }

        mapper = DynamoDBMapper(ddb)
    }

    fun mapper(): DynamoDBMapper? {
        return DynamoDBManager.mapper
    }
}