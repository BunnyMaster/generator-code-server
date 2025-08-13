const DatabaseTable = defineComponent({
    name: "MainTable",
    template: `
        <div class="card mt-2 shadow-sm">
            <!-- 表格标题和选择选项 -->
            <div class="d-flex justify-content-between card-header bg-primary bg-opacity-10">
                <h5 class="card-title mb-0">
                    <i class="bi bi-table me-2"></i>数据表列表
                </h5>
        
                <div>
                    <div class="form-check form-check-inline">
                        <input class="form-check-input" name="TableSelectRadios" type="radio" id="radioSelectAll" value="all"
                            v-model="selectedOption">
                        <label class="form-check-label" for="radioSelectAll">选择全部</label>
                    </div>
                    <div class="form-check form-check-inline">
                        <input class="form-check-input" name="TableSelectRadios" type="radio" id="radioSelectCurrentPage"
                            value="current" v-model="selectedOption">
                        <label class="form-check-label" for="radioSelectCurrentPage">选择当前页</label>
                    </div>
                </div>
            </div>
        
            <!-- 表格内容区域 -->
            <div class="card-body p-0">
                <div class="table-responsive">
                    <!-- 加载状态 -->
                    <div class="p-5 text-center" v-if="loading">
                        <div class="spinner-border" role="status">
                            <span class="visually-hidden">Loading...</span>
                        </div>
                    </div>
        
                    <!-- 空状态提示 -->
                    <div class="p-5 text-center" v-else-if="tableList.length === 0">
                        <i class="bi bi-exclamation-circle fs-1 text-muted"></i>
                        <p class="mt-2 text-muted">没有找到数据表</p>
                    </div>
        
                    <!-- 数据表格 -->
                    <table class="table table-striped table-bordered table-hover mb-0" v-else>
                        <thead class="table-light">
                            <tr>
                                <th scope="col" width="2%">
                                    <input class="form-check-input border-secondary" type="checkbox"
                                        v-model="tableSelectAllChecked" @change="onSelectAllTable">
                                </th>
                                <th scope="col" width="3%">#</th>
                                <th scope="col" width="25%">表名</th>
                                <th scope="col" width="25%">注释</th>
                                <th scope="col" width="15%">所属数据库</th>
                                <th class="text-center" scope="col" width="15%">操作</th>
                            </tr>
                        </thead>
                        <tbody>
                            <template v-for="(table, index) in paginatedTableList" :key="table.tableName">
                                <tr>
                                    <td>
                                        <input class="form-check-input border-secondary" type="checkbox" v-model="table.checked"
                                            @change="onSelectTable($event.target.checked, table)">
                                    </td>
                                    <td>{{ (currentPage - 1) * itemsPerPage + index + 1 }}</td>
                                    <td>
                                        <a class="text-decoration-none" href="#" @click.prevent="toggleTableDetails(table)">
                                            {{ table.tableName }}
                                            <i class="bi"
                                                :class="{'bi-chevron-down': !table.showDetails, 'bi-chevron-up': table.showDetails}"></i>
                                        </a>
                                    </td>
                                    <td>{{ table.comment || '无注释' }}</td>
                                    <td>{{ table.tableCat }}</td>
                                    <td class="d-flex justify-content-around">
                                        <button class="btn btn-sm btn-outline-primary"
                                            @click="onSelectTable(!table.checked, table)">
                                            <i class="bi bi-database-check"></i> 选择
                                        </button>
                                        <button class="btn btn-sm btn-outline-primary" @click="toggleTableDetails(table)">
                                            <i class="bi bi-gear"></i> 查看
                                        </button>
                                    </td>
                                </tr>
        
                                <!-- 表详情展开行 -->
                                <tr v-if="table.showDetails">
                                    <td colspan="6" class="p-0">
                                        <div class="p-3 bg-light">
                                            <h6 class="mb-3">表信息</h6>
                                            <div class="row mb-4">
                                                <div class="col-md-3">
                                                    <p class="mb-1"><strong>表名:</strong> {{table.tableName}}</p>
                                                </div>
                                                <div class="col-md-3">
                                                    <p class="mb-1"><strong>类型:</strong> {{table.tableType}}</p>
                                                </div>
                                                <div class="col-md-6">
                                                    <p class="mb-1"><strong>注释:</strong> {{table.comment || '无'}}</p>
                                                </div>
                                            </div>
        
                                            <h6 class="mb-3">列信息</h6>
                                            <div class="table-responsive">
                                                <table class="table table-sm table-bordered">
                                                    <thead>
                                                        <tr>
                                                            <th>列名</th>
                                                            <th>数据库类型</th>
                                                            <th>Java类型</th>
                                                            <th>JS类型</th>
                                                            <th>主键</th>
                                                            <th>注释</th>
                                                        </tr>
                                                    </thead>
                                                    <tbody>
                                                        <tr v-for="column in table.columns" :key="column.columnName">
                                                            <td>{{column.columnName}}</td>
                                                            <td>{{column.jdbcType}}</td>
                                                            <td>{{column.javaType}}</td>
                                                            <td>{{column.javascriptType}}</td>
                                                            <td>
                                                                <span class="badge bg-success"
                                                                    v-if="column.isPrimaryKey">是</span>
                                                                <span class="badge bg-secondary" v-else>否</span>
                                                            </td>
                                                            <td>{{column.comment || '无'}}</td>
                                                        </tr>
                                                    </tbody>
                                                </table>
                                            </div>
                                        </div>
                                    </td>
                                </tr>
                            </template>
                        </tbody>
                    </table>
                </div>
            </div>
        
            <!-- 分页控件 -->
            <div class="card-footer bg-light" v-if="tableList.length > 0">
                <div class="d-flex justify-content-between align-items-center">
                    <div class="form-text">
                        显示 {{ (currentPage - 1) * itemsPerPage + 1 }}~{{ Math.min(currentPage * itemsPerPage,
                        rawTableList.length) }} 条，共 {{ rawTableList.length }} 条
                    </div>
        
                    <nav aria-label="Page navigation">
                        <ul class="pagination mb-0">
                            <li :class="{ disabled: currentPage === 1 }" class="page-item">
                                <a @click.prevent="goToPage(1)" class="page-link" href="#">
                                    <i class="bi bi-chevron-double-left"></i>
                                </a>
                            </li>
                            <li :class="{ disabled: currentPage === 1 }" class="page-item">
                                <a @click.prevent="goToPage(currentPage - 1)" class="page-link" href="#">
                                    <i class="bi bi-chevron-left"></i>
                                </a>
                            </li>
        
                            <!-- 显示页码 -->
                            <li v-for="page in visiblePages" :key="page" :class="{ active: currentPage === page }"
                                class="page-item">
                                <a @click.prevent="goToPage(page)" class="page-link" href="#">{{ page }}</a>
                            </li>
        
                            <li :class="{ disabled: currentPage === totalPages }" class="page-item">
                                <a @click.prevent="goToPage(currentPage + 1)" class="page-link" href="#">
                                    <i class="bi bi-chevron-right"></i>
                                </a>
                            </li>
                            <li :class="{ disabled: currentPage === totalPages }" class="page-item">
                                <a @click.prevent="goToPage(totalPages)" class="page-link" href="#">
                                    <i class="bi bi-chevron-double-right"></i>
                                </a>
                            </li>
                        </ul>
                    </nav>
        
                    <div class="dropdown">
                        <button aria-expanded="false" class="btn btn-outline-secondary dropdown-toggle"
                            data-bs-toggle="dropdown" id="itemsPerPageDropdown" type="button">
                            每页 {{ itemsPerPage }} 条
                        </button>
                        <ul aria-labelledby="itemsPerPageDropdown" class="dropdown-menu">
                            <li v-for="option in tablePageOptions" :key="option">
                                <a @click.prevent="changeItemsPerPage(option)" class="dropdown-item" href="#">
                                    {{ option }} 条/页
                                </a>
                            </li>
                        </ul>
                    </div>
                </div>
            </div>
        </div>
    `,
    props: {
        // 加载状态
        loading: {type: Boolean, default: false},
        // 处理后的表格数据（包含checked状态）
        tableList: {type: Array, required: true, default: () => []},
        // 原始表格数据（不包含checked状态）
        rawTableList: {type: Array, required: true, default: () => []},
        // 生成代码的回调函数
        onGeneratorCode: {type: Function, required: true},
        // 表单数据
        form: {type: Object, required: true},
        // 数据库选择发生变化
        dbSelect: {type: String, required: true},
    },
    data() {
        return {
            // 当前页码
            currentPage: 1,
            // 每页显示条数
            itemsPerPage: 30,
            // 每页条数选项
            tablePageOptions: [5, 10, 15, 30, 50, 100, 150, 200],
            // 选择模式：all-全选 current-当前页
            selectedOption: "current",
            // 表头全选状态
            tableSelectAllChecked: false
        }
    },
    computed: {
        /**
         * 计算总页数
         * @returns {number} 总页数
         */
        totalPages() {
            return Math.ceil(this.rawTableList.length / this.itemsPerPage);
        },

        /**
         * 当前页的数据
         * @returns {Array} 分页后的数据
         */
        paginatedTableList() {
            const start = (this.currentPage - 1) * this.itemsPerPage;
            const end = start + this.itemsPerPage;
            return this.tableList.slice(start, end);
        },

        /**
         * 当前选中的表名数组
         * @returns {Array} 选中的表名
         */
        selectedTableNames() {
            return this.tableList.filter(table => table.checked).map(table => table.tableName);
        }
    },
    methods: {
        /**
         * 跳转到指定页码
         * @param {number} page - 目标页码
         */
        goToPage(page) {
            if (page >= 1 && page <= this.totalPages) {
                this.currentPage = page;
            }
        },

        /**
         * 更改每页显示条数
         * @param {number} size - 每页条数
         */
        changeItemsPerPage(size) {
            this.itemsPerPage = size;
            // 重置到第一页
            this.currentPage = 1;
        },

        /**
         * 计算可见的页码范围
         * @returns {Array} 可见页码数组
         */
        visiblePages() {
            // 显示当前页前后各2页
            const range = 2;
            const start = Math.max(1, this.currentPage - range);
            const end = Math.min(this.totalPages, this.currentPage + range);

            const pages = [];
            for (let i = start; i <= end; i++) {
                pages.push(i);
            }
            return pages;
        },

        /**
         * 表头全选/取消全选
         * 当前选择的是所有书库，操作列表为 this.tableList
         * 当前选择的是当前页的数据库表，操作列表为 this.paginatedTableList
         */
        onSelectAllTable() {
            const checked = this.tableSelectAllChecked;

            // 选择是否是所有的数据库
            const tablesToUpdate = this.selectedOption === "all"
                ? this.tableList
                : this.paginatedTableList;

            // 将数据库变为当前选中的状态
            tablesToUpdate.forEach(table => table.checked = checked);

            // 更新父级列表状态
            this.updateFormSelectedTables();
        },

        /**
         * 选择单个表
         * @param {Boolean} checked - 事件对象
         * @param {Object} table - 表数据对象
         */
        onSelectTable(checked, table) {
            table.checked = checked;
            this.updateFormSelectedTables();

            // 更新表头全选状态
            if (!table.checked) {
                this.tableSelectAllChecked = false;
            } else {
                this.tableSelectAllChecked = this.paginatedTableList.every(t => t.checked);
            }
        },

        /**
         * 切换表格详情展开/收起状态
         * 如果是首次展开且没有列数据，则请求获取列信息
         * @param {Object} table - 表格对象
         * @returns {Promise<void>}
         */
        async toggleTableDetails(table) {
            // 如果是展开操作且没有列数据，则请求获取列信息
            if (!table.showDetails) {
                // 发送请求获取列信息
                const {data, code, message} = await axiosInstance({
                    url: "/table/tableColumnInfo",
                    params: {tableName: table.tableName}
                });

                // 请求成功时保存列数据
                if (code === 200) {
                    table.columns = data;
                    antd.message.success(message);
                }
            }

            // 切换展开/收起状态
            table.showDetails = !table.showDetails;
        },

        /* 更新表单中选中的表名 */
        updateFormSelectedTables() {
            const payload = {
                ...this.form,
                tableNames: this.selectedTableNames
            }
            // 更新父级 form 的内容
            this.$emit("update:form", payload);
        },

        /**
         * 重置父级表单
         * 如果要同时更新会变得很复杂所以在这里讲逻辑定义为：
         * 选型变化时直接取消全部，之后重新选择
         */
        resetForm() {
            this.tableSelectAllChecked = false;
            this.tableList.forEach(table => table.checked = false);
            this.updateFormSelectedTables();
        }
    },
    watch: {
        /* 监听选择模式变化 */
        selectedOption: 'resetForm',
        /* 监听当前页变化 */
        currentPage: 'resetForm',
        /* 监听每页条数变化 */
        itemsPerPage: 'resetForm',
        /* 数据库选择发生变化也重置表单 */
        dbSelect: 'resetForm',
    }
});