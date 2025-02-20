package se.inera.intyg.intygsadmin.web.controller.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Value;
import se.inera.intyg.intygsadmin.web.service.status.NotificationStatusEnum;

@Value
@Builder
@JsonDeserialize(builder = CountStatusesForUnitsRequestDTO.CountStatusesForUnitsRequestDTOBuilder.class)
public class CountStatusesForUnitsRequestDTO {

    List<String> unitIds;
    LocalDateTime start;
    LocalDateTime end;
    List<NotificationStatusEnum> status;

    @JsonPOJOBuilder(withPrefix = "")
    public static class CountStatusesForUnitsRequestDTOBuilder {
    }
}