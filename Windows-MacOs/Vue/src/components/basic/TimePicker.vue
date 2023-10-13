<template>
  <div>
    <el-time-select
      :style="{width}"
      :required="required"
      :class="[required ? 'timepicker-required' : 'timepicker']"
      :is-range="isrange"
      v-on="$listeners"
      v-bind="$attrs"
    ></el-time-select>
  </div>
</template>
<script>
export default {
  name: 'timepicker',
  props: {
    width: {
      type: String,
      default: '180px'
    },
    required: {
      type: Boolean,
      default: false,
    },
    isrange: {
      type: Boolean,
      default: false,
    },
  },
}
</script>

<style scoped>
  .timepicker >>> .el-input__inner {
    height: 30px;
    line-height: 30px;
    border-radius: 1px;
    padding-right: 5px;
  }

  .timepicker-required >>> .el-input__inner {
    height: 30px;
    line-height: 30px;
    border-radius: 1px;
    padding-right: 5px;
    background: lightgoldenrodyellow;
  }

</style>