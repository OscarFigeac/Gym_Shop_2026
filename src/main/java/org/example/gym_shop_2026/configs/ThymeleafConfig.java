package org.example.gym_shop_2026.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

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

    /**
     * @author Oscar
     * Configures the Thymeleaf resolver for the application.
     * Note: Caching is currently disabled for development purposes. Toggle back on
     * when finished development.
     * @return a configured {@link ClassLoaderTemplateResolver} for processing HTML templates.
     */
    @Bean
    public ClassLoaderTemplateResolver templateResolver() {
        ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
        resolver.setPrefix("templates/");
        resolver.setSuffix(".html");
        resolver.setTemplateMode(TemplateMode.HTML);
        resolver.setCharacterEncoding("UTF-8");
        resolver.setCacheable(false); //false during development to auto update. toggle for regular functionality
        return resolver;
    }
}
