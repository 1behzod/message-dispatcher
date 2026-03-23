package uz.behzod.message_dispatcher.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import uz.behzod.message_dispatcher.enums.MessageStatus;

import java.time.Instant;

@Entity
@Table(name = "message")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class Message extends BaseEntity {

    @Column(name = "name")
    String name;

    @Column(name = "url")
    String url;

    @Column(name = "app_id")
    String appId;

    @Column(name = "locked_at")
    Instant lockedAt;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    MessageStatus status = MessageStatus.NEW;

}

