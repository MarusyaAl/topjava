package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.List;

public interface CrudMealRepository extends JpaRepository<Meal, Integer> {
    @Transactional
    @Modifying
//    @Query(name = User.DELETE)
    @Query("DELETE FROM Meal m WHERE m.id=:id AND m.user.id =:user_id")
    int delete(@Param("id") int id, @Param("user_id") int userId);

    @Transactional(readOnly = true)
    @Query("SELECT m FROM Meal m WHERE m.id=:id AND m.user.id =:user_id")
    Meal findById(@Param("id") int id, @Param("user_id") int userId);

    @Transactional(readOnly = true)
    @Query("SELECT m FROM Meal m WHERE m.user.id=:user_id ORDER BY m.dateTime DESC")
    List<Meal> getAll(@Param("user_id") int userId);

    @Transactional(readOnly = true)
    @Query("SELECT m FROM Meal m WHERE m.user.id=:user_id   AND m.dateTime>=:start_date_time AND m.dateTime <:end_date_time ORDER BY m.dateTime DESC")
    List<Meal> getBetweenHalfOpen(@Param("start_date_time") LocalDateTime startDateTime, @Param("end_date_time") LocalDateTime endDateTime, @Param("user_id") int userId);

   /* @Transactional
    @Modifying
    @Query("UPDATE Meal m SET  m.description=:description, m.calories=:calories, m.dateTime=:date_time, m.user.id=:user_id")
    Meal save(@Param("description") String description, @Param("calories") int calories,
              @Param("date_time") LocalDateTime dateTime, @Param("user_id") int userId);*/

   /* @Transactional
    @Modifying
    @Query("UPDATE Meal m SET  m.description=:description, m.calories=:calories, m.dateTime=:date_time WHERE m.id=:id")
    Meal update(@Param("description") String description, @Param("calories") int calories,
                @Param("date_time") LocalDateTime dateTime, @Param("id") int id);*/
}
