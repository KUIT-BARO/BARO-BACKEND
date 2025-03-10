package konkuk.kuit.baro.domain.schedule.repository;

import konkuk.kuit.baro.domain.schedule.model.DayOfWeek;
import konkuk.kuit.baro.domain.schedule.model.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    @Query("""
        SELECT s FROM Schedule s
        WHERE s.user.id = :userId
        AND s.dayOfWeek = :dayOfWeek
        AND (
            (:startTime < s.endTime AND :endTime > s.startTime)
        )
    """)
    List<Schedule> findOverlappingSchedule(
            @Param("userId") Long userId,
            @Param("dayOfWeek") DayOfWeek dayOfWeek,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime
    );
}
