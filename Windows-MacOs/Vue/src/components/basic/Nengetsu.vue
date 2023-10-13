<!-- element-guiコンポーネントです。以下の公式ドキュメントを参照してください。 -->
<!-- https://uxd.netlify.app/#/component/date-picker -->
<template>
  <div>
    <el-date-picker
      :style="{width}"
      :required="required"
      :type="type"
      :class="[required ? 'nengetsu-required' : 'nengetsu']"
      :clearable="clearable"
      v-on="$listeners"
      v-bind="$attrs"
    ></el-date-picker>
  </div>
</template>
<script>
export default {
  name: 'nengetsu',
  props: {
    width: {
      type: String,
      default: '180px'
    },
    required: {
      type: Boolean,
      default: false,
    },
    clearable: {
      type: Boolean,
      default: true,
    },
    type: {
      type: String,
      default: 'date'
    },
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

  .el-range-editor{
    background-color: transparent;
    border: none;
    display: inline-block !important;
  }
  .el-range-editor.el-input__inner{
    padding: 0;
  }
  .el-range-editor >>> .el-range__icon{
    position: absolute;
    top: 0;
    height: 100%;
    color: #C0C4CC;
    text-align: center;
    margin-left: 0;
  }
  .el-range-editor >>> .el-range-input{
    border: 1px solid #DCDFE6;
    height: 30px;
    line-height: 30px;
    /* padding-left: 30px; */
    width: 46%;
  }
  .el-range-editor >>> .el-range-input:hover{
    border-color: #C0C4CC;
  }
  .el-range-editor >>> .el-range-input:last-of-type{
    padding-left: 0;
    width: 46%;
  }
  .el-date-editor >>> .el-range-separator{
    padding-left: 0;
    padding-right: 0;
    line-height: 30px;
  }

</style>