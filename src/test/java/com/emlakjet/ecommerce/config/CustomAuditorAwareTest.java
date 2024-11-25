package com.emlakjet.ecommerce.config;

import com.emlakjet.ecommerce.util.AuthUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


@ExtendWith({SpringExtension.class})
@ContextConfiguration(classes = {CustomAuditorAware.class})
class CustomAuditorAwareTest {

    @Autowired
    private CustomAuditorAware customAuditorAware;

    @MockBean
    private AuthUtil authUtil;

    @Test
    void shouldReturnEmptyOptionalWhenUserIdIsEmpty() {
        when(authUtil.getUserId()).thenReturn("");

        var result = customAuditorAware.getCurrentAuditor();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldReturnOptionalContainingUserIdWhenUserIdIsNotEmpty() {
        var userId = "testUserId";
        when(authUtil.getUserId()).thenReturn(userId);

        var result = customAuditorAware.getCurrentAuditor();

        assertNotNull(result);
        assertTrue(result.isPresent());
        assertEquals(userId, result.get());
    }

}