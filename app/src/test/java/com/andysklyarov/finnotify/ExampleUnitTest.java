package com.andysklyarov.finnotify;

import org.junit.Test;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.LocalTime;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void setAlarmTime24_isCorrect() {
        ZonedDateTime time = setAlarmTime24(10, 0);
        assertEquals(ZonedDateTime.of(
                LocalDateTime.of(
                        LocalDate.of(2020, 9, 2),
                        LocalTime.of(10, 0, 0, 0)),
                ZoneId.systemDefault()),
                time);
    }

    private ZonedDateTime setAlarmTime24(int hour, int minutes) {
        ZonedDateTime alarmTime;
        ZonedDateTime startOfDayTime = ZonedDateTime.now().toLocalDate().atStartOfDay(ZoneId.systemDefault());
        alarmTime = startOfDayTime.plusHours(hour).plusMinutes(minutes);

        if (alarmTime.isBefore(ZonedDateTime.now())) {
            alarmTime = alarmTime.plusDays(1);
        }

        return alarmTime;
    }

    @Test
    public void loadServiceData_isCorrect() {
        ZonedDateTime time = setAlarmTime24(10, 0);
        assertEquals(time, loadServiceData(time.toInstant().toEpochMilli()));
    }

    private ZonedDateTime loadServiceData(long timeToStartInMillis) {
        ZonedDateTime alarmTime;
        Instant i = Instant.ofEpochMilli(timeToStartInMillis);
        alarmTime = ZonedDateTime.ofInstant(i, ZoneId.systemDefault());
        return alarmTime;
    }

    @Test
    public void trim_isCorrect() {
        String name = "Фунт стерлингов Соединенного королевства                                                                                                                                                                                                                      ";
        name = name.trim();
        assertEquals("Фунт стерлингов Соединенного королевства", name);
    }
}