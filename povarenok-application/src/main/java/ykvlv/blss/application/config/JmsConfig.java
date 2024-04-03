package ykvlv.blss.application.config;

import com.rabbitmq.jms.admin.RMQConnectionFactory;
import com.rabbitmq.jms.admin.RMQDestination;
import jakarta.jms.ConnectionFactory;
import jakarta.jms.Destination;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;

@Configuration
public class JmsConfig {

	@Value("${image.processing.request.queue.name}")
	private String imageProcessingRequestQueue;

	@Value("${processed.image.response.queue.name}")
	private String processedImageResponseQueue;

	@Bean
	public ConnectionFactory jmsConnectionFactory() {
		RMQConnectionFactory connectionFactory = new RMQConnectionFactory();
		connectionFactory.setUsername("guest");
		connectionFactory.setPassword("guest");
		connectionFactory.setVirtualHost("/");
		connectionFactory.setHost("localhost");
		connectionFactory.setPort(5672);
		return connectionFactory;
	}

	@Bean
	public Destination destinationToListen() {
		RMQDestination jmsDestination = new RMQDestination();
		jmsDestination.setDestinationName(processedImageResponseQueue);
		jmsDestination.setAmqp(true);
		return jmsDestination;
	}

	@Bean
	public Destination destinationToSend() {
		RMQDestination jmsDestination = new RMQDestination();
		jmsDestination.setDestinationName(imageProcessingRequestQueue);
		jmsDestination.setAmqp(true);
		return jmsDestination;
	}

	@Bean
	public JmsTemplate defaultJmsTemplate(ConnectionFactory connectionFactory) {
		return new JmsTemplate(connectionFactory);
	}

	@Bean
	public DefaultJmsListenerContainerFactory jmsListenerContainerFactory(ConnectionFactory connectionFactory) {
		DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
		factory.setConnectionFactory(connectionFactory);

		factory.setDestinationResolver((session, destinationName, pubSubDomain) -> {
			RMQDestination jmsDestination = new RMQDestination();
			jmsDestination.setDestinationName(destinationName);
			jmsDestination.setAmqpQueueName(destinationName);
			jmsDestination.setAmqp(true);
			return jmsDestination;
		});

		factory.setAutoStartup(true);
		return factory;
	}
}
