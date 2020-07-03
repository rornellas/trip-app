package br.com.rornellas.fiap.model

import com.amazonaws.services.dynamodbv2.datamodeling.*

@DynamoDBTable(tableName = "trip")
data class Trip(
    @DynamoDBHashKey(attributeName = "country")
    var country: String? = null,
    @DynamoDBRangeKey(attributeName = "city")
    var city: String? = null,
    @DynamoDBIndexRangeKey(attributeName = "date", localSecondaryIndexName = "dateIndex")
    var date: String? = null,
    @DynamoDBAttribute(attributeName = "reason")
    var reason: String? = null
)