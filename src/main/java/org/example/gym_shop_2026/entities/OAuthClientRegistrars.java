package org.example.gym_shop_2026.entities;


import lombok.Getter;
import lombok.Setter;
import org.example.gym_shop_2026.enums.OAuthMethod;

/**
 * Registers OAuth client implementation details.
 *
 * @author Cal Woods
 */
@Getter
@Setter
public abstract class OAuthClientRegistrars {
    protected String registrationId;
    protected String clientId;
    protected String clientSecret;
    protected OAuthMethod oAuthMethod;
    protected String redirectUri;
    protected String scopes;
    protected String grantTypes;

    /**
     * A no arguments constructor that reads
     * details from a file.
     */
    protected OAuthClientRegistrars(String registrationId, String clientId, String clientSecret, OAuthMethod oAuthMethod, String redirectUri, String scopes, String grantTypes) {
        this.registrationId = registrationId;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.oAuthMethod = oAuthMethod;
        this.redirectUri = redirectUri;
        this.scopes = scopes;
        this.grantTypes = grantTypes;
    }
}
