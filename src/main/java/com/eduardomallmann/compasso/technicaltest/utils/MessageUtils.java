package com.eduardomallmann.compasso.technicaltest.utils;

import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * Util class responsible for the bundle message static retrieve.
 *
 * @author eduardomallmann
 * @since 0.0.1
 */
@Component
public class MessageUtils implements MessageSourceAware {

    private static MessageSource messageSource;

    /**
     * Retrieves resource bundle message.
     *
     * @param key  resource key
     * @param args resource args
     *
     * @return a String.
     */
    public static String getMessage(String key, Object... args) {
        return messageSource.getMessage(key, args, Locale.getDefault());
    }

    /**
     * Set the MessageSource that this object runs in.
     * <p>Invoked after population of normal bean properties but before an init
     * callback like InitializingBean's afterPropertiesSet or a custom init-method. Invoked before ApplicationContextAware's setApplicationContext.
     *
     * @param messageSource message source to be used by this object
     */
    @Override
    public void setMessageSource(final MessageSource messageSource) {
        MessageUtils.messageSource = messageSource;
    }
}
