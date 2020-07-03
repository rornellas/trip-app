package br.com.rornellas.fiap.model

data class HandlerRequest (
    var body: String? = null,
    var path: String? = null,
    var pathParameters: Map<String, String>? = null,
    var queryStringParameters: Map<String, String>? = null
)