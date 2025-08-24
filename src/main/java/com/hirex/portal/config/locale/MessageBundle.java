package com.hirex.portal.config.locale;

import com.hirex.portal.config.lib.commom.Messages;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import java.util.Locale;
import java.util.Objects;
import java.util.Properties;

/**
 * Adaptador de {@link ReloadableResourceBundleMessageSource} a interface {@link Messages}.
 */
public class MessageBundle extends ReloadableResourceBundleMessageSource implements Messages {

    private Locale locale;

    public MessageBundle(Locale locale) {
        this.locale = locale;
    }

    private Locale getLocale() {
        return Objects.isNull(locale) ? LocaleContextHolder.getLocale() : locale;
    }

    /**
     * @see Messages
     */
    @Override
    public Properties getMessages() {
        return super.getMergedProperties(getLocale()).getProperties();
    }

    /**
     * @see Messages
     */
    @Override
    public String getMessage(String code, Object... args) {
        return this.getMessage(code, args, code, getLocale());
    }
}
