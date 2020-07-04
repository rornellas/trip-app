package br.com.rornellas.fiap.model

import com.amazonaws.services.dynamodbv2.datamodeling.*

@DynamoDBTable(tableName = "trip")
data class Trip(
    @DynamoDBHashKey(attributeName = "country")
    var country: String? = null,
    @DynamoDBRangeKey(attributeName = "date")
    var date: String? = null,
    @DynamoDBIndexRangeKey(attributeName = "city", localSecondaryIndexName = "cityIndex")
    var city: String? = null,
    @DynamoDBAttribute(attributeName = "reason")
    var reason: String? = null
)