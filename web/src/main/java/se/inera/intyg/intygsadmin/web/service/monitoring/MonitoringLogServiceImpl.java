/*
 * Copyright (C) 2024 Inera AB (http://www.inera.se)
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import se.inera.intyg.intygsadmin.logging.MdcCloseableMap;
import se.inera.intyg.intygsadmin.logging.MdcLogConstants;
import se.inera.intyg.intygsadmin.web.auth.AuthenticationMethod;

@Service("webMonitoringLogService")
public class MonitoringLogServiceImpl implements MonitoringLogService {

    private static final Object SPACE = " ";
    private static final Logger LOG = LoggerFactory.getLogger(MonitoringLogService.class);

    @Override
    public void logUserLogin(String userId, AuthenticationMethod authMethod) {
        try (MdcCloseableMap mdc =
            MdcCloseableMap.builder()
                .put(MdcLogConstants.EVENT_ACTION, toEventType(MonitoringEvent.USER_LOGIN))
                .put(MdcLogConstants.EVENT_TYPE, MdcLogConstants.EVENT_TYPE_INFO)
                .put(MdcLogConstants.EVENT_LOGIN_METHOD, authMethod.name())
                .put(MdcLogConstants.USER_ID, userId)
                .build()
        ) {
            logEvent(MonitoringEvent.USER_LOGIN, userId, authMethod);
        }
    }

    @Override
    public void logUserLogout(String userId, AuthenticationMethod authMethod) {
        try (MdcCloseableMap mdc =
            MdcCloseableMap.builder()
                .put(MdcLogConstants.EVENT_ACTION, toEventType(MonitoringEvent.USER_LOGOUT))
                .put(MdcLogConstants.EVENT_TYPE, MdcLogConstants.EVENT_TYPE_INFO)
                .put(MdcLogConstants.EVENT_LOGIN_METHOD, authMethod.name())
                .put(MdcLogConstants.USER_ID, userId)
                .build()
        ) {
        logEvent(MonitoringEvent.USER_LOGOUT, userId, authMethod);
        }
    }

    @Override
    public void logUserSessionExpired(String userId, AuthenticationMethod authMethod) {
        try (MdcCloseableMap mdc =
            MdcCloseableMap.builder()
                .put(MdcLogConstants.EVENT_ACTION, toEventType(MonitoringEvent.USER_SESSION_EXPIRY))
                .put(MdcLogConstants.EVENT_TYPE, MdcLogConstants.EVENT_TYPE_INFO)
                .put(MdcLogConstants.EVENT_LOGIN_METHOD, authMethod.name())
                .put(MdcLogConstants.USER_ID, userId)
                .build()
        ) {
            logEvent(MonitoringEvent.USER_SESSION_EXPIRY, userId, authMethod);
        }
    }

    @Override
    public void logFailedLogin(String exceptionMessage) {
        try (MdcCloseableMap mdc =
            MdcCloseableMap.builder()
                .put(MdcLogConstants.EVENT_ACTION, toEventType(MonitoringEvent.USER_LOGIN_FAIL))
                .put(MdcLogConstants.EVENT_TYPE, MdcLogConstants.EVENT_TYPE_INFO)
                .build()
        ) {
            logEvent(MonitoringEvent.USER_LOGIN_FAIL, exceptionMessage);
        }
    }

    private void logEvent(MonitoringEvent logEvent, Object... logMsgArgs) {
        LOG.info(buildMessage(logEvent), logMsgArgs);
    }

    private String buildMessage(MonitoringEvent logEvent) {
        StringBuilder logMsg = new StringBuilder();
        logMsg.append(logEvent.name()).append(SPACE).append(logEvent.getMessage());
        return logMsg.toString();
    }

    private String toEventType(MonitoringEvent monitoringEvent) {
        return monitoringEvent.name().toLowerCase().replace("_", "-");
    }

    private enum MonitoringEvent {
        USER_LOGIN("Login user '{}' using scheme '{}'"),
        USER_LOGOUT("Logout user '{}' using scheme '{}'"),
        USER_SESSION_EXPIRY("Session expired for user '{}' using scheme '{}'"),
        USER_LOGIN_FAIL("Login failed with message '{}'");

        private final String message;

        MonitoringEvent(String msg) {
            this.message = msg;
        }

        public String getMessage() {
            return message;
        }
    }
}
