package org.dieschnittstelle.jee.esa.basics.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <h1>DisplayAs</h1>
 * [The DisplayAs description comes here]
 *
 * @author Kevin Mattutat
 * @version 05.10.2018
 * @since 05.10.2018
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface DisplayAs
{
    String value();
}
