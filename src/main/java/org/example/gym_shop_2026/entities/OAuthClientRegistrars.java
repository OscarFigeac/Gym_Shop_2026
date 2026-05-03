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
    protected OAuthClientRegistrars() {

    }
}
