package com.handson.Oauth.entity;

import lombok.Data;

@Data
public class TokenResponse
{
    private String access_token;
    private String expires_in;
    private String token_type;

}
