/*
 * Copyright (C) 2025 Inera AB (http://www.inera.se)
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
package se.inera.intyg.intygsadmin.web.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.web.http.CookieSerializer;
import se.inera.intyg.infra.security.common.cookie.IneraCookieSerializer;

@Configuration
@EnableRedisHttpSession(redisNamespace = "${spring.session.redis.namespace}")
public class SessionConfig {

    @Autowired
    private JedisConnectionFactory jedisConnectionFactory;

    @Bean
    public CookieSerializer cookieSerializer() {
        /*
        This is needed to make IdP functionality work.
        This will not satisfy all browsers, but it works for IE, Chrome and Edge.
        Reference: https://auth0.com/blog/browser-behavior-changes-what-developers-need-to-know/
         */
        return new IneraCookieSerializer();
    }

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return jedisConnectionFactory;
    }

}
