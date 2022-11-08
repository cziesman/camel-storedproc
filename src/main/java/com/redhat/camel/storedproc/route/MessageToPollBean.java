package com.redhat.camel.storedproc.route;

import com.redhat.camel.storedproc.db.MessageToPoll;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.time.ZoneId;

public class MessageToPollBean {

    public MessageToPoll convert(Object[] fields) {

        if (fields == null) {
            return null;
        }

        MessageToPoll retval = new MessageToPoll();
        retval.setId(id(fields[0]));
        retval.setBody((String) fields[1]);
        retval.setPollingStatus((String) fields[2]);
        retval.setTimestamp(offsetDateTime(fields[3]));

        return retval;
    }

    protected Long id(Object bigInteger) {

        return ((BigInteger) bigInteger).longValue();
    }

    protected OffsetDateTime offsetDateTime(Object timestamp) {

        return OffsetDateTime.ofInstant(((Timestamp) timestamp).toInstant(), ZoneId.of("UTC"));
    }
}
