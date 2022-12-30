export default {
    slice(file, pieceSize) {
        console.log(file);
        const fileChunkList = [];
        //源文件名称
        const fileName = file.name;
        //记录切片具体size值
        let cur = 0
        //记录切片下标
        let index = 0
        console.log(pieceSize);
        while (cur < file.size) {
            fileChunkList.push({
                //切片文件
                file: file.slice(cur, cur + pieceSize),
                //生成切片名称
                hash: file.name + '_' + index,
            });
            index += 1;
            cur += pieceSize;
        }
        //将切片好的文件都追加上文件标志
        const requestList = [];
        fileChunkList.map((file,hash)=>{
            const formData = new FormData();
            console.log(file);
            formData.append("file",file); //单个文件切片
            formData.append("hash",hash); //文件切片名称
            formData.append("filename",fileName); //文件名称，方便后台生成文件夹
            requestList.push(formData);
        })
        return requestList;
    },
}