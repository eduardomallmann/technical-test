package com.eduardomallmann.compasso.technicaltest.configs;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

/**
 * Application beans instantiation by configurations.
 *
 * @author eduardomallmann
 * @since 0.0.1
 */
@Configuration
public class AppConfig {

    /**
     * Configure and instantiate {@link MessageSource} component.
     *
     * @return the {@link MessageSource} component instantiated.
     */
    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:i18n/messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

    /**
     * Configure and instantiate {@link LocalValidatorFactoryBean} component.
     *
     * @return the {@link LocalValidatorFactoryBean} component instantiated.
     */
    @Bean
    public LocalValidatorFactoryBean getValidator() {
        LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
        localValidatorFactoryBean.setValidationMessageSource(messageSource());
        return localValidatorFactoryBean;
    }
}
