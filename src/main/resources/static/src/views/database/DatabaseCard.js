const DatabaseCard = defineComponent({
    name: "MainCard",
    template: `
    <div class="card shadow-sm position-relative">
        <!-- 使用提示区域：包含标题、数据库统计信息和操作指引 -->
        <div class="card-header bg-primary bg-opacity-10 border-bottom">
            <div class="d-flex justify-content-between align-items-center">
                <div>
                    <h5 class="card-title mb-0">
                        <i class="bi bi-info-circle-fill text-primary me-2"></i>使用提示
                    </h5>
                </div>
                <div>
                <span class="badge bg-primary rounded-pill me-2">
                    <i class="bi bi-database me-1"></i>
                    数据库: <span class="fw-normal">{{databaseList.length}}</span>
                </span>
                    <span class="badge bg-primary rounded-pill">
                    <i class="bi bi-table me-1"></i>
                    数据库表: <span class="fw-normal">{{rawTableList.length}}</span>
                </span>
                </div>
            </div>
            <p class="card-subtitle mt-2 text-muted">
                <i class="bi bi-mouse me-1"></i>
                点击 <code class="bg-primary bg-opacity-10">复选框</code>或
                <code class="bg-primary bg-opacity-10">选择按钮</code>
                进行选择，之后点击 <code class="bg-primary bg-opacity-10">生成选中表</code> 进行预览。
            </p>
        </div>
        
        <!-- 数据库操作区域：包含数据库选择、表选择和查询功能 -->
        <div class="card-body bg-light">
            <div class="row g-3 align-items-end">
                <!-- 数据库选择下拉框 -->
                <div class="col-md-5">
                    <label class="form-label fw-semibold" for="databaseSelect">
                        <i class="bi bi-database me-1"></i>数据库选择
                    </label>
                    <select class="form-select shadow-sm" id="databaseSelect" v-model="dbName">
                        <option disabled selected>请选择数据库...</option>
                        <option :key="index" :title="db.comment" :value="db.tableCat" 
                                v-for="(db,index) in databaseList">
                            {{db.tableCat}}
                        </option>
                    </select>
                </div>
    
                <!-- 数据库表搜索输入框 -->
                <div class="col-md-5">
                    <label class="form-label fw-semibold" for="tableSelect">
                        <i class="bi bi-table me-1"></i>
                        数据库表选择
                    </label>
                    <input class="form-control shadow-sm" id="tableSelect" placeholder="输入表名或表注释"
                           v-model="tableName"/>
                </div>
    
                <!-- 查询按钮 -->
                <div class="col-md-2 d-grid">
                    <button class="btn btn-primary shadow-sm" disabled type="button"
                            v-if="dbLoading">
                        <span aria-hidden="true" class="spinner-grow spinner-grow-sm"></span>
                        <span role="status">Loading...</span>
                    </button>
    
                    <button @click="onRefresh" class="btn btn-primary shadow-sm" v-else>
                        <i class="bi bi-search me-1"></i>
                        查询
                    </button>
                </div>
            </div>
    
            <!-- 数据库连接详情折叠面板 -->
            <div class="pt-1 bg-light">
                <a class="d-flex align-items-center text-decoration-none" data-bs-toggle="collapse"
                   href="#dbInfoCollapse">
                    <i class="bi bi-database me-2"></i>
                    <span>数据库连接详情</span>
                    <i class="bi bi-chevron-down ms-auto"></i>
                </a>
                <div class="collapse mt-2" id="dbInfoCollapse">
                    <div class="card card-body bg-white">
                        <ul class="list-unstyled mb-0">
                            <li class="mb-2">
                                <strong>数据库:</strong> {{databaseInfo.databaseProductName}}
                                {{databaseInfo.databaseProductVersion}}
                            </li>
                            <li class="mb-2">
                                <strong>驱动:</strong> {{databaseInfo.driverName}}
                                ({{databaseInfo.driverVersion}})
                            </li>
                            <li class="mb-2"><strong>URL:</strong>
                                <code class="d-block text-break">{{databaseInfo.url}}</code>
                            </li>
                            <li class="mb-2"><strong>用户:</strong> {{databaseInfo.username}}</li>
                            <li><strong>当前库:</strong> {{dbName}}</li>
                        </ul>
                    </div>
                </div>
            </div>
        </div>
    </div>
    `,
    props: {
        /* 原始表列表数据，由父组件传入 */
        rawTableList: {type: Array,},
    },
    data() {
        return {
            // 当前选中的数据库名称
            dbName: ref(""),
            // 输入搜索的表名或表注释
            tableName: ref(""),
            // 数据库加载状态标志
            dbLoading: ref(false),
            // 数据库连接信息对象
            databaseInfo: ref({}),
            // 所有数据库列表
            databaseList: ref([]),
        }
    },
    methods: {
        /**
         * 获取当前连接的数据库元数据信息
         * 包括数据库信息、数据库列表和当前数据库名称
         * 此方法会触发数据库表列表的更新
         * @async
         * @returns {Promise<void>}
         */
        async getDatabaseInfoMetaData() {
            this.dbLoading = true;

            try {
                const response = await axiosInstance.get("/table/databaseInfoMetaData");
                const {data, code, message} = response;

                if (code !== 200) {
                    antd.message.error(message);
                    return
                }

                // 设置数据库连接信息
                this.databaseInfo = data;

                // 设置所有数据库列表
                this.databaseList = data.databaseList;

                // 如果当前未选择数据库，则使用默认数据库
                if (!this.dbName) {
                    this.dbName = data.currentDatabase;
                }
            } finally {
                this.dbLoading = false;
            }
        },

        /**
         * 刷新查询操作
         * 重新获取数据库信息并触发父组件更新表列表
         * @async
         * @returns {Promise<void>}
         */
        async onRefresh() {
            await this.getDatabaseInfoMetaData();
            // 通知父组件更新表列表
            this.$emit("getDatabaseTableList");
        }
    },
    async mounted() {
        // 组件挂载时初始化数据库信息
        await this.getDatabaseInfoMetaData();
    },
    watch: {
        /* 监听数据库名称变化并通知父组件 */
        dbName: {
            handler(val) {
                this.$emit("update:dbSelect", val);
            },
        },

        /* 监听表名输入变化并通知父组件 */
        tableName: {
            handler(val) {
                this.$emit("update:tableSelect", val);
            }
        }
    }
});