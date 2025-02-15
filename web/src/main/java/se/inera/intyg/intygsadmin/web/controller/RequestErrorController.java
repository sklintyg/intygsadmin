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
package se.inera.intyg.intygsadmin.web.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.WebAttributes;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import se.inera.intyg.intygsadmin.web.controller.dto.ApiErrorResponse;
import se.inera.intyg.intygsadmin.web.exception.IaAuthenticationException;
import se.inera.intyg.intygsadmin.web.exception.IaErrorCode;
import se.inera.intyg.intygsadmin.web.exception.IaServiceException;

/**
 * Custom ErrorController that overrides Spring boots default "whitepage" error handling.
 */
@RestController
@Slf4j
public class RequestErrorController implements ErrorController {

    public static final String IA_ERROR_CONTROLLER_PATH = "/error";
    public static final String IA_SPRING_SEC_ERROR_CONTROLLER_PATH = IA_ERROR_CONTROLLER_PATH + "/spring-sec-error";
    private static final String IA_CLIENT_ROOTPATH = "/#/";
    public static final String IA_CLIENT_EXIT_BOOT_PATH = IA_CLIENT_ROOTPATH + "exit/";
    private final ErrorAttributes errorAttributes = new DefaultErrorAttributes();

    /**
     * Intercept errors forwarded by a spring security AuthenticationFailureHandler.
     *
     * @param request - request
     * @return modelAndView - modelAndView
     */
    @RequestMapping(path = IA_SPRING_SEC_ERROR_CONTROLLER_PATH, produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView handleSpringSecurityErrorAsRedirect(WebRequest webRequest, HttpServletRequest request) {
        final String errorContext = getErrorContext(webRequest);
        // Check if Spring security saved an exception in request
        final Throwable error = (Throwable) request.getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        ApiErrorResponse apiErrorResponse;
        if (error != null) {
            apiErrorResponse = fromException(error);
        } else {
            apiErrorResponse = fromHttpStatus(getDispatcherErrorStatusCode(request));
        }
        String redirectView = "redirect:" + IA_CLIENT_EXIT_BOOT_PATH + apiErrorResponse.getErrorCode() + "/" + apiErrorResponse.getLogId();

        log.error(String.format("(Page) Spring Security error intercepted: %s, %s - responding with: %s",
            errorContext, apiErrorResponse, redirectView), error);
        return new ModelAndView(redirectView);
    }

    /**
     * For normal "browser navigation" initiated requests, we handle all error with a redirect to a specific
     * startup view to present an error.
     *
     * @param request - request
     * @return modelAndView - modelAndView
     */
    @RequestMapping(path = IA_ERROR_CONTROLLER_PATH, produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView handleErrorAsRedirect(WebRequest webRequest, HttpServletRequest request) {
        final String errorContext = getErrorContext(webRequest);
        final HttpStatus httpStatus = getDispatcherErrorStatusCode(request);
        final Throwable error = this.errorAttributes.getError(webRequest);
        ApiErrorResponse apiErrorResponse;

        // ApiErrorResponse apiErrorResponse = fromHttpStatus(getDispatcherErrorStatusCode(request));
        if (error != null) {
            apiErrorResponse = fromException(error);
        } else {
            apiErrorResponse = fromHttpStatus(httpStatus);
        }
        String redirectView = "redirect:" + IA_CLIENT_EXIT_BOOT_PATH + apiErrorResponse.getErrorCode() + "/" + apiErrorResponse.getLogId();

        log.error(String.format("(Page) Request error intercepted: %s - responding with: %s", errorContext, redirectView));
        return new ModelAndView(redirectView);
    }

    /**
     * For xhr-requests, we handle all errors by responding with a specific error code json struct that client can act on.
     *
     * @param request - request
     * @return responsEntity
     */
    @RequestMapping(path = IA_ERROR_CONTROLLER_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<ApiErrorResponse> handleErrorAsJsonResponse(WebRequest webRequest, HttpServletRequest request) {
        final HttpStatus httpStatus = getDispatcherErrorStatusCode(request);
        final String errorContext = getErrorContext(webRequest);
        final Throwable error = this.errorAttributes.getError(webRequest);
        ApiErrorResponse apiErrorResponse;
        if (error != null) {
            apiErrorResponse = fromException(error);
        } else {
            apiErrorResponse = fromHttpStatus(httpStatus);
        }
        log.error(String.format(
            "(REST) Request error intercepted: %s - responding with: %s", errorContext, apiErrorResponse), error);
        return new ResponseEntity<>(apiErrorResponse, httpStatus);
    }

    private String getErrorContext(WebRequest webRequest) {
        Map<String, Object> attributes = this.errorAttributes.getErrorAttributes(webRequest, ErrorAttributeOptions.defaults());
        return Arrays.toString(attributes.entrySet().toArray());
    }

    private HttpStatus getDispatcherErrorStatusCode(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        return (status != null) ? HttpStatus.valueOf(Integer.parseInt(status.toString())) : HttpStatus.INTERNAL_SERVER_ERROR;
    }

    private ApiErrorResponse fromException(Throwable error) {

        if (error instanceof IaServiceException) {
            final IaServiceException iaException = (IaServiceException) error;
            // A more fine grained IA exception thrown by our service layer
            return new ApiErrorResponse(iaException.getErrorCode().name(), error.getMessage(), iaException.getLogId());
        } else if (error instanceof AuthenticationException) {
            // Thrown by Spring security during authentication (login)

            if (error instanceof IaAuthenticationException) {
                return new ApiErrorResponse(
                    ((IaAuthenticationException) error).getErrorCode().name(),
                    error.getMessage(),
                    ((IaAuthenticationException) error).getLogId());
            } else {
                return new ApiErrorResponse(
                    IaErrorCode.LOGIN_FEL001.name(), error.getMessage(), UUID.randomUUID().toString());
            }
        } else {
            return new ApiErrorResponse(
                IaErrorCode.INTERNAL_ERROR.name(), error.getMessage(), UUID.randomUUID().toString());
        }
    }

    private ApiErrorResponse fromHttpStatus(final HttpStatus statusCode) {
        IaErrorCode errorCode = IaErrorCode.INTERNAL_ERROR;

        if (statusCode == HttpStatus.FORBIDDEN) {
            errorCode = IaErrorCode.LOGIN_FEL002;
        } else if (statusCode == HttpStatus.NOT_FOUND) {
            errorCode = IaErrorCode.NOT_FOUND;
        }
        return new ApiErrorResponse(errorCode.name(), "<no message>", UUID.randomUUID().toString());
    }

}
