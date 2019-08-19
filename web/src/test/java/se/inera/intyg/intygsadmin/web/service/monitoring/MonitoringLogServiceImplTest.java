/*
 * Copyright (C) 2019 Inera AB (http://www.inera.se)
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

package se.inera.intyg.intygsadmin.web.service.monitoring;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.inera.intyg.intygsadmin.web.auth.AuthenticationMethod;

@ExtendWith(MockitoExtension.class)
public class MonitoringLogServiceImplTest {

    @Mock
    private Appender<ILoggingEvent> appender;

    @Captor
    private ArgumentCaptor<ILoggingEvent> captor;

    @InjectMocks
    private MonitoringLogServiceImpl monitoringLogService;

    @BeforeEach
    public void setup() {
        ch.qos.logback.classic.Logger logger = (ch.qos.logback.classic.Logger) LoggerFactory
            .getLogger(Logger.ROOT_LOGGER_NAME);
        logger.addAppender(appender);
    }

    @Test
    public void testLogUserLogin() {
        monitoringLogService.logUserLogin("HSA1", AuthenticationMethod.FAKE);

        verify(appender, times(1)).doAppend(captor.capture());

        List<ILoggingEvent> allValues = captor.getAllValues();
        assertNotNull(allValues);
        assertEquals(1, allValues.size());

        ILoggingEvent iLoggingEvent = allValues.get(0);

        assertEquals(Level.INFO, iLoggingEvent.getLevel());

        // This must match the Logtash filter/pattern. Changes must bu reflected in these as well.
        assertEquals("USER_LOGIN Login user 'HSA1' using scheme 'FAKE'", iLoggingEvent.getFormattedMessage());
    }

    @Test
    public void testLogUserLogout() {
        monitoringLogService.logUserLogout("HSA1", AuthenticationMethod.FAKE);

        verify(appender, times(1)).doAppend(captor.capture());

        List<ILoggingEvent> allValues = captor.getAllValues();
        assertNotNull(allValues);
        assertEquals(1, allValues.size());

        ILoggingEvent iLoggingEvent = allValues.get(0);

        assertEquals(Level.INFO, iLoggingEvent.getLevel());

        // This must match the Logtash filter/pattern. Changes must bu reflected in these as well.
        assertEquals("USER_LOGOUT Logout user 'HSA1' using scheme 'FAKE'", iLoggingEvent.getFormattedMessage());
    }

    @Test
    public void testLogUserSessionExpired() {
        monitoringLogService.logUserSessionExpired("HSA1", AuthenticationMethod.FAKE);

        verify(appender, times(1)).doAppend(captor.capture());

        List<ILoggingEvent> allValues = captor.getAllValues();
        assertNotNull(allValues);
        assertEquals(1, allValues.size());

        ILoggingEvent iLoggingEvent = allValues.get(0);

        assertEquals(Level.INFO, iLoggingEvent.getLevel());

        // This must match the Logtash filter/pattern. Changes must bu reflected in these as well.
        assertEquals("USER_SESSION_EXPIRY Session expired for user 'HSA1' using scheme 'FAKE'", iLoggingEvent.getFormattedMessage());
    }

}
