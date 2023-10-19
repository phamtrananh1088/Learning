<template>
  <div class="middle-box">
    <div class="text-center animated fadeInDown">
      <h1>{{errorNumber}}</h1>
      <h3 class="font-bold">ページは見つかりませんでした。</h3>
      <slot></slot>
      <div class="error-desc">{{text}}</div>
      <div class="back">
        <Button type="success" @click="backHome" icon="md-arrow-round-back">TOPページへ戻る</Button>
      </div>
    </div>
  </div>
</template>
  <script>
import { Script } from "vm";
export default {
  props:{
    errorNumber:{
      type:String,
      default:'500'
    },
    text:{
      type:String,
      default:'エラーが出てきました。'
    }
  },
  methods: {
    backHome: function () {
      if (this.$router.options.routes[0].name == 'Index_R') {
        this.$router.push({ name: 'Rhome' });
      } else if (this.$router.options.routes[0].name == 'Index_T') {
        this.$router.push({ name: 'Thome' });
      } else if (this.$router.options.routes[0].name == 'Index_W') {
        this.$router.push({ name: 'WD00010' });
      } else {
        this.$router.push({ path: '/' });
      }
    }
  }
};
</script>
<style lang="less" scoped>
body {
  background-color: #fff;
}
.middle-box {
  text-align: center;
  padding-top: 80px;
  height: 100%;
  h1 {
    font-size: 140px;
    font-weight: 100;
  }
  .back {
    padding: 10px;
  }
}
</style>

