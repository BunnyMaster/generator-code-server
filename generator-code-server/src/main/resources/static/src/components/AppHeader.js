// 如果一开始定义过了 defineComponent 就不要在下 script 标签中再写了
const {defineComponent} = Vue;

// 定义 Header 组件
const AppHeader = defineComponent({
    name: "AppHeader",
    template: `
        <header class="app-header">
            <div class="header-content text-center mb-4 p-4 bg-light rounded shadow-sm">
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
            </div>
        </header>
  `,
    data() {
        return {
            title: document.title,
        }
    }
});
