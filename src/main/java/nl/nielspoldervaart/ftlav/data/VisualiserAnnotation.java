package nl.nielspoldervaart.ftlav.data;

import nl.nielspoldervaart.ftlav.visualiser.GraphLineColor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface VisualiserAnnotation {
	boolean isEnabledByDefault() default false;
	GraphLineColor defaultGraphLineColor() default GraphLineColor.PURPLE;
	TableColumnCategory category() default TableColumnCategory.MISC;
}
