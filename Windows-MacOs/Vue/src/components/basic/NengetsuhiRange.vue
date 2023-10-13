<template>
  <div style="display:flex">
    <el-date-picker
      :style="{width}"
      :required="required?required:dateStartRequired"
      :class="[(required?required:dateStartRequired) ? 'nengetsu-required' : 'nengetsu']"
      :format="dateStartformat"
      v-model="startDate"
    ></el-date-picker>
    <el-time-select
      :style="{width}"
      :class="[(required?required:timeStartRequired) ? 'nengetsu-required' : 'nengetsu']"
      v-model="startTime"
    ></el-time-select>
    <span class="el-range-separator">~</span>
    <el-date-picker
      :style="{width}"
      :required="(required?required:dateEndRequired)"
      :class="[(required?required:dateEndRequired) ? 'nengetsu-required' : 'nengetsu']"
      :format="dateEndformat"
      v-model="endDate"
    ></el-date-picker>
    <el-time-select
      :style="{width}"
      :class="[(required?required:timeEndRequired) ? 'nengetsu-required' : 'nengetsu']"
      v-model="endTime"
    ></el-time-select>
  </div>
</template>
<script>
export default {
  name: 'nengetsu',
  props: {
    width: {
      type: String,
      default: '130px'
    },
    required: {
      type: Boolean,
      default: false,
    },
    dateStartRequired: {
      type: Boolean,
      default: false,
    },
    timeStartRequired: {
      type: Boolean,
      default: false,
    },
    dateEndRequired: {
      type: Boolean,
      default: false,
    },
    timeEndRequired: {
      type: Boolean,
      default: false,
    },
    startDate: String,
    startTime: String,
    endDate: String,
    endTime: String,
    dateStartformat: String,
    dateEndformat: String,
  },
}
</script>

<style scoped>
  .nengetsu >>> .el-input__inner {
    height: 30px;
    line-height: 30px;
    border-radius: 1px;
    padding-right: 5px;
  }

  .nengetsu-required >>> .el-input__inner {
    height: 30px;
    line-height: 30px;
    border-radius: 1px;
    padding-right: 5px;
    background: lightgoldenrodyellow;
  }

  .nengetsu-required >>> .el-range-input {
    padding-right: 5px;
    background: lightgoldenrodyellow;
  }
</style>