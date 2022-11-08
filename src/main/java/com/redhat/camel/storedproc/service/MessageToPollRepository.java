package com.redhat.camel.storedproc.service;

import com.redhat.camel.storedproc.db.MessageToPoll;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageToPollRepository extends JpaRepository<MessageToPoll, Long> {

}
