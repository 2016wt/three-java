package com.sirius.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 用于移动端实体店主登录验证(不允许访问)
 * 
 * @author dohko
 * 
 */

@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ShopkeeperApp {

}
