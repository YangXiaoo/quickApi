package quickcore.annotations;

import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Import;
import quickcore.web.controller.QuickApiController;

import java.lang.annotation.*;

/**
 * SpringBoot项目启动类注解
 * @author yangxiao
 */
@Documented
@Target(ElementType.TYPE)
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Import({QuickApiController.class})
@ServletComponentScan
public @interface QuickApi {
    String basePackages() default "";               // 配置的扫描包
    String projectName() default "QuickApi接口";     // 项目名
    String description() default "v1.0.0";          // 项目描述
    String serverNames() default "";                // 后台服务器名, 多个服务器使用','连接
    String hostServiceName() default "";            // 远程服务器名
    String localServiceName() default "";           // 本地服务器名
    String version() default "v1.0.0";              // 版本
    String author() default "";                     // 作者
    boolean enabled() default true;                 // 是否支持开启, 默认开启, 生产环境需要关闭
}
