var e=Object.defineProperty,a=Object.defineProperties,t=Object.getOwnPropertyDescriptors,l=Object.getOwnPropertySymbols,r=Object.prototype.hasOwnProperty,n=Object.prototype.propertyIsEnumerable,o=(a,t,l)=>t in a?e(a,t,{enumerable:!0,configurable:!0,writable:!0,value:l}):a[t]=l,u=(e,a,t)=>new Promise(((l,r)=>{var n=e=>{try{u(t.next(e))}catch(a){r(a)}},o=e=>{try{u(t.throw(e))}catch(a){r(a)}},u=e=>e.done?l(e.value):Promise.resolve(e.value).then(n,o);u((t=t.apply(e,a)).next())}));import{E as s,b as i,t as d,z as p,g as c,G as m,H as g,v,h as f,o as b,i as y,y as h,I as _,w as k,J as w,K as O,c as N,F as C,L as q,M as U,O as x,P,Q as S,R as j,S as W,A as L,U as T,u as A,V as M,W as R,X as V,Y as D,Z as E,_ as $,$ as F,x as z,q as I,a0 as K,a1 as B,a as G}from"./vendor-CiQEHsBN.js";import{s as H,u as J}from"./table-ButEA8kS.js";const Z=s("vmsStore",{state:()=>({generators:[],serverOptions:[],webOptions:[],formValue:{author:"Bunny",packageName:"cn.bunny.services",requestMapping:"/api",className:"",tableName:"",simpleDateFormat:"yyyy-MM-dd HH:mm:ss",tablePrefixes:"t_,sys_,qrtz_,log_",comment:"",path:[]},formOption:{generatorServer:[],generatorWeb:[]}}),getters:{},actions:{generator(e){return u(this,null,(function*(){const u=yield(e=>H({url:"/vms/generator",method:"post",data:e}))(e);200!==u.code&&window.$message.error(u.message),this.generators=u.data.map((e=>{return u=((e,a)=>{for(var t in a||(a={}))r.call(a,t)&&o(e,t,a[t]);if(l)for(var t of l(a))n.call(a,t)&&o(e,t,a[t]);return e})({},e),s={path:e.path.replace(".vm","")},a(u,t(s));var u,s})),window.$message.success(`生成成功，共 ${this.generators.length} 数据`)}))},getVmsPathList(){return u(this,null,(function*(){const e=yield H({url:"/vms/getVmsPathList",method:"get"});if(200!==e.code)return window.$message.error(e.message),{};this.webOptions=e.data.web,this.serverOptions=e.data.server}))},clearGenerators(){this.generators=[]}}}),Q=[{title:"序号",key:"no",titleAlign:"center",align:"center",render:(e,a)=>a+1},{title:"列名称",key:"columnName",titleAlign:"center",align:"center",render:e=>i(d,{type:"primary"},{default:()=>[e.columnName]})},{title:"字段名称",key:"fieldName",titleAlign:"center",align:"center",render:e=>i(d,null,{default:()=>[e.fieldName]})},{title:"数据库字段类型",key:"jdbcType",titleAlign:"center",align:"center",render:e=>i(d,null,{default:()=>[e.jdbcType]})},{title:"Java类型",key:"javaType",titleAlign:"center",align:"center",render:e=>i(d,{type:"warning"},{default:()=>[e.javaType]})},{title:"是否为主键",key:"isPrimaryKey",titleAlign:"center",align:"center",render:e=>e.isPrimaryKey?i(d,{type:"error"},{default:()=>[p("是")]}):i(d,{type:"success"},{default:()=>[p("否")]})},{title:"字段注释",key:"comment",titleAlign:"center",align:"center",render:e=>i(d,{type:"info"},{default:()=>[e.comment]})}],X=c({__name:"index",setup(e){const a=m(),t=J(),l=g([]),r=()=>u(this,null,(function*(){const e=a.query.tableName;l.value=yield t.getColumnInfo(e)}));return v((()=>{r()})),(e,a)=>(b(),f(y(h),{bordered:!0,columns:y(Q),data:l.value},null,8,["columns","data"]))}});function Y(e,a){const t=new File([e],a,{type:"text/plain"}),l=URL.createObjectURL(t),r=document.createElement("a");r.href=l,r.download=a,document.body.appendChild(r),r.click(),requestIdleCallback((()=>{document.body.removeChild(r),URL.revokeObjectURL(r.href)}))}const ee=c({__name:"select-button-group",props:{data:{type:Array,required:!0},selected:{type:Array,default:()=>[]},idKey:{type:String,default:"name"}},emits:["update:selected"],setup(e,{emit:a}){const t=e,l=a,r=()=>{const e=t.data.map((e=>e[t.idKey]));l("update:selected",[...e])},n=()=>{const e=t.selected,a=t.data.map((e=>e[t.idKey])).filter((a=>!e.includes(a)));l("update:selected",a)},o=()=>{l("update:selected",[])};return(e,a)=>(b(),f(y(_),{size:"small"},{default:k((()=>[i(y(w),{round:"",type:"primary",onClick:r},{default:k((()=>a[0]||(a[0]=[p("全选")]))),_:1}),i(y(w),{type:"warning",onClick:n},{default:k((()=>a[1]||(a[1]=[p("反选")]))),_:1}),i(y(w),{round:"",type:"error",onClick:o},{default:k((()=>a[2]||(a[2]=[p("全不选")]))),_:1})])),_:1}))}}),ae=c({__name:"generator-form",setup(e){const a=Z(),{formValue:t,formOption:l}=O(a);return(e,r)=>(b(),N(C,null,[i(y(q),{cols:24,"x-gap":24},{default:k((()=>[i(y(U),{span:8,label:"作者名称",path:"author"},{default:k((()=>[i(y(x),{value:y(t).author,"onUpdate:value":r[0]||(r[0]=e=>y(t).author=e),placeholder:"作者名称"},null,8,["value"])])),_:1}),i(y(U),{span:8,label:"requestMapping名称",path:"requestMapping"},{default:k((()=>[i(y(x),{value:y(t).requestMapping,"onUpdate:value":r[1]||(r[1]=e=>y(t).requestMapping=e),placeholder:"requestMapping名称"},null,8,["value"])])),_:1}),i(y(U),{span:8,label:"表名称",path:"tableName"},{default:k((()=>[i(y(x),{value:y(t).tableName,"onUpdate:value":r[2]||(r[2]=e=>y(t).tableName=e),placeholder:"表名称"},null,8,["value"])])),_:1})])),_:1}),i(y(q),{cols:24,"x-gap":24},{default:k((()=>[i(y(U),{span:8,label:"类名称",path:"className"},{default:k((()=>[i(y(x),{value:y(t).className,"onUpdate:value":r[3]||(r[3]=e=>y(t).className=e),placeholder:"类名称"},null,8,["value"])])),_:1}),i(y(U),{span:8,label:"包名称",path:"packageName"},{default:k((()=>[i(y(x),{value:y(t).packageName,"onUpdate:value":r[4]||(r[4]=e=>y(t).packageName=e),placeholder:"包名称"},null,8,["value"])])),_:1}),i(y(U),{span:8,label:"时间格式",path:"simpleDateFormat"},{default:k((()=>[i(y(x),{value:y(t).simpleDateFormat,"onUpdate:value":r[5]||(r[5]=e=>y(t).simpleDateFormat=e),placeholder:"时间格式"},null,8,["value"])])),_:1})])),_:1}),i(y(q),{cols:24,"x-gap":24},{default:k((()=>[i(y(U),{span:8,label:"去除开头前缀",path:"tablePrefixes"},{default:k((()=>[i(y(x),{value:y(t).tablePrefixes,"onUpdate:value":r[6]||(r[6]=e=>y(t).tablePrefixes=e),placeholder:"去除开头前缀"},null,8,["value"])])),_:1}),i(y(U),{span:8,label:"修改注释名称",path:"comment"},{default:k((()=>[i(y(x),{value:y(t).comment,"onUpdate:value":r[7]||(r[7]=e=>y(t).comment=e),placeholder:"修改注释名称"},null,8,["value"])])),_:1})])),_:1}),i(y(q),{cols:24,"x-gap":24},{default:k((()=>[i(y(U),{span:12,label:"生成后端",path:"generatorServer"},{default:k((()=>[i(y(P),{value:y(l).generatorServer,"onUpdate:value":r[9]||(r[9]=e=>y(l).generatorServer=e)},{default:k((()=>[i(y(S),null,{default:k((()=>[(b(!0),N(C,null,j(y(a).serverOptions,((e,a)=>(b(),f(y(W),{key:a,value:e.name},{default:k((()=>[p(L(e.label),1)])),_:2},1032,["value"])))),128)),i(ee,{selected:y(l).generatorServer,"onUpdate:selected":r[8]||(r[8]=e=>y(l).generatorServer=e),data:y(a).serverOptions,"id-key":"name"},null,8,["selected","data"])])),_:1})])),_:1},8,["value"])])),_:1}),i(y(U),{span:12,label:"生成前端",path:"generatorWeb"},{default:k((()=>[i(y(P),{value:y(l).generatorWeb,"onUpdate:value":r[11]||(r[11]=e=>y(l).generatorWeb=e)},{default:k((()=>[i(y(S),null,{default:k((()=>[(b(!0),N(C,null,j(y(a).webOptions,((e,a)=>(b(),f(y(W),{key:a,value:e.name,"onUpdate:value":a=>e.name=a},{default:k((()=>[p(L(e.label),1)])),_:2},1032,["value","onUpdate:value"])))),128)),i(ee,{selected:y(l).generatorWeb,"onUpdate:selected":r[10]||(r[10]=e=>y(l).generatorWeb=e),data:y(a).webOptions,"id-key":"name"},null,8,["selected","data"])])),_:1})])),_:1},8,["value"])])),_:1})])),_:1})],64))}}),te=c({__name:"generator-preview",setup(e){const a=T(),t=A(),l=Z();return(e,r)=>(b(),f(y(M),null,{default:k((()=>[(b(!0),N(C,null,j(y(l).generators,((e,l)=>(b(),f(y(R),{key:l,name:e.path,title:e.path},{"header-extra":k((()=>[i(y(w),{quaternary:"",type:"info",onClick:l=>((e,l)=>{l=l.split("/")[1];const r=g(l);a.info({title:"修改文件名",positiveText:"下载",negativeText:"取消",content:()=>i(x,{placeholder:"Tiny Input",clearable:!0,value:r.value,onInput:e=>r.value=e},null),onPositiveClick:()=>{Y(e,r.value)},onNegativeClick:()=>{t.info("取消下载")}})})(e.code,e.path)},{default:k((()=>r[0]||(r[0]=[p("下载")]))),_:2},1032,["onClick"])])),default:k((()=>[i(y(x),{autosize:{minRows:3},placeholder:e.comment,value:e.code,type:"textarea"},null,8,["placeholder","value"])])),_:2},1032,["name","title"])))),128))])),_:1}))}}),le=Z(),{formValue:re,formOption:ne}=O(le),oe=()=>{ne.value.generatorServer=le.serverOptions.map((e=>e.name)),ne.value.generatorWeb=le.webOptions.map((e=>e.name))},ue=()=>{const e=le.serverOptions.map((e=>e.name)),a=ne.value.generatorServer;ne.value.generatorServer=e.filter((e=>!a.includes(e)));const t=le.webOptions.map((e=>e.name)),l=ne.value.generatorWeb;ne.value.generatorWeb=t.filter((e=>!l.includes(e)))},se=()=>{ne.value.generatorServer=[],ne.value.generatorWeb=[],re.value.path=[]},ie=()=>{const e=ne.value.generatorWeb,a=ne.value.generatorServer;re.value.path=[...a,...e],re.value.path.length<=0&&window.$message.error("选择要生成的模板")},de=Z(),{formOption:pe}=O(de),ce=()=>pe.value.generatorServer.length>0||pe.value.generatorWeb.length>0,me={author:{required:!0,trigger:["blur","change","input"],message:"作者不能为空"},packageName:{required:!0,trigger:["blur","change","input"],message:"包名不能为空"},requestMapping:{required:!0,trigger:["blur","change","input"],message:"请求路径不能为空"},className:{required:!0,trigger:["blur","change","input"],message:"类名不能为空"},tableName:{required:!0,trigger:["blur","change","input"],message:"表名不能为空"},simpleDateFormat:{required:!0,trigger:["blur","change","input"],message:"日期格式不能为空"},generatorServer:[{required:!0,trigger:["blur","change","input"],message:"选择要生成的服务端",validator:ce}],generatorWeb:[{required:!0,trigger:["blur","change","input"],message:"选择要生成的前端",validator:ce}]},ge=c({__name:"index",setup(e){const a=m(),t=Z(),{formValue:l,formOption:r}=O(t),n=A(),o=g(),s=V((()=>!(r.value.generatorWeb.length>0||r.value.generatorServer.length>0))),d=e=>{var a;e.preventDefault(),null==(a=o.value)||a.validate((e=>u(this,null,(function*(){e?e.forEach((e=>{e.forEach((e=>{n.error(`${e.message}->${e.field}`)}))})):(ie(),yield t.generator(l.value))}))))},c=()=>{t.generators.forEach((e=>{Y(e.code,e.path.split("/")[1])}))},f=()=>u(this,null,(function*(){ie();var e;(e=>{try{const a=e.headers["content-disposition"];let t="download.zip";if(a){const e=a.match(/filename="?(.+)"?/);e&&e[1]&&(t=e[1])}const l=window.URL.createObjectURL(new Blob([e.data])),r=document.createElement("a");r.href=l,r.setAttribute("download",t),document.body.appendChild(r),r.click(),r.remove(),window.URL.revokeObjectURL(l)}catch(a){}})(yield(e=l.value,H({url:"/vms/downloadByZip",method:"POST",data:e,responseType:"blob"})))}));return v((()=>{(e=>{re.value.tableName=e.toString();let a=e;re.value.tablePrefixes.split(/[,，]/).forEach((e=>{a=a.replace(e,"")})),re.value.className=a})(a.query.tableName),t.getVmsPathList()})),(e,a)=>(b(),N(C,null,[i(y(D),{ref_key:"formRef",ref:o,"label-width":80,model:y(l),rules:y(me)},{default:k((()=>[i(ae),i(y(E),null,{default:k((()=>[i(y(q),{class:"justify-items-center",cols:"3","x-gap":"24"},{default:k((()=>[i(y($),null,{default:k((()=>[i(y(w),{"attr-type":"button",type:"success",onClick:y(oe)},{default:k((()=>a[1]||(a[1]=[p("全部选择")]))),_:1},8,["onClick"]),i(y(w),{"attr-type":"button",type:"warning",onClick:y(ue)},{default:k((()=>a[2]||(a[2]=[p("全部反选")]))),_:1},8,["onClick"]),i(y(w),{"attr-type":"button",type:"error",onClick:y(se)},{default:k((()=>a[3]||(a[3]=[p("全选取消")]))),_:1},8,["onClick"])])),_:1}),i(y($),null,{default:k((()=>[i(y(w),{"attr-type":"button",type:"success",onClick:d},{default:k((()=>a[4]||(a[4]=[p("开始生成")]))),_:1}),i(y(w),{"attr-type":"button",type:"error",onClick:a[0]||(a[0]=e=>y(t).clearGenerators())},{default:k((()=>a[5]||(a[5]=[p(" 清空已生成 ")]))),_:1}),i(y(w),{disabled:!(y(t).generators.length>0),"attr-type":"button",type:"primary",onClick:c},{default:k((()=>[p(" 下载全部 "+L(y(t).generators.length),1)])),_:1},8,["disabled"])])),_:1}),i(y($),{class:"w-full"},{default:k((()=>[i(y(w),{disabled:y(s),"attr-type":"button",class:"w-full",type:"success",onClick:f},{default:k((()=>a[6]||(a[6]=[p(" 下载zip ")]))),_:1},8,["disabled"])])),_:1})])),_:1})])),_:1})])),_:1},8,["model","rules"]),i(te)],64))}}),ve=c({__name:"index",setup(e){const a=I(),t=m(),l=J(),r=Z(),{formValue:n,formOption:o}=O(r),s=F({tableName:"",comment:"",tableCat:"",tableType:""}),d=()=>u(this,null,(function*(){const e=t.query.tableName,a=yield l.getTableMetaData(e);Object.assign(s,a),n.value.comment=s.comment}));return v((()=>{d()})),(e,l)=>(b(),f(y(z),null,{header:k((()=>[i(y(z),{title:"数据库信息"},{default:k((()=>[G("span",{class:"color-blue cursor-pointer",onClick:l[0]||(l[0]=e=>y(a).push("/"))},"回到首页"),G("ul",null,[G("li",null,"表名："+L(y(t).query.tableName),1),G("li",null,"表注释："+L(s.comment),1),G("li",null,"数据库名："+L(s.tableCat),1),G("li",null,"类型："+L(s.tableType),1)])])),_:1})])),default:k((()=>[i(y(B),{animated:"",type:"line"},{default:k((()=>[i(y(K),{name:"generator-code",tab:"生成"},{default:k((()=>[i(ge)])),_:1}),i(y(K),{name:"columns-info",tab:"列字段"},{default:k((()=>[i(X)])),_:1})])),_:1})])),_:1}))}});export{ve as default};
