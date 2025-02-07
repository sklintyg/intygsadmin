package se.inera.intyg.intygsadmin.web.controller.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import se.inera.intyg.intygsadmin.web.service.status.NotificationStatusEnum;

@Getter
@Builder
public class SendStatusForCareGiverRequestDTO {

    private String careGiverId;
    private LocalDateTime start;
    private LocalDateTime end;
    private List<NotificationStatusEnum> status;
    private LocalDateTime activationTime;

}
