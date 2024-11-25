package com.emlakjet.ecommerce.util;

import com.emlakjet.ecommerce.model.CommerceUser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith({SpringExtension.class})
@ContextConfiguration(classes = {AuthUtil.class})
class AuthUtilTest {

    @Autowired
    private AuthUtil authUtil;

    @MockBean
    private SecurityContext securityContext;

    @MockBean
    private Authentication authentication;

    @MockBean
    private UserDetails userDetails;


    @Test
    void getUser_shouldReturnCommerceUser() {
        var user = CommerceUser.builder().id("123").build();

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn(user);

        SecurityContextHolder.setContext(securityContext);

        var result = authUtil.getUser();

        assertNotNull(result);
        assertEquals("123", result.getId());
    }

    @Test
    void getUser_shouldReturnNullWhenUnauthenticated() {
        when(securityContext.getAuthentication()).thenReturn(null);

        SecurityContextHolder.setContext(securityContext);

        var result = authUtil.getUser();

        assertNull(result);
    }

    @Test
    void getUserId_shouldReturnUserId() {
        var user = CommerceUser.builder().id("456").build();

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn(user);

        SecurityContextHolder.setContext(securityContext);

        var result = authUtil.getUserId();

        assertNotNull(result);
        assertEquals("456", result);
    }

    @Test
    void generateToken_shouldReturnToken() {
        when(userDetails.getUsername()).thenReturn("testUser");

        var result = authUtil.generateToken(userDetails);

        assertNotNull(result);
    }

}