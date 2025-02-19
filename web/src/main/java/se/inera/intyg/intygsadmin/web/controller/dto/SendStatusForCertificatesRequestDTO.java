package se.inera.intyg.intygsadmin.web.controller.dto;

import java.util.List;
import lombok.Builder;
import lombok.Value;
import se.inera.intyg.intygsadmin.web.service.status.NotificationStatusEnum;

@Value
@Builder
public class SendStatusForCertificatesRequestDTO {

    List<String> certificateIds;
    List<NotificationStatusEnum> status;
}
