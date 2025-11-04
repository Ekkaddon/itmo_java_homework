package ru.itmo.javaadvanced.diploma.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.itmo.javaadvanced.diploma.domain.entity.FlightCrew;

import java.time.OffsetDateTime;
import java.util.Set;

@Repository
public interface FlightCrewRepository extends JpaRepository<FlightCrew, Long> {

    @Query("select distinct fc.crewMember.id from FlightCrew fc "
            + "where fc.flight.scheduledDeparture < :intervalEnd "
            + "and fc.flight.scheduledArrival > :intervalStart")
    Set<Long> findBusyCrewMemberIds(@Param("intervalStart") OffsetDateTime intervalStart,
                                    @Param("intervalEnd") OffsetDateTime intervalEnd);
}