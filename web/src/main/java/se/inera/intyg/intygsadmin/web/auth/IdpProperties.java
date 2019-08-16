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

package se.inera.intyg.intygsadmin.web.auth;

import java.net.URL;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@NoArgsConstructor
@ConfigurationProperties(prefix = "inera.idp")
public class IdpProperties {

    /**
     * The ID that this client is registered with at the OP.
     */
    private String clientId;

    /**
     * The password that this client has at the OP.
     */
    private String clientSecret;

    /**
     * The URL at the client (this application) where the OP should redirect the user when authentication is completed.
     * This URL must be registered at the OP.
     */
    private URL redirectUri;

    /**
     * The URL at the client (this application) where the OP should redirect the user when logout is completed.
     * This URL must be registered at the OP.
     */
    private URL logoutRedirectUri;

    /**
     * The ID of the OP/issuer.
     * This is normally a URL with /oidc at the end. The application will use this to access the OP's configuration
     * via the default path for .well-known-location
     */
    private URL issuerUri;

    /**
     * A list of claims that should be requested from this OP.
     */
    private List<String> requestedClaims = List.of("employeeHsaId", "given_name", "family_name");

}
