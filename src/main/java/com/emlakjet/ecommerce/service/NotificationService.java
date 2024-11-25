package com.emlakjet.ecommerce.service;

import com.emlakjet.ecommerce.exception.ServiceException;
import com.slack.api.Slack;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.request.chat.ChatPostMessageRequest;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;

import static com.emlakjet.ecommerce.constants.Messages.NOTIFICATION_FAILED;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final Slack slack;

    @Value("${ecommerce.slack.token}")
    private String slackToken;

    /**
     * Send a Slack notification with the given message.
     * <p>
     * This method uses the {@link io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker} annotation to
     * handle exceptions that may occur while sending the notification. If such an exception occurs, the
     * {@link #sendNotificationFallback} fallback method is called. The fallback method does not throw an exception,
     * but logs the message and the exception.
     * </p>
     *
     * @param message the message to send
     */
    @CircuitBreaker(name = "send-notification", fallbackMethod = "sendNotificationFallback")
    public void sendNotification(String message) {
        var methods = slack.methods(slackToken);
        var text = String.format(
                ":warning: *Alert Triggered* :warning: <!channel> %n>%s %n <http://www.example.com|View Details>",
                message
        );

        var request = ChatPostMessageRequest.builder()
                .channel("#alerts")
                .text(text)
                .build();

        try {
            var response = methods.chatPostMessage(request);
            log.info("Notification response {}", response);
        } catch (IOException | SlackApiException e) {
            throw new ServiceException(HttpStatus.INTERNAL_SERVER_ERROR, NOTIFICATION_FAILED);
        }
    }

    public void sendNotificationFallback(String message, Throwable throwable) {
        log.info("[NotificationService#sendNotification]: Fallback method called. Message: {}", message);
    }

}
