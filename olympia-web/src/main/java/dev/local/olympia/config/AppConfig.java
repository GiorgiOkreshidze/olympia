package dev.local.olympia.config;

import org.springframework.context.annotation.*;

@Configuration
@ComponentScan(basePackages = {
        "dev.local.olympia.service.impl",
        "dev.local.olympia.hibernateImpl"
})
@PropertySource("classpath:application.properties")
@Import(HibernateConfig.class)
public class AppConfig {
}
