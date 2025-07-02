// 如果一开始定义过了 defineComponent 就不要在下 script 标签中再写了
const {defineComponent} = Vue;

// 定义 Header 组件
const AppHeader = defineComponent({
    name: "AppHeader",
    template: `
        <header>
            <!-- 头部 -->
            <div class="header-content text-center mb-2 p-2 bg-light rounded shadow-sm">
                <h2 class="text-primary fw-bold mb-3">
                    <i class="bi bi-code-square me-2"></i>
                    Bunny{{ title || '代码生成器' }}
                </h2>
                <p class="text-muted mb-0">
                    快速生成数据库表对应的代码，这里可以跳转到 
                    <a href="/database" class="text-decoration-none"><i class="bi bi-database-fill"></i>数据库生成</a> 
                    或 
                    <a href="/sql" class="text-decoration-none"><i class="bi bi-filetype-sql"></i>SQL语句生成</a>
                </p>
                
                <!-- 代码仓库链接区域：包含GitHub和Gitee仓库链接 --> 
                <div class="d-flex justify-content-center align-items-center mt-2 gap-3">
                    <!-- GitHub 仓库链接 -->
                    <a class="btn btn-outline-dark d-flex align-items-center gap-2"
                       href="https://github.com/BunnyMaster/generator-code-server" target="_blank">
                        <svg class="feather feather-github" fill="none" height="20" stroke="currentColor"
                             stroke-linecap="round" stroke-linejoin="round" stroke-width="2" viewBox="0 0 24 24"
                             width="20"
                             xmlns="http://www.w3.org/2000/svg">
                            <path d="M9 19c-5 1.5-5-2.5-7-3m14 6v-3.87a3.37 3.37 0 0 0-.94-2.61c3.14-.35 6.44-1.54 6.44-7A5.44 5.44 0 0 0 20 4.77 5.07 5.07 0 0 0 19.91 1S18.73.65 16 2.48a13.38 13.38 0 0 0-7 0C6.27.65 5.09 1 5.09 1A5.07 5.07 0 0 0 5 4.77a5.44 5.44 0 0 0-1.5 3.78c0 5.42 3.3 6.61 6.44 7A3.37 3.37 0 0 0 9 18.13V22"></path>
                        </svg>
                        GitHub 仓库
                    </a>
        
                    <!-- Gitee 仓库链接 -->
                    <a class="btn btn-outline-danger d-flex align-items-center gap-2"
                       href="https://gitee.com/BunnyBoss/generator-code-server" target="_blank">
                        <svg height="20" viewBox="0 0 24 24" width="20" xmlns="http://www.w3.org/2000/svg">
                            <path d="M11.984 0A12 12 0 0 0 0 12a12 12 0 0 0 12 12a12 12 0 0 0 12-12A12 12 0 0 0 12 0zm6.09 5.333c.328 0 .593.266.592.593v1.482a.594.594 0 0 1-.593.592H9.777c-.982 0-1.778.796-1.778 1.778v5.63c0 .327.266.592.593.592h5.63c.982 0 1.778-.796 1.778-1.778v-.296a.593.593 0 0 0-.592-.593h-4.15a.59.59 0 0 1-.592-.592v-1.482a.593.593 0 0 1 .593-.592h6.815c.327 0 .593.265.593.592v3.408a4 4 0 0 1-4 4H5.926a.593.593 0 0 1-.593-.593V9.778a4.444 4.444 0 0 1 4.445-4.444h8.296Z"
                                  fill="currentColor"/>
                        </svg>
                        Gitee 仓库
                    </a>
                </div>
            </div>
        </header>
  `,
    data() {
        return {
            title: document.title,
        }
    }
});
