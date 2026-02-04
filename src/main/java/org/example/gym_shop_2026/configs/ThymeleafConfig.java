package org.example.gym_shop_2026.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import org.springframework.context.support.ResourceBundleMessageSource;

@Configuration
public class ThymeleafConfig {
    /**
     * Setup to configure a properties file viewdisplay.properties for use with {@link org.thymeleaf.Thymeleaf}
     * to display messages in view.
     *
     * @return A {@link ResourceBundleMessageSource} pointing to viewdisplay.properties in resources folder
     */
    @Bean
    @Description("org.springframework.context.support.ResourceBundleMessageSource configuration bean for Thymeleaf messages")
    public ResourceBundleMessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("viewdisplay");

        return messageSource;
    }
}
