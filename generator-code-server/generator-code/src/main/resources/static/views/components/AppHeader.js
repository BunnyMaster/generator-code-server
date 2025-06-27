// 如果一开始定义过了 defineComponent 就不要在下 script 标签中再写了
const {defineComponent} = Vue;

// 定义 Header 组件
const AppHeader = defineComponent({
    name: "AppHeader",
    template: `
     <div class="text-center mb-4">
        <h2 class="text-primary fw-bold">
            <i class="bi bi-code-square me-2"></i>
            Bunny代码生成器
        </h2>
        <p class="text-muted">快速生成数据库表对应的代码</p>
    </div>
  `
});
