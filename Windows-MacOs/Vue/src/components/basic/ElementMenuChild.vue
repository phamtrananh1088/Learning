<template>
  <div>
    <template v-for="(item, index) of list">
      <el-menu-item :key="index"
                    :index="''+item.SubMenuId">
        <span slot="title"> {{ item.SubMenuName }}</span>
      </el-menu-item>
    </template>
  </div>
</template>

<script>
export default {
  name: 'element-menu-child',
  props: {
    list: Array
  },
}
</script>