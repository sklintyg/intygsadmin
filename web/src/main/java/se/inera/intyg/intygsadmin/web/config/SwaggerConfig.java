/*
 * Copyright (C) 2020 Inera AB (http://www.inera.se)
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

import static springfox.documentation.schema.AlternateTypeRules.newRule;

import com.fasterxml.classmate.TypeResolver;
import java.lang.reflect.Type;
import java.util.Arrays;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.Ordered;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import springfox.documentation.builders.AlternateTypeBuilder;
import springfox.documentation.builders.AlternateTypePropertyBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Profile("dev")
@EnableSwagger2
@Configuration
public class SwaggerConfig {

    private final TypeResolver typeResolver;
    private final RepositoryRestConfiguration restConfiguration;

    public SwaggerConfig(TypeResolver typeResolver, RepositoryRestConfiguration restConfiguration) {
        this.typeResolver = typeResolver;
        this.restConfiguration = restConfiguration;
    }

    @Bean
    @ConditionalOnMissingBean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
            .alternateTypeRules(newRule(typeResolver.resolve(Pageable.class),
                pageableMixin(restConfiguration),
                Ordered.HIGHEST_PRECEDENCE))
            .select()
            .apis(RequestHandlerSelectors
                .basePackage("se.inera.intyg.intygsadmin.web.controller"))
            .paths(PathSelectors.regex("/.*"))
            .build();
    }

    private Type pageableMixin(RepositoryRestConfiguration restConfiguration) {
        return new AlternateTypeBuilder()
            .fullyQualifiedClassName(
                String.format("%s.generated.%s",
                    Pageable.class.getPackage().getName(),
                    Pageable.class.getSimpleName()))
            .withProperties(Arrays.asList(
                property(Integer.class,
                    restConfiguration.getPageParamName()),
                property(Integer.class,
                    restConfiguration.getLimitParamName()),
                property(String.class,
                    restConfiguration.getSortParamName())
            ))
            .build();
    }

    private AlternateTypePropertyBuilder property(Class<?> type, String name) {
        return new AlternateTypePropertyBuilder()
            .withName(name)
            .withType(type)
            .withCanRead(true)
            .withCanWrite(true);
    }
}
