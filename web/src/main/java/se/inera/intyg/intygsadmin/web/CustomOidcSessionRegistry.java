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

package se.inera.intyg.intygsadmin.web;

import org.springframework.security.oauth2.client.oidc.authentication.logout.OidcLogoutToken;
import org.springframework.security.oauth2.client.oidc.session.OidcSessionInformation;
import org.springframework.security.oauth2.client.oidc.session.OidcSessionRegistry;

//public class CustomOidcSessionRegistry implements OidcSessionRegistry {
//  private final OidcProviderSessionRepository sessions;
//
//  // ...
//
//  @Override
//  public void saveSessionInformation(OidcSessionInformation info) {
//    this.sessions.save(info);
//  }
//
//  @Override
//  public OidcSessionInformation removeSessionInformation(String clientSessionId) {
//    return this.sessions.removeByClientSessionId(clientSessionId);
//  }
//
//  @Override
//  public Iterable<OidcSessionInformation> removeSessionInformation(OidcLogoutToken token) {
//    return token.getSessionId() != null ?
//        this.sessions.removeBySessionIdAndIssuerAndAudience(...) :
//    this.sessions.removeBySubjectAndIssuerAndAudience(...);
//  }
//}