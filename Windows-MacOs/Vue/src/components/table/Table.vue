<template>
  <div>
    <div
      class="reafs-table"
      :class="[textInline ? 'text-inline' : '', isChrome ? 'chrome' : '']"
    >
      <div class="mask" v-show="loading"></div>
      <div class="message" v-show="loading">データ検索中</div>
      <el-table
        :show-summary="summary"
        :summary-method="
          (columns, row) => {
            return this.summaryData;
          }
        "
        :row-key="rowKey"
        :key="randomTableKey"
        :load="loadTreeChildren"
        :height="realHeight"
        :max-height="realMaxHeight"
        :data="url ? rowData : tableDataMulti"
        :row-class-name="initIndex"
        :cell-style="getCellStyle"
        :cell-class-name="cellClassFunction"
        :multirow="multirow"
        style="width: 100%"
        lazy
        border
        ref="table"
        class="v-table"
        tooltip-effect="dark"
        @selection-change="selectionChange"
        @row-click="
          (row, column, event) => {
            this.rowclick(row, column, event);
          }
        "
        @row-dblclick="
          (row, column, event) => {
            this.rowDblclick(row, column, event);
          }
        "
        @header-click="headerClick"
        @cell-mouse-leave="
          (row, column, cell) => {
            !this.clickEdit && this.rowEndEdit(row, column, cell);
          }
        "
        @sort-change="sortChange"
        :span-method="arraySpanMethod"
        :header-cell-style="headerStyle"
      >
        <!-- 行番号列 -->
        <el-table-column
          v-if="columnIndex"
          type="index"
          label="No."
          :fixed="ck_index_fixed"
          width="45"
        ></el-table-column>
        <!-- チェックボックス列 -->
        <el-table-column
          v-if="columnChkbox"
          type="selection"
          :fixed="ck_index_fixed"
          width="45"
        ></el-table-column>
        <!-- row1 -->
        <el-table-column
          v-for="(column, cindex) in filterColumns"
          :key="cindex"
          :prop="column.field"
          :label="column.title"
          :min-width="column.width"
          :formatter="formatter"
          :fixed="column.fixed"
          :align="column.align"
          :sortable="column.sort ? '' : false"
          :sort-by="column.sortby ? column.sortby : ''"
          :type="column.type ? column.type : ''"
        >
          <!-- 3 -->
          <!-- column.children -->
          <!-- row2 -->
          <template v-if="hasSub(column.children, 'children')">
            <el-table-column
              style="border: none"
              v-for="columnChildren in filterChildrenColumn(column.children)"
              :key="columnChildren.field"
              :min-width="columnChildren.width"
              :class-name="columnChildren.class"
              :prop="columnChildren.field"
              :align="columnChildren.align"
              :label="columnChildren.title"
            >
              <!-- row3 -->
              <el-table-column
                style="border: none"
                v-for="columnChildren3 in filterChildrenColumn(
                  columnChildren.children
                )"
                :key="columnChildren3.field"
                :min-width="columnChildren3.width"
                :class-name="columnChildren3.class"
                :prop="columnChildren3.field"
                :align="columnChildren3.align"
                :label="columnChildren3.title"
                :sortable="columnChildren3.sort ? '' : false"
                :sort-by="column.sortby ? column.sortby : ''"
              >
                <template slot-scope="scope1">
                  <table-render
                    v-if="
                      columnChildren3.render &&
                      typeof columnChildren3.render == 'function'
                    "
                    :row="scope1.row"
                    :index="scope1.$index"
                    :column="columnChildren3"
                    :render="columnChildren3.render"
                  ></table-render>
                  <template v-else>
                    <a
                      v-if="column.link"
                      href="javascript:void(0)"
                      @click="
                        link(scope.row, columnChildren3, scope1.$index, $event)
                      "
                      v-text="scope1.row[columnChildren3.field]"
                    ></a>
                    <div
                      v-else-if="columnChildren3.formatter"
                      @click="
                        columnChildren3.click &&
                          columnChildren3.click(
                            scope1.row,
                            columnChildren3,
                            scope1.$index
                          )
                      "
                      v-html="
                        columnChildren3.formatter(
                          scope1.row,
                          columnChildren3,
                          scope1.$index
                        )
                      "
                    ></div>
                    <div v-else-if="column.bind">
                      {{ formatter(scope1.row, columnChildren3, true) }}
                    </div>
                    <span v-else-if="column.type == 'date'">
                      {{ formatterDate(scope1.row, columnChildren3) }}
                    </span>
                    <template v-else>
                      {{ scope1.row[columnChildren3.field] }}
                    </template>
                  </template>
                </template>
              </el-table-column>
            </el-table-column>
          </template>
          <!-- 2 -->
          <template v-else>
            <el-table-column
              style="border: none"
              v-for="columnChildren in filterChildrenColumn(column.children)"
              :key="columnChildren.field"
              :min-width="columnChildren.width"
              :class-name="columnChildren.class"
              :prop="columnChildren.field"
              :align="columnChildren.align"
              :label="columnChildren.title"
              :sortable="columnChildren.sort ? '' : false"
              :sort-by="columnChildren.sortby ? columnChildren.sortby : ''"
            >
              <template slot-scope="scope1">
                <table-render
                  v-if="
                    columnChildren.render &&
                    typeof columnChildren.render == 'function'
                  "
                  :row="scope1.row"
                  :index="scope1.$index"
                  :column="columnChildren"
                  :render="columnChildren.render"
                ></table-render>
                <template v-else>
                  <a
                    v-if="column.link"
                    href="javascript:void(0)"
                    @click="
                      link(scope1.row, columnChildren, scope1.$index, $event)
                    "
                    v-text="scope1.row[columnChildren.field]"
                  ></a>
                  <div
                    v-else-if="columnChildren.formatter"
                    @click="
                      columnChildren.click &&
                        columnChildren.click(
                          scope1.row,
                          columnChildren,
                          scope1.$index
                        )
                    "
                    v-html="
                      columnChildren.formatter(
                        scope1.row,
                        columnChildren,
                        scope1.$index
                      )
                    "
                  ></div>
                  <div v-else-if="column.bind">
                    {{ formatter(scope1.row, columnChildren, true) }}
                  </div>
                  <span v-else-if="column.type == 'date'">
                    {{ formatterDate(scope1.row, columnChildren) }}
                  </span>
                  <template v-else>
                    {{ scope1.row[columnChildren.field] }}
                  </template>
                </template>
              </template>
            </el-table-column>
          </template>

          <!--　明細部 作用域插槽 スコープ付きスロット-->
          <template slot-scope="scope">
            <!-- render関数でカストマイズColumnを設定 -->
            <table-render
              v-if="column.render && typeof column.render == 'function'"
              :row="scope.row"
              :index="scope.$index"
              :column="column"
              :render="column.render"
            ></table-render>
            <!-- render関数使用しない-->
            <div v-if="column.edit" class="edit-el">
              <div @click.stop class="e-item">
                <!-- 入力エリア -->
                <reafsinputtext
                  v-for="item in column.edit.reafsinputtext"
                  :key="item.ref"
                  v-model="scope.row[item.field]"
                  :maxlength="item.maxlength"
                  :width="item.width"
                  :required="item.required"
                  @on-change="
                    (event) => {
                      onChange(scope, scope.row[item.field], event, item);
                    }
                  "
                  @on-keypress="
                    ($event) => {
                      column.onKeyPress &&
                        column.onKeyPress($event, scope, column);
                    }
                  "
                >
                </reafsinputtext>
                <!-- 数値エリア -->
                <reafsinputnumber
                  v-for="item in column.edit.reafsinputnumber"
                  :key="item.ref"
                  v-model="scope.row[item.field]"
                  :width="item.width"
                  :required="item.required"
                  autocomplete="off"
                  @on-change="
                    (event) => {
                      onChange(scope, scope.row[item.field], event, item);
                    }
                  "
                  @on-keypress="
                    ($event) => {
                      column.onKeyPress &&
                        column.onKeyPress($event, scope, column);
                    }
                  "
                >
                </reafsinputnumber>
                <!-- 営業店 -->
                <eigyoten
                  v-for="item in column.edit.eigyoten"
                  :key="item.ref"
                  v-model="scope.row[item.field]"
                  :disabled="false"
                  :appendlabelText="item.eigyotenNm"
                  :append="'append'"
                >
                </eigyoten>
              </div>
            </div>
            <!-- 表示列 -->
            <template v-else>
              <!-- リンク -->
              <a
                v-if="column.link"
                href="javascript:void(0)"
                @click="link(scope.row, column, scope.$index, $event)"
                v-text="scope.row[column.field]"
              >
              </a>
              <!-- 日付 -->
              <span v-else-if="column.type == 'date'">
                {{ formatterDate(scope.row, column) }}
              </span>
              <!-- formatterカストマイズ -->
              <div
                v-else-if="column.formatter"
                @click="formatterClick(scope.row, column)"
                v-html="column.formatter(scope.row, column)"
              ></div>
              <!-- 文字列 -->
              <div
                v-else-if="column.bind && column.normal"
                @click="formatterClick(scope.row, column)"
                :style="column.getStyle && column.getStyle(scope.row, column)"
              >
                {{ formatter(scope.row, column, true) }}
              </div>
              <div
                v-else-if="column.click"
                @click="formatterClick(scope.row, column)"
              >
                {{ scope.row[column.field] }}
              </div>
              <!-- 色付き -->
              <span
                v-else-if="column.bind"
                :color="getColor(scope.row, column)"
              >
                {{ formatter(scope.row, column, true) }}
              </span>
              <!-- チェックボックス -->
              <span v-else-if="column.type == 'selection'">
                <el-checkbox v-model="scope.row[column.field] "></el-checkbox>
              </span>
              <template v-else>
                {{ formatter(scope.row, column, true) }}
              </template>
            </template>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <!-- ページング -->
    <div class="block pagination" v-if="!paginationHide">
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="paginations.page"
        :page-sizes="paginations.sizes"
        :page-size="paginations.size"
        layout="total, prev, pager, next, jumper"
        :total="paginations.total"
      ></el-pagination>
    </div>
  </div>
</template>
<script>
import TableRender from "./TableRender";
import reafsinputtext from "@/components/basic/ReafsControl/ReafsInputText.vue";
import reafsinputnumber from "@/components/basic/ReafsControl/ReafsInputNumber.vue";
import eigyoten from "@/components/basic/EigyoTen.vue";
var $vue;
export default {
  components: {
    "table-render": TableRender,
    reafsinputtext,
    reafsinputnumber,
    eigyoten,
  },
  props: {
    rowKey: {
      typeof: String,
      default: undefined,
    },
    loadTreeChildren: {
      type: Function,
      default: (tree, treeNode, resolve) => {
        return resolve([]);
      },
    },
    textInline: {
      type: Boolean,
      default: true,
    },
    tableData: {
      type: Array,
      default: () => {
        return [];
      },
    },
    columns: {
      type: Array,
      default: [],
    },
    height: {
      type: Number,
      default: 0,
    },
    maxHeight: {
      type: Number,
      default: 0,
    },
    linkView: {
      type: Function,
      default: function () {
        return 1;
      },
    },
    pagination: {
      type: Object,
      default: function () {
        return { total: 0, size: 0, sortName: "" };
      },
    },
    url: {
      type: String,
      default: "",
    },
    paginationHide: {
      type: Boolean,
      default: true,
    },
    color: {
      type: Boolean,
      default: true,
    },
    index: {
      type: Boolean,
      default: false,
    },
    allowEmpty: {
      type: Boolean,
      default: true,
    },
    defaultLoadPage: {
      type: Boolean,
      default: true,
    },
    loadKey: {
      type: Boolean,
      default: false,
    },
    single: {
      type: Boolean, //
      default: false,
    },
    doubleEdit: {
      type: Boolean, //
      default: true,
    },
    beginEdit: {
      type: Function,
      default: function (row, column, index) {
        return true;
      },
    },
    endEditBefore: {
      type: Function,
      default: function (row, column, index) {
        return true;
      },
    },
    endEditAfter: {
      type: Function,
      default: function (row, column, index) {
        return true;
      },
    },
    columnChkbox: {
      type: Boolean,
      default: true,
    },
    columnIndex: {
      type: Boolean,
      default: true,
    },
    multirow: {
      type: Number,
      default: 1,
    },
    //明細span変数
    spanColumns: {
      type: Array,
      default() {
        return [];
      },
    },
    //ヘッダspan変数
    spanHeaderColumns: {
      type: Array,
      default() {
        return [];
      },
    },
    rowClassFunction: {
      type: Function,
      default: function (row) {
        return "";
      },
    },
    cellClassFunction: {
      type: Function,
      default: function (row) {
        return "";
      },
    },

    rowclick: {
      type: Function,
      default: function (row, column, event) {
        return "";
      },
    },

    rowDblclick: {
      type: Function,
      default: function (row, column, event) {
        return "";
      },
    },
    cellStyleFunction: {
      type: Function,
      default: function (row, column, event) {
        return "";
      },
    },
  },
  data() {
    return {
      clickEdit: true, //
      randomTableKey: 1,
      key: "",
      realHeight: 0,
      realMaxHeight: 0,
      enableEdit: false, //
      empty: this.allowEmpty ? "" : "--",
      defaultImg: 'this.src="' + require("@/assets/imgs/error.png") + '"',
      loading: false,
      footer: {},
      total: 0,
      formatConfig: {},
      defaultColor: "default",
      colors: [
        "cyan",
        "red",
        "blue",
        "green",
        "magenta",
        "geekblue",
        "gold",
        "orange",
        "default",
      ],
      rule: {
        phone: /^[1][3,4,5,6,7,8,9][0-9]{9}$/,
        decimal: /(^[\-0-9][0-9]*(.[0-9]+)?)$/,
        number: /(^[\-0-9][0-9]*([0-9]+)?)$/,
      },
      rowData: [],
      paginations: {
        sort: "",
        order: "desc",
        Foots: "",
        total: 0,
        sizes: [30, 60, 100, 120],
        size: 30,
        Wheres: [],
        page: 1,
        rows: 30,
      },
      errorFiled: "",
      edit: { columnIndex: -1, rowIndex: -1 }, // 当前双击编辑的行与列坐标
      editStatus: {},
      summary: false, // 是否显示合计
      summaryData: [],
      summaryIndex: {},
      remoteColumns: [], // 需要每次刷新或分页后从后台加载字典数据源的列配置
      cellStyleColumns: {}, // 色付きの設定
      isChrome: false, //Chrome coreブラウザの場合はtable高さを再計算変数
      ck_index_fixed: false, //左側checkbox列と行番号列固定変数
      tableDataMulti: [],
    };
  },
  created() {
    //複雑行処理時、複数行を作成する
    if (this.multirow > 1) {
      this.tableData.forEach((item, _index) => {
        for (let i = 0; i < this.multirow; i++) {
          this.tableDataMulti.splice(_index * this.multirow, 0, item);
        }
      });
    } else {
      this.tableDataMulti = this.tableData;
    }

    //Chrome coreブラウザ
    if (
      navigator.userAgent.indexOf("Chrome") != -1 ||
      navigator.userAgent.indexOf("Edge") != -1
    ) {
      this.isChrome = true;
    }
    //合計行
    let hasSummary = JSON.parse(JSON.stringify(this.columns)).find((x) => {
      return x.summary;
    });
    if (
      JSON.parse(JSON.stringify(this.columns)).some((x) => {
        return x.fixed;
      })
    ) {
      if (hasSummary) {
        this.isChrome = false;
      }
      this.ck_index_fixed = true;
    }

    if (
      JSON.parse(JSON.stringify(this.columns)).some((x) => {
        return x.summary;
      })
    ) {
      this.realHeight = null;
      this.realMaxHeight = this.maxHeight || this.height;
    } else {
      this.realHeight = this.getHeight();
      this.realMaxHeight = this.getMaxHeight();
    }

    let keys = [];
    let columnBind = [];
    this.summaryData.push("合計");
    if (this.columnIndex) {
      this.summaryData.push(" ");
    }
    JSON.parse(JSON.stringify(this.columns)).forEach((x, _index) => {
      if (x.cellStyle) {
        this.cellStyleColumns[x.field] = x.cellStyle;
      }
      if (!x.hidden) {
        this.summaryData.push("");
        this.summaryIndex[x.field] = this.summaryData.length - 1;
      }
      if (x.summary && !this.summary) {
        this.summary = true;
      }
      if (x.bind && x.bind.key && (!x.bind.data || x.bind.data.length == 0)) {
        if (!x.bind.data) x.bind.data = [];
        if (x.bind.remote) {
          this.remoteColumns.push(x);
        } else if (this.loadKey) {
          keys.push(x.bind.key);
          x.bind.valueTyoe = x.type;
          columnBind.push(x.bind);
        }
      }
    });
    if (keys.length > 0) {
      this.http
        .post("/api/Sys_Dictionary/GetVueDictionary", keys)
        .then((dic) => {
          dic.forEach((x) => {
            columnBind.forEach((c) => {
              if (c.valueTyoe == "int" || c.valueTyoe == "sbyte") {
                x.data.forEach((d) => {
                  if (!isNaN(d.key)) {
                    d.key = ~~d.key;
                  }
                });
              }
              if (c.key == x.dicNo) c.data.push(...x.data);
            });
          });
        });
    }

    this.paginations.sort = this.pagination.sortName;
    Object.assign(this.paginations, this.pagination);
    if (this.pagination.size) {
      this.paginations.rows = this.pagination.size;
    }
    this.enableEdit = JSON.parse(JSON.stringify(this.columns)).some((x) => {
      return x.hasOwnProperty("edit");
    });
    let keyColumn = JSON.parse(JSON.stringify(this.columns)).find((x) => {
      return x.isKey;
    });
    if (keyColumn) {
      this.key = keyColumn.field;
    }
    $vue = this;
    this.defaultLoadPage && this.load();
  },
  computed: {
    filterColumns() {
      return this.columns.filter((x) => {
        //非表示列を除外する
        return !x.hidden;
      });
    },
  },

  methods: {
    hasSub(obj, subproperty) {
      let returnBool = typeof obj == "object" && subproperty in obj[0];
      return returnBool;
    },

    //
    headerStyle({ row, column, rowIndex, columnIndex }) {
      if (this.multirow > 1) {
        let cellitem = this.spanHeaderColumns.find(
          (item) =>
            item.rowIndex === rowIndex && item.columnIndex === columnIndex
        );

        if (cellitem) {
          this.$nextTick(() => {
            if (document.getElementsByClassName(column.id).length !== 0) {
              document
                .getElementsByClassName(column.id)[0]
                .setAttribute("rowSpan", cellitem.rowspan);
              document
                .getElementsByClassName(column.id)[0]
                .setAttribute("colSpan", cellitem.colspan);
              return false;
            }
          });

          if (cellitem.rowspan == 0 && cellitem.colspan == 0) {
            // this.$nextTick(() => {
            //   if (document.getElementsByClassName(column.id).length !== 0) {
            //     document
            //       .getElementsByClassName(column.id)[0]
            //       .setAttribute("rowSpan", cellitem.rowspan);
            //     document.getElementsByClassName(column.id)[0].remove();
            //   }
            // });
            return { visibility: "hidden" };
          }

          return column;
        }
      }
    },

    //明細行結合処理
    arraySpanMethod({ row, column, rowIndex, columnIndex }) {
      //指定されたセルの結合処理
      if (this.multirow > 1) {
        if (rowIndex < this.multirow) {
          let cellitem = this.spanColumns.find(
            (item) =>
              item.rowIndex === rowIndex && item.columnIndex === columnIndex
          );
          if (cellitem) {
            return [cellitem.rowspan, cellitem.colspan];
          }
        } else if (rowIndex >= this.multirow) {
          let cellitem = this.spanColumns.find(
            (item) =>
              item.rowIndex === rowIndex % this.multirow &&
              item.columnIndex === columnIndex
          );
          if (cellitem) {
            return [cellitem.rowspan, cellitem.colspan];
          }
        }
      }
    },
    //
    headerClick(column, event) {
      if (this.clickEdit && this.edit.rowIndex != -1) {
        if (
          this.rowEndEdit(
            this.url
              ? this.rowData[this.edit.rowIndex]
              : this.tableDataMulti[this.edit.rowIndex],
            column
          )
        ) {
          this.edit.rowIndex = -1;
        }
      }
    },
    // rowDbClick(row, column, event) {
    //   alert(123);
    //   console.log(row);
    //   // this.$emit("row-dbclick", { row, column, event });
    // },
    // rowClick(row, column, event) {
    //   if (!column) {
    //     return;
    //   }
    //   this.$emit("rowClick", { row, column, event });
    //   if (!this.doubleEdit) {
    //     return;
    //   }
    //   if (this.clickEdit && this.edit.rowIndex != -1) {
    //     if (row.elementIdex == this.edit.rowIndex) {
    //       let _field;
    //       if (event && event.property) {
    //         _field = event.property;
    //       } else {
    //         _field = column.property;
    //       }
    //       if (
    //         !JSON.parse(JSON.stringify(this.columns)).some(
    //           (x) => x.field == _field && x.edit
    //         )
    //       ) {
    //         if (this.rowEndEdit(row, event)) {
    //           this.edit.rowIndex = -1;
    //         }
    //       }
    //       return;
    //     }
    //     if (this.rowEndEdit(row, event && event.property ? event : column)) {
    //       this.edit.rowIndex = -1;
    //     }
    //   }
    //   this.rowBeginEdit(row, column);
    // },
    reset() {
      if (this.tableDataMulti && this.tableDataMulti.length > 0) {
        this.tableDataMulti.splice(0);
      }
      if (this.rowData && this.rowData.length > 0) {
        this.rowData.splice(0);
      }
      if (!this.paginationHide) {
        this.paginations.page = 1;
        if (this.paginations.wheres && this.paginations.wheres.length > 0) {
          this.paginations.wheres.splice(0);
        }
      }
      this.errorFiled = "";
      this.edit.columnIndex = -1;
      this.edit.rowIndex = -1;
    },
    getHeight() {
      if (!this.height && !this.maxHeight) {
        return null;
      }
      if (this.maxHeight) {
        return null;
      }
      return this.height;
    },
    getMaxHeight() {
      if (!this.height && !this.maxHeight) {
        return null;
      }
      if (this.maxHeight) {
        return this.maxHeight;
      }
      return null;
    },
    getSelectedOptions(column) {
      if (column.bind && column.bind.data && column.bind.data.length > 0) {
        return column.bind.data;
      }
      return [];
    },
    formatterClick(row, column, event) {
      column.click && column.click(row, column, event);
    },
    initIndex(obj) {
      if (this.index) {
        obj.row.elementIdex = obj.rowIndex;
      }

      let rowClass = this.rowClassFunction(obj);
      return rowClass;
    },
    toggleEdit(event) {},
    setEditStatus(status) {
      JSON.parse(JSON.stringify(this.columns)).forEach((x) => {
        if (x.hasOwnProperty("edit")) {
          this.$set(x.edit, "status", status);
        }
      });
    },
    beginWithButtonEdit(scope) {
      this.rowBeginEdit(
        scope.row,
        JSON.parse(JSON.stringify(this.columns))[scope.$index]
      );
    },
    endWithButtonEdit(scope) {
      if (this.edit.rowIndex == -1) return;

      if (
        !this.endEditBefore(
          scope.row,
          JSON.parse(JSON.stringify(this.columns))[scope.$index],
          scope.$index
        )
      ) {
        return;
      }

      for (
        let index = 0;
        index < JSON.parse(JSON.stringify(this.columns)).length;
        index++
      ) {
        let column = JSON.parse(JSON.stringify(this.columns))[index];
        if (!column.hidden && (column.required || column.require)) {
          if (!this.validateColum(column, scope.row)) return;
        }
      }
      this.edit.rowIndex = -1;
      if (
        !this.endEditAfter(
          scope.row,
          JSON.parse(JSON.stringify(this.columns))[scope.$index],
          scope.$index
        )
      ) {
      }
    },
    rowBeginEdit(row, column) {
      if (this.edit.rowIndex != -1) {
        return;
      }
      if (!this.enableEdit) return;
      if (!this.beginEdit(row, column, row.elementIdex)) return;
      if (row.hasOwnProperty("elementIdex")) {
        if (this.edit.rowIndex == row.elementIdex) {
          return;
        }
        this.edit.rowIndex = row.elementIdex;
      }
    },
    validateColum(option, data) {
      if (option.hidden || option.bind) return true;
      let val = data[option.field];
      if (option.require || option.required) {
        if (val != "0" && (val == "" || val == undefined)) {
          if (!this.errorFiled) {
            this.$Message.error(option.title + "は空白できません。");
          }
          return false;
        }
      }
      if (!option.edit) {
        return true;
      }
      let editType = option.edit.type;
      if (editType == "int" || editType == "decimal" || editType == "number") {
        if (val == "" || val == undefined) return true;
        if (editType == "decimal") {
          if (!this.rule.decimal.test(val)) {
            this.$Message.error(option.title + "は数値のみです。");
            return false;
          }
        } else if (!this.rule.decimal.test(val)) {
          this.$Message.error(option.title + "は整数のみです。");
          return false;
        }
        if (
          option.edit.min != undefined &&
          typeof option.edit.min === "number" &&
          val < option.edit.min
        ) {
          this.$Message.error(option.title + "不能小于" + option.edit.min);
          return false;
        }
        if (
          option.edit.max != undefined &&
          typeof option.edit.max === "number" &&
          val > option.edit.max
        ) {
          this.$Message.error(option.title + "不能大于" + option.edit.max);
          return false;
        }
        return true;
      }

      if (val && (editType == "text" || editType == "string")) {
        if (
          option.edit.min != undefined &&
          typeof option.edit.min === "number" &&
          val.length < option.edit.min
        ) {
          this.$Message.error(
            option.title + "至少" + option.edit.min + "个字符"
          );
          return false;
        }
        if (
          option.edit.max != undefined &&
          typeof option.edit.max === "number" &&
          val.length > option.edit.max
        ) {
          this.$Message.error(
            option.title + "最多" + option.edit.max + "个字符"
          );
          return false;
        }
        return true;
      }
      return true;
    },
    rowEndEdit(row, column, event) {
      if (this.clickEdit && event) {
        return true;
      }
      if (!this.enableEdit) {
        if (!this.errorFiled) {
          if (
            this.edit.rowIndex != -1 &&
            !this.endEditAfter(row, column, this.edit.rowIndex)
          ) {
            return false;
          }
          this.edit.rowIndex = -1;
        }
        return true;
      }
      if (!this.doubleEdit && event) {
        return true;
      }
      if (!this.endEditBefore(row, column, this.edit.rowIndex)) return false;

      if (
        this.edit.rowIndex != -1 &&
        (!this.errorFiled || this.errorFiled == column.property)
      ) {
        let data = (this.url ? this.rowData : this.tableDataMulti)[
          this.edit.rowIndex
        ];
        let option = JSON.parse(JSON.stringify(this.columns)).find((x) => {
          return x.field == column.property;
        });
        if (!option || !option.edit) {
          return true;
        }
        if (
          option.edit.type == "datetime" ||
          option.edit.type == "date" ||
          option.edit.type == "select"
        ) {
          if (this.edit.rowIndex == row.elementIdex) {
            return true;
          }
        }
        if (!this.validateColum(option, data)) {
          this.errorFiled = option.field;
          return false;
        } else {
          this.errorFiled = "";
        }
      }
      if (this.errorFiled) {
        return false;
      }
      if (!this.endEditAfter(row, column, this.edit.rowIndex)) return false;
      this.edit.rowIndex = -1;
      return true;
    },
    delRow() {
      let rows = this.getSelected();
      if (rows.length == 0) return this.$Message.error("请选择要删除的行!");

      let data = this.url ? this.rowData : this.tableDataMulti;
      let indexArr = this.getSelectedIndex();
      if (indexArr.length == 0) {
        return this.$Message.error(
          "删除操作必须设置VolTable的属性index='true'"
        );
      }
      if (indexArr.length == 0) {
      } else {
        for (let i = data.length - 1; i >= 0; i--) {
          if (indexArr.indexOf(i) != -1) {
            data.splice(i, 1);
          }
        }
      }
      this.edit.rowIndex = -1;
      return rows;
    },
    addRow(row) {
      if (!row) {
        row = {};
      }
      JSON.parse(JSON.stringify(this.columns)).forEach((x) => {
        if (!row.hasOwnProperty(x.field)) {
          if (x.edit && x.edit.type == "switch") {
            row[x.field] = x.type == "bool" ? false : 0;
          } else if (!row.hidden) {
            row[x.field] = undefined;
          }
        }
      });
      if (!this.url) {
        this.tableDataMulti.push(row);
        return;
      }
      this.rowData.push(row);
    },
    viewImg(row, column, url) {
      this.base.previewImg(url);
    },
    link(row, column, rowIndex, $e) {
      $e.stopPropagation();
      this.$props.linkView(row, column, rowIndex);
    },
    getSelected() {
      return this.$refs.table.selection;
    },
    getSelectedIndex() {
      if (!this.index) {
        return [];
      }
      let indexArr = this.$refs.table.selection.map((x) => {
        return x.elementIdex;
      });
      return indexArr || [];
    },
    GetTableDictionary(rows) {
      if (this.remoteColumns.length == 0 || !rows || rows.length == 0) return;
      let remoteInfo = {};
      for (let index = 0; index < this.remoteColumns.length; index++) {
        const column = this.remoteColumns[index];
        let key = column.bind.key;
        let data = [];
        rows.forEach((row) => {
          if (data.indexOf(row[column.field]) == -1) {
            data.push(row[column.field]);
          }
        });
        if (data.length > 0) {
          remoteInfo[key] = data;
        }
      }
      if (remoteInfo.length == 0) return;
      this.http
        .post("/api/Sys_Dictionary/GetTableDictionary", remoteInfo)
        .then((dic) => {
          dic.forEach((x) => {
            this.remoteColumns.forEach((column) => {
              if (column.bind.key == x.key) {
                column.bind.data = Object.assign([], column.bind.data, x.data);
              }
            });
          });
        });
    },
    //テーブル初期化
    load(query, isResetPage) {
      if (!this.url) return;
      if (isResetPage) {
        this.resetPage();
      }
      let param = {
        page: this.paginations.page,
        rows: this.paginations.rows,
        sort: this.paginations.sort,
        order: this.paginations.order,
        wheres: [], // 查询条件，格式为[{ name: "字段", value: "xx" }]
      };
      let status = true;
      if (query) {
        param = Object.assign(param, query);
      }
      this.$emit("loadBefore", param, (result) => {
        status = result;
      });
      if (!status) return;

      if (param.wheres && param.wheres instanceof Array) {
        param.wheres = JSON.stringify(param.wheres);
      }
      this.loading = true;
      this.http.post(this.url, param).then(
        (data) => {
          if (this.rowKey) {
            this.randomTableKey++;
            this.rowData.splice(0);
          }
          this.loading = false;
          this.$emit(
            "loadAfter",
            data.rows || [],
            (result) => {
              status = result;
            },
            data
          );
          if (!status) return;
          this.GetTableDictionary(data.rows);
          this.rowData = data.rows || [];
          this.paginations.total = data.total;
          // 合計
          this.getSummaries(data);
        },
        (error) => {
          this.loading = false;
        }
      );
    },
    getSummaries(data) {
      if (!this.summary || !data.summary) return;
      this.summaryData.splice(0);
      if (this.columnIndex) {
        this.summaryData.push("");
      }
      if (this.ck) {
        this.summaryData.push("");
      }

      JSON.parse(JSON.stringify(this.columns)).forEach((col) => {
        if (!col.hidden) {
          if (data.summary.hasOwnProperty(col.field)) {
            this.summaryData.push(data.summary[col.field]);
          } else {
            this.summaryData.push("");
          }
        }
      });
      if (this.summaryData.length > 0 && this.summaryData[0] == "") {
        this.summaryData[0] = "合計";
      }
    },
    getInputChangeSummaries() {},
    handleSizeChange(val) {
      this.$emit("backData", val);
      this.paginations.rows = val;
      this.load();
    },
    handleCurrentChange(val) {
      this.$emit("backData", val);
      this.paginations.page = val;
      this.load();
    },
    sortChange(sort) {
      this.paginations.sort = sort.prop;
      this.paginations.order = sort.order == "ascending" ? "asc" : "desc";
      this.load();
    },
    resetPage() {
      this.paginations.page = 1;
    },
    selectionChange(selection) {
      if (this.single && selection.length == 1) {
        this.$emit("rowChange", selection[0]); //
      }
      if (this.single && selection.length > 1) {
        this.$refs.table.toggleRowSelection(selection[0]);
      }
    },
    getColor(row, column) {
      let val = row[column.field];

      if (column.getColor && typeof column.getColor === "function") {
        let _color = column.getColor(row, column);
        if (_color) {
          return _color;
        }
      }
      if (!val && val != "0") {
        return this.defaultColor;
      }
      if (!this.formatConfig[column.field]) {
        this.formatConfig[column.field] = [val];
        return this.colors[0];
      }
      let index = this.formatConfig[column.field].indexOf(val);
      if (index != -1) {
        return this.colors[index];
      }
      if (this.formatConfig[column.field].length > 15) {
        return this.defaultColor;
      }

      if (index == -1) {
        this.formatConfig[column.field].push(val);
        index = this.formatConfig[column.field].length - 1;
      }
      return this.colors[index];
    },
    formatterDate(row, column) {
      return (row[column.field] || "").substr(0, 10);
    },
    formatter(row, column, template) {
      if (!template) return row[column.property];
      let val = row[column.field];
      if (!column.bind || !column.bind.data) {
        return row[column.field];
      }
      if (!val && val != 0) return val;
      if (
        (column.bind.type == "selectList" || column.bind.type == "checkbox") &&
        typeof val === "string" &&
        val.indexOf(",") != -1
      ) {
        return this.getSelectFormatter(column, val);
      }
      let source = column.bind.data.filter((x) => {
        return x.key !== "" && x.key !== undefined && x.key + "" === val + "";
      });
      if (source && source.length > 0) val = source[0].value;
      return val;
    },
    getSelectFormatter(column, val) {
      let valArr = val.split(",");
      for (let index = 0; index < valArr.length; index++) {
        column.bind.data.forEach((x) => {
          if (
            x.key !== "" &&
            x.key !== undefined &&
            x.key + "" == valArr[index] + ""
          ) {
            valArr[index] = x.value;
          }
        });
      }
      return valArr.join(",");
    },
    onChange(scope, val, event, column) {
      let row = scope.row;
      if (column.onChange && !column.onChange(row, val, event)) {
        return;
      }
      // 输入框求和实时计算
      this.getInputSummaries(scope, val, event, column);
    },
    // input输入实时求和
    getInputSummaries(scope, val, event, column) {
      // column列设置了summary属性的才计算值
      if (!column.summary) return;
      let sum = 0;
      let _index = 0;
      (this.url ? this.rowData : this.tableDataMulti).forEach((x, index) => {
        if (x.hasOwnProperty(column.field) && !isNaN(x[column.field])) {
          _index = index;
          sum += x[column.field] * 1;
        }
      });
      this.$set(this.summaryData, this.summaryIndex[column.field], sum);
    },
    getCellStyle(row) {
      if (this.multirow > 1) {
        // if (row.column.property) {
        //   return (
        //     this.cellStyleColumns[row.column.property] &&
        //     this.cellStyleColumns[row.column.property](
        //       row.row,
        //       row.rowIndex,
        //       row.columnIndex
        //     )
        //   );
        // }
        return this.cellStyleFunction(row);
      } else {
        return this.cellStyleFunction(row);
      }
    },
    filterChildrenColumn(children) {
      if (!children) {
        return [];
      } else if (!children.hasOwnProperty("length")) {
        return children;
      } else {
        return children.filter((x) => {
          return !x.hidden;
        });
      }
    },
  },
};
</script>
<style lang="less" scoped>
.reafs-table {
  position: relative;
  .mask {
    opacity: 0.2;
    position: absolute;
    width: 100%;
    height: 100%;
    background: #d0d0d0;
    z-index: 100;
  }

  .message {
    text-align: center;
    color: #635c5c;
    font-size: 15px;
    font-weight: 600;
    background: #eee;
    transform: translateY(-50%);
    top: 50%;
    position: absolute;
    z-index: 200;
    left: 0;
    right: 0;
    width: 150px;
    margin: 0 auto;
    line-height: 40px;
    border-radius: 4px;
    border: 1px solid #a09e9e;
  }
}

.e-item {
  display: block;
}
</style>

<style scoped>
.pagination {
  text-align: right;
  padding: 2px 28px;
  border: 1px solid #eee;
  border-top: 0px;
}

.v-table >>> .el-table__header th {
  padding: 0px !important;
  background-color: #f8f8f9 !important;
  font-size: 13px;
  height: 42px;
  color: #616161;
}

.v-table >>> .el-table__header th.is-sortable {
  padding: 3px !important;
}

/* .reafs-table.text-inline >>> .el-table__body .cell {
  word-break: inherit !important;
  white-space: nowrap !important;
} */

.v-table >>> .el-table__body td {
  padding: 5px 0 !important;
}

.v-table >>> .el-table__footer td {
  padding: 7px 0 !important;
}

.reafs-table >>> .el-table-column--selection .cell {
  display: inline;
}

.reafs-table.text-inline >>> .el-table th > .cell {
  white-space: nowrap !important;
}

.reafs-table >>> .el-table__body-wrapper::-webkit-scrollbar {
  /* width: 10px; */
  height: 10px;
}

.reafs-table >>> .el-table__body-wrapper::-webkit-scrollbar-thumb {
  border-radius: 5px;
  background: #ddd;
}

.reafs-table .table-img {
  height: 40px;
  border-radius: 5px;
  margin-right: 10px;
  width: 40px;
  object-fit: cover;
}

.reafs-table .table-img:hover {
  cursor: pointer;
}

.v-table >>> .ivu-tag-default {
  border: none !important;
  background: none !important;
}

.v-table >>> .el-table__fixed:before {
  border-color: none !important;
}

.chrome >>> .el-table__fixed {
  height: calc(100% - 11px) !important;
  background: white;
}

.chrome >>> .el-table__body-wrapper::-webkit-scrollbar {
  /* width: 11px; */
  height: 11px;
}

.chrome >>> .el-table__body-wrapper::-webkit-scrollbar-thumb {
  border-radius: 5px;
  background: #ddd;
}

.chrome >>> .el-table__fixed:before {
  background-color: unset;
}

.chrome >>> .el-table__fixed-footer-wrapper {
  bottom: -11.5px;
}
</style>
<style>

.el-pagination__jump{
  display: inline !important;
}

.tableRowClassName {
  background-color: rgb(248, 230, 230) !important;
  min-height: 32px;
}
.tableRowClassName:hover td {
  background-color: rgb(248, 230, 230) !important;
}
.noPadding .cell{
  padding-left: 0px !important;
  padding-right: 0px !important;
}
.textRed{
  color: red;
}
.textRed :hover {
  color: red;
}
.textRed :link {
  color: red;
}
.textRed :visited {
  color: red;
}
.textBlue :hover {
  color: blue;
}
</style>