package br.com.rornellas.fiap.model

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import java.nio.charset.StandardCharsets
import java.util.*

class HandlerResponse(
    val statusCode: Int,
    val body: String?,
    val headers: Map<String, String>,
    // API Gateway expects the property to be called "isBase64Encoded" => isIs
    val isIsBase64Encoded: Boolean
) {

    class Builder {
        private var statusCode = 200
        private var headers =
            emptyMap<String, String>()
        private var rawBody: String? = null
        private var objectBody: Any? = null
        private var binaryBody: ByteArray? = null
        private var base64Encoded = false
        fun setStatusCode(statusCode: Int): Builder {
            this.statusCode = statusCode
            return this
        }

        fun setHeaders(headers: Map<String, String>): Builder {
            this.headers = headers
            return this
        }

        /**
         * Builds the [HandlerResponse] using the passed raw body string.
         */
        fun setRawBody(rawBody: String?): Builder {
            this.rawBody = rawBody
            return this
        }

        /**
         * Builds the [HandlerResponse] using the passed object body
         * converted to JSON.
         */
        fun setObjectBody(objectBody: Any?): Builder {
            this.objectBody = objectBody
            return this
        }

        /**
         * Builds the [HandlerResponse] using the passed binary body
         * encoded as base64. [ setBase64Encoded(true)][.setBase64Encoded] will be in invoked automatically.
         */
        fun setBinaryBody(binaryBody: ByteArray?): Builder {
            this.binaryBody = binaryBody
            setBase64Encoded(true)
            return this
        }

        /**
         * A binary or rather a base64encoded responses requires
         *
         *  1. "Binary Media Types" to be configured in API Gateway
         *  1. a request with an "Accept" header set to one of the "Binary Media
         * Types"
         *
         */
        fun setBase64Encoded(base64Encoded: Boolean): Builder {
            this.base64Encoded = base64Encoded
            return this
        }

        fun build(): HandlerResponse {
            var body: String? = null
            if (rawBody != null) {
                body = rawBody
            } else if (objectBody != null) {
                body = try {
                    objectMapper.writeValueAsString(
                        objectBody
                    )
                } catch (e: JsonProcessingException) {
                    throw RuntimeException(e)
                }
            } else if (binaryBody != null) {
                body = String(Base64.getEncoder().encode(binaryBody), StandardCharsets.UTF_8)
            }
            return HandlerResponse(statusCode, body, headers, base64Encoded)
        }

        companion object {
            private val objectMapper = ObjectMapper()
        }
    }

    companion object {
        fun builder(): Builder {
            return Builder()
        }
    }

}