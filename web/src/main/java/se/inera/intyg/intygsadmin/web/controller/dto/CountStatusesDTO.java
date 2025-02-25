package se.inera.intyg.intygsadmin.web.controller.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CountStatusesDTO {

    Integer count;
    Integer max;

}

