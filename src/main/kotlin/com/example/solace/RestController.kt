package com.example.solace

import com.beust.klaxon.Klaxon
import com.solacesystems.jms.message.SolTextMessage
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.jms.core.JmsTemplate
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import javax.jms.TextMessage


@RestController
class RestController(val jmsTemplate: JmsTemplate,
					 @Value("\${solace.target.queue}") private var queueName: String = "job_queue",
					 @Value("\${solace.target.topic}") private var topicName: String = "pub_sub_topic",
					 @Value("\${solace.target.rpc}") private var rpcName: String = "rpc_topic") {
	companion object {
		private val logger = KotlinLogging.logger {}
	}

	@PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE], value = ["/queue"])
	fun postQueue(@RequestBody myData: MyData) {
		jmsTemplate.isPubSubDomain = false
		logger.debug { "Sending $myData to queue - $queueName" }
		jmsTemplate.convertAndSend(queueName, Klaxon().toJsonString(myData))
		logger.info { "Sent $myData to queue - $queueName" }
	}

	@PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE], value = ["/topic"])
	fun postTopic(@RequestBody myData: MyData) {
		jmsTemplate.isPubSubDomain = true
		logger.debug { "Sending $myData to topic - $topicName" }
		jmsTemplate.convertAndSend(topicName, Klaxon().toJsonString(myData))
		logger.info { "Sent $myData to topic - $topicName" }
	}

	@PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE], value = ["/rpc"])
	fun postRPC(@RequestBody myData: MyData) {
		jmsTemplate.isPubSubDomain = true
		logger.debug { "Sending $myData to queue - $rpcName" }
		val response = jmsTemplate.sendAndReceive(rpcName) { session ->
			val message: TextMessage = session.createTextMessage(Klaxon().toJsonString(myData))
			message.jmsReplyTo = session.createTemporaryQueue()
			message
		}
		logger.info { "Sent $myData to queue - $rpcName response is: ${(response as SolTextMessage).text}" }
	}
}