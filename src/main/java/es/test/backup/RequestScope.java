package es.test.backup;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.inject.Scope;
import org.glassfish.hk2.api.Proxiable;

import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.ElementType.METHOD;

@Documented
@Scope
@Proxiable
@Retention(RUNTIME)
@Target({ TYPE, METHOD })
public @interface RequestScope {
}
