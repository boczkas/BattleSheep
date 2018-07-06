package pl.jakubowskiprzemyslaw.REST.configuration;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.jakubowskiprzemyslaw.tajgertim.services.QueueService;
import pl.jakubowskiprzemyslaw.tajgertim.services.SessionService;

@Configuration
public class QueueAndSessionConfiguration {
  private final RabbitTemplate template;

  @Autowired
  public QueueAndSessionConfiguration(RabbitTemplate template) {
    this.template = template;
  }

  @Bean
  public QueueService getQueueService() {
    return new QueueService(template);
  }

  @Bean
  public SessionService getSessionService() {
    return new SessionService();
  }
}
