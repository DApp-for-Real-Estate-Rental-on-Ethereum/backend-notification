package ma.fstt.notificationservice.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

@Configuration
@Slf4j
public class RabbitMQConfiguration {

    @Value("${app.rabbitmq.exchange}")
    private String exchangeName;

    @Value("${app.rabbitmq.queue}")
    private String queueName;

    @Value("${app.rabbitmq.routingkey}")
    private String routingKey;

    @Value("${app.rabbitmq.dql.exchange}")
    private String dqlExchangeName;

    @Value("${app.rabbitmq.dql.queue}")
    private String dqlQueueName;

    @Value("${app.rabbitmq.dql.routingkey}")
    private String dqlRoutingKey;

    @Bean
    public TopicExchange dlxExchange() {
        return new TopicExchange(dqlExchangeName);
    }

    @Bean
    public Queue dlqQueue() {
        return new Queue(dqlQueueName, true);
    }

    @Bean
    public Binding dlqBinding(Queue dlqQueue, TopicExchange dlxExchange) {
        return BindingBuilder
                .bind(dlqQueue)
                .to(dlxExchange)
                .with(dqlRoutingKey);
    }

    @Bean
    public Queue queue() {
        return QueueBuilder
                .durable(queueName)
                .withArgument("x-dead-letter-exchange", dqlExchangeName)
                .withArgument("x-dead-letter-routing-key", dqlRoutingKey)
                .build();
    }

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(exchangeName);
    }

    @Bean
    public Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder
                .bind(queue)
                .to(exchange)
                .with(routingKey);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(jsonMessageConverter());
        
        RetryTemplate retryTemplate = new RetryTemplate();
        
        ExponentialBackOffPolicy backOffPolicy = new ExponentialBackOffPolicy();
        backOffPolicy.setInitialInterval(5000);
        backOffPolicy.setMultiplier(2.0);
        backOffPolicy.setMaxInterval(60000);
        retryTemplate.setBackOffPolicy(backOffPolicy);
        
        SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy();
        retryPolicy.setMaxAttempts(3);
        retryTemplate.setRetryPolicy(retryPolicy);
        
        factory.setRetryTemplate(retryTemplate);
        
        factory.setAutoStartup(true);
        
        factory.setRecoveryInterval(30000L);
        
        factory.setMissingQueuesFatal(false);
        
        log.info("RabbitMQ Listener Container Factory configured with retry policy and recovery interval");
        
        return factory;
    }
}
