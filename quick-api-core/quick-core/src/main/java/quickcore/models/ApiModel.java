package quickcore.models;

import java.util.ArrayList;
import java.util.List;

/**
 * QApi注解
 * @author yangxiao
 */
public class ApiModel implements Comparable<ApiModel>{
    private String name;    // 组别名
    private String description;
    private String group;
    List<MethodModel> methodModels = new ArrayList<>();
    private String value;   // 类名
    private String version;

    /**
     * 将对象按名称典序升序排序
     * @param o 当前对象
     * @return int
     */
    @Override
    public int compareTo(ApiModel o) {
        return this.value.compareTo(o.getValue());
    }

    public String getVersion() {
        return version;
    }
    public void setVersion(String version) {
        this.version = version;
    }
    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getGroup() {
        return group;
    }
    public void setGroup(String group) {
        this.group = group;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public List<MethodModel> getMethodModels() {
        return methodModels;
    }
    public void setMethodModels(List<MethodModel> methodModels) {
        this.methodModels = methodModels;
    }
}

