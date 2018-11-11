package engineer.thomas_werner.dashboard;

import engineer.thomas_werner.dashboard.controllers.GithubController;
import engineer.thomas_werner.dashboard.controllers.TrackerController;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@SpringBootApplication
public class DashboardApplication {

	public static void main(String[] args) {
		SpringApplication.run(DashboardApplication.class, args);
	}

	@Bean
	public WebMvcConfigurer corsConfigurer(@Value("${server.cors}") final String cors) {
		return new WebMvcConfigurerAdapter() {
			@Override
			public void addCorsMappings(final CorsRegistry registry) {
                if(cors.isEmpty())
                    return;

                registry.addMapping(GithubController.PULL_REQUEST_MAPPING).allowedOrigins(cors);
                registry.addMapping(GithubController.REPO_LIST_MAPPING).allowedOrigins(cors);

                registry.addMapping(TrackerController.CURRENT_SPRINT_MAPPING).allowedOrigins(cors);
				registry.addMapping(TrackerController.SPECIFIC_SPRINT_MAPPING).allowedOrigins(cors);
			}
		};
	}


}
