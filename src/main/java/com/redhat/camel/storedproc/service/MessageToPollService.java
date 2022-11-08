package com.redhat.camel.storedproc.service;

import com.redhat.camel.storedproc.db.MessageToPoll;
import com.redhat.camel.storedproc.db.PollingStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Transactional
@Slf4j
public class MessageToPollService {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private MessageToPollRepository messageToPollRepository;

    public MessageToPoll makeMessage(String body) {

        MessageToPoll messageToPoll = new MessageToPoll(null, body, PollingStatus.UNREAD.name(), OffsetDateTime.now());

        return save(messageToPoll);
    }

    public List<MessageToPoll> save(List<MessageToPoll> messageToPolls, String extension) {

        messageToPollRepository.saveAll(messageToPolls);
        em.flush();

        return findAllMessages();
    }

    @Transactional(readOnly = true)
    public List<MessageToPoll> findAllMessages() {

        return StreamSupport
                .stream(messageToPollRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public MessageToPoll findMessageById(Long id) {

        Objects.requireNonNull(id, "<id> cannot be null");

        return messageToPollRepository.findById(id).orElse(null);
    }

    public MessageToPoll save(MessageToPoll messageToPoll) {

        Objects.requireNonNull(messageToPoll, "<messageToPoll> cannot be null");

        MessageToPoll retval = messageToPollRepository.save(messageToPoll);
        em.flush();

        return retval;
    }

    public void delete(MessageToPoll messageToPoll) {

        Objects.requireNonNull(messageToPoll, "<messageToPoll> cannot be null");

        messageToPollRepository.delete(messageToPoll);
        em.flush();
    }

    public void deleteAll() {

        messageToPollRepository.deleteAll();
        em.flush();
    }
}
