package quickcore.common.utils;

/**
 * 路径相关工具
 */
public class PathUtil {
    /**
     * 拼接两个路径，返回路径首位为斜杠，末尾没有斜杠
     * @param path1
     * @param path2
     * @return java.lang.String
     * @author yangxiao
     * @date 2021/11/10 21:37
     */
    public static String joinPath(String path1, String path2) {
        if (StringUtils.isEmpty(path1)) {
            return path2.indexOf("/") == 0 ? path2 : "/" + path2;
        }
        if (StringUtils.isEmpty(path2)) {
            return path1.indexOf("/") == 0 ? path1 : "/" + path1;
        }

        if (path1.indexOf("/") != 0) {
            path1 = "/" + path1;
        }

        return removeSuffix(path1) + "/" + removePrefixSlash(path2);
    }

    public static String removePrefixSlash(String path) {
        if (StringUtils.isEmpty(path)) {
            return path;
        }

        if (path.indexOf("/") == 0) {
            return path.substring(1);
        }

        return path;
    }

    public static String removeSuffix(String path) {
        if (StringUtils.isEmpty(path)) {
            return path;
        }
        if (path.lastIndexOf("/") == (path.length() - 1)) {
            return path.substring(0, path.length() - 1);
        }

        return path;
    }
}
