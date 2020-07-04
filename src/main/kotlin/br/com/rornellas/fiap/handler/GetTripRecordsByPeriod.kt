package br.com.rornellas.fiap.handler

import br.com.rornellas.fiap.dao.TripRepository
import br.com.rornellas.fiap.model.HandlerRequest
import br.com.rornellas.fiap.model.HandlerResponse
import br.com.rornellas.fiap.model.Trip
import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler

class GetTripRecordsByPeriod :
    RequestHandler<HandlerRequest, HandlerResponse> {
    private val repository: TripRepository = TripRepository()
    override fun handleRequest(
        request: HandlerRequest,
        context: Context
    ): HandlerResponse {
        val starts: String = request.queryStringParameters?.get("starts")!!
        val ends: String = request.queryStringParameters?.get("ends")!!
        context.logger
            .log("Searching for registered trips between $starts and $ends")
        val studies: List<Trip?>? = repository.findByPeriod(starts, ends)

        return HandlerResponse.builder().setStatusCode(200).setObjectBody(studies).build()
    }
}