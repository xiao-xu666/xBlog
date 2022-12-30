<template>
    <div class="adBox">
        <a :href="advertisingLink" target="_blank">
            <img :src="advertisingImg" alt="">
        </a>
    </div>
</template>
<script>
    import {
        onMounted,
        reactive,
        toRefs
    } from "vue";
    export default {
        props: ['advertisingImg', 'advertisingLink'],
        setup(props) {
            const state = reactive({

            })
            return {
                ...toRefs(state)
            }
        }
    }
</script>

<style scoped>
    .adBox {
        width: 100%;
        background: white;
        height: auto;
        border-radius: 6px;
        margin-bottom: 20px;
    }

    .adBox img {
        display: block;
        width: 100%;
        height: auto;
        border-radius: 6px;
    }
</style>