package se.inera.intyg.intygsadmin.web.controller.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Value;
import se.inera.intyg.intygsadmin.web.service.status.NotificationStatusEnum;

@Value
@Builder
public class CountStatusesForCareGiverRequestDTO {

    String careGiverId;
    LocalDateTime start;
    LocalDateTime end;
    List<NotificationStatusEnum> status;

}
