package service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import model.schedule.RequestSchedule;

public class SectionRequestValidator {
    private static int lastUsedRequestKey = -1;
    private static int lastUsedStatus = -1;

    public static String getRequestStatus(List<RequestSchedule> requestSchedule) {
        LocalDate today = LocalDate.now();
        RequestSchedule latestResolvedRequest = null;

        for (RequestSchedule rs : requestSchedule) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss[.n]");
            LocalDateTime dateTimeRequested = LocalDateTime.parse(rs.getDateRequested(), formatter);
            LocalDate dateRequested = dateTimeRequested.toLocalDate();

            //skips if the request is not from today
            if (!dateRequested.equals(today)) {
                continue;
            }

            int status = Integer.parseInt(rs.getStatus());

            if (status == 1) { //pending
                lastUsedRequestKey = rs.getID();
                lastUsedStatus = status;
                return "pending";
            }

            //get the most recent void/declined/pending request in the list
            if (status == 0 || status == 2 || status == 3) {
                if (latestResolvedRequest == null) {
                    latestResolvedRequest = rs;
                } else {
                    LocalDateTime latestTime = LocalDateTime.parse(latestResolvedRequest.getDateRequested(), formatter);
                    if (dateTimeRequested.isAfter(latestTime)) {
                        latestResolvedRequest = rs;
                    }
                }
            }
        }

        //tracks if the status is checked ONCE and re-assigns the last used status
        if (latestResolvedRequest != null) {
            int requestKey = latestResolvedRequest.getID();
            int status = Integer.parseInt(latestResolvedRequest.getStatus());

            //only return if either the requestKey is new OR the status changed
            if (requestKey != lastUsedRequestKey || status != lastUsedStatus) {
                lastUsedRequestKey = requestKey;
                lastUsedStatus = status;

                if (status == 0)
                    return "void";
                if (status == 2)
                    return "declined";
                if (status == 3)
                    return "approved";
            }
        }

        //deafult if no type of request
        return "standby";
    }

    public static int getLastUsedRequestKey() {
        return lastUsedRequestKey;
    }
}