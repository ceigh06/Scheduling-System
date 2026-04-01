package service;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import model.schedule.RequestSchedule;

public class SectionRequestValidator {
    private static int lastUsedRequestKey = -1;
    static LocalDateTime dateTimeToday = LocalDateTime.now();
    static LocalDate date = dateTimeToday.toLocalDate();
    static LocalTime time = dateTimeToday.toLocalTime();

    public String getRequestStatus(List<RequestSchedule> requestSchedule) {
        for (RequestSchedule rs : requestSchedule) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss[.n]");
            LocalDate dateRequested = LocalDateTime.parse(rs.getDateRequested(), formatter).toLocalDate();
            if (!dateRequested.equals(LocalDate.now())) {
                continue;
            }

            int status = Integer.parseInt(rs.getStatus());
            int requestKey = rs.getID();

            if (status == 1) { // pending
                lastUsedRequestKey = requestKey;
                return "pending";
            }
            if (status == 0) { // void
                if (!(requestKey == lastUsedRequestKey)) {
                    lastUsedRequestKey = requestKey;
                    return "void";
                }
            } else if (status == 2) { // approved
                if (!(requestKey == lastUsedRequestKey)) {
                    lastUsedRequestKey = requestKey;
                    return "approved";
                }
            } else if (status == 3) { // denied
                if (!(requestKey == lastUsedRequestKey)) {
                    lastUsedRequestKey = requestKey;
                    return "declined";
                }
            }
        }
        return "standby";
    }

    public static int getLastUsedRequestKey() {
        return lastUsedRequestKey;
    }
}