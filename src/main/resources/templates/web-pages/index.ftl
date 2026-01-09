<#include "./common/reference.ftl"/>
<!DOCTYPE html>
<html lang="zh">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>生成器首页</title>
    <!-- 引入Element Plus样式 -->
    <link href="https://unpkg.com/element-plus/dist/index.css" rel="stylesheet"/>
    <!-- 引入Tailwind CSS -->
    <script src="https://unpkg.com/@tailwindcss/browser@4"></script>
    <!-- 引入Vue和Element Plus -->
    <script src="https://unpkg.com/vue@3/dist/vue.global.js"></script>
    <script src="https://unpkg.com/element-plus"></script>
    <script src="https://unpkg.com/axios/dist/axios.min.js"></script>
    <style>
        .gradient-bg {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
        }

        .glass-effect {
            background: rgba(255, 255, 255, 0.95);
            backdrop-filter: blur(10px);
        }

        .info-card {
            transition: all 0.3s ease;
        }

        .info-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 10px 25px rgba(0, 0, 0, 0.1);
        }
    </style>
</head>
<body class="gradient-bg min-h-screen py-8">
<div id="app" class="container mx-auto px-4">
    <!-- 标题区域 -->
    <div class="text-center mb-10">
        <h1 class="text-4xl font-bold text-white mb-4">数据库信息</h1>
        <p class="text-xl text-blue-100">查看数据库配置和系统信息</p>
    </div>

    <!-- 主内容区域 -->
    <div class="glass-effect rounded-2xl shadow-2xl p-8">
        <!-- 路由列表 -->
        <div class="mb-10">
            <h2 class="text-2xl font-semibold text-gray-800 mb-6 pb-3 border-b border-gray-200 flex items-center">
                路由列表
            </h2>
            <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
                <a v-for="link in routerList" :key="link.url" :href="link.url"
                   class="info-card bg-white rounded-xl p-5 border border-gray-200 hover:border-blue-300 hover:shadow-md transition-all duration-300 flex items-center justify-center"
                >
                    <span class="text-lg font-medium text-gray-700">{{ link.name }}</span>
                </a>
            </div>
        </div>
    </div>
</div>

<!-- 工具类 -->
<@commonPackage/>

<script>
    const {createApp, onMounted} = Vue;
    const {ElMessage, ElLoading} = ElementPlus;

    const app = createApp({
        setup() {
            const routerList = [
                {url: '/', name: '首页'},
                {url: '/database-parser', name: '数据库生成'},
                {url: '/sql-parser', name: 'SQL解析'},
            ];

            return {
                routerList,
            };
        }
    });

    app.use(ElementPlus);
    app.mount('#app');
</script>
</body>
</html>