package rems.telegrambot.ChlorophyllNetBot;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@SpringBootApplication
public class ChlorophyllNetBotApplication {

	private Flyway flyway;

	@Value("${spring.flyway.enabled}")
	private Boolean fwEnabled;

	@Autowired(required = false)
	public void setFlyway(Flyway flyway) {
		this.flyway = flyway;
	}

	@EventListener(ApplicationReadyEvent.class)
	public void dbMigration() { if (fwEnabled) flyway.migrate();}


	public static void main(String[] args) {
		SpringApplication.run(ChlorophyllNetBotApplication.class, args);
	}

}
