package com.sirius.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 登录验证(允许访问)
 * 
 * @author dohko
 * 
 */

@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Token {

}
