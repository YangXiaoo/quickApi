<template>
  <div>
    <el-card>
      <div style="margin-top: 70px">
        <div>
          {{ methodName }}
          <i class="el-icon-edit" @click="handleMethodChangeClick"> </i>
        </div>
        <el-row>
          <el-col :span="4">
            <el-select
              v-model="selectType"
              placeholder="请选择"
              @change="handleSelectTypeClick"
            >
              <el-option label="POST" value="POST"></el-option>
              <el-option label="GET" value="GET"></el-option>
              <el-option label="PUT" value="PUT"></el-option>
              <el-option label="DELETE" value="DELETE"></el-option>
            </el-select>
          </el-col>
          <el-col :span="16">
            <el-input
              placeholder="请输入内容"
              v-model="path"
              class="input-with-select"
            ></el-input>
          </el-col>
          <el-col :span="2">
            <el-button
              type="primary"
              style="margin-left: 15px"
              @click="handleSendRequest"
            >
              发送
            </el-button>
          </el-col>
          <el-col :span="2">
            <el-button type="primary" @click="handleClickSave"
              >保存<i class="el-icon-upload el-icon--right"
            /></el-button>
          </el-col>
        </el-row>
      </div>
      <div class="option-bar" style="margin-top: 15px">
        <el-tabs v-model="requestActiveName" @tab-click="handleClickRequest">
          <el-tab-pane label="Param" name="Param">
            <vue-json-editor
              v-model="paramData"
              :showBtns="false"
              :mode="'code'"
              lang="zh"
              @json-change="onParamChange"
            />
          </el-tab-pane>
          <el-tab-pane label="Header" name="Header">
            <vue-json-editor
              v-model="headerJson"
              :showBtns="false"
              :mode="'code'"
              lang="zh"
              @json-change="onHeaderChange"
            />
          </el-tab-pane>
          <el-tab-pane label="Body" name="Body">
            <el-radio-group
              v-model="contentType"
              @change="changeBodyType"
              style="margin-bottom: 10px"
            >
              <el-radio label="none">none</el-radio>
              <el-radio label="application/json">application/json</el-radio>
              <el-radio label="bodyFile">文件</el-radio>
            </el-radio-group>
            <vue-json-editor
              v-show="bodyNoneShow"
              v-model="bodyStringData"
              :showBtns="false"
              :mode="'code'"
              lang="zh"
              @json-change="onBodyChange"
            />
            <vue-json-editor
              v-show="bodyJsonShow"
              v-model="bodyJsonData"
              :showBtns="false"
              :mode="'code'"
              lang="zh"
              @json-change="onBodyChange"
            />
            <el-card class="body-file-box" v-show="bodyFileShow">
              <el-upload
                class="body-upload-file"
                action=""
                multiple
                :limit="1"
                :file-list="fileList"
              >
                <el-button size="small" type="primary">点击上传</el-button>
                <div slot="tip" class="el-upload__tip">可上传任意格式文件</div>
              </el-upload>
            </el-card>
          </el-tab-pane>
        </el-tabs>
      </div>
      <el-divider>Response</el-divider>
      <div class="request-result" style="margin-top: 15px">
        <el-tabs v-model="responseActiveName" @tab-click="handleClickResponse">
          <el-tab-pane label="Body" name="Body">
            <vue-json-editor
              v-model="responseBody"
              :showBtns="false"
              :mode="'code'"
              lang="zh"
              @json-change="onResponseBodyChange"
            />
          </el-tab-pane>
          <!-- <el-tab-pane label="Cookies" name="Cookies">
            <el-card class="response-cookies-box"> 没有Cookies </el-card>
          </el-tab-pane> -->
          <el-tab-pane label="Headers" name="Headers">
            <vue-json-editor
              v-model="responseHeader"
              :showBtns="false"
              :mode="'code'"
              lang="zh"
              @json-change="onResponseHeaderChange"
            />
          </el-tab-pane>
        </el-tabs>
      </div>
    </el-card>

    <!-- 保存弹框 -->
    <el-dialog title="完善文档" :visible.sync="dialogTableVisible">
      <div v-show="headerJson">
        <el-divider>请求头说明</el-divider>
        <el-table :data="headerJsonValues" border fit highlight-current-row>
          <!-- 参数名 -->
          <el-table-column label="参数名">
            <template slot-scope="{ row }">
              <span> {{ row.name }} </span>
            </template>
          </el-table-column>
          <!-- 参数值 -->
          <el-table-column label="参数值">
            <template slot-scope="{ row }">
              <span> {{ row.value }} </span>
            </template>
          </el-table-column>
          <!-- 参数说明 -->
          <el-table-column label="参数说明">
            <template slot-scope="{ row }">
              <el-input v-model="row.description" size="small" />
            </template>
          </el-table-column>
        </el-table>
      </div>
      <div v-show="paramData">
        <el-divider>get参数说明</el-divider>
        <el-table :data="paramValues" border fit highlight-current-row>
          <!-- 参数名 -->
          <el-table-column label="参数名">
            <template slot-scope="{ row }">
              <span> {{ row.name }} </span>
            </template>
          </el-table-column>
          <!-- 参数值 -->
          <el-table-column label="参数值">
            <template slot-scope="{ row }">
              <span> {{ row.value }} </span>
            </template>
          </el-table-column>
          <!-- 参数说明 -->
          <el-table-column label="参数说明">
            <template slot-scope="{ row }">
              <el-input v-model="row.description" size="small" />
            </template>
          </el-table-column>
        </el-table>
      </div>
      <div v-show="bodyJsonData">
        <el-divider>POST参数说明</el-divider>
        <el-table :data="bodyJsonDataValues" border fit highlight-current-row>
          <!-- 参数名 -->
          <el-table-column label="参数名">
            <template slot-scope="{ row }">
              <span> {{ row.name }} </span>
            </template>
          </el-table-column>
          <!-- 参数值 -->
          <el-table-column label="参数值">
            <template slot-scope="{ row }">
              <span> {{ row.value }} </span>
            </template>
          </el-table-column>
          <!-- 参数说明 -->
          <el-table-column label="参数说明">
            <template slot-scope="{ row }">
              <el-input v-model="row.description" size="small" />
            </template>
          </el-table-column>
        </el-table>
      </div>
      <div v-show="responseBody">
        <el-divider>响应值说明</el-divider>
        <el-table :data="responseBodyValues" border fit highlight-current-row>
          <!-- 参数名 -->
          <el-table-column label="参数名">
            <template slot-scope="{ row }">
              <span> {{ row.name }} </span>
            </template>
          </el-table-column>
          <!-- 参数值 -->
          <el-table-column label="参数值">
            <template slot-scope="{ row }">
              <span> {{ row.value }} </span>
            </template>
          </el-table-column>
          <!-- 参数说明 -->
          <el-table-column label="参数说明">
            <template slot-scope="{ row }">
              <el-input v-model="row.description" size="small" />
            </template>
          </el-table-column>
        </el-table>
      </div>
      <div v-show="responseHeader">
        <el-divider>响应头说明</el-divider>
        <el-input
          type="textarea"
          :rows="4"
          placeholder="请输入说明"
          v-model="responseHeaderDesc"
        />
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogTableVisible = false">取 消</el-button>
        <el-button type="primary" @click="handleSaveData">确 定</el-button>
      </div>
    </el-dialog>
    <el-dialog title="修改方法名" :visible.sync="dialogApiInfoVisible">
      <el-form>
        <el-form-item label="所属组">
          <el-select
            v-model="methodGroup"
            filterable
            allow-create
            default-first-option
            placeholder="请选择组别"
          >
            <el-option
              v-for="item of groupList"
              :key="item"
              :label="item"
              :value="item"
            >
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="方法名">
          <el-input v-model="methodName"> </el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogApiInfoVisible = false">取 消</el-button>
        <el-button type="primary" @click="handleSaveMethodName"
          >确 定</el-button
        >
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { mapGetters } from "vuex";
import vueJsonEditor from "vue-json-editor";
import { callApi } from "@/api/request";
import { setData, getData } from "@/utils/storage";
import { getPageData, savePageData, saveApiInfo } from "@/api/apiInfo";
import { getRoutes } from "@/utils/routerTool";
export default {
  name: "Request",
  components: {
    vueJsonEditor,
  },
  computed: {
    ...mapGetters(["localServiceName", "pageStatus", "routes", "groupList"]),
  },
  data() {
    return {
      requestActiveName: "Body",
      responseActiveName: "Body",
      path: "",
      paramData: null,
      bodyJsonData: null,
      bodyStringData: null,
      fileList: [],
      headerJson: null,
      responseBody: null,
      responseHeader: null,
      requestServiceName: "",
      selectType: "POST",
      contentType: "none",
      bodyNoneShow: true,
      bodyJsonShow: false,
      bodyFileShow: false,

      dialogTableVisible: false, // 文档保存弹框
      paramValues: [], // 存储所有参数
      bodyJsonDataValues: [], // post请求参数说明
      bodyStringDataValues: [],
      headerJsonValues: [], // 头参数说明
      responseBodyValues: [],
      responseHeaderDesc: [],

      // 方法名修改
      dialogApiInfoVisible: false,
      methodName: "",
      methodGroup: "",
    };
  },
  mounted() {
    this.setCurRul();
    this.initCurData();
    this.setApiInfo();
  },
  methods: {
    handleClickRequest(tab, event) {
      console.log(tab, event);
    },
    handleClickResponse(tab, event) {
      console.log(tab, event);
    },
    handleSendRequest() {
      let queryData = null;
      let contentType = this.contentType;
      if (this.selectType === "POST") {
        if (this.contentType === "none") {
          queryData = this.bodyStringData;
          contentType = "";
        } else if (this.contentType === "application/json") {
          queryData = this.bodyJsonData;
        }
      } else if (this.selectType === "GET") {
        queryData = this.paramData;
        contentType = "";
      }

      callApi(
        this.path,
        contentType,
        this.headerJson,
        queryData,
        this.selectType
      )
        .then((res) => {
          console.log(res);
          this.responseBody = res.data;
          this.responseHeader = res.headers;
          this.$message({
            message: res.statusText || "请求成功",
            type: "success",
          });
        })
        .catch((error) => {
          this.$message({
            message: error || "请求失败",
            type: "warning",
          });
        });
    },
    /* 点击保存时显示弹框，完善文档 */
    handleClickSave() {
      this.dialogTableVisible = true;

      // 解析各参数为表单数据
      this.headerJsonValues = this.parseParams(this.headerJson);
      this.paramValues = this.parseParams(this.paramData);
      this.bodyJsonDataValues = this.parseParams(this.bodyJsonData);
      this.responseBodyValues = this.parseParams(this.responseBody);
    },
    /* 保存文档数据 */
    handleSaveData() {
      this.dialogTableVisible = false; // 关闭对话框

      // 保存页面数据
      const curData = JSON.parse(JSON.stringify(this.$data));
      const dataKey = this.path;
      const data = {
        path: dataKey,
        pageData: curData,
      };

      savePageData(data)
        .then((res) => {
          this.$message({
            message: "保存成功",
            type: "success",
          });
        })
        .catch((error) => {
          this.$message({
            message: "保存失败",
            type: "warning",
          });
        });
    },
    // 设置当前输入框的请求
    setCurRul() {
      if (this.$route.path === "/home") {
        this.path = "http://localhost";
      } else {
        console.log("this.localServiceName: " + this.localServiceName);
        this.path =
          this.localServiceName +
          this.$route.path.substring(
            this.$route.path.substring(1).indexOf("/") + 1
          );
      }
    },
    initCurData() {
      const curPagePath = this.$route.path;
      const curData = getPageData(curPagePath).then((res) => {
        if (res.data.code === "000") {
          if (res.data.data) {
            Object.assign(this.$data, res.data.data.pageData);
          }
        }
      });
    },
    onParamChange(value) {
      console.log(value);
    },
    onParamSave(value) {
      console.log(value);
    },
    onHeaderChange(value) {
      console.log(value);
    },
    onBodyChange(value) {
      console.log(value);
    },
    onBodySave(value) {
      console.log(value);
    },
    changeBodyType(value) {
      if (value === "application/json") {
        this.bodyNoneShow = false;
        this.bodyJsonShow = true;
        this.bodyFileShow = false;
        this.contentType = "application/json";
      } else if (value === "bodyFile") {
        this.bodyNoneShow = false;
        this.bodyJsonShow = false;
        this.bodyFileShow = true;
      } else if (value === "none") {
        this.bodyNoneShow = true;
        this.bodyJsonShow = false;
        this.bodyFileShow = false;
        this.contentType = "none";
      }
    },
    onResponseBodyChange(value) {
      console.log(value);
    },
    onResponseHeaderChange(value) {
      console.log(value);
    },
    handleSelectTypeClick(value) {
      if (value === "POST") {
        this.requestActiveName = "Body";
      } else {
        this.requestActiveName = "Param";
        this.contentType = "none";
      }
    },
    // 请求参数转换为对象
    parseParams(paramData) {
      let paramValues = [];
      if (paramData) {
        for (let key in paramData) {
          // 定义表数据格式
          let tableItem = {
            name: null, // 参数名
            value: null, // 参数值
            description: null, // 说明
          };

          tableItem.name = key;
          tableItem.value = paramData[key];
          tableItem.description = "暂无说明";

          paramValues.push(tableItem);
        }
      }

      return paramValues;
    },
    // 获得接口信息
    setApiInfo() {
      this.methodName = this.$route.meta.title;
      this.methodGroup = this.$route.meta.group;
    },
    // 弹框修改方法名和组别
    handleMethodChangeClick() {
      this.dialogApiInfoVisible = true;
    },
    // 修改方法名
    handleSaveMethodName() {
      // 获得菜单，根据菜单path对菜单的title进行修改
      const path = this.$route.path.substring(
        this.$route.path.substring(1).indexOf("/") + 1
      );
      const param = {
        path: path,
        newName: this.methodName,
        methodGroup: this.methodGroup,
      }
      // 修改本地routes信息
      this.$store.dispatch("api/changeApiInfo", param);

      // 上传修改信息至服务器, 生成路由时, 从服务器获得api信息解析时进行设置
      saveApiInfo(param).then(res => {
        if (res.data.code === '000') {
          // 成功
          this.$message({
            message: "保存成功",
            type: "success",
          });
        } else {
          // 失败
          this.$message({
            message: res.data.message || "保存失败",
            type: "error",
          });
        }
      })
      this.dialogApiInfoVisible = false;
    },
  },
};
</script>
<style scoped>
.el-tag {
  margin-left: 10px;
}

.request-type {
  display: inline-flex;
}

.input-with-select .el-input-group__prepend {
  background-color: #fff;
}
.jsoneditor-poweredBy {
  display: none;
}
.body-file-box {
  align-items: center;
  height: 185px;
  text-align: center;
}
.body-upload-file {
  margin-top: 50px;
}
.response-cookies-box {
  align-items: center;
  height: 185px;
  text-align: center;
}
.jsoneditor-menu a {
  display: none !important;
}
</style>
