package dev.local.olympia.config;

import dev.local.olympia.interfaces.TraineeDAO;
import dev.local.olympia.interfaces.TrainerDAO;
import dev.local.olympia.util.UsernameGenerator;
import org.springframework.context.annotation.*;

@Configuration
@ComponentScan(basePackages = {
        "dev.local.olympia.service.impl",
        "dev.local.olympia.hibernateImpl",
        "dev.local.olympia.util",
})
@PropertySource("classpath:application.properties")
@Import(HibernateConfig.class)
public class AppConfig {

    @Bean
    public UsernameGenerator.UsernameExistsChecker usernameExistsChecker(TraineeDAO traineeDAO, TrainerDAO trainerDAO) {
        return username ->
                traineeDAO.findByUsername(username).isPresent() ||
                        trainerDAO.findByUsername(username).isPresent();
    }
}
