const MainForm = {
    name: "MainForm", template: `
    <div class="card shadow-sm mt-2 bg-body-secondary">
        <div class="card-header p-3">
            <a aria-controls="generatorFormCollapse"
               aria-expanded="false"
               class="d-flex align-items-center text-decoration-none"
               data-bs-toggle="collapse"
               href="#generatorFormCollapse">
                <i class="bi bi-pencil me-2"></i>
                <span class="fw-semibold">填写生成表单信息</span>
                <i class="bi bi-chevron-down ms-auto transition-transform rotate-180"></i>
            </a>
        </div>

        <div class="collapse" :class="{ 'show': defaultCollapse }" id="generatorFormCollapse">
            <form class="card-body row">
                <!-- 基本信息输入 -->
                <div class="col-md-4 mb-3">
                    <label class="form-label fw-medium" for="authorName">作者名称</label>
                    <input class="form-control border-secondary" id="authorName" placeholder="输入作者名称" required
                          v-model="form.authorName" type="text">
                </div>
                <div class="col-md-4 mb-3">
                    <label class="form-label fw-medium"
                           for="requestMapping">requestMapping名称</label>
                    <input class="form-control border-secondary" id="requestMapping" placeholder="输入requestMapping名称" required
                           v-model="form.requestMapping" type="text">
                </div>
                <div class="col-md-4 mb-3">
                    <label class="form-label fw-medium" for="packageName">包名称</label>
                    <input class="form-control border-secondary" id="packageName" placeholder="输入包名称" required
                           v-model="form.packageName" type="text">
                </div>
                <div class="col-md-4 mb-3">
                    <label class="form-label fw-medium" for="simpleDateFormat">时间格式</label>
                    <input class="form-control border-secondary" id="simpleDateFormat" placeholder="输入时间格式" required
                           v-model="form.simpleDateFormat" type="text">
                </div>
                <div class="col-md-4 mb-3">
                    <label class="form-label fw-medium" for="tablePrefixes">去除开头前缀</label>
                    <input class="form-control border-secondary" id="tablePrefixes" placeholder="去除开头前缀" required
                           v-model="form.tablePrefixes" type="text">
                </div>       
               <div class="col-md-4 mb-3">
                    <label class="form-label fw-medium" for="comment">注释内容</label>
                    <input class="form-control border-secondary" id="comment" placeholder="注释内容" required
                           v-model="form.comment" type="text">
                </div>

                <!-- 前端模板选项 -->
                <div class="col-12 mt-3 mb-3 p-3 bg-light rounded">
                    <label class="form-check-inline col-form-label fw-medium">生成前端模板：</label>
                <div class="form-check form-check-inline" v-for="(web,index) in webList" :key="index">
                        <input class="form-check-input border-secondary" :id="web.id" type="checkbox"
                               :value="web.name" v-model="web.checked">
                        <label class="form-check-label" :for="web.id">{{web.label}}</label>
                    </div>
                    <div class="form-check form-check-inline btn-group ms-2">
                        <button class="btn btn-outline-primary btn-sm" type="button" @click="onTableSelectAll(webList)">全选</button>
                        <button class="btn btn-outline-secondary btn-sm" type="button" @click="onTableInvertSelection(webList)">反选</button>
                        <button class="btn btn-outline-danger btn-sm" type="button" @click="onTableClearAll(webList)">全不选</button>
                    </div>
                </div>

                <!-- 后端模板选项 -->
                <div class="col-12 mt-2 mb-3 p-3 bg-light rounded">
                    <label class="form-check-inline col-form-label fw-medium">生成后端模板：</label>
                    <div class="form-check form-check-inline" v-for="(server,index) in serverList" :key="index">
                        <input class="form-check-input border-secondary" :id="server.id" type="checkbox"
                               :value="server.name" v-model="server.checked">
                        <label class="form-check-label" :for="server.id" :data-bs-title="server.name" 
                        data-bs-toggle="tooltip">{{server.label}}</label>
                    </div>
                    <div class="form-check form-check-inline btn-group ms-2">
                        <button class="btn btn-outline-primary btn-sm" type="button" @click="onTableSelectAll(serverList)">全选</button>
                        <button class="btn btn-outline-secondary btn-sm" type="button" @click="onTableInvertSelection(serverList)">反选</button>
                        <button class="btn btn-outline-danger btn-sm" type="button" @click="onTableClearAll(serverList)">全不选</button>
                    </div>
                </div>

                <!-- 操作按钮 -->
                <div class="row mt-4">
                    <div class="col-md-4 btn-group">
                        <button class="btn btn-outline-primary" data-bs-title="选择数据表中所有的内容"
                                data-bs-toggle="tooltip" type="button" @click="onSelectAll">
                            全部选择
                        </button>
                        <button class="btn btn-outline-secondary" data-bs-title="将选择的表格内容反向选择"
                                data-bs-toggle="tooltip" type="button" @click="onInvertSelection">
                            全部反选
                        </button>
                        <button class="btn btn-outline-danger" data-bs-title="取消全部已经选择的数据表"
                                data-bs-toggle="tooltip" type="button" @click="onClearAll">
                            全部取消
                        </button>
                    </div>
                    <div class="col-md-4 btn-group">
                        <button class="btn btn-primary" data-bs-title="取消全部已经选择的数据表"
                                data-bs-toggle="tooltip" type="button">
                            开始生成
                        </button>
                        <button class="btn btn-warning" type="button">清空生成记录</button>
                        <button class="btn btn-success" type="button">下载全部</button>
                    </div>
                    <div class="col-md-4 d-grid gap-2">
                        <button class="btn btn-primary text-white" type="button">下载ZIP</button>
                    </div>
                </div>
            </form>
        </div>
    </div>
    `,
    props: {
        form: {
            type: Object,
            default: {}
        }
    },
    data() {
        return {
            // 默认是否折叠编辑表单
            defaultCollapse: true,
            // 后端路径列表
            serverList: ref([]),
            // 前端路径列表
            webList: ref([]),

        }
    },
    methods: {
        /* 获取所有vms下的文件路径 */
        async getVmsResourcePathList() {
            const response = await axiosInstance.get("/vms/vmsResourcePathList");
            const {data, code, message} = response;

            if (code !== 200) {
                antd.message.error(message);
                return;
            }
            this.serverList = data.server;
            this.webList = data.web;
        },

        /* 表格选择全部 */
        onTableSelectAll(list) {
            list.forEach((item) => item.checked = true)
                .filter((item) => {
                    console.log(item);
                })
        },

        /* 表格反选所选的 */
        onTableInvertSelection(list) {
            list.forEach((item) => item.checked = !item.checked);
        },

        /* 表格全不选 */
        onTableClearAll(list) {
            list.forEach((item) => item.checked = false);
        },

        /* 全部选择 */
        onSelectAll() {
            this.webList.forEach((item) => item.checked = true);
            this.serverList.forEach((item) => item.checked = true);
        },

        /* 反选 */
        onInvertSelection() {
            this.webList.forEach((item) => item.checked = !item.checked);
            this.serverList.forEach((item) => item.checked = !item.checked);
        },

        /* 清除所选中的 */
        onClearAll() {
            this.webList.forEach((item) => item.checked = false);
            this.serverList.forEach((item) => item.checked = false);
        },

        /* 开始生成 */
        onGenerator() {

        },
    },
    async mounted() {
        await this.getVmsResourcePathList();
    }
}