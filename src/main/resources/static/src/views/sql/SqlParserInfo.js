const SqlParserInfo = {
    name: "SqlParserInfo",
    template: `
        <div class="card database-info-card">
            <!-- 数据库信息折叠面板 -->
            <div class="card-header bg-light d-flex justify-content-between align-items-center" data-bs-toggle="collapse" 
                 :data-bs-target="'#databaseInfoCollapse'" aria-expanded="true" aria-controls="databaseInfoCollapse" 
                 style="cursor: pointer">
                <h4 class="mb-0"><i class="fas fa-database me-2"></i>当前表信息</h4>
                <i class="fas fa-chevron-down transition-all"></i>
            </div>
            
            <div id="databaseInfoCollapse" class="collapse show">
                <div class="card-body">
                    <!-- 表信息折叠面板 -->
                    <div class="table-info-section mb-3">
                        <div class="d-flex justify-content-between align-items-center mb-2" 
                             data-bs-toggle="collapse" 
                             :data-bs-target="'#tableInfoCollapse'" 
                             aria-expanded="true" 
                             aria-controls="tableInfoCollapse"
                             style="cursor: pointer">
                            <h5 class="mb-0"><i class="fas fa-table me-2"></i>表信息</h5>
                            <i class="fas fa-chevron-down transition-all"></i>
                        </div>
                        
                        <div id="tableInfoCollapse" class="collapse show">
                            <div class="row">
                                <div class="col-md-6">
                                    <ul class="list-group list-group-flush">
                                        <li class="list-group-item">
                                            <strong>表名:</strong> {{ tableInfo.tableName }}
                                        </li>
                                        <li class="list-group-item">
                                            <strong>注释:</strong> {{ tableInfo.comment || '无' }}
                                        </li>
                                    </ul>
                                </div>
                                <div class="col-md-6">
                                    <ul class="list-group list-group-flush">
                                        <li class="list-group-item">
                                            <strong>表类型:</strong> {{ tableInfo.tableType || '无' }}
                                        </li>
                                        <li class="list-group-item">
                                            <strong>表目录:</strong> {{ tableInfo.tableCat || '无' }}
                                        </li>
                                    </ul>
                                </div>
                            </div>
                        </div>
                    </div>
        
                    <!-- 列信息折叠面板 -->
                    <div>
                        <div class="d-flex justify-content-between align-items-center mb-2" 
                             data-bs-toggle="collapse" 
                             :data-bs-target="'#columnInfoCollapse'" 
                             aria-expanded="true" 
                             aria-controls="columnInfoCollapse"
                             style="cursor: pointer">
                            <h5 class="mb-0"><i class="fas fa-columns me-2"></i>列信息 ({{ columnMetaList.length }})</h5>
                            <i class="fas fa-chevron-down transition-all"></i>
                        </div>
                        
                        <div id="columnInfoCollapse" class="collapse show">
                            <div class="column-list">
                                <div :key="index" class="card column-item mb-2" v-for="(column, index) in columnMetaList">
                                    <div class="card-body">
                                        <div class="d-flex justify-content-between align-items-start">
                                            <p class="card-text text-muted small mb-2">
                                                {{column.columnName.replace(/\`/g, '')}} {{ column.comment || '无注释' }}
                                            </p>
                                            <span class="badge bg-secondary" v-if="column.isPrimaryKey">主键</span>
                                        </div>
                                        <div class="row">
                                            <div class="col-md-4">
                                                <small class="text-muted">Java类型:</small>
                                                <span class="badge badge-java ms-2">{{ column.javaType }}</span>
                                            </div>
                                            <div class="col-md-4">
                                                <small class="text-muted">JDBC类型:</small>
                                                <span class="badge badge-jdbc ms-2">{{ column.jdbcType }}</span>
                                            </div>
                                            <div class="col-md-4">
                                                <small class="text-muted">JS类型:</small>
                                                <span class="badge badge-js ms-2">{{ column.javascriptType }}</span>
                                            </div>
                                        </div>
                                        <div class="mt-2">
                                            <small class="text-muted">字段名:</small>
                                            <code class="ms-2">{{ column.lowercaseName.replace(/\`/g, '') }}</code>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
`,
    props: {
        // sql语句中表信息
        tableInfo: {type: Object, required: true},
        // sql语句中列信息
        columnMetaList: {type: Object, required: true},
    },
    data() {
        return {};
    },

};