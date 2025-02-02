package com.emlakjet.ecommerce.service;

import com.emlakjet.ecommerce.exception.ServiceException;
import com.slack.api.Slack;
import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.request.chat.ChatPostMessageRequest;
import com.slack.api.methods.response.chat.ChatPostMessageResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {NotificationService.class})
class NotificationServiceTest {

    @Autowired
    private NotificationService notificationService;

    @MockBean
    private Slack slack;

    @Mock
    private MethodsClient methodsClient;


    @Test
    void sendNotification_shouldSendMessageSuccessfully() throws Exception {
        var response = new ChatPostMessageResponse();
        response.setOk(true);
        when(slack.methods(anyString())).thenReturn(methodsClient);
        when(methodsClient.chatPostMessage(any(ChatPostMessageRequest.class)))
                .thenReturn(response);

        notificationService.sendNotification("Test Message");

        verify(methodsClient).chatPostMessage(any(ChatPostMessageRequest.class));
    }

    @Test
    void sendNotification_shouldHandleExceptionAndThrowServiceException() throws Exception {
        when(slack.methods(anyString())).thenReturn(methodsClient);
        when(methodsClient.chatPostMessage(any(ChatPostMessageRequest.class)))
                .thenThrow(new IOException("Test Exception"));

        var exception = assertThrows(ServiceException.class,
                () -> notificationService.sendNotification("Test Message"));

        assertNotNull(exception);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getHttpStatus());
    }

}