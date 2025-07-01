const MainTable = defineComponent({
    name: "MainTable",
    props: ["tableList", "rawTableList", "loading"],
    template: `
    <div class="card mt-2 shadow-sm">
        <div class="card-header bg-primary bg-opacity-10">
            <h5 class="card-title mb-0">
                <i class="bi bi-table me-2"></i>数据表列表
            </h5>
        </div>

        <div class="card-body p-0">
            <div class="table-responsive">
                <!-- 加载中... -->
                <div class="p-5 text-center" v-if="loading">
                    <div class="spinner-border" role="status">
                        <span class="visually-hidden">Loading...</span>
                    </div>
                </div>

                <!-- 展示当前数据库所有的表 -->
                <table class="table table-striped table-bordered table-hover mb-0" v-else>
                    <thead class="table-light">
                    <tr>
                        <th scope="col" width="2%">
                            <input class="form-check-input border-secondary" type="checkbox" value="option2">
                        </th>
                        <th scope="col" width="5%">#</th>
                        <th scope="col" width="30%">表名</th>
                        <th scope="col" width="35%">注释</th>
                        <th scope="col" width="18%">所属数据库</th>
                        <th class="text-center" scope="col" width="10%">操作</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr :key="index" v-for="(table,index) in paginatedTableList">
                        <th scope="row">
                         <input class="form-check-input border-secondary" type="checkbox" value="option2">
                       </th>
                        <th scope="row">{{index + 1}}</th>
                        <td><a class="text-decoration-none" href="#">{{ table.tableName}}</a></td>
                        <td>{{ table.comment }}</td>
                        <td>{{ table.tableCat }}</td>
                        <td class="text-center">
                            <button class="btn btn-sm btn-outline-primary">
                                <i class="bi bi-gear"></i> 生成
                            </button>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>

        <!-- 在表格显示卡片底部添加分页 -->
        <div class="card-footer bg-light">
            <div class="d-flex justify-content-between align-items-center">
                <div class="form-text">共 {{ rawTableList.length }} 条</div>

                <nav aria-label="Page navigation">
                    <ul class="pagination mb-0">
                        <li :class="{ disabled: currentPage === 1 }" class="page-item">
                            <a @click.prevent="currentPage = 1" class="page-link" href="#">
                                <i class="bi bi-chevron-double-left"></i>
                            </a>
                        </li>
                        <li :class="{ disabled: currentPage === 1 }" class="page-item">
                            <a @click.prevent="currentPage--" class="page-link" href="#">
                                <i class="bi bi-chevron-left"></i>
                            </a>
                        </li>

                        <!-- 显示页码 -->
                        <li :class="{ active: currentPage === page }" :key="page" class="page-item"
                            v-for="page in visiblePages">
                            <a @click.prevent="currentPage = page" class="page-link" href="#">{{ page }}</a>
                        </li>

                        <li :class="{ disabled: currentPage === totalPages }" class="page-item">
                            <a @click.prevent="currentPage++" class="page-link" href="#">
                                <i class="bi bi-chevron-right"></i>
                            </a>
                        </li>
                        <li :class="{ disabled: currentPage === totalPages }" class="page-item">
                            <a @click.prevent="currentPage = totalPages" class="page-link" href="#">
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
                        <li :key="index" v-for="(table,index) in tablePageOptions">
                            <a @click.prevent="itemsPerPage = table" class="dropdown-item" href="JavaScript:">
                                {{table}} 条/页
                            </a>
                        </li>
                    </ul>
                </div>
            </div>
        </div>
    </div>
    `,
    data() {
        return {
            // 分页相关数据
            currentPage: ref(1),
            // 每页数
            itemsPerPage: ref(10),
            // 表格选项
            tablePageOptions: [5, 10, 20, 50, 100, 150, 200]
        }
    },
    computed: {
        // 计算总页数
        totalPages() {
            const totalItems = this.rawTableList.length;
            return Math.ceil(totalItems / this.itemsPerPage);
        },
        // 分页后的数据
        paginatedTableList() {
            const start = (this.currentPage - 1) * this.itemsPerPage;
            const end = start + this.itemsPerPage;
            return this.tableList.slice(start, end);
        }
    },
    methods: {
        /* 计算可见的页码范围 */
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
        }
    }
})