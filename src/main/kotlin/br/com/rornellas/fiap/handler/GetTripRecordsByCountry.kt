package br.com.rornellas.fiap.handler

import br.com.rornellas.fiap.dao.TripRepository
import br.com.rornellas.fiap.model.HandlerRequest
import br.com.rornellas.fiap.model.HandlerResponse
import br.com.rornellas.fiap.model.Trip
import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler

class GetTripRecordsByCountry :
    RequestHandler<HandlerRequest, HandlerResponse> {
    private val repository: TripRepository = TripRepository()
    override fun handleRequest(
        request: HandlerRequest,
        context: Context
    ): HandlerResponse {
        val country: String = request.pathParameters?.get("country")!!

        val city: String? = request.queryStringParameters?.get("city")

        val trips: List<Trip?>?

        trips = if(city == null) {
            context.logger.log("Searching for registered trips for $country")
            repository.findByCountry(country)
        } else {
            context.logger
                .log("Searching for registered trips for $country at $city")
            repository.findByCity(country, city)
        }

        return if (trips == null || trips.isEmpty()) {
            HandlerResponse.builder().setStatusCode(404).build()
        } else HandlerResponse.builder().setStatusCode(200).setObjectBody(trips).build()
    }
}