package com.hirex.config.locale;

import com.hirex.config.lib.commom.Messages;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

@Configuration
public class LocaleConfiguration implements WebMvcConfigurer {

    private static LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
        lci.setParamName("lang");
        return lci;
    }

    private static MessageBundle messageBundle() {
        MessageBundle messageSource = new MessageBundle(null);
        messageSource.setFallbackToSystemLocale(false);
        messageSource.setBasenames(
                "classpath:i18n/messages",
                "classpath:i18n/validations",
                "classpath:i18n/fields",
                "classpath:i18n/permissions"
        );
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

    @Bean
    public LocaleResolver localeResolver() {
        return new RequestHeaderLocaleResolver();
    }

    @Bean
    public MessageSource messageSource() {
        return messageBundle();
    }

    @Bean
    public Messages messages() {
        return messageBundle();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
    }
}
