const AppGeneratorPage = defineComponent({
    name: "MainGeneratorPage",
    template: `
<div class="offcanvas offcanvas-start" data-bs-scroll="false" tabindex="-1" ref="offcanvasElementRef">
    <!-- 侧边栏头部 -->
    <div class="offcanvas-header bg-primary text-white">
        <div>
            <h5 class="offcanvas-title mb-1">
                <i class="bi bi-file-earmark-code me-2"></i>模板生成页面
            </h5>
            <p class="small mb-0 opacity-75">已生成的文件列表</p>
        </div>
        <button @click="onGeneratorPageFlagClick" aria-label="Close" class="btn-close btn-close-white" type="button"></button>
    </div>

    <!-- 侧边栏内容区域 -->
    <div class="offcanvas-body p-0">
        <ol class="list-group list-group-numbered rounded-0">
            <li class="list-group-item border-0 p-3" v-for="[table, value] in Object.entries(generatorData)" :key="table">
                <h6 class="mb-0 fw-bold text-primary d-inline-block"><i class="bi bi-table me-2"></i>{{table}}</h6>
                <span class="badge bg-primary rounded-pill float-end">{{value.length}} 模板</span>
                
                <!-- 生成的文件列表 -->
                <ul class="list-group list-group-flush">
                    <!-- 单个文件卡片 -->
                    <li class="list-group-item p-0 mb-2 border-0" v-for="(item, index) in value" :key="item.id">
                        <div class="card shadow-sm border-0">
                            <!-- 文件标题 - 可折叠 -->
                            <div class="card-header bg-light d-flex justify-content-between align-items-center p-3" 
                                 role="button" data-bs-toggle="collapse" :data-bs-target="'#collapse-' + item.id" aria-expanded="false">
                                <div class="d-flex align-items-center">
                                    <i class="bi bi-bi-file-earmark-code me-2 text-primary fs-5"></i>
                                    <span class="text-truncate" style="max-width: 99%" :title="item.path">
                                        【{{item.comment}}】{{item.path}}
                                    </span>
                                </div>
                                <i class="bi bi-chevron-down transition-all"></i>
                            </div>
                            
                            <!-- 文件内容 -->
                            <div class="collapse" :id="'collapse-' + item.id">
                                <div class="card-body p-0 bg-dark">
                                    <div class="position-relative">
                                        <!-- 使用条件渲染显示不同图标 -->
                                        <i v-if="!copied" 
                                           class="bi bi-clipboard position-absolute text-white fs-4" 
                                           role="button" style="top: 10px; right: 10px" @click="onCopyToClipboard(item.code)"></i>
                                        
                                        <i v-else
                                           class="bi bi-clipboard-check position-absolute text-success fs-4"
                                           role="button" style="top: 10px; right: 10px"></i>
                                    </div>
                                    <pre>
                                       <code class="language-javascript hljs">
{{item.code}}
                                        </code>
                                    </pre>
                                </div>
                            </div>
                        </div>
                    </li>
                </ul>
            </li>
        </ol>
    </div>

    <!-- 侧边栏底部操作按钮 -->
    <div class="offcanvas-footer p-3 bg-light border-top">
        <div class="d-grid gap-2">
            <button class="btn btn-primary" @click="onDownloadAllFile">
                <i class="bi bi-download me-2"></i>下载全部文件
            </button>
        </div>
    </div>
</div>

    `,
    props: {
        // 是否显示生成页面
        generatorPageFlag: {type: Boolean, default: true},
        // 生成模板数据
        generatorData: {type: Object},
    },
    data() {
        return {
            // 控制图标状态的响应式变量
            copied: ref(false),
            // 存储 offcanvas 实例
            offcanvas: ref(null)
        }
    },
    methods: {
        /**
         * 点击复制图表
         * 几秒后恢复原状
         */
        async onCopyToClipboard(code) {
            this.copied = true;

            try {
                await navigator.clipboard.writeText(code);
                antd.notification.open({
                    type: 'success',
                    message: '复制成功',
                    description: '已将内容复制至剪切板',
                    duration: 3,
                });
            } catch (err) {
                antd.notification.open({
                    type: 'error',
                    message: '复制失败',
                    description: err.message``,
                    duration: 3,
                });
                console.error('复制失败:', err);
                // 回退到传统方法
                const textarea = document.createElement('textarea');
                textarea.value = code;
                document.body.appendChild(textarea);
                textarea.select();
                document.execCommand('copy');
                document.body.removeChild(textarea);
            }

            // 显示已复制图标时长
            if (this.copied) {
                setTimeout(() => {
                    this.copied = false;
                }, 2000)
            }
        },

        /* 下载全部文件 */
        onDownloadAllFile() {
            // 遍历所有文件并下载
            Object.values(this.generatorData).flat().forEach(item => {
                const blob = new Blob([item.code], {type: 'text/plain'});
                const url = URL.createObjectURL(blob);
                const link = document.createElement('a');

                // 从路径中提取文件名
                const fileName = item.path.split('/').pop();

                link.href = url;
                link.download = fileName;
                document.body.appendChild(link);
                link.click();

                // 清理
                setTimeout(() => {
                    document.body.removeChild(link);
                    URL.revokeObjectURL(url);
                }, 100);
            });
        },

        /* 关闭窗口 */
        onGeneratorPageFlagClick() {
            this.$emit("update:generatorPageFlag", !this.generatorPageFlag);
        },
    },
    mounted() {
        // 初始化 OffCanvas
        this.offcanvas = new bootstrap.Offcanvas(this.$refs.offcanvasElementRef);
        // 监听隐藏事件
        this.$refs.offcanvasElementRef.addEventListener('hidden.bs.offcanvas', () => {
            this.$emit("update:generatorPageFlag", false);
        });
    },
    watch: {
        generatorPageFlag(newVal) {
            if (newVal) {
                this.offcanvas.show();
            } else {
                this.offcanvas.hide();
            }
        },
    }
});