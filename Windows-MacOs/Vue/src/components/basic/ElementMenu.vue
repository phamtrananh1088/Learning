<template>
  <div class="el-menu">
    <el-menu
      close="el-menu--vertical"
      @select="select"
      :unique-opened="true"
      :collapse="isCollapse"
    >
      <template v-for="item in treeList">
        <el-submenu
          :key="item.MenuId"
          :index="'' + item.MenuId"
          v-if="item.children.length"
        >
          <template slot="title">
            <i class="el-icon-s-order"></i>
            <span slot="title"> {{ item.MenuName }}</span>
          </template>
          <element-menu-child :list="item.children"></element-menu-child>
        </el-submenu>
        <template v-else>
          <el-menu-item
            class="el-menu-item-lv1"
            :key="item.SubMenuId"
            :index="'' + item.SubMenuId"
          >
            <i class="el-icon-s-order"></i>
            <span slot="title"> {{ item.SubMenuName }}</span>
          </el-menu-item>
        </template>
      </template>
    </el-menu>
  </div>
</template>

<script>
import ElementMenuChild from "./ElementMenuChild";
export default {
  components: {
    "element-menu-child": ElementMenuChild,
  },
  props: {
    isCollapse: {
      type: Boolean,
      default: false,
    },
    onSelect: {
      type: Function,
      default: (x) => {
        // console.log(x);
      },
    },
    list: {
      type: Array,
      default: [],
    },
    MenuId: {
      type: String,
      default: "",
    },
  },
  data() {
    return {
      treeList: [],
    };
  },
  created() {
    this.treeList = this.convertTree(this.list);
  },
  methods: {
    //メニュー表示用TreeList作成
    convertTree(data) {
      var MenuId = !isNaN(this.MenuId) ? ~~this.MenuId : this.MenuId;
      var root_data = [];
      data.forEach((item) => {
        if (!item.children) item.children = [];
        //「共通」は表示せず、1階層目にTOPページやお知らせ一覧を表示
        if (item.MenuId === "A") {
          item.isRoot = true;
          root_data.push(item);
        } else if (item.MenuId === "I") { //「タブレット」の親階層も表示不要
          item.isRoot = true;
          root_data.push(item);
        } else {
          if (item.MenuId != MenuId) {
            item.isRoot = true;
            root_data.push(item);
            this.getTree(item.MenuId, item, data);
          }
        }
        MenuId = item.MenuId;
      });
      return root_data;
    },
    getTree(MenuId, node, data) {
      data.forEach((item) => {
        if (item.MenuId == MenuId) {
          if (!node.children) node.children = [];
          node.children.push(item);
        }
      });
    },
    //TOPメニューに画面タイトルとしてサブメニュー名を表示される
    select(index) {
      let _item = this.list.find((item) => {
        return item.SubMenuId == index;
      });

      this.$emit("menuTitle", _item.SubMenuName);
      //router.push、router.replace は、ナビゲーションの成功時と失敗時のコールバックを引数で指定することができ、
      //3.1以降は、コールバックが省略されると、Promiseを戻すようになっている。.catch(err => {})でエラーに対応
      //this.$router.push({ path: '/' + _item.Path }).catch(err => {});
      //window.location.href = "/" + _item.Path;
      const router = this.$router.resolve({ name: _item.SubMenuId }) || {}
      window.location.href = router.href
    }
  }
}
</script>
<style scoped>
.el-menu {
  box-sizing: content-box;
  width: 100%;
}
.el-menu-item {
  padding: 0px;
}
</style>

