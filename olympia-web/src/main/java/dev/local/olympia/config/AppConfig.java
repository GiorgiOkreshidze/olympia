package dev.local.olympia.config;

import dev.local.olympia.interfaces.TraineeDAO;
import dev.local.olympia.interfaces.TrainerDAO;
import dev.local.olympia.util.UsernameGenerator;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.context.annotation.*;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@ComponentScan(basePackages = {
        "dev.local.olympia.service.impl",
        "dev.local.olympia.hibernateImpl",
        "dev.local.olympia.util",
})
@PropertySource("classpath:application.properties")
@Import(HibernateConfig.class)
public class AppConfig {
}
