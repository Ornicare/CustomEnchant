package com.space.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.space.enums.LaunchPriority;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LaunchInfo {
	LaunchPriority priority() default LaunchPriority.NORMAL;
}
