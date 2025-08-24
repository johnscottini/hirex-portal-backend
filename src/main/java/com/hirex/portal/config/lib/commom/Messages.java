package com.hirex.portal.config.lib.commom;

import java.util.Properties;

/**
 * Interface para obten????o das mensagens dos arquivos de resources
 */
public interface Messages {
    Properties getMessages();
    String getMessage(String code, Object... args);
}
