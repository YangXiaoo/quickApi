package quickcore.core.scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import quickcore.core.utils.RequestUtil;
import quickcore.core.utils.StringUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 包扫描
 * @author yangxiao
 */
public class PackageScanner {
    private static final Logger logger = LoggerFactory.getLogger(PackageScanner.class);

    private String basePackage;
    private ClassLoader cl;

    /**
     * 初始化1
     * @param basePackage 基础包名
     */
    public PackageScanner(String basePackage) {
        this.basePackage = basePackage;
        this.cl = getClass().getClassLoader();
    }

    /**
     * 初始化2
     * @param basePackage 基础包名
     * @param cl 类装载器
     */
    public PackageScanner(String basePackage, ClassLoader cl) {
        this.basePackage = basePackage;
        this.cl = cl;
    }

    /**
     * 获取指定包下的所有字节码文件的全类名
     * @return list 字节码文件名集合
     */
    public List<String> getFullyQualifiedClassNameList() throws IOException {
        return doScan(basePackage, new ArrayList<>());
    }

    /**
     * doScan函数
     * @param basePackage 基础包名
     * @param nameList 名称列表
     * @return list 字节码文件名集合
     * @throws IOException 异常
     */
    private List<String> doScan(String basePackage, List<String> nameList) throws IOException {
        String splashPath = StringUtils.dotToSplash(basePackage);
        Enumeration<URL> resources = cl.getResources(splashPath);

        if (resources == null) {
            return nameList;
        }

        while(resources.hasMoreElements()) {
            URL url = resources.nextElement();
            String path = java.net.URLDecoder.decode(url.getFile(),"utf-8");
            String filePath = StringUtils.getRootPath(path);
            List<String> names;
            if (isJarFile(filePath)) {
                names = readFromJarFile(filePath, splashPath);
                for (String name : names) {
                    if (isClassFile(name)) {
                        String s1 = name.substring(0, name.lastIndexOf(".class"));
                        if (!nameList.contains(s1)) {
                            nameList.add(s1);
                        }
                    } else {
                        doScan(name, nameList);
                    }
                }
            } else {
                names = readFromDirectory(filePath);
                if (names != null) {
                    for (String name : names) {
                        if (isClassFile(name)) {
                            String s1 = toFullyQualifiedName(name, basePackage);
                            if (!nameList.contains(s1)) {
                                nameList.add(s1);
                            }
                        } else {
                            doScan(basePackage + "." + name, nameList);
                        }
                    }
                }
            }
        }
        return nameList;
    }

    /**
     * 文件路径格式转换
     * @param shortName shortName
     * @param basePackage basePackage
     * @return string string
     */
    private String toFullyQualifiedName(String shortName, String basePackage) {
        StringBuilder sb = new StringBuilder(basePackage);
        sb.append('.');
        sb.append(StringUtils.trimExtension(shortName));
        return sb.toString();
    }

    /**
     * 读取jar里面的文件
     * @param jarPath jar包名
     * @param splashedPackageName jar包路径
     * @return list 集合
     * @throws IOException 异常
     */
    private List<String> readFromJarFile(String jarPath, String splashedPackageName) throws IOException {
        JarFile jarFile = new JarFile(new File(jarPath));
        Enumeration<JarEntry> entries = jarFile.entries();
        List<String> nameList = new ArrayList<>();
        while (entries.hasMoreElements()) {
            JarEntry entry = entries.nextElement();
            String name = entry.getName();
            if (name.contains(splashedPackageName) && isClassFile(name)) {
                name = name.substring(name.indexOf(splashedPackageName)).replaceAll("\\/",".");
                nameList.add(name);
            }
        }
        return nameList;
    }

    /**
     * 读取指定目录里的文件
     * @param path 路径
     * @return list 集合
     */
    private List<String> readFromDirectory(String path) {
        if (path == null) return new ArrayList<>();
        File file = new File(path);
        String[] names = file.list();

        if (null == names) {
            return null;
        }

        return Arrays.asList(names);
    }

    /**
     * 判断是否是字节码文件
     * @param name 文件名
     * @return boolean
     */
    private boolean isClassFile(String name) {
        if (name == null) return false;
        return name.endsWith(".class");
    }

    /**
     * 判断是否是jar包文件
     * @param name
     * @return boolean
     */
    private boolean isJarFile(String name) {
        if (name == null) return false;
        return name.endsWith(".jar");
    }
}
