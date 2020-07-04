package br.com.rornellas.fiap.dao

import br.com.rornellas.fiap.model.Trip
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression
import com.amazonaws.services.dynamodbv2.model.AttributeValue
import com.amazonaws.services.dynamodbv2.model.ScanRequest

class TripRepository {
    private val mapper = DynamoDBManager.mapper()

    fun save(trip: Trip): Trip? {
        mapper!!.save<Any>(trip)
        return trip
    }

    fun findByPeriod(
        starts: String?,
        ends: String?
    ): List<Trip?>? {
        val eav: MutableMap<String, AttributeValue> =
            HashMap()
        eav[":val2"] = AttributeValue().withS(starts)
        eav[":val3"] = AttributeValue().withS(ends)

        val expression: MutableMap<String, String> =
            java.util.HashMap()

        // date is a reserved word in DynamoDB
        expression["#date"] = "date"

        val queryExpression: DynamoDBScanExpression = DynamoDBScanExpression()
            .withFilterExpression("#date between :val2 and :val3")
            .withExpressionAttributeValues(eav).withExpressionAttributeNames(expression)

        return mapper!!.scan(Trip::class.java, queryExpression)
    }

    fun findByCity(country: String?, city: String?): List<Trip?>? {
        val eav: MutableMap<String, AttributeValue> =
            HashMap()
        eav[":val1"] = AttributeValue().withS(country)
        eav[":val2"] = AttributeValue().withS(city)
        val queryExpression: DynamoDBQueryExpression<Trip> = DynamoDBQueryExpression<Trip>()
            .withKeyConditionExpression("country = :val1")
            .withFilterExpression("contains(city, :val2)")
            .withExpressionAttributeValues(eav)
        return mapper!!.query(Trip::class.java, queryExpression)
    }

    fun findByCountry(country: String?): List<Trip?>? {
        val eav: MutableMap<String, AttributeValue> =
            HashMap()
        eav[":val1"] = AttributeValue().withS(country)

        val queryExpression: DynamoDBQueryExpression<Trip> = DynamoDBQueryExpression<Trip>()
            .withKeyConditionExpression("country = :val1").withExpressionAttributeValues(eav)
        return mapper!!.query(Trip::class.java, queryExpression)
    }
}