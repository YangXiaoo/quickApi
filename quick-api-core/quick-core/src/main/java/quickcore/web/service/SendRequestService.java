package quickcore.web.service;

import org.springframework.web.bind.annotation.*;
import quickcore.common.tools.JsonModel;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 前端请求服务器数据
 * @author yangxiao
 */
@RestController
@RequestMapping("/service")
public class SendRequestService {
    /**
     * @param map 查询条件
     *            path 接口方法在项目中的路由
     *            projectName 方法名
     * @return quickcore.common.tools.JsonModel
     * @author yangxiao
     * @date 2021/1/3 22:16
     */
    @PostMapping(value = "getMethodApiData")
    public JsonModel getPageData(@RequestBody Map<String, String> map) {
        JsonModel jsonModel = new JsonModel();
        try {
            String path = map.get("path");
            String projectName = map.get("projectName");

            jsonModel.success("获得数据成功", "");    // todo
        } catch (Exception e) {
            jsonModel.error(e.getLocalizedMessage());
        }

        return jsonModel;
    }

    /**
     * @param map 查询条件
     *            projectName 方法名
     * @return quickcore.common.tools.JsonModel
     * @author yangxiao
     * @date 2021/1/3 22:18
     */
    @PostMapping(value = "getProjectData")
    public JsonModel getProjectData(@RequestBody Map<String, String> map) {
        JsonModel jsonModel = new JsonModel();
        try {
            String projectName = map.get("projectName");
            jsonModel.success("获得数据成功", "");    // todo
        } catch (Exception e) {
            jsonModel.error(e.getLocalizedMessage());
        }

        return jsonModel;
    }

    /**
     * @param map 请求参数
     *            path 接口路由
     *            data 保存的数据
     * @return void
     * @author yangxiao
     * @date 2021/1/3 22:19
     */
    @PostMapping(value = "savePageData")
    public void saveData(@RequestBody Map<String, Object> map) {
        String path = (String) map.get("path");
        System.out.println(path);
        // todo
    }

    /**
     * 保存接口方法信息
     * @param map
     * @param request
     * @return quickcore.common.tools.JsonModel
     * @author yangxiao
     * @date 2020/12/27 22:18
     */
    @RequestMapping(value = "saveMethodData")
    public JsonModel saveApiInfo(@RequestBody Map<String, String> map, HttpServletRequest request) {
        JsonModel jsonModel = new JsonModel();
        try {
            String projectName = map.get("path");
            // TODO
            jsonModel.success("获得数据成功", "");
        } catch (Exception e) {
            jsonModel.error(e.getLocalizedMessage());
        }

        return jsonModel;
    }

    /**
     * 同步本地api数据到服务器
     * @param map
     * @return quickcore.common.tools.JsonModel
     * @author yangxiao
     * @date 2020/12/27 22:23
     */
    @RequestMapping(value = "pushLocalData")
    public JsonModel pushLocalData(@RequestBody Map<String, String> map) {
        JsonModel jsonModel = new JsonModel();
        try {
            String projectName = map.get("path");
            // TODO
            jsonModel.success("获得数据成功", "");
        } catch (Exception e) {
            jsonModel.error(e.getLocalizedMessage());
        }

        return jsonModel;
    }
}
