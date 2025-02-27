package se.inera.intyg.intygsadmin.web.controller.dto;

import java.util.List;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Value;
import se.inera.intyg.intygsadmin.web.service.status.NotificationStatusEnum;

@Value
@Builder
@JsonDeserialize(builder = CountStatusesForCertificatesRequestDTO.CountStatusesForCertificatesRequestDTOBuilder.class)

public class CountStatusesForCertificatesRequestDTO {

    List<String> certificateIds;
    List<NotificationStatusEnum> statuses;

    @JsonPOJOBuilder(withPrefix = "")
    public static class CountStatusesForCertificatesRequestDTOBuilder {
    }
}