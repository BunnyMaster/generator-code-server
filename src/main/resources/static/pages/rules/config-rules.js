// 生成策略规则
const strategyRules = {
    provider: [
        {required: true, message: "生成的类型不能为空", trigger: "blur"}
    ],
    mode: [
        {required: true, message: "生成模式不能为空", trigger: "blur"}
    ],
}

// 公共配置
const commonConfigRules = {
    author: [
        {
            required: false,
            pattern: /^[\u4e00-\u9fa5a-zA-Z0-9_\-\s]+$/,
            message: "作者名称只能包含中文、英文、数字、下划线和横线",
            trigger: "blur"
        }
    ],
    commentTimeFormat: [
        {
            required: false,
            pattern: /^[yYMdDhHmsS:\-\/\s]+$/,
            message: "时间格式只能包含日期时间格式字符",
            trigger: "blur"
        }
    ],
    baseOutputDir: [
        {required: true, message: "基础输出根目录不能为空", trigger: "blur"},
        {
            pattern: /^[a-zA-Z]:[\\/].*|^\/.*|^[a-zA-Z0-9_\-\\/]+$/,
            message: "请输入有效的目录路径",
            trigger: "blur"
        }
    ],
    ignoreTablePrefixes: [
        {
            required: false,
            validator: (rule, value, callback) => {
                if (value && value.some(prefix => !/^[a-zA-Z_][a-zA-Z0-9_]*$/.test(prefix))) {
                    callback(new Error("表前缀只能包含英文、数字和下划线，且以字母或下划线开头"));
                } else {
                    callback();
                }
            },
            trigger: "change"
        }
    ]
}

// 服务端配置
const typeConfigRules = {
    requestPrefix: [
        {
            required: false,
            pattern: /^\/[a-zA-Z0-9_\-/]*$/,
            message: "请求映射前缀必须以斜杠开头，只能包含英文、数字、下划线、横线和斜杠",
            trigger: "blur"
        }
    ],
    packagePath: [
        {required: true, message: "包名称不能为空", trigger: "blur"}
    ],
    typeOutputDir: [
        {required: true, message: "输出根目录不能为空", trigger: "blur"},
        {pattern: /^[a-zA-Z]:[\\/].*|^\/.*|^[a-zA-Z0-9_\-\\/]+$/, message: "请输入有效的目录路径", trigger: "blur"}
    ],
    templates: [
        {
            required: true,
            message: "模板配置",
            trigger: "change",
            validator: (rule, value, callback) => {
                if (!value || Object.keys(value).length === 0) {
                    callback(new Error("请至少配置一个模板"));
                } else {
                    callback();
                }
            }
        }
    ]
};

// 模板规则
const templateRules = {
    key: [
        {required: true, message: "模板key不能为空", trigger: "blur"}
    ],
    fileExtension: [
        {required: true, message: "文件扩展名不能为空", trigger: "blur"},
        {
            pattern: /^\.[a-zA-Z0-9_\-]+$/,
            message: "文件扩展名必须以点号开头，只能包含英文、数字、下划线和横线",
            trigger: "blur"
        }
    ],
    filenameType: [
        {required: true, message: "文件名类型不能为空", trigger: "blur"}
    ],
    prefix: [
        {
            required: false,
            pattern: /^[a-zA-Z0-9_\-]*$/,
            message: "文件前缀只能包含英文、数字、下划线和横线",
            trigger: "blur"
        }
    ],
    suffix: [
        {
            required: false,
            pattern: /^[a-zA-Z0-9_\-]*$/,
            message: "文件后缀只能包含英文、数字、下划线和横线",
            trigger: "blur"
        }
    ],
    templatePath: [
        {required: true, message: "模板路径不能为空", trigger: "blur"},
        {pattern: /^[a-zA-Z]:[\\/].*|^\/.*|^[a-zA-Z0-9_\-\\/.]+$/, message: "请输入有效的模板文件路径", trigger: "blur"}
    ],
    outputSubDir: [
        {
            required: false,
            pattern: /^[a-zA-Z0-9_\-\\/]*$/,
            message: "输出子目录只能包含英文、数字、下划线、横线和路径分隔符",
            trigger: "blur"
        }
    ],
    overwrite: [
        {required: true, message: "覆盖已存在文件", trigger: "blur"}
    ],
    isGenerate: [
        {required: true, message: "是否生成", trigger: "blur"}
    ],
};
