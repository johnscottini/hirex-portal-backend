package com.hirex.portal.config.locale;

import com.google.common.base.Strings;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.LocaleResolver;

import java.util.Locale;

public class RequestHeaderLocaleResolver implements LocaleResolver {
    private static final String LOCALE_HEADER = "app-locale";
    private static final String DEFAULT_LOCALE = "pt-BR";

    @Override
    @NonNull
    public Locale resolveLocale(HttpServletRequest httpServletRequest) {
        String locale = httpServletRequest.getHeader(LOCALE_HEADER);
        if (Strings.isNullOrEmpty(locale)) {
            locale = DEFAULT_LOCALE;
        }
        return new Locale(locale);
    }

    @Override
    public void setLocale(@NonNull HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Locale locale) {
        throw new IllegalArgumentException("O Locale deve vir do client atrav??s do header");
    }
}
