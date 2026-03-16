package com.api.app.getdonapi.global.config.jwt.security;

public record UserPrincipal(Long userId, String email, String role) {
}
