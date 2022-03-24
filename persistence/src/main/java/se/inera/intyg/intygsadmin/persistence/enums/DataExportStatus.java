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
package se.inera.intyg.intygsadmin.persistence.enums;

import java.util.stream.Stream;

public enum DataExportStatus {

    CREATED("CREATED", "Notification delivery status success"),
    UPDATED("UPDATED", "Notification delivery status success"),
    DELETED("DELETED", "Notification delivery status success"),
    CREATING_PACKAGE("CREATING_PACKAGE", "Notification delivery status failure"),
    FINISHED_CREATING_PACKAGE("FINISHED_CREATING_PACKAGE", "Notification delivery status failure"),
    PACKAGE_DOWNLOADED("PACKAGE_DOWNLOADED", "Notification delivery status failure"),
    DOWNLOAD_CONFIRMED("PACKAGE_DOWNLOADED", "Notification delivery status failure"),
    CRYPTO_KEY_DELIVERED("CRYPTO_KEY_DELIVERED", "Notification delivery status resend");


    private final String value;
    private final String description;

    DataExportStatus(String value, String description) {
        this.value = value;
        this.description = description;
    }

    public String value() {
        return this.value;
    }

    public String description() {
        return this.description;
    }

    public static DataExportStatus fromValue(String value) {
        return Stream.of(values()).filter((s) -> value.equals(s.value())).findFirst()
            .orElseThrow(() -> new IllegalArgumentException(value));
    }
}
