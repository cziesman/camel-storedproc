package com.redhat.camel.storedproc.route;

import com.redhat.camel.storedproc.service.MessageToPollService;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Adds some messages.
 */
@Component
public class MessageToPollInitializerRoute extends RouteBuilder {

    @Autowired
    private MessageToPollService messageToPollService;

    @Override
    public void configure() {

        from("timer:messageInitializer?repeatCount=1&delay=1s")
                .routeId("MessageToPoll Initializer")
                .process(exchange -> {

                    for (int i = 0; i < 1000; i++) {
                        messageToPollService.makeMessage(UUID.randomUUID().toString().toLowerCase());
                    }
                });
    }
}
