package uz.behzod.message_dispatcher;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class MessageDispatcherApplication {

	public static void main(String[] args) {
		SpringApplication.run(MessageDispatcherApplication.class, args);
	}

}
