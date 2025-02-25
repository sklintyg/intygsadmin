package se.inera.intyg.intygsadmin.web.controller.dto;

import java.time.LocalDateTime;
import java.util.List;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Value;
import se.inera.intyg.intygsadmin.web.service.status.NotificationStatusEnum;

@Value
@Builder
@JsonDeserialize(builder = CountStatusesForCareGiverRequestDTO.CountStatusesForCareGiverRequestDTOBuilder.class)
public class CountStatusesForCareGiverRequestDTO {

    String careGiverId;
    LocalDateTime start;
    LocalDateTime end;
    List<NotificationStatusEnum> statuses;

    @JsonPOJOBuilder(withPrefix = "")
    public static class CountStatusesForCareGiverRequestDTOBuilder {
    }
}