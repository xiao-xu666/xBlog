<template>
    <el-row :gutter="20">
        <el-col :xs="24" :sm="24" :md="24" :lg="18" :xl="18">
            <div class="left_box">
                <h3 class="title">
                    <el-icon style="margin-right: 10px;">
                        <User />
                    </el-icon>
                    个人设置
                </h3>
                <div class="set">
                    <h4>基本设置</h4>
                    <el-form ref="basicFormRef" :rules="basicRules" status-icon :model="basic" class="demo-form-inline">
                        <el-row :gutter="20">
                            <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="8">
                                <el-form-item prop="userName" label="昵称：" style="width: 100%;">
                                    <el-input v-model="basic.userName" placeholder="请输入用户名" />
                                </el-form-item>
                            </el-col>
                            <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="8">
                                <el-form-item prop="userSignature" label="签名：" style="width: 100%;">
                                    <el-input v-model="basic.userSignature" placeholder="请输入个性签名" maxlength="20"
                                        show-word-limit />
                                </el-form-item>
                            </el-col>
                            <el-col :xs="12" :sm="12" :md="12" :lg="5" :xl="5">
                                <el-form-item label="头像：" style="margin: auto;">
                                    <el-upload class="avatar-uploader" :action="uploadURL" :show-file-list="false"
                                        :on-success="handleAvatarSuccess" :before-upload="beforeUploadFunction">
                                        <!-- 用户没设置头像的情况下使用系统默认的 -->
                                        <img v-if="basic.userIcon" :src="basic.userIcon"
                                            style="width: 50px;border-radius: 50px;" />
                                        <img v-if="!basic.userIcon" src="../../../../assets/default_avatar.png"
                                            style="width: 50px;border-radius: 50px;" />
                                        <!-- <el-icon v-if="!basic.userIcon" class="avatar-uploader-icon">
                                            <Plus />
                                        </el-icon> -->
                                        <span style="margin-left: 10px;
                                        font-size: 10px;
                                        text-decoration: underline;line-height: 20px;">点击更换</span>
                                    </el-upload>
                                </el-form-item>
                            </el-col>
                            <el-col :xs="12" :sm="12" :md="12" :lg="3" :xl="3">
                                <el-form-item>
                                    <el-button plain color="#2fa7b9" @click="onBasicSubmit" style="margin-left: 50px;">
                                        提交
                                    </el-button>
                                </el-form-item>
                            </el-col>
                        </el-row>
                    </el-form>
                </div>
                <el-divider border-style="dashed" />
                <div class="set">
                    <h4>绑定邮箱</h4>
                    <p>
                        <span>已绑定邮箱：</span><span style="font-size: 25px;">{{userInfo.userEmail}}</span>
                    </p>
                    <p>邮箱作为您身份验证的重要方式，请谨慎操作！</p>
                    <p>
                        <el-form ref="toBindFormRef" :rules="toBindRules" status-icon :model="toBind"
                            class="demo-form-inline">
                            <el-row :gutter="20">
                                <el-col :xs="24" :sm="15" :md="12" :lg="12" :xl="12"
                                    v-show="showGetCode && !showNewEmail">
                                    <el-form-item prop="code" style="width: 100%;">
                                        <el-input v-model="toBind.code" placeholder="请输入邮箱验证码">
                                            <template #append>
                                                <input type="button" :plain="true" @click="getCode(1)" :disabled="!show"
                                                    style="width: 100%;height: 100%;border: 0px;background: none;width: 70px;color: #ababab;"
                                                    :value="codeText" />
                                            </template>
                                        </el-input>
                                    </el-form-item>
                                </el-col>
                                <el-col :xs="24" :sm="12" :md="12" :lg="9" :xl="8"
                                    v-show="showNewEmail && !showGetCode">
                                    <el-form-item prop="email" style="width: 100%;">
                                        <el-input v-model="toBind.email" placeholder="请输入新邮箱号" />
                                    </el-form-item>
                                </el-col>
                                <el-col :xs="24" :sm="12" :md="12" :lg="9" :xl="8"
                                    v-show="showNewEmail && !showGetCode">
                                    <el-form-item prop="code2" style="width: 100%;">
                                        <el-input v-model="toBind.code2" placeholder="请输入新邮箱验证码">
                                            <template #append>
                                                <input type="button" :plain="true" @click="getCode(2)" :disabled="!show"
                                                    style="width: 100%;height: 100%;border: 0px;background: none;width: 70px;color: #ababab;"
                                                    :value="codeText" />
                                            </template>
                                        </el-input>
                                    </el-form-item>
                                </el-col>

                                <el-col :xs="24" :sm="24" :md="24" :lg="6" :xl="8"
                                    v-show="showNewEmail && !showGetCode">
                                    <el-form-item style="width: 100%;">
                                        <el-button plain color="#2fa7b9" @click="toBindSubmit">提交
                                        </el-button>
                                    </el-form-item>
                                </el-col>
                                <el-col :xs="24" :sm="9" :md="12" :lg="12" :xl="12"
                                    v-show="showGetCode && !showNewEmail">
                                    <el-form-item style="width: 100%;">
                                        <el-button plain color="#2fa7b9" @click="confirmCode">
                                            确认更换
                                        </el-button>
                                    </el-form-item>
                                </el-col>
                                <el-col :xs="24" :sm="24" :md="24" :lg="24" :xl="24"
                                    v-show="!showGetCode && !showNewEmail">
                                    <el-button plain color="#2fa7b9" @click="showGetCode = true"
                                        style="margin-left: 0px;">更换绑定邮箱
                                    </el-button>
                                </el-col>
                            </el-row>
                        </el-form>
                    </p>
                </div>
                <el-divider border-style="dashed" />

                <!-- 修改密码 -->
                <div class="set">
                    <h4>修改密码</h4>
                    <p style="display: inline-flex;
                    justify-content: center;
                    align-items: center;">
                        <el-icon style="margin-right: 5px;">
                            <CircleCheck />
                        </el-icon>密码6~18位字母、数字、特殊字符组成
                    </p>
                    <br>
                    <p style="display: inline-flex;
                    justify-content: center;
                    align-items: center;">
                        <el-icon style="margin-right: 5px;">
                            <Warning />
                        </el-icon>安全提示：新密码请勿与旧密码过于相似
                    </p>
                    <el-form ref="modifyFormRef" :rules="modifyRules" :model="modify" class="demo-form-inline">
                        <el-row :gutter="20">
                            <el-col :xs="24" :sm="24" :md="10" :lg="10" :xl="10">
                                <el-form-item prop="usedPass" label="旧密码：" style="width: 100%;">
                                    <el-input v-model="modify.usedPass" show-password placeholder="请输入旧密码" />
                                </el-form-item>
                            </el-col>
                            <el-col :xs="24" :sm="24" :md="10" :lg="10" :xl="10">
                                <el-form-item prop="newPass" label="新密码：" style="width: 100%;">
                                    <el-input v-model="modify.newPass" show-password placeholder="请输入新密码" />
                                </el-form-item>
                            </el-col>
                            <el-col :xs="24" :sm="24" :md="4" :lg="4" :xl="4" style="text-align: center;">
                                <el-form-item style="margin-right: 0px;">
                                    <el-button plain color="#2fa7b9" @click="modifySubmit">提交
                                    </el-button>
                                </el-form-item>
                            </el-col>
                        </el-row>
                    </el-form>
                </div>
            </div>
        </el-col>

        <!-- 右侧个人信息展示 -->
        <el-col class="hidden-md-and-down" :lg="6" :xl="6">
            <div class="right_box">
                <h3 class="title">
                    <el-icon style="margin-right: 10px;">
                        <Grid />
                    </el-icon>
                    信息展示
                </h3>
                <div class="info">
                    <div class="right_photo">
                        <!-- 用户没设置头像的情况下使用系统默认的 -->
                        <img v-if="userInfo.userIcon === null || userInfo.userIcon === ''" src="../../../../assets/default_avatar.png">
                        <img v-if="userInfo.userIcon !== null && userInfo.userIcon !== ''" :src="userInfo.userIcon">
                    </div>
                    <h2>Hi，{{userInfo.userName}}</h2>
                    <p class="email">{{userInfo.userEmail}}</p>
                    <p class="slogan" v-if="userInfo.userSignature===null">这家伙很懒什么也没留下</p>
                    <p class="slogan" v-if="userInfo.userSignature!==null">{{userInfo.userSignature}}</p>
                </div>
                <el-divider />
                <h3 class="title">
                    <el-icon style="margin-right: 10px;">
                        <Sunrise />
                    </el-icon>
                    账号类型
                </h3>
                <div class="type">
                    <h3 v-if="userInfo.userType==0">"管理员"</h3>
                    <h3 v-if="userInfo.userType==1">"普通用户"</h3>
                    <br>
                    <p>账号类型分为：普通用户、管理员</p>
                    <p>管理员可对本站文章、界面、用户进行管理</p>
                    <br>
                    <p>
                        站长邮箱：<a href="http://mail.qq.com/cgi-bin/qm_share?t=qm_mailme&amp;email=2529023117@qq.com"
                            target="_blank" data-v-634963c2="">2529023117 @qq.com</a>
                    </p>
                </div>
            </div>
        </el-col>
    </el-row>
</template>
<script>
    import {
        reactive,
        toRefs,
        ref,
        onMounted
    } from 'vue'
    import {
        Plus
    } from '@element-plus/icons-vue'
    import axios from "axios";
    import {
        UploadProps,
        ElMessage,
        ElLoading,
        ElNotification
    } from 'element-plus'


    export default {
        setup(props) {
            onMounted(() => {
                // 获取登录的用户信息
                const tokenStr = sessionStorage.getItem("userInfo");
                const userInfo = JSON.parse(tokenStr);
                state.userInfo = userInfo.data;

                // 将用户的基础信息填进表单中
                state.basic.userName = state.userInfo.userName
                state.basic.userSignature = state.userInfo.userSignature
                state.basic.userIcon = state.userInfo.userIcon
                

                // 将邮箱中间位置转为星号
                state.userEmail = state.userInfo.userEmail;
                state.userInfo.userEmail = state.userInfo.userEmail.substr(0, 3) + '****' + state.userInfo
                    .userEmail.substr(7)
            })

            const basicFormRef = ref('')
            const toBindFormRef = ref('');
            const modifyFormRef = ref('');

            const state = reactive({
                // 当前登录的用户信息
                userInfo: [],
                // 用户邮箱
                userEmail: '',
                // 基本信息
                basic: {
                    userName: '',
                    userSignature: '',
                    userIcon: ''
                },
                // 更换邮箱绑定
                toBind: {
                    // 随机生成的验证码
                    verificationCode: '',
                    // 输入的验证码
                    code: '',
                    email: '',
                    // 输入的验证码
                    code2: ''
                },
                // 获取验证码 点击后为禁止状态
                show: true,
                // 获取验证码的文字 点击后改变
                codeText: '获取验证码',
                // 当前秒数
                count: '',
                // 时间  计时器
                timer: null,
                // 计时三分钟   后 销毁验证码
                timer2: null,
                // 存放生成的六位随机数   验证码
                code: '',
                // 三分钟后清除验证码,计时器
                clearCount: "",
                // 图片上传到服务器的路径
                uploadURL: process.env.VUE_APP_URL + "/upload",
                // 服务器路径
                url: process.env.VUE_APP_URL,
                // 修改密码
                modify: {
                    usedPass: "",
                    newPass: ""
                },
                // 点击更改绑定邮箱 显示获取验证码框
                showGetCode: false,
                // 输入旧邮箱的验证码正确后  点击按钮 显示输入新邮箱的表单
                showNewEmail: false
            })

            // 用户名正则表达式判断
            var nameGrep = /^[\u4e00-\u9fa5a-zA-Z0-9_]{2,8}$/;
            // 用户名的非空、正则验证
            const validateUserName = (rule, value, callback) => {
                if (!value) {
                    return callback(new Error('请填写此字段~'))
                } else {
                    if (value !== state.userInfo.userName) {
                        if (!nameGrep.test(value)) {
                            callback(new Error('用户名由2~8位中英文、数字、下划线组成'));
                        } else {
                            let params = {
                                registerName: value
                            };
                            axios.get("/register/showUserName", {
                                params
                            }).then((res) => {
                                if (res.data.code == 1) {
                                    callback(new Error('该用户名太受欢迎了，请更换一个'));
                                } else {
                                    callback()
                                }
                            })
                        }
                    } else {
                        callback()
                    }

                }
            }
            // 基本信息修改  正则验证
            const basicRules = reactive({
                userName: [{
                    validator: validateUserName,
                    trigger: 'blur'
                }],
            })

            // 旧邮箱的验证码的非空、输入正确验证
            const validateCode = (rule, value, callback) => {
                if (!state.show) {
                    if (!value) {
                        return callback(new Error('请填写此字段~'))
                    } else {
                        // 输入的验证码正确
                        if (value != state.toBind.verificationCode) {
                            return callback(new Error('验证码有误，请重新输入'))
                        } else {
                            callback()
                        }
                    }
                } else {
                    return callback(new Error('获取验证码，填写此字段~'))
                }
            }

            // 判断邮箱的正则是否通过，没有通过则先不验证此字段
            // ruleFormRef.value.validateField('registerEmail', err => {})

            // 邮箱正则表达式判断
            var emailGrep =
                /[\w!#$%&'*+/=?^_`{|}~-]+(?:\.[\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\w](?:[\w-]*[\w])?\.)+[\w](?:[\w-]*[\w])?/;
            // 邮箱的非空验证
            const validateEmail = (rule, value, callback) => {
                if (!value) {
                    return callback(new Error('请填写此字段~'))
                } else {
                    if (!emailGrep.test(value)) {
                        callback(new Error('请按照正确的邮箱格式输入'));
                    } else {
                        let params = {
                            registerEmail: value
                        };
                        axios.get("/register/showUserEmail", {
                            params
                        }).then((res) => {
                            if (res.data.code == 1) {
                                callback(new Error('此邮箱已被注册，请更换一个'));
                            } else {
                                callback()
                            }
                        })
                    }
                }
            }

            // 新邮箱的验证码的非空、输入正确验证
            const validateCode2 = (rule, value, callback) => {
                toBindFormRef.value.validateField('email', err => {
                    if (err) {
                        if (!state.show) {
                            if (!value) {
                                return callback(new Error('请填写此字段~'))
                            } else {
                                // 输入的验证码正确
                                if (value != state.toBind.verificationCode) {
                                    return callback(new Error('验证码有误，请重新输入'))
                                } else {
                                    callback()
                                }
                            }
                        } else {
                            return callback(new Error('获取验证码，填写此字段~'))
                        }
                    }
                })

            }

            // 更换绑定邮箱  正则验证
            const toBindRules = reactive({
                code: [{
                    validator: validateCode,
                    trigger: 'blur'
                }],
                email: [{
                    validator: validateEmail,
                    trigger: 'blur'
                }],
                code2: [{
                    validator: validateCode2,
                    trigger: 'blur'
                }]
            });

            const validateUsedPass = (rule, value, callback) => {
                if (!value) {
                    return callback(new Error('请填写此字段~'))
                } else {
                    // 输入的旧密码正确
                    if (value != state.userInfo.userPass) {
                        return callback(new Error('旧密码输入有误，请重新输入'))
                    } else {
                        callback()
                    }
                }
            }

            // 密码正则表达式判断
            var passGrep = /^[a-zA-Z0-9_.-=*&^%$#@!+]{6,18}$/;
            const validateNewPass = (rule, value, callback) => {
                if (!value) {
                    return callback(new Error('请填写此字段~'))
                } else {
                    if (!passGrep.test(value)) {
                        callback(new Error('密码6~18位字母、数字、特殊字符组成'));
                    } else {
                        callback()
                    }
                }
            }

            // 修改密码正则验证
            const modifyRules = {
                usedPass: [{
                    validator: validateUsedPass,
                    trigger: 'blur'
                }],
                newPass: [{
                    validator: validateNewPass,
                    trigger: 'blur'
                }],
            }


            // 图片上传格式、大小要求
            const beforeUploadFunction = (rawFile) => {
                if (rawFile.type !== 'image/jpeg' &&
                    rawFile.type !== 'image/jpg' &&
                    rawFile.type !== 'image/png' &&
                    rawFile.type !== 'image/gif') {
                    ElMessage.error('仅支持图片格式.png | .jpg | .jpeg | .gif ')
                    return false
                } else if (rawFile.size / 1024 / 1024 > 5) {
                    ElMessage.error('仅支持大小不超过5MB的图片!')
                    return false
                }
                return true
            }

            // 图片上传成功后执行的函数
            const handleAvatarSuccess = (response) => {
                state.basic.userIcon = response.data;
            }

            // 提交基本信息
            const onBasicSubmit = () => {
                // 信息没有任何变化
                if (state.basic.userName !== state.userInfo.userName ||
                    state.basic.userSignature !== state.userInfo.userSignature ||
                    state.basic.userIcon !== state.userInfo.userIcon) {
                    // 校验表单
                    basicFormRef.value.validate((valid) => {
                        // 表单校验成功 提交数据到后台进行修改
                        if (valid) {
                            let params = {
                                userId: state.userInfo.userId,
                                userName: state.basic.userName,
                                userSignature: state.basic.userSignature,
                                userIcon: state.basic.userIcon
                            };
                            // 将修改信息传给服务器
                            axios.get("/user/updateUserBasicsInfo", {
                                params
                            }).then((res) => {
                                if (res.data.code == 0) {
                                    // 自动重新登录一次 修改token的值
                                    let params = {
                                        loginName: state.basic.userName,
                                        loginPass: state.userInfo.userPass,
                                    };
                                    axios.get("/login", {
                                        params
                                    }).then((res) => {
                                        if (res.data.code == "0") {
                                            // 修改token的值
                                            window.sessionStorage.setItem("userInfo", JSON
                                                .stringify(res.data));
                                            // 提示
                                            ElMessage({
                                                message: '基础信息修改成功~',
                                                type: 'success',
                                            })
                                            // 刷新界面
                                            location.reload();
                                        }
                                    })
                                }
                            })
                        }
                    })
                } else {
                    ElMessage({
                        message: '小样，基础信息没有任何的变化~',
                        type: 'success',
                    })
                }
            }

            // 获取验证码60秒倒计时
            const TIME_COUNT = 60;
            // 3分钟=180秒，三分钟后清除验证码
            const TIME_CLEAR_COUNT = 180;

            // 存储确认好的邮箱  一旦用户再次修改则清除验证码 需重新获取
            let affirmEmail = "";

            // 获取验证码 
            const getCode = (type) => {
                // 邮箱是否输入正确
                toBindFormRef.value.validateField('email', err => {
                    // type = 1  为旧邮箱  不需要进行邮箱验证
                    if (err || type == 1) {
                        var Email = '';
                        if (type == 1) {
                            Email = state.userEmail // 旧邮箱
                        } else {
                            Email = state.toBind.email // 新输入的邮箱
                        }
                        // 生成六位数随机验证码发送给后台
                        for (var i = 0; i < 6; i++) {
                            state.toBind.verificationCode += Math.floor(Math.random() * 10);
                        }
                        // 将验证码、用户输入的邮箱传给后台，发送给用户邮箱
                        const params = {
                            registerEmail: Email, // 用户当前邮箱
                            content: "【xBlog博客】验证码：" + state.toBind.verificationCode +
                                "，此验证码用于更换邮箱绑定，请勿将验证码告知他人，有效期3分钟，请妥善保管。"
                        }
                        axios.get("/sendEmail", {
                            params
                        }).then((res) => {
                            if (res.data.code == 0) {
                                // 点击后倒计时60秒
                                if (!state.timer) {
                                    state.count = TIME_COUNT;
                                    state.show = false;
                                    state.timer = setInterval(() => {
                                        if (state.count > 0 && state.count <= TIME_COUNT) {
                                            if (Email === state.toBind.email || type == 1) {
                                                state.count--;
                                                state.codeText = state.count + 's';
                                            } else {
                                                state.show = true;
                                                window.clearInterval(state.timer);
                                                state.timer = null;
                                                state.codeText = "重新获取";
                                                Email = ""
                                                state.toBind.email = ""
                                            }

                                        } else {
                                            state.show = true;
                                            window.clearInterval(state.timer);
                                            state.timer = null;
                                            state.codeText = "重新获取";
                                            Email = ""
                                        }
                                    }, 1000)
                                }
                                // 3分钟后销毁生成的验证码
                                if (!state.timer2) {
                                    state.clearCount = TIME_CLEAR_COUNT;
                                    state.timer2 = setInterval(() => {
                                        if (state.clearCount > 0 && state.clearCount <= TIME_CLEAR_COUNT) {
                                            state.clearCount--;
                                        } else {
                                            window.clearInterval(state.timer2);
                                            state.timer2 = null;
                                            // 存放验证码的变量清空
                                            state.toBind.verificationCode = "";
                                        }
                                    }, 1000)
                                }
                            } else {
                                ElMessage.error('验证码获取失败.')
                            }
                        })

                    }
                })
            }


            // 提交旧邮箱验证码
            const confirmCode = () => {
                // 校验验证码是否输入正确
                toBindFormRef.value.validateField('code', err => {
                    if (err) {
                        // 显示新邮箱的输入框 
                        state.showNewEmail = true;
                        // 关闭旧邮箱验证码输入框 
                        state.showGetCode = false

                        // 清除计时器
                        state.show = true;
                        window.clearInterval(state.timer);
                        state.timer = null;
                        state.codeText = "获取验证码";
                        // window.clearInterval(state.timer2);
                        // state.timer2 = null;
                        state.toBind.verificationCode = ""
                    } else {
                        ElMessage.error('按要求进行更换邮箱绑定操作.')
                        return false;
                    }
                })
            }

            // 提交邮箱更改绑定
            const toBindSubmit = () => {
                // 新邮箱验证
                toBindFormRef.value.validateField('email', err1 => {
                    if (err1) {
                        // 验证码输入校验
                        toBindFormRef.value.validateField('code2', err2 => {
                            if (err2) {
                                // 清除计时器
                                state.show = true;
                                window.clearInterval(state.timer);
                                state.timer = null;
                                state.codeText = "获取验证码";
                                // clearInterval(state.timer2);
                                // state.timer2 = null;
                                state.toBind.verificationCode = ""

                                // 修改邮箱
                                let params = {
                                    userId: state.userInfo.userId,
                                    userEmail: state.toBind.email,
                                };
                                // 将修改信息传给服务器
                                axios.get("/user/updateUserEmail", {
                                    params
                                }).then((res) => {
                                    if (res.data.code == 0) {
                                        // 自动重新登录一次 修改token的值
                                        let params = {
                                            loginName: state.userInfo.userName,
                                            loginPass: state.userInfo.userPass,
                                        };
                                        axios.get("/login", {
                                            params
                                        }).then((res) => {
                                            if (res.data.code == "0") {
                                                // 修改token的值
                                                window.sessionStorage.setItem(
                                                    "userInfo", JSON
                                                    .stringify(res.data));
                                                // 提示
                                                ElMessage({
                                                    message: '更换绑定邮箱成功~',
                                                    type: 'success',
                                                })
                                                // 刷新界面
                                                location.reload();
                                            }
                                        })
                                    }
                                })
                            } else {
                                ElMessage.error('按要求进行更换邮箱绑定操作.')
                                return false;
                            }
                        })
                    } else {
                        ElMessage.error('按要求进行更换邮箱绑定操作.')
                        return false;
                    }

                })
            }

            // 提交修改密码
            const modifySubmit = () => {
                // 旧密码验证
                modifyFormRef.value.validateField('usedPass', err => {
                    if (err) {
                        // 新密码验证
                        modifyFormRef.value.validateField('newPass', err => {
                            if (err) {
                                // 新密码 != 旧密码
                                if (state.modify.newPass !== state.modify.usedPass) {
                                    // 修改邮箱
                                    let params = {
                                        userId: state.userInfo.userId,
                                        userPass: state.modify.newPass,
                                    };
                                    // 将修改信息传给服务器
                                    axios.get("/user/updateUserPass", {
                                        params
                                    }).then((res) => {
                                        if (res.data.code == 0) {
                                            // 自动重新登录一次 修改token的值
                                            let params = {
                                                loginName: state.userInfo.userName,
                                                loginPass: state.modify.newPass,
                                            };
                                            axios.get("/login", {
                                                params
                                            }).then((res) => {
                                                if (res.data.code == "0") {
                                                    // 修改token的值
                                                    window.sessionStorage.setItem(
                                                        "userInfo", JSON
                                                        .stringify(res.data));
                                                    // 提示
                                                    ElMessage({
                                                        message: '修改密码成功~',
                                                        type: 'success',
                                                    })
                                                    // 刷新界面
                                                    location.reload();
                                                }
                                            })
                                        }
                                    })
                                } else {
                                    ElMessage.error('新密码与旧密码过于相似')
                                    return false;
                                }


                            } else {
                                ElMessage.error('按要求进行修改密码操作.')
                                return false;
                            }
                        })
                    } else {
                        ElMessage.error('按要求进行修改密码操作.')
                        return false;
                    }
                })
            }
            return {
                ...toRefs(state),
                onBasicSubmit,
                handleAvatarSuccess,
                beforeUploadFunction,
                basicFormRef,
                basicRules,
                toBindRules,
                toBindFormRef,
                confirmCode,
                getCode,
                toBindSubmit,
                modifyFormRef,
                modifyRules,
                modifySubmit
            }
        }
    }
</script>
<style scoped>
    .personSetup_box {
        width: 100%;
        height: auto;
    }

    .personSetup_box>h3 {
        color: #2fa7b9;
        margin-bottom: 10px;
        padding: 20px 20px;
        background-color: white;
    }

    .title {
        color: #2fa7b9;
        margin-bottom: 10px;
        padding: 20px 20px;
        display: inline-flex;
        justify-content: center;
        align-items: center;
    }

    .left_box {
        width: 100%;
        height: auto;
        background: white;
        padding: 20px;
        /* 添加此属性 padding间距不扩大div */
        box-sizing: border-box;
    }

    .set {
        text-align: left;
        padding: 0px 20px;
        margin-bottom: 10px;
        color: #8f8f8f;
        line-height: 35px;
    }

    .set h4 {
        line-height: 45px;
        color: #8f8f8f;
    }

    .right_box {
        width: 100%;
        height: auto;
        background: white;
        padding: 20px;
        /* 添加此属性 padding间距不扩大div */
        box-sizing: border-box;
    }

    .right_photo {
        position: relative;
        text-align: center;
        height: 100px;
        width: 100px;
        margin-left: -50px;
        left: 50%;
    }

    .info {
        text-align: center;
    }

    .right_photo img {
        width: 100%;
        border-radius: 50%;
    }

    .right_box h2 {
        padding-top: 10px;
        letter-spacing: 0;
        font-size: 30px;
    }

    .email {
        padding: 5px 0px;
        font-size: 14px;
        font-weight: lighter;
    }

    .slogan {
        opacity: 0.3;
        font-size: 15px;
    }

    .type {
        text-align: center;
        opacity: 0.4;
        line-height: 30px;
    }
</style>