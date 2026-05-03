package org.example.gym_shop_2026.entities;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.example.gym_shop_2026.enums.OAuthMethod;

/**
 * Registers OAuth client implementation details.
 *
 * @author Cal Woods
 */
@Getter
@Setter(AccessLevel.PROTECTED)
public abstract class OAuthClientRegistrars {
    protected String registrationId;
    protected String clientId;
    protected String clientSecret;
    protected OAuthMethod oAuthMethod;
    protected String redirectUri;
    protected String scopes;
    protected String grantTypes;

    /**
     * An all-arguments constructor that sets field attributes to parameterised
     * values.
     * @param registrationId Url of authorisation server
     * @param clientId Client id for authorisation server
     * @param clientSecret Client secret from provider client setup
     * @param oAuthMethod Method of authorisation, an enum {@link OAuthMethod}
     * @param redirectUri Resource for further redirection
     * @param scopes Permission scopes
     * @param grantTypes Types of granted privileges issued
     */
    protected OAuthClientRegistrars(String registrationId, String clientId, String clientSecret, String oAuthMethod, String redirectUri, String scopes, String grantTypes) {
        this.registrationId = registrationId;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.oAuthMethod = OAuthMethod.valueOf(oAuthMethod);
        this.redirectUri = redirectUri;
        this.scopes = scopes;
        this.grantTypes = grantTypes;
    }
}