package org.example.gym_shop_2026.entities;

import org.example.gym_shop_2026.enums.OAuthMethod;

import java.util.Properties;

public class GoogleOAuthClientRegistrar extends OAuthClientRegistrars {
    /**
     * Loads all details for oauth client: Google
     */
    protected GoogleOAuthClientRegistrar(Properties properties) {
        super(properties.getProperty("url", "NO_PROPERTIES_FILE_DETECTED!"), "Google", properties.getProperty("client-id", "NO_PROPERTIES_FILE_DETECTED!)"), properties.getProperty("client-secret", "NO_PROPERTIES_FILE_DETECTED!"), properties.getProperty("OAuthMethod", "NO_PROPERTIES_FILE_DETECTED!"), properties.getProperty("RedirectUri", "NO_PROPERTIES_FILE_DETECTED!"), properties.getProperty("Scopes", "NO_PROPERTIES_FILE_DETECTED!"), properties.getProperty("Grant_Types"));
    }
}
