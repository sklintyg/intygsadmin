/*
 * Copyright (C) 2022 Inera AB (http://www.inera.se)
 *
 * This file is part of sklintyg (https://github.com/sklintyg).
 *
 * sklintyg is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * sklintyg is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package se.inera.intyg.intygsadmin.web.service;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import se.inera.intyg.intygsadmin.persistence.entity.UserEntity;
import se.inera.intyg.intygsadmin.web.auth.AuthenticationMethod;
import se.inera.intyg.intygsadmin.web.auth.IntygsadminUser;
import se.inera.intyg.intygsadmin.web.controller.dto.CreateDataExportDTO;
import se.inera.intyg.intygsadmin.web.controller.dto.UpdateDataExportDTO;
import se.inera.intyg.intygsadmin.web.integration.TerminationRestService;
import se.inera.intyg.intygsadmin.web.integration.model.in.DataExportResponse;
import se.inera.intyg.intygsadmin.web.integration.model.out.CreateDataExport;

@ExtendWith(MockitoExtension.class)
class TerminationServiceImplTest {

    @Mock
    private TerminationRestService terminationRestService;

    @Captor
    private ArgumentCaptor<CreateDataExport> createDataExportArgumentCaptor;

    @Mock
    private UserService userService;

    @InjectMocks
    private TerminationServiceImpl terminationService;

    private static final UUID TERMINATION_ID_1 = UUID.fromString("f2b3c63b-dcb8-408a-b68f-1d88d779131e");
    private static final UUID TERMINATION_ID_2 = UUID.fromString("ddaba575-096e-4020-8b72-6bc3506faf02");
    private static final UUID TERMINATION_ID_3 = UUID.fromString("ec1b9858-cbb9-4a38-b996-49776037aeb5");

    private static final String TERMINATION_ID = "f2b3c63b-dcb8-408a-b68f-1d88d779131e";
    private static final String HSA_ID = "SE2325000098-TEST";
    private static final String PERSON_ID = "19121212-1212";
    private static final String EMAIL_ADDRESS = "email@address.se";
    private static final String PHONE_NUMBER = "070-12345678";

    @Nested
    class GetTerminationsTest {

        private final DataExportResponse termination1 = create(parse("2022-07-17T22:47:11"), TERMINATION_ID_1, "Örjan Varg");
        private final DataExportResponse termination2 = create(parse("2022-07-12T12:22:13"), TERMINATION_ID_2, "Anders Räv");
        private final DataExportResponse termination3 = create(parse("2022-07-17T16:26:12"), TERMINATION_ID_3, "Åke Vessla");
        private final List<DataExportResponse> terminations = List.of(termination1, termination2, termination3);

        @BeforeEach
        public void init() {
            when(terminationRestService.getDataExports()).thenReturn(terminations);
        }

        @Test
        public void shouldHandlePagingOfFetchedTerminations() {
            final var pageable1 = PageRequest.of(0, 2, Sort.by(Direction.DESC, "createdAt"));
            final var pageable2 = PageRequest.of(1, 2, Sort.by(Direction.DESC, "createdAt"));
            when(terminationRestService.getDataExports()).thenReturn(terminations);

            final var page1 = terminationService.getDataExports(pageable1);
            final var page2 = terminationService.getDataExports(pageable2);

            assertAll(
                () -> assertIterableEquals(List.of(termination1, termination3), page1.getContent()),
                () -> assertIterableEquals(List.of(termination2), page2.getContent()),
                () -> assertTrue(page1.hasNext()),
                () -> assertFalse(page2.hasNext())
            );
        }

        @Test
        public void shouldHandleEmptyTerminationsList() {
            final var terminationsEmpty = Collections.<DataExportResponse>emptyList();
            final var pageable = PageRequest.of(0, 10, Sort.by(Direction.DESC, "createdAt"));
            when(terminationRestService.getDataExports()).thenReturn(terminationsEmpty);

            final var page = terminationService.getDataExports(pageable);

            assertAll(
                () -> verify(terminationRestService, times(1)).getDataExports(),
                () -> assertNotNull(page),
                () -> assertEquals(0, page.getContent().size())
            );
        }

        @Test
        public void shouldHandleDescendingSort() {
            final var pageable = PageRequest.of(0, 10, Sort.by(Direction.DESC, "createdAt"));

            final var page = terminationService.getDataExports(pageable);

            assertIterableEquals(List.of(termination1, termination3, termination2), page.getContent());
        }

        @Test
        public void shouldHandleAscendingSort() {
            final var pageable = PageRequest.of(0, 10, Sort.by(Direction.ASC, "createdAt"));

            final var page = terminationService.getDataExports(pageable);

            assertIterableEquals(List.of(termination2, termination3, termination1), page.getContent());
        }

        @Test
        public void shouldHandleSwedishCharsInDescendingSort() {
            final var pageable = PageRequest.of(0, 10, Sort.by(Direction.DESC, "creatorName"));

            final var page = terminationService.getDataExports(pageable);

            assertIterableEquals(List.of(termination1, termination3, termination2), page.getContent());
        }

        @Test
        public void shouldHandleSwedishCharsInAscendingSort() {
            final var pageable = PageRequest.of(0, 10, Sort.by(Direction.ASC, "creatorName"));

            final var page = terminationService.getDataExports(pageable);

            assertIterableEquals(List.of(termination2, termination3, termination1), page.getContent());
        }

        @Test
        public void shouldSortByCreatedDescendingWhenPrimarySortColumnWithEqualValues() {
            final var pageable = PageRequest.of(0, 10, Sort.by(Direction.ASC, "status"));

            final var page = terminationService.getDataExports(pageable);

            assertIterableEquals(List.of(termination1, termination3, termination2), page.getContent());
        }

        @ParameterizedTest
        @ValueSource(strings = {"createdAt", "creatorName", "terminationId", "status", "careProviderHsaId", "organizationNumber",
            "representativePersonId", "representativeEmailAddress", "representativePhoneNumber"})
        void shouldHandleSortForAllColumns(String columnName) {
            final var pageable = PageRequest.of(0, 10, Sort.by(Direction.DESC, columnName));

            final var page = terminationService.getDataExports(pageable);

            assertNotNull(page);
            verify(terminationRestService, times(1)).getDataExports();
            assertIterableEquals(List.of(termination1, termination3, termination2), page.getContent());
        }
    }

    @Test
    void testCreateDataExport() {

        UserEntity userEntity = new UserEntity();
        userEntity.setName("test");
        userEntity.setEmployeeHsaId("Nmågot hsa-id");
        AuthenticationMethod authenticationMethod= AuthenticationMethod.FAKE;
        OAuth2AccessToken oAuth2AccessToken = new DefaultOAuth2AccessToken("Token");
        IntygsadminUser intygsadminUser = new IntygsadminUser(userEntity, authenticationMethod, oAuth2AccessToken);

        CreateDataExportDTO createDataExportDTO = new CreateDataExportDTO();
        createDataExportDTO.setHsaId("1");
        createDataExportDTO.setPersonId("2");
        createDataExportDTO.setPhoneNumber("3");
        createDataExportDTO.setEmailAddress("4");
        createDataExportDTO.setOrganizationNumber("5");

        when(userService.getActiveUser()).thenReturn(intygsadminUser);
        when(terminationRestService.createDataExport(any(CreateDataExport.class))).thenReturn(new DataExportResponse());

        assertNotNull(terminationService.createDataExport(createDataExportDTO));

        verify(userService, times(1)).getActiveUser();
        verify(terminationRestService, times(1)).createDataExport(any(CreateDataExport.class));
        verify(terminationRestService).createDataExport(createDataExportArgumentCaptor.capture());

        CreateDataExport createdCreateDataExport = createDataExportArgumentCaptor.getValue();
        assertEquals(createdCreateDataExport.getCreatorName() , userEntity.getName());
        assertEquals(createdCreateDataExport.getCreatorHSAId() , userEntity.getEmployeeHsaId());

        assertEquals(createDataExportDTO.getHsaId() , createDataExportDTO.getHsaId());
        assertEquals(createDataExportDTO.getPersonId() , createDataExportDTO.getPersonId());
        assertEquals(createDataExportDTO.getPhoneNumber() , createDataExportDTO.getPhoneNumber());
        assertEquals(createDataExportDTO.getEmailAddress() , createDataExportDTO.getEmailAddress());
        assertEquals(createDataExportDTO.getOrganizationNumber() , createDataExportDTO.getOrganizationNumber());
    }

    @Test
    void shouldCallRestServiceWithProperUpdateInformation() {
        final var dataExportResponse = createDataExportResponse();

        final var updateExportCaptor = ArgumentCaptor.forClass(UpdateDataExportDTO.class);
        when(terminationRestService.updateDataExport(eq(TERMINATION_ID), any(UpdateDataExportDTO.class))).thenReturn(dataExportResponse);

        assertNotNull(terminationService.updateDataExport(dataExportResponse));

        verify(terminationRestService, times(1)).updateDataExport(eq(TERMINATION_ID), updateExportCaptor.capture());
        assertAll (
            () -> assertEquals(HSA_ID, updateExportCaptor.getValue().getHsaId()),
            () -> assertEquals(PERSON_ID, updateExportCaptor.getValue().getPersonId()),
            () -> assertEquals(EMAIL_ADDRESS, updateExportCaptor.getValue().getEmailAddress()),
            () -> assertEquals(PHONE_NUMBER, updateExportCaptor.getValue().getPhoneNumber())
        );
    }

    @Test
    void testErase() {
        String terminationId = "201d403d-7bcb-4017-a529-0309bb6693a2";
        String responseStatus = "Avslutad";
        when(terminationRestService.eraseDataExport(terminationId)).thenReturn(responseStatus);

        assertNotNull(terminationService.eraseDataExport(terminationId));

        verify(terminationRestService, times(1)).eraseDataExport(terminationId);
    }

    private DataExportResponse create(LocalDateTime time, UUID id, String creator) {
        final var names = creator.split(" ");
        final var termination = new DataExportResponse();
        termination.setTerminationId(id);
        termination.setCreated(time);
        termination.setStatus("Notifiering skickad");
        termination.setCreatorName(creator);
        termination.setPhoneNumber("031-21222324");
        termination.setEmailAddress(names[0] + "@" + names[1] + ".se");
        termination.setOrganizationNumber("1123456-1234");
        termination.setPersonId("191212121212");
        termination.setHsaId("SE23100000076-45R");
        return termination;
    }

    private LocalDateTime parse(String time) {
        return LocalDateTime.parse(time);
    }

    @Test
    void resendDataExportKey() {
        String terminationId = "201d403d-7bcb-4017-a529-0309bb6693a2";
        String responseStatus = "Kryptonyckel skickad igen";
        when(terminationRestService.resendDataExportKey(terminationId)).thenReturn(responseStatus);

        assertNotNull(terminationService.resendDataExportKey(terminationId));

        verify(terminationRestService, times(1)).resendDataExportKey(terminationId);
    }

    private DataExportResponse createDataExportResponse() {
        final var dataExportResponse = new DataExportResponse();
        dataExportResponse.setTerminationId(TERMINATION_ID_1);
        dataExportResponse.setHsaId(HSA_ID);
        dataExportResponse.setPersonId(PERSON_ID);
        dataExportResponse.setEmailAddress(EMAIL_ADDRESS);
        dataExportResponse.setPhoneNumber(PHONE_NUMBER);
        return dataExportResponse;
    }
}
