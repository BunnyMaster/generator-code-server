const SqlForm = {
    name: "MainForm",
    template: `
    <div class="card shadow-sm mt-2 bg-body-secondary">
        <!-- 表单标题和折叠控制 -->
        <div class="card-header p-3">
            <a aria-controls="generatorFormCollapse" aria-expanded="false"
                class="d-flex align-items-center text-decoration-none" data-bs-toggle="collapse"
                href="#generatorFormCollapse">
                <i class="bi bi-pencil me-2"></i>
                <span class="fw-semibold">填写生成表单信息</span>
                <i class="bi bi-chevron-down ms-auto transition-transform rotate-180"></i>
            </a>
        </div>
    
        <!-- 表单内容区域 -->
        <div class="collapse" :class="{ 'show': defaultCollapse }" id="generatorFormCollapse">
            <form class="card-body row" @submit.prevent="handleSubmit" novalidate>
                <!-- 基本信息输入区域 -->
                <div class="col-md-4 mb-3 has-validation">
                    <label class="form-label fw-medium" for="author">作者名称</label>
                    <input class="form-control border-secondary" :class="{ 'is-invalid': errors.author }"
                        id="author" placeholder="输入作者名称" v-model="form.author" type="text"
                        @input="validateField('author')">
                    <div class="invalid-feedback">
                        {{ errors.author || '请输入作者名称' }}
                    </div>
                </div>
                <div class="col-md-4 mb-3 has-validation">
                    <label class="form-label fw-medium" for="requestMapping">requestMapping名称</label>
                    <input class="form-control border-secondary" :class="{ 'is-invalid': errors.requestMapping }"
                        id="requestMapping" placeholder="输入requestMapping名称" v-model="form.requestMapping" type="text"
                        @input="validateField('requestMapping')">
                    <div class="invalid-feedback">
                        {{ errors.requestMapping || '请输入requestMapping名称' }}
                    </div>
                </div>
                <div class="col-md-4 mb-3 has-validation">
                    <label class="form-label fw-medium" for="packageName">包名称</label>
                    <input class="form-control border-secondary" :class="{ 'is-invalid': errors.packageName }"
                        id="packageName" placeholder="输入包名称" v-model="form.packageName" type="text"
                        @input="validateField('packageName')">
                    <div class="invalid-feedback">
                        {{ errors.packageName || '请输入包名称' }}
                    </div>
                </div>
                <div class="col-md-4 mb-3 has-validation">
                    <label class="form-label fw-medium" for="simpleDateFormat">时间格式</label>
                    <input class="form-control border-secondary" :class="{ 'is-invalid': errors.simpleDateFormat }"
                        id="simpleDateFormat" placeholder="输入时间格式" v-model="form.simpleDateFormat" type="text"
                        @input="validateField('simpleDateFormat')">
                    <div class="invalid-feedback">
                        {{ errors.simpleDateFormat || '请输入时间格式' }}
                    </div>
                </div>
                <div class="col-md-4 mb-3 has-validation">
                    <label class="form-label fw-medium" for="tablePrefixes">去除开头前缀</label>
                    <input class="form-control border-secondary" :class="{ 'is-invalid': errors.tablePrefixes }"
                        id="tablePrefixes" placeholder="去除开头前缀" v-model="form.tablePrefixes" type="text"
                        @input="validateField('tablePrefixes')">
                    <div class="invalid-feedback">
                        {{ errors.tablePrefixes || '请输入去除开头前缀' }}
                    </div>
                </div>
                <div class="col-md-12 mb-3 has-validation">
                    <label class="form-label fw-medium" for="sql">Sql语句</label>
                    <textarea class="form-control border-secondary" style="height: 150px;" :class="{ 'is-invalid': errors.sql }"
                        id="sql" placeholder="请输入Sql语句" v-model="form.sql" @input="validateField('sql')"/>
                    <div class="invalid-feedback">
                        {{ errors.sql || '请输入Sql语句' }}
                    </div> 
                </div>

                <!-- 前端模板选择区域 -->
                <div class="col-12 mt-3 mb-3 p-3 bg-light rounded">
                    <label class="form-check-inline col-form-label fw-medium">生成前端模板：</label>
                    <div class="form-check form-check-inline" v-for="(web,index) in webList" :key="index">
                        <input class="form-check-input border-secondary" :class="{ 'is-invalid': errors.webTemplates }"
                            :id="web.id" type="checkbox" v-model="web.checked"
                            @change="validateTemplates">
                        <label class="form-check-label" :for="web.id" :title="web.name">{{web.label}}</label>
                    </div>
                    <div class="form-check form-check-inline btn-group ms-2">
                        <button class="btn btn-outline-primary btn-sm" type="button"
                            @click="onTableSelectAll(webList)">全选</button>
                        <button class="btn btn-outline-secondary btn-sm" type="button"
                            @click="onTableInvertSelection(webList)">反选</button>
                        <button class="btn btn-outline-danger btn-sm" type="button"
                            @click="onTableClearAll(webList)">全不选</button>
                    </div>
                    <div v-if="errors.webTemplates" class="invalid-feedback d-block">
                        {{ errors.webTemplates }}
                    </div>
                </div>

                <!-- 后端模板选择区域 -->
                <div class="col-12 mt-2 mb-3 p-3 bg-light rounded">
                    <label class="form-check-inline col-form-label fw-medium">生成后端模板：</label>
                    <div class="form-check form-check-inline" v-for="(server,index) in serverList" :key="index">
                        <input class="form-check-input border-secondary"
                            :class="{ 'is-invalid': errors.serverTemplates }" :id="server.id" type="checkbox"
                            v-model="server.checked" @change="validateTemplates">
                        <label class="form-check-label" :title="server.name" :for="server.id">{{server.label}}</label>
                    </div>
                    <div class="form-check form-check-inline btn-group ms-2">
                        <button class="btn btn-outline-primary btn-sm" type="button"
                            @click="onTableSelectAll(serverList)">全选</button>
                        <button class="btn btn-outline-secondary btn-sm" type="button"
                            @click="onTableInvertSelection(serverList)">反选</button>
                        <button class="btn btn-outline-danger btn-sm" type="button"
                            @click="onTableClearAll(serverList)">全不选</button>
                    </div>
                    <div v-if="errors.serverTemplates" class="invalid-feedback d-block">
                        {{ errors.serverTemplates }}
                    </div>
                </div>

                <!-- 操作按钮区域 -->
                <div class="row mt-4">
                    <div class="col-md-4 btn-group">
                        <button class="btn btn-outline-primary" data-bs-title="选择数据表中所有的内容" data-bs-toggle="tooltip"
                            type="button" @click="onSelectAll">
                            全部选择
                        </button>
                        <button class="btn btn-outline-secondary" data-bs-title="将选择的表格内容反向选择" data-bs-toggle="tooltip"
                            type="button" @click="onInvertSelection">
                            全部反选
                        </button>
                        <button class="btn btn-outline-danger" data-bs-title="取消全部已经选择的数据表" data-bs-toggle="tooltip"
                            type="button" @click="onClearAll">
                            全部取消
                        </button>
                    </div>
                    <div class="col-md-4 btn-group">
                       <button class="btn btn-primary shadow-sm" disabled type="button"
                        v-if="generatorCodeLoading">
                            <span aria-hidden="true" class="spinner-grow spinner-grow-sm"></span>
                            <span role="status">生成中...</span>
                        </button>
                        <button v-else class="btn btn-primary" type="submit">
                            开始生成
                        </button>
                        <button class="btn btn-info" type="button" @click="onGetSqlParserInfo">获取Sql内容信息</button>
                        <button class="btn btn-warning" type="button" @click="onClearGeneratorData">清空生成记录</button>
                    </div>
                    <div class="col-md-4 d-grid gap-2">
                         <button class="btn btn-primary shadow-sm" disabled type="button"
                                v-if="downloadLoading">
                            <span aria-hidden="true" class="spinner-grow spinner-grow-sm"></span>
                            <span role="status">下载ZIP...</span>
                        </button>
                        
                        <button v-else class="btn btn-primary text-white" type="button" @click="onDownloadZip">下载ZIP</button>
                    </div>
                </div>
            </form>
        </div>
    </div>
    `,
    props: {
        // 表单数据对象，包含生成代码所需的各种参数
        form: {type: Object, required: true},
        // 生成代码的回调函数
        onGeneratorCode: {type: Function, required: true},
        // 清空生成记录
        onClearGeneratorData: {type: Function, required: true},
        // 生成代码加载
        generatorCodeLoading: {type: Boolean, required: true},
        // sql语句中表信息
        tableInfo: {type: Object, required: true},
        // sql语句中列信息
        columnMetaList: {type: Object, required: true},
    },
    data() {
        return {
            // 控制表单默认是否展开
            defaultCollapse: true,
            // 后端模板选项列表
            serverList: ref([]),
            // 前端模板选项列表
            webList: ref([]),
            // 错误信息对象
            errors: {
                author: '',
                requestMapping: '',
                packageName: '',
                simpleDateFormat: '',
                tablePrefixes: '',
                webTemplates: '',
                serverTemplates: '',
                sql: ''
            },
            downloadLoading: ref(false),
        };
    },
    methods: {
        /**
         * 验证表单字段
         * @param {string} field - 字段名
         */
        validateField(field) {
            if (!this.form[field] || this.form[field].trim() === '') {
                this.errors[field] = '此字段为必填项';
                return false;
            }
            this.errors[field] = '';
            return true;
        },

        /* 验证模板选择 */
        validateTemplates() {
            // 检查列表是否有一个选中的
            const hasWebSelected = this.webList.some(item => item.checked);
            const hasServerSelected = this.serverList.some(item => item.checked);

            // 列表都没有选中
            if (!hasWebSelected && !hasServerSelected) {
                this.errors.webTemplates = '请至少选择一个前端或后端模板';
                this.errors.serverTemplates = '请至少选择一个前端或后端模板';
                return false;
            }

            // 列表选中让错误提示小时
            this.errors.webTemplates = '';
            this.errors.serverTemplates = '';

            // 发生选择变化时，同时父级更新表单
            this.updateForm();
            return true;
        },

        /* 验证整个表单 */
        validateForm() {
            let isValid = true;

            // 验证文本字段
            const textFields = ['author', 'requestMapping', 'packageName', 'simpleDateFormat', 'tablePrefixes', "sql"];
            textFields.forEach(field => {
                if (!this.validateField(field)) {
                    isValid = false;
                }
            });

            // 验证模板选择
            if (!this.validateTemplates()) {
                isValid = false;
            }

            return isValid;
        },

        /* 更新父级表单 */
        updateForm() {
            const webList = this.webList.filter(item => item.checked).map(item => item.name);
            const serverList = this.serverList.filter(item => item.checked).map(item => item.name);

            const newForm = {...this.form, path: [...webList, ...serverList,]};
            this.$emit("update:form", newForm);
        },

        /* 获取Sql内容信息 */
        async onGetSqlParserInfo() {
            const validate = this.validateForm();
            if (!validate) return;

            // 设置请求参数
            const data = {sql: this.form.sql};

            // 获取表信息
            const tableInfoResponse = await axiosInstance.post("/sqlParser/tableInfo", data, {headers: {'Content-Type': 'multipart/form-data'}});
            if (tableInfoResponse.code === 200) {
                this.$emit("update:tableInfo", tableInfoResponse.data)
            }

            // 获取sql中的列信息
            const columnMetaDataResponse = await axiosInstance.post("/sqlParser/columnMetaData", data, {headers: {'Content-Type': 'multipart/form-data'}});
            if (columnMetaDataResponse.code === 200) {
                this.$emit("update:columnMetaList", columnMetaDataResponse.data)
            }
        },

        /* 处理表单提交 */
        handleSubmit() {
            if (this.validateForm()) {
                this.updateForm();
                // 如果验证通过，调用父组件提供的生成代码方法
                this.onGeneratorCode();
            } else {
                // 验证失败，可以在这里添加额外的处理逻辑
                antd.message.error("表单验证失败")
            }
        },

        /**
         * 获取VMS资源路径列表
         * 从服务器获取前端和后端模板的路径列表
         * @async
         * @returns {Promise<void>}
         */
        async getVmsResourcePathList() {
            const response = await axiosInstance.get("/vms/vmsResourcePathList");
            const {data, code, message} = response;

            if (code !== 200) {
                antd.message.error(message);
                return;
            }
            // 初始化模板选择状态
            this.serverList = data.server.map(item => ({...item, checked: false}));
            this.webList = data.web.map(item => ({...item, checked: false}));
        },

        /**
         * 下载Zip文件
         * @returns {Promise<void>}
         */
        async onDownloadZip() {
            this.downloadLoading = true;
            try {
                const response = await axiosInstance({
                    url: "/generator/downloadByZip",
                    method: "POST",
                    data: this.form,
                    responseType: 'blob' // 重要：指定响应类型为blob
                });

                // 从响应头中获取文件名
                const contentDisposition = response.headers['content-disposition'];
                let fileName = 'download.zip';
                if (contentDisposition) {
                    const fileNameMatch = contentDisposition.match(/filename=(.+)/);
                    if (fileNameMatch && fileNameMatch[1]) {
                        fileName = fileNameMatch[1];
                        // 处理可能的编码文件名（如UTF-8编码）
                        if (fileName.startsWith("UTF-8''")) {
                            fileName = decodeURIComponent(fileName.replace("UTF-8''", ''));
                        }
                    }
                }

                // 创建Blob对象
                const blob = new Blob([response.data]);

                // 创建下载链接
                const downloadUrl = window.URL.createObjectURL(blob);
                const link = document.createElement('a');
                link.href = downloadUrl;
                link.download = fileName;
                document.body.appendChild(link);

                // 触发点击下载
                link.click();

                // 清理
                window.URL.revokeObjectURL(downloadUrl);
                document.body.removeChild(link);
            } catch (error) {
                console.error('下载失败:', error);
            }

            this.downloadLoading = false;
        },

        /**
         * 全选指定列表
         * @param {Array} list - 要处理的列表
         */
        onTableSelectAll(list) {
            list.forEach(item => item.checked = true);
            this.validateTemplates();
        },

        /**
         * 反选指定列表
         * @param {Array} list - 要处理的列表
         */
        onTableInvertSelection(list) {
            list.forEach(item => item.checked = !item.checked);
            this.validateTemplates();
        },

        /**
         * 清空指定列表的选择
         * @param {Array} list - 要处理的列表
         */
        onTableClearAll(list) {
            list.forEach(item => item.checked = false);
            this.validateTemplates();
        },

        /* 全选所有模板 */
        onSelectAll() {
            this.onTableSelectAll(this.webList);
            this.onTableSelectAll(this.serverList);
        },

        /* 反选所有模板 */
        onInvertSelection() {
            this.onTableInvertSelection(this.webList);
            this.onTableInvertSelection(this.serverList);
        },

        /* 清空所有模板的选择 */
        onClearAll() {
            this.onTableClearAll(this.webList);
            this.onTableClearAll(this.serverList);
        }
    },
    async mounted() {
        // 组件挂载时获取模板列表
        await this.getVmsResourcePathList();
    },
}