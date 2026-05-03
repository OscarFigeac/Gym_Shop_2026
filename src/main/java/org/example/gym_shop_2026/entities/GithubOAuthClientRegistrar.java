package org.example.gym_shop_2026.entities;

import java.util.Properties;

public class GithubOAuthClientRegistrar extends OAuthClientRegistrars {
    public GithubOAuthClientRegistrar(Properties properties) {
        super(properties.getProperty("url", "NO_PROPERTIES_FILE_DETECTED!"), "Google", properties.getProperty("client-id", "NO_PROPERTIES_FILE_DETECTED!)"), properties.getProperty("client-secret", "NO_PROPERTIES_FILE_DETECTED!"), properties.getProperty("OAuthMethod", "NO_PROPERTIES_FILE_DETECTED!"), properties.getProperty("RedirectUri", "NO_PROPERTIES_FILE_DETECTED!"), properties.getProperty("Scopes", "NO_PROPERTIES_FILE_DETECTED!"), properties.getProperty("Grant_Types"));
    }
}
