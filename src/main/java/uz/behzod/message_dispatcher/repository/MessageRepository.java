package uz.behzod.message_dispatcher.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uz.behzod.message_dispatcher.domain.Message;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    @Query(value = """
            SELECT * FROM message
            WHERE status = 'NEW'
              AND (locked_at IS NULL OR locked_at < NOW() - INTERVAL '2 minutes')
            ORDER BY id
            LIMIT :limit
            FOR UPDATE SKIP LOCKED
            """, nativeQuery = true)
    List<Message> findAndLockPending(@Param("limit") int limit);
}
