package ru.mshchurkin.mirowidgetsapp.api.advice;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.zalando.problem.spring.web.advice.ProblemHandling;

/**
 * Exception handler to normalize errors in api calls
 */
@ControllerAdvice
public class ExceptionHandling implements ProblemHandling {
}
