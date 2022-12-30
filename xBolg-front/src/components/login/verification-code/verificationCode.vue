<template>
    <div class="ValidCode disabled-select" :style="`width:${width}; height:${height};padding:0px 10px;`"
        @click="refreshCode">
        <span v-for="(item, index) in codeList" :key="index" :style="getStyle(item)">{{item.code}}</span>
    </div>
</template>

<script>
    import {
        toRefs,
        reactive,
        onMounted
    } from 'vue'
    export default {
        props: {
            width: {
                type: String,
                default: '80px'
            },
            height: {
                type: String,
                default: '34px'
            },
            length: {
                type: Number,
                default: 4
            }
        },
        setup(props, {
            emit
        }) {
            const state = reactive({
                codeList: [],
            });
            onMounted(() => {
                createdCode();
            });
            const refreshCode = () => {
                createdCode();
            };
            const createdCode = () => {
                const len = props.length
                const codeList = []
                const chars = 'ABCDEFGHJKMNPQRSTWXYZabcdefhijkmnprstwxyz0123456789'
                const charsLen = chars.length
                // 生成
                for (let i = 0; i < len; i++) {
                    const rgb = [Math.round(Math.random() * 220), Math.round(Math.random() * 240), Math.round(Math
                        .random() * 200)]
                    codeList.push({
                        code: chars.charAt(Math.floor(Math.random() * charsLen)),
                        color: `rgb(${rgb})`,
                        fontSize: `1${[Math.floor(Math.random() * 10)]}px`,
                        padding: `${[Math.floor(Math.random() * 10)]}px`,
                        transform: `rotate(${Math.floor(Math.random() * 90) - Math.floor(Math.random() * 90)}deg)`
                    })
                }
                // 指向
                state.codeList = codeList
                // 将当前数据派发出去
                emit('getVerificationCode', codeList.map(item => item.code).join(''))
            }
            const getStyle = (data) => {
                return `color: ${data.color}; font-size: ${data.fontSize}; padding: ${data.padding}; transform: ${data.transform}`
            }
            return {
                ...toRefs(state),
                onMounted,
                refreshCode,
                createdCode,
                getStyle
            }
        },
    }
</script>

<style scoped>
    .ValidCode {
        user-select: none;
        display: flex;
        justify-content: center;
        align-items: center;
        cursor: pointer;
    }

    .ValidCode span {
        display: inline-block;
    }
</style>