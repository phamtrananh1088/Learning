<template>
  <div style="display: flex;">
    <div>
      <el-input
        :style="{width}"
        :required="required"
        :disabled="disabled"
        v-on="$listeners"
        v-bind="$attrs"
        maxlength="4"
        class="reafs-input">
      </el-input>
    </div>
    <div>
      <el-input
        :required="required"
        :disabled="disabled"
        v-on="$listeners"
        v-bind="$attrs"
        maxlength="3"
        class="reafs-input2">
      </el-input>
    </div>
    <div style="padding-left: 5px;">
      <slot name="append"></slot>
    </div>
  </div>
</template>
<script>
export default {
  name: 'reafsinputtext2',
  props: {
    width: {
      type: String,
      default: '180px'
    },
    required: {
      type: Boolean,
      default: false,
    },
    disabled: {
      type: Boolean,
      default: false,
    },
  },
}
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

  .el-button{
    padding-top: 7px;
    padding-bottom: 7px;
    margin-left: -4px;
    border-start-start-radius: 1px;
    border-end-start-radius: 1px;
  }
  .reafs-input2{
    width: 55px;
  }
</style>