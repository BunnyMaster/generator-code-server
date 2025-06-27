const MainForm = {
    name: "MainForm",
    template: `
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
                          v-model="authorName" type="text">
                </div>
                <div class="col-md-4 mb-3">
                    <label class="form-label fw-medium"
                           for="requestMapping">requestMapping名称</label>
                    <input class="form-control border-secondary" id="requestMapping" placeholder="输入requestMapping名称" required
                           v-model="requestMapping" type="text">
                </div>
                <div class="col-md-4 mb-3">
                    <label class="form-label fw-medium" for="packageName">包名称</label>
                    <input class="form-control border-secondary" id="packageName" placeholder="输入包名称" required
                           v-model="packageName" type="text">
                </div>
                <div class="col-md-4 mb-3">
                    <label class="form-label fw-medium" for="simpleDateFormat">时间格式</label>
                    <input class="form-control border-secondary" id="simpleDateFormat" placeholder="输入时间格式" required
                           v-model="simpleDateFormat" type="text">
                </div>
                <div class="col-md-4 mb-3">
                    <label class="form-label fw-medium" for="comment">去除开头前缀</label>
                    <input class="form-control border-secondary" id="comment" placeholder="去除开头前缀" required
                           v-model="comment" type="text">
                </div>

                <!-- 前端模板选项 -->
                <div class="col-12 mt-3 mb-3 p-3 bg-light rounded">
                    <label class="form-check-inline col-form-label fw-medium">生成前端模板：</label>
                    <div class="form-check form-check-inline">
                        <input class="form-check-input border-secondary" id="inlineCheckbox1" type="checkbox"
                               value="option1">
                        <label class="form-check-label" for="inlineCheckbox1">路径1</label>
                    </div>
                    <div class="form-check form-check-inline">
                        <input class="form-check-input border-secondary" id="inlineCheckbox2" type="checkbox"
                               value="option2">
                        <label class="form-check-label" for="inlineCheckbox2">路径12</label>
                    </div>
                    <div class="form-check form-check-inline btn-group ms-2">
                        <button class="btn btn-outline-primary btn-sm" type="button">全选</button>
                        <button class="btn btn-outline-secondary btn-sm" type="button">反选</button>
                        <button class="btn btn-outline-danger btn-sm" type="button">全不选</button>
                    </div>
                </div>

                <!-- 后端模板选项 -->
                <div class="col-12 mt-2 mb-3 p-3 bg-light rounded">
                    <label class="form-check-inline col-form-label fw-medium">生成后端模板：</label>
                    <div class="form-check form-check-inline">
                        <input class="form-check-input border-secondary" id="inlineCheckbox3" type="checkbox"
                               value="option1">
                        <label class="form-check-label" for="inlineCheckbox3">路径11</label>
                    </div>
                    <div class="form-check form-check-inline">
                        <input class="form-check-input border-secondary" id="inlineCheckbox4" type="checkbox"
                               value="option2">
                        <label class="form-check-label" for="inlineCheckbox4">路径12</label>
                    </div>
                    <div class="form-check form-check-inline btn-group ms-2">
                        <button class="btn btn-outline-primary btn-sm" type="button">全选</button>
                        <button class="btn btn-outline-secondary btn-sm" type="button">反选</button>
                        <button class="btn btn-outline-danger btn-sm" type="button">全不选</button>
                    </div>
                </div>

                <!-- 操作按钮 -->
                <div class="row mt-4">
                    <div class="col-md-4 btn-group">
                        <button class="btn btn-outline-primary" data-bs-title="选择数据表中所有的内容"
                                data-bs-toggle="tooltip" type="button">
                            全部选择
                        </button>
                        <button class="btn btn-outline-secondary" data-bs-title="将选择的表格内容反向选择"
                                data-bs-toggle="tooltip" type="button">
                            全部反选
                        </button>
                        <button class="btn btn-outline-danger" data-bs-title="取消全部已经选择的数据表"
                                data-bs-toggle="tooltip" type="button">
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
    data() {
        return {
            // 默认是否折叠编辑表单
            defaultCollapse: false,
            // 作者名称
            authorName: "Bunny",
            // requestMapping名称
            requestMapping: "/api",
            // // 表名称
            // tableName: "",
            // // 类名称
            // className: "",
            // 包名称
            packageName: "cn.bunny",
            // 时间格式
            simpleDateFormat: "yyyy-MM-dd HH:mm:ss",
            // 去除开头前缀
            comment: "t_,sys_"
        }
    },
    methods: {
        /* 选择全部 */
        onSelectAll() {

        },
        /* 开始生成 */
        onGenerator() {

        },
    }
}