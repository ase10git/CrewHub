package io.github.crewhub.utils;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

/**
 * 날짜 및 시간 유틸 클래스
 */
@Component
public class DateUtils {

    public LocalDateTime toLocalDateTime(Date date) {
        return date
                .toInstant()
                .atZone(ZoneOffset.UTC)
                .toLocalDateTime();
    }
}
