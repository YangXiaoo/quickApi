package quickcore.annotations.component;

import java.lang.annotation.*;

/**
 * Controller类注解
 * <p>
 *     注解在controller类上, 将请求分组. <br>
 *     前端会生成下拉菜单栏，同一个group的Controller类会放到一个组里面，
 *     并且按照sortIndex的值将value排序显示； 若没有sortIndex，默认根据
 *     value的大小从小到大进行排序.
 * </p>
 * @author yangxiao
 */
@Documented
@Target(ElementType.TYPE)
@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface QApi {
    // v1.0.0
    String value()  default "";             // 类名称
    String group() default "";              // 所属组
    String description() default "";        // 类描述
    String sortIndex() default "";          // 归类后的排序
    boolean hidden() default false;         // 是否隐藏
}
