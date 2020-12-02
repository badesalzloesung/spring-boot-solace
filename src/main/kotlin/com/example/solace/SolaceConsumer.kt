package com.example.solace

import com.solacesystems.jms.message.SolMessage
import com.solacesystems.jms.message.SolTextMessage
import mu.KotlinLogging
import org.springframework.jms.annotation.JmsListener
import org.springframework.jms.core.JmsTemplate
import org.springframework.stereotype.Component
import java.util.*
import javax.jms.Message

@Component
class SolaceConsumer(val jmsTemplate: JmsTemplate) {
	companion object {
		private val logger = KotlinLogging.logger {}
	}

	@JmsListener(destination = "\${solace.target.queue}", concurrency = "1")
	fun consumeQueue1(message: SolMessage) {
		if (message !is SolTextMessage) {
			logger.error { "Wrong message $message" }
			throw Exception(" Wrong message $message")
		}
		logger.info { "consumeQueue1 ${message.text}" }
	}

	@JmsListener(destination = "\${solace.target.queue}", concurrency = "2")
	fun consumeQueue2(message: SolMessage) {
		if (message !is SolTextMessage) {
			logger.error { "Wrong message $message" }
			throw Exception(" Wrong message $message")
		}
		logger.info { "consumeQueue2 ${message.text}" }
	}

	@JmsListener(destination = "\${solace.target.topic}", containerFactory = "jmsTopicContainerFactory")
	fun consumeTopic1(message: SolMessage) {
		logger.info { "consumeTopic1 $message" }
	}

	@JmsListener(destination = "\${solace.target.topic}", containerFactory = "jmsTopicContainerFactory")
	fun consumeTopic2(message: SolMessage) {
		logger.info { "consumeTopic2 $message" }
	}

	//	@JmsListener(destination = "rpc_queue")
	@JmsListener(destination = "\${solace.target.rpc}", containerFactory = "jmsTopicContainerFactory")
	fun consumeRPC(message: Message) {
		logger.info("consumeRPC $message")
		jmsTemplate.convertAndSend(message.jmsReplyTo, Date().toString())
	}

}