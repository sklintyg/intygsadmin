package se.inera.intyg.intygsadmin.web.controller.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import se.inera.intyg.intygsadmin.web.service.status.NotificationStatusEnum;

@Getter
@Builder
public class SendStatusForCertificatesRequestDTO {

    private List<String> certificateIds;
    private List<NotificationStatusEnum> status;
    private LocalDateTime activationTime;

}
