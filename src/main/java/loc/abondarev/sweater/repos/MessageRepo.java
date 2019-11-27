package loc.abondarev.sweater.repos;

import loc.abondarev.sweater.domain.Message;
import loc.abondarev.sweater.domain.User;
import loc.abondarev.sweater.domain.dto.MessageDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface MessageRepo extends CrudRepository<Message, Long> {

    @Query(
            "select new loc.abondarev.sweater.domain.dto.MessageDto(" +
                    "   m, " +
                    "   count (ml), " +
                    "   (sum(case when ml = :user then 1 else 0 end) > 0)" +
                    ") " +
                    "from Message m left join m.likes ml where m.tag = :tag " +
                    "group by m"
    )
    Page<MessageDto> findByTag(@Param("tag") String tag, @Param("user") User user, Pageable pageable);

    @Query(
            "select new loc.abondarev.sweater.domain.dto.MessageDto(" +
                    "   m, " +
                    "   count (ml), " +
                    "   (sum(case when ml = :user then 1 else 0 end) > 0)" +
                    ") " +
                    "from Message m left join m.likes ml " +
                    "group by m"
    )
    Page<MessageDto> findAll(Pageable pageable, @Param("user") User user);

    @Query(
            "select new loc.abondarev.sweater.domain.dto.MessageDto(" +
                    "   m, " +
                    "   count (ml), " +
                    "   (sum(case when ml = :user then 1 else 0 end) > 0)" +
                    ") " +
                    "from Message m left join m.likes ml  where m.author = :author " +
                    "group by m"
    )
    Page<MessageDto> findByUser(@Param("author") User author, @Param("user") User currentUser, Pageable pageable);
}
