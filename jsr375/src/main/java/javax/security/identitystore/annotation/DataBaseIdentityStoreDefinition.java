package javax.security.identitystore.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Target({TYPE, METHOD, FIELD, PARAMETER})
public @interface DataBaseIdentityStoreDefinition {

    String dataSourceLookup() default "java:comp/DefaultDataSource"; // default data source when omitted

    String callerQuery();

    String groupsQuery();

    String hashAlgorithm() default ""; // default no hash (for now) todo: make enum?

    String hashEncoding() default ""; // default no encoding (for now) todo: make enum?

}