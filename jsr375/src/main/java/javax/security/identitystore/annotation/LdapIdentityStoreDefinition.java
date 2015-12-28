package javax.security.identitystore.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Target({TYPE, METHOD, FIELD, PARAMETER})
public @interface LdapIdentityStoreDefinition {

    String url() default "";

    String callerBaseDn() default "";

    String callerNameAttribute() default "uid";

    String groupBaseDn() default "";

    String groupNameAttribute() default "cn";

    String groupCallerDnAttribute() default "member";

}