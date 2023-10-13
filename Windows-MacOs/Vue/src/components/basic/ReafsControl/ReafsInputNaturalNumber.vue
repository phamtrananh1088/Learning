<template>
  <div>
    <el-input
      :style="{ width }"
      :required="required"
      :disabled="disabled"
      :class="[required ? 'reafs-input-required' : 'reafs-input']"
      v-on="$listeners"
      v-bind="$attrs"
      v-model="defaultValue"
    >
    </el-input>
    <slot name="append"></slot>
  </div>
</template>
<script>
export default {
  name: "reafsinputnaturalnumber",
  props: {
    width: {
      type: String,
      default: "180px"
    },
    required: {
      type: Boolean,
      default: false
    },
    disabled: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      defaultValue: ""
    };
  },

  ///////////////////////////////////////////////
  // ウォッチドグ（監視）
  // バインド変数に変化有れば通知され以下関数が実行される
  ///////////////////////////////////////////////
  watch: {
    ///////////////////////////////////////////////
    // defaultValueを監視
    ///////////////////////////////////////////////
    defaultValue: function(value) {
      // 数値以外の文字は空文字に置換する
      value = value.replace(/[^0-9]/g, "");
      this.defaultValue = value;
    }
  }
};
</script>

<style scoped>
.reafs-input >>> .el-input__inner {
  height: 30px;
  line-height: 30px;
  border-radius: 1px;
}

.reafs-input-required >>> .el-input__inner {
  height: 30px;
  line-height: 30px;
  border-radius: 1px;
  background: lightgoldenrodyellow;
}

.el-button {
  padding-top: 7px;
  padding-bottom: 7px;
  margin-left: -4px;
  border-start-start-radius: 1px;
  border-end-start-radius: 1px;
}
</style>
