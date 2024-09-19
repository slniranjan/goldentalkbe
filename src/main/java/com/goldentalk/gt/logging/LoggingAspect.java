package com.goldentalk.gt.logging;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InaccessibleObjectException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;

@Aspect
@Component
public class LoggingAspect {

  private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

  @Before("execution(* com.goldentalk.gt.controller..*(..)) || execution(* com.goldentalk.gt.service..*(..))")
  public void logBefore(JoinPoint joinPoint) {

    MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
    Method method = methodSignature.getMethod();

    Annotation[][] paramAnnotations = method.getParameterAnnotations();
    Object[] args = joinPoint.getArgs();

    String pathVariableLog =
        IntStream.range(0, args.length).filter(i -> isPathVariable(paramAnnotations[i]))
            .mapToObj(i -> "PathVariable: " + args[i]).collect(Collectors.joining(", "));

    String filteredArgs =
        Arrays.stream(args).map(this::filteredFields).collect(Collectors.joining(", "));

    logger.info("Entering method: {} with Path Variables: [{}], Request Object: [{}]",
        method.getName(), pathVariableLog, filteredArgs);
  }

  @AfterReturning(pointcut = "execution(* com.goldentalk.gt.controller..*(..)) || execution(* com.goldentalk.gt.service..*(..))",
      returning = "result")
  public void logAfter(JoinPoint joinPoint, Object result) {
    logger.info("Exiting method: {} with result: {}", joinPoint.getSignature(), result);
  }

  private Set<String> ignoreList() {
    return Set.of("nic", "dob", "whatsAppNumber");
  }

  private String filteredFields(Object result) {
    StringBuilder filterdField = new StringBuilder();

    Field[] fields = result.getClass().getDeclaredFields();

    for (Field field : fields) {
      try {

        field.setAccessible(true);

        if (!ignoreList().contains(field.getName())) {
          filterdField.append(field.getName()).append("=").append(field.get(result)).append(", ");
        }
      } catch (IllegalArgumentException | IllegalAccessException | InaccessibleObjectException e) {
        // if error do nothing

      }
    }

    return filterdField.toString();
  }

  private boolean isPathVariable(Annotation[] paramAnnotations) {
    for (Annotation annotation : paramAnnotations) {
      if (annotation instanceof PathVariable) {
        return true;
      }
    }
    return false;
  }
}
