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
package se.inera.intyg.intygsadmin.web.auth.filter;

import com.nimbusds.openid.connect.sdk.ClaimsRequest;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter;
import org.springframework.security.oauth2.client.resource.UserRedirectRequiredException;

/**
 * This filter extends the OAuth2ClientContextFilter.
 * It adds functionality of specifying individual claims in the authentications request
 * according to the specification https://openid.net/specs/openid-connect-core-1_0.html
 */
public class IndividualClaimsOuth2ContextFilter extends OAuth2ClientContextFilter {

    private List<String> specificClaims;

    public IndividualClaimsOuth2ContextFilter(List<String> specificClaims) {
        super();
        this.specificClaims = specificClaims;
    }

    @Override
    protected void redirectUser(UserRedirectRequiredException e,
        HttpServletRequest request, HttpServletResponse response)
        throws IOException {

        if (specificClaims != null && !specificClaims.isEmpty()) {
            ClaimsRequest claimsRequest = new ClaimsRequest();
            for (String specificClaim : specificClaims) {
                claimsRequest.addIDTokenClaim(specificClaim);
            }

            // Extend the Exception with our custom claims request
            Map<String, String> requestParams = e.getRequestParams();
            requestParams.put("claims", claimsRequest.toString());
        }

        super.redirectUser(e, request, response);

    }
}
