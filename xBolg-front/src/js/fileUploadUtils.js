
import axios from "axios";
import { ElMessage } from "element-plus";
import hex_md5 from "md5-hex";
import Tool from "@/js/tool" 

function fileUpload(shardIndex,file,insertFn) {
    //from表单提交
    var fd = new FormData();
    //获取表单中的file
    var file= file
    //文件分片  以20MB去分片
    var shardSize = 1 * 1024 * 1024;
    //定义分片索引
    var shardIndex = shardIndex;
    //定义分片的起始位置
    var start = (shardIndex-1) * shardSize;
    //定义分片结束的位置  file哪里来的?
    var end = Math.min(file.size,start + shardSize);
    //从文件中截取当前的分片数据
    var fileShard = file.slice(start,end);
    //分片的大小
    var size = file.size;
    //总片数
    var shardTotal = Math.ceil(size / shardSize);
    //文件的后缀名
    var fileName = file.name;
    var suffix = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length).toLowerCase();
    //把视频的信息存储为一个字符串
    var filedetails=file.name+file.size+file.type+file.lastModifiedDate;
    //使用当前文件的信息用md5加密生成一个key 这个加密是根据文件的信息来加密的  如果相同的文件 加的密还是一样的
    var key = hex_md5(filedetails);
    var key10 = parseInt(key,16);
    //把加密的信息 转为一个64位的
    var key62 = Tool._10to62(key10);
    //前面的参数必须和controller层定义的一样
    fd.append('file',fileShard);
    fd.append('suffix',suffix);
    fd.append('shardIndex',shardIndex);
    fd.append('shardSize',shardSize);
    fd.append('shardTotal',shardTotal);
    fd.append('size',size);
    fd.append("key",key62)
    axios({
        url:"/fileUpload",
        method:"post",
        cache: false,
        data:fd,
        processData: false,
        contentType: false,
    }).then(res=>{
        if(res.data.code ==0 ){
            if(shardIndex < shardTotal){
                var index=shardIndex +1;
                 fileUpload(index,file,insertFn);
            }else
            {
                insertFn(process.env.VUE_APP_URL+"/download/"+res.data.data)
            }
        }
    })
}

//判断这个加密文件存在不存在
function check(file,insertFn) {
    //把视频的信息存储为一个字符串
    var filedetails=file.name+file.size+file.type+file.lastModifiedDate;
    //使用当前文件的信息用md5加密生成一个key 这个加密是根据文件的信息来加密的  如果相同的文件 加的密还是一样的
    var key = hex_md5(filedetails);
    var key10 = parseInt(key,16);
    //把加密的信息 转为一个64位的
    var key62 = Tool._10to62(key10);
    //检查这个key存在不存在
    axios({
        url:"/check",
        method:"get",
        params:{'key':key62},
    }).then(res=>{
        if(res.data.code == 0){
            let data = res.data.data
            if(data.startsWith("index_")){
                data = data.slice(6)
                fileUpload(data,file,insertFn);
           }else if(data.startsWith("path_")){
                data = data.slice(5)
                insertFn(process.env.VUE_APP_URL+"/download/"+data)
           }
        }else{
            console.log("重新开始上传");
            fileUpload(1,file,insertFn);
        }
    })
}

export {
    fileUpload,
    check
}