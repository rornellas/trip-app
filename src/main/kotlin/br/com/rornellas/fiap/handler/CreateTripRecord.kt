package br.com.rornellas.fiap.handler

import br.com.rornellas.fiap.dao.TripRepository
import br.com.rornellas.fiap.model.HandlerRequest
import br.com.rornellas.fiap.model.HandlerResponse
import br.com.rornellas.fiap.model.Trip
import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler
import com.fasterxml.jackson.databind.ObjectMapper
import java.io.IOException

class CreateTripRecord :
    RequestHandler<HandlerRequest, HandlerResponse> {
    private val repository: TripRepository = TripRepository()
    override fun handleRequest(
        request: HandlerRequest,
        context: Context
    ): HandlerResponse {
        var trip: Trip? = null
        try {
            trip = ObjectMapper().readValue(request.body, Trip::class.java)
        } catch (e: IOException) {
            return HandlerResponse.builder().setStatusCode(400).setRawBody("Your Trip has invalid data/format!").build()
        }
//        context.logger.log("Creating a new trip record for the topic " + trip.topic)
        val tripRecorded: Trip? = repository.save(trip)
        return HandlerResponse.builder().setStatusCode(201).setObjectBody(tripRecorded).build()
    }
}