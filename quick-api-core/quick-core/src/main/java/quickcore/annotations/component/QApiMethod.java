package quickcore.annotations.component;

import java.lang.annotation.*;

/**
 * Controller方法注解
 * <p>
 *     将请求的方法命名, 如果如果存在所属组，
 *     则直接将该方法归于所属组中的一级子菜单中
 * </p>
 * @author yangxiao
 */
@Documented
@Target(ElementType.METHOD)
@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface QApiMethod {
    // v1.0.0
    String value() default "";              // 方法名称
    String group() default "";              // 所属组
    String description() default "";        // 描述
    String version() default "";            // 版本
    String contentType() default "application/x-www-form-urlencoded";   // 请求类型
    boolean hidden() default false;         // 是否隐藏
    String author() default "";             // 作者
    String createTime() default "";         // 创建时间
    String updateTime() default "";         // 更新时间
    boolean download() default false;       // 是否为下载方法
    boolean token() default true;           // 是否需要token验证

}
