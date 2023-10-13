<template>
  <div>
    <el-select
      :class="[required ? 'reafs-dropdown-required' : 'reafs-dropdown']"
      v-on="$listeners"
      v-bind="$attrs"
      ref=elSelect>
      <el-option>
      </el-option>
    </el-select>
  </div>
</template>
<script>
export default {
  name: 'reafsdropdown',
  props: {
    width: {
      type: String,
      default: '180px'
    },
    required: {
      type: Boolean,
      default: false
    },
    disabled: {
      type: Boolean,
      default: false
    },
    methods: {
      focus () {
        this.$refs.elSelect.focus()
      }
    }
  }
}
</script>

<style scoped>
  .reafs-dropdown >>> .el-input__inner {
    height: 30px;
    line-height: 30px;
    border-radius: 1px;
  }

  .reafs-dropdown-required >>> .el-input__inner {
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
</style>
