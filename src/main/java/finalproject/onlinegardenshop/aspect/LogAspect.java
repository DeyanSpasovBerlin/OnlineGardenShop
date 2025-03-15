package finalproject.onlinegardenshop.aspect;

import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Log4j2
@Component
public class LogAspect {

    @Pointcut("within(finalproject.onlinegardenshop.service..*)")
    public void methodExecuting() {
    }

    @Before("methodExecuting()")
    public void beforeAdvice(JoinPoint joinPoint) {
        log.debug("Method {} from class {} started with args:{}",
                () -> joinPoint.getSignature().getName(),
                () -> joinPoint.getSourceLocation().getWithinType().getName(),
                () -> Arrays.toString(joinPoint.getArgs()));
    }

    @AfterReturning(value = "methodExecuting()", returning = "returningValue")
    public void recordSuccessfulExecution(JoinPoint joinPoint, Object returningValue) {
        if (returningValue != null) {
            log.debug("Method {} from class {} completed with return value: {}",
                    () -> joinPoint.getSignature().getName(),
                    () -> joinPoint.getSourceLocation().getWithinType().getName(),
                    () -> returningValue);
        } else {
            log.debug("Method {} from class {} completed",
                    () -> joinPoint.getSignature().getName(),
                    () -> joinPoint.getSourceLocation().getWithinType().getName());
        }
    }

    @AfterThrowing(value = "methodExecuting()", throwing = "exception")
    public void recordFailedExecution(JoinPoint joinPoint, Exception exception) {
        log.debug("Method {} from class {} produced exception: {}",
                () -> joinPoint.getSignature().getName(),
                () -> joinPoint.getSourceLocation().getWithinType().getName(),
                () -> exception);
    }

}
