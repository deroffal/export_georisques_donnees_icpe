package fr.deroffal.extract_georisques_icpe.service

import org.springframework.stereotype.Service
import java.time.Clock
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

@Service
class DateService {

    var clock = Clock.systemUTC()

    fun today() = LocalDate.now(clock)

    fun now() = Instant.now(clock)

    //Format 2018-11-30T18:35:24.00Z
    fun setTime(instant: String) {
        clock = Clock.fixed(Instant.parse(instant), ZoneId.of("UTC"))
    }

    fun resetTime() {
        clock = Clock.systemUTC()
    }
}