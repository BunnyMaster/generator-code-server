var e=(e,t,a)=>new Promise(((s,r)=>{var o=e=>{try{i(a.next(e))}catch(t){r(t)}},n=e=>{try{i(a.throw(e))}catch(t){r(t)}},i=e=>e.done?s(e.value):Promise.resolve(e.value).then(o,n);i((a=a.apply(e,t)).next())}));import{a2 as t,a3 as a,H as s}from"./vendor-B-dPIqey.js";const r=t.create({baseURL:"/api",timeout:5e4,headers:{"Content-Type":"application/json;charset=utf-8"},paramsSerializer:e=>a.stringify(e)});r.interceptors.request.use((e=>{const t=localStorage.getItem("accessToken");return t&&(e.headers.Authorization=t),e}),(e=>Promise.reject(e))),r.interceptors.response.use((e=>"blob"===e.config.responseType||"arraybuffer"===e.config.responseType?e:200===e.status?e.data:Promise.reject(e.data.message||"Error")),(e=>{if(e.response.data){const{code:t,message:a}=e.response.data;500===t?window.$message.error(a):window.$message.error(a||"系统出错")}return Promise.reject(e.message)}));const o=s("tableStore",{state:()=>({tableList:[],tableListLoading:!1,dbList:[]}),getters:{},actions:{getDbList(){return e(this,null,(function*(){this.tableListLoading=!0;const e=yield r({url:"/table/getDbList",method:"GET"});if(200!==e.code)return window.$message.error(e.message),void(this.tableListLoading=!1);const t=e.data.map((e=>({label:e.tableCat,value:e.tableCat,comment:e.comment})));t.unshift({label:"无",value:void 0,comment:"查询全部"}),this.dbList=t,this.tableListLoading=!1}))},getDbTables(t){return e(this,null,(function*(){const e=yield(a=t,r({url:"/table/getDbTables",method:"get",params:a}));var a;200!==e.code&&window.$message.error(e.message),this.tableList=e.data}))},getTableMetaData(t){return e(this,null,(function*(){const e=yield(a={tableName:t},r({url:"/table/getTableMetaData",method:"get",params:a}));var a;return 200!==e.code?(window.$message.error(e.message),{}):e.data}))},getColumnInfo(t){return e(this,null,(function*(){const e=yield(a={tableName:t},r({url:"/table/getColumnInfo",method:"get",params:a}));var a;return 200!==e.code?(window.$message.error(e.message),{}):e.data}))}}});export{r as s,o as u};
