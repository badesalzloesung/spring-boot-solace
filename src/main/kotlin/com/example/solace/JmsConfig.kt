package com.example.solace

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jms.annotation.EnableJms
import org.springframework.jms.config.DefaultJmsListenerContainerFactory
import org.springframework.jms.connection.CachingConnectionFactory
import org.springframework.jms.core.JmsTemplate


@Configuration
@EnableJms
class JmsConfig {

	@Autowired
	private lateinit var jmsTemplate: JmsTemplate

	@Bean
	fun jmsListenerContainerFactory(): DefaultJmsListenerContainerFactory? {
		val factory = DefaultJmsListenerContainerFactory()
		val cachingConnectionFactory = CachingConnectionFactory()
		cachingConnectionFactory.targetConnectionFactory = jmsTemplate.connectionFactory
		factory.setConnectionFactory(cachingConnectionFactory)
		return factory
	}

	@Bean
	fun jmsTopicContainerFactory(): DefaultJmsListenerContainerFactory? {
		val factory = DefaultJmsListenerContainerFactory()
		val cachingConnectionFactory = CachingConnectionFactory()
		cachingConnectionFactory.targetConnectionFactory = jmsTemplate.connectionFactory
		factory.setConnectionFactory(cachingConnectionFactory)
		factory.setPubSubDomain(true)
		return factory
	}

}