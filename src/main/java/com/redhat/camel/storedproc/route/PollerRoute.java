package com.redhat.camel.storedproc.route;

import com.redhat.camel.storedproc.db.MessageToPoll;
import com.redhat.camel.storedproc.db.PollingStatus;
import com.redhat.camel.storedproc.service.MessageToPollService;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;

@Slf4j
@Component
public class PollerRoute extends RouteBuilder {

    private static final String URI =
            "jpa://com.redhat.demo.poller.db.MessageToPoll" +
                    "?nativeQuery=select * from message_to_poll where polling_status = 'UNREAD'" +
                    "&delay=1000" +
                    "&maximumResults=%d" +
                    "&consumeDelete=false" +
                    "&consumeLockEntity=true" +
                    "&skipLockedEntity=true";

    @Value("${com.redhat.demo.batch-size}")
    private int batchSize;

    @Value("${com.redhat.demo.number-of-threads}")
    private int numberOfThreads;

    @Autowired
    private MessageToPollService messageToPollService;

    @Override
    public void configure() {

        from(String.format(URI, batchSize))
                .routeId("jpa-poller-route")
                .log(LoggingLevel.TRACE, "\n" + body())
                .bean(MessageToPollBean.class)
                .threads(numberOfThreads)
                .to("direct:handle-message");

        from("direct:handle-message")
                .routeId("handle-message")
                .transacted()
                .log(LoggingLevel.TRACE, "\n" + body())
                .process(exchange -> {
                    MessageToPoll messageToPoll = exchange.getIn().getBody(MessageToPoll.class);
                    LOG.debug("{}", messageToPoll);

                    messageToPoll.setPollingStatus(PollingStatus.READ.name());
                    messageToPoll.setTimestamp(OffsetDateTime.now());

                    messageToPoll = messageToPollService.save(messageToPoll);
                    LOG.debug("{}", messageToPoll);

                    // set the parameter needed for the stored procedure call
                    exchange.getIn().setHeader("parameter", messageToPoll.getBody());
                })
                .to("direct:call-procedure");

        from("direct:call-procedure")
                .routeId("call-procedure")
                .to("sql-stored:TO_UPPERCASE(VARCHAR ${header.parameter})")
                .bean(ToStringBean.class)
                .log(LoggingLevel.DEBUG, "result :: ${body}");
    }
}
