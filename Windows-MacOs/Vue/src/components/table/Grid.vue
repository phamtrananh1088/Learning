// https://happy-coding-clans.github.io/vue-easytable/#/en/doc/intro
<template>
<div>
  <ve-table
    ref="tableRef"
    :style="tablestyle"
    :scroll-width="scrollWidth"
    :max-height="maxHeight"
    :columns="columns"
    :table-data="tableData"
    border-y
    border-x
    border-around
    :fixed-header="isFixedHeader"
    :cell-style-option="cellStyleOptionBase"
    :row-style-option="rowStyleOptionBase"
    :row-key-field-name="rowKeyFieldName"
    :editOption="editOptionBase"
    :cell-selection-option="cellSelectionOption"
    :checkbox-option="checkboxOption"
    :radio-option="radioOption"
    :columnHiddenOption="columnHiddenOption"
    :event-custom-option="eventOptionBase"
    :virtual-scroll-option="virtualScrollOption"
    :class="tableClass"
    :footer-data="footerData"
    :sort-option="sortOption"
    />
        <!-- :class="height ? 'setHeight' : ''" -->
  <ve-pagination v-if="IsPagging" :total="tableData.length" :pageSizeOption="pageSizeOption" :layout='layout' />
 </div>
</template>
<script>
import 'dion-vue-table/libs/theme-default/index.css' // import style
import { VeTable, VePagination, VeIcon, VeLoading, VeLocale } from 'dion-vue-table' // import library
import ja from './ja.js'
export default {
  components: {
    VeTable,
    VePagination,
    VeIcon,
    VeLocale
  },
  props: {
    tableData: {
      type: Array,
      default: () => {
        return []
      }
    },
    footerData: {
      type: Array,
      default: () => {
        return []
      }
    },
    columns: {
      type: Array,
      default: () => {
        return []
      }
    },
    HiddenColumnKeys: {
      type: Array,
      default: () => {
        return []
      }
    },
    hideSelect: {
      type: Boolean,
      default: false
    },
    isFixedHeader: {
      type: Boolean,
      default: true
    },
    height: {
      default: ''
    },
    customStyle: {
      type: String,
      default: ''
    },
    maxHeight: {
      default: ''
    },
    // page size option
    pageSizeOption: {
      type: Array,
      default: () => {
        return [10, 20, 30]
      }
    },
    // use pageging option
    IsPagging: {
      type: Boolean,
      default: false
    },
    // set layout for pagging
    layout: {
      type: Array,
      default () {
        return ['total', 'prev', 'pager', 'next', 'sizer', 'jumper']
      }
    },
    cellStyleOption: {
      type: Object,
      default: function () {
        return null
      }
    },
    editOption: {
      type: Object,
      default: function () {
        return null
      }
    },
    rowStyleOption: {
      type: Object,
      default: function () {
        return null
      }
    },
    rowKeyFieldName: {
      type: String,
      default: 'rowKey'
    },
    cellSelectionOption: {
      // default true
      enable: false
    },
    rowSelectEvent: {
      type: Function,
      default: () => {}
    },
    cellClickEvent: {
      type: Function,
      default: () => {}
    },
    checkboxCheckEvent: {
      type: Function,
      default: () => {}
    },
    tableStyle: {
      type: String,
      default: ''
    },
    scrollWidth: {
      type: Number,
      default: 0
    },
    virtualScrollOption: {
      type: Object,
      default: () => {
        return {
          enable: false,
          minRowHeight: 30
        }
      }
    },
    disableSelectedRowKeys: {
      type: Array,
      default: () => {
        return []
      }
    },
    defaultSelectedRowKeys: {
      type: Array,
      default: () => {
        return []
      }
    },
    sortOption: {
      type: Object,
      default: () => {
        return {}
      }
    },
    resizeAble: {
      type: Boolean,
      default: false
    }
  },
  data () {
    return {
      submitData: [],
      columnHiddenOption: {
        // default hidden column keys
        defaultHiddenColumnKeys: this.HiddenColumnKeys
      },
      cellStyleOptionBase: {
        bodyCellClass: ({ row, column, rowIndex }) => {
          let cssClass = ''
          // more logic here
          if (column.vAlign === 'top') {
            cssClass += ' cell-vertical-align-top'
          } else if (column.vAlign === 'middle') {
            cssClass += ' cell-vertical-align-middle'
          } else if (column.vAlign === 'bottom') {
            cssClass += ' cell-vertical-align-bottom'
          }
          if (this.cellStyleOption && this.cellStyleOption.bodyCellClass) {
            const csClass = this.cellStyleOption.bodyCellClass({
              row,
              column,
              rowIndex
            })

            if (typeof csClass !== 'undefined') {
              cssClass += ' ' + csClass
            }
          }
          return cssClass
        },
        headerCellClass: ({column, rowIndex}) => {
          let cssClass = ''
          // more logic here
          if (this.cellStyleOption && this.cellStyleOption.headerCellClass) {
            const csClass = this.cellStyleOption.headerCellClass({
              column,
              rowIndex
            })
            if (typeof csClass !== 'undefined') {
              cssClass += ' ' + csClass
            }
          }
          cssClass += this.setRowSpan(column, cssClass) || ''
          return cssClass
        },
        footerCellClass: ({row, column, rowIndex}) => {
          let cssClass = ''
          // more logic here
          if (this.cellStyleOption && this.cellStyleOption.footerCellClass) {
            const csClass = this.cellStyleOption.footerCellClass({
              row,
              column,
              rowIndex
            })
            if (typeof csClass !== 'undefined') {
              cssClass += ' ' + csClass
            }
          }

          return cssClass
        }
      },
      editOptionBase: {
        // cell value change
        cellValueChange: ({ row, column }) => {
          if (this.editOption && this.editOption.cellValueChange) {
            this.editOption.cellValueChange({
              row,
              column
            })
          }
        }
      },
      eventOptionBase: {
        // body row event custom
        bodyRowEvents: ({ row, rowIndex }) => {
          return {
            click: (event) => { this.rowSelectEvent({row, rowIndex}) },
            dblclick: (event) => {},
            contextmenu: (event) => {},
            mouseenter: (event) => { this.checkBorder() },
            mouseleave: (event) => {}
          }
        },
        // body column event custom
        bodyCellEvents: ({ row, column, rowIndx }) => {
          return {
            click: (event) => {
              this.cellClickEvent(row, column)
            },
            dblclick: (event) => {},
            contextmenu: (event) => {},
            mouseenter: (event) => { this.checkBorder() },
            mouseleave: (event) => {}
          }
        },
        // header row event custom
        headerRowEvents: ({ rowIndx }) => {
          return {
            click: (event) => {},
            dblclick: (event) => {},
            contextmenu: (event) => {},
            mouseenter: (event) => { this.checkBorder() },
            mouseleave: (event) => {}
          }
        },
        // header column event custom
        headerCellEvents: ({ column, rowIndx }) => {
          return {
            click: (event) => {},
            dblclick: (event) => {},
            contextmenu: (event) => {},
            mouseenter: (event) => { this.checkBorder() },
            mouseleave: (event) => {}
          }
        },
        // footer row event custom
        footerRowEvents: ({ row, rowIndex }) => {
          return {
            click: (event) => {},
            dblclick: (event) => {},
            contextmenu: (event) => {},
            mouseenter: (event) => { this.checkBorder() },
            mouseleave: (event) => {}
          }
        },
        // footer column event custom
        footerCellEvents: ({ row, column, rowIndx }) => {
          return {
            click: (event) => {},
            dblclick: (event) => {},
            contextmenu: (event) => {},
            mouseenter: (event) => { this.checkBorder() },
            mouseleave: (event) => {}
          }
        }
      },
      isBorderBottom: true,
      selectedRowKeys: []
    }
  },
  created () {
    VeLoading({
      target: '#loading-1',
      name: 'grid',
      tip: 'loading...'
    })
    VeLocale.use(ja)
  },
  mounted () {
    this.checkBorder()
  },
  beforeDestroy () {
  },
  updated () {
    this.checkBorder()
    this.columnChange()
  },
  computed: {
    tablestyle () {
      let style = this.tableStyle
      if (this.height !== 0) {
        style += 'height: ' + this.height + 'px'
      }
      return style
    },
    rowStyleOptionBase () {
      let data = {
        clickHighlight: true
      }
      if (this.rowStyleOption) {
        data = Object.assign(data, this.rowStyleOption)
      }

      return data
    },
    tableClass () {
      let cls = ''
      if (this.isBorderBottom) {
        cls += ' border-bottom'
      }

      return cls
    },
    checkboxOption () {
      return {
        hideSelectAll: this.hideSelect,
        disableSelectedRowKeys: this.disableSelectedRowKeys,
        selectedRowKeys: this.selectedRowKeys,
        selectedRowChange: ({ row, isSelected, selectedRowKeys }) => {
          // eslint-disable-next-line vue/no-side-effects-in-computed-properties
          this.selectedRowKeys = selectedRowKeys
          this.$emit('selectedRowChange', { row, isSelected, selectedRowKeys })
        },
        selectedAllChange: ({ isSelected, selectedRowKeys }) => {
          // eslint-disable-next-line vue/no-side-effects-in-computed-properties
          this.selectedRowKeys = selectedRowKeys
          this.$emit('selectedRowChange', { row: null, isSelected, selectedRowKeys })
          if (this.checkboxCheckEvent) {
            this.checkboxCheckEvent(isSelected, selectedRowKeys)
          }
        }
      }
    },
    radioOption () {
      return {
        defaultSelectedRowKey: this.defaultSelectedRowKey,
        disableSelectedRowKeys: this.disableSelectedRowKeys,
        selectedRowChange: ({ row }) => {
          // eslint-disable-next-line vue/no-side-effects-in-computed-properties
          this.selectedRowKeys = [row[this.rowKeyFieldName]]
        }
      }
    },
    defaultSelectedRowKey () {
      if (this.defaultSelectedRowKeys && this.defaultSelectedRowKeys.length > 0) {
        // eslint-disable-next-line vue/no-side-effects-in-computed-properties
        this.selectedRowKeys = this.defaultSelectedRowKeys
        return this.defaultSelectedRowKeys[0]
      } else return ''
    }
  },
  watch: {
    tableData (newValue) {
      if (!newValue || newValue.length <= 0) {
        this.selectedRowKeys = []
        this.$emit('selectedRowChange', [])
      }
      setTimeout(() => {
        this.checkBorder()
      }, 200)
    },
    columns (newVal) {
      this.columnChange()
    }
  },
  methods: {
    columnChange () {
      if (!this.resizeAble) {
        return
      }
      // Query the table
      const table = this.$refs.tableRef.$el

      // Query all headers
      const cols = table.querySelectorAll('th')
      const me = this;
      // Loop over them
      [].forEach.call(cols, function (col, index) {
        if (col.querySelectorAll('.resizer') && col.querySelectorAll('.resizer').length > 0) {
          return
        }
        // Create a resizer element
        const resizer = document.createElement('div')
        resizer.classList.add('resizer')

        // Set the height
        const height = table.offsetHeight - 17
        resizer.style.height = `${height}px`
        // Add a resizer element to the column
        col.appendChild(resizer)

        // Will be implemented in the next section
        me.createResizableColumn(col, index, resizer, me)
      })
    },
    createResizableColumn (col, index, resizer, me) {
    // Track the current position of mouse
      let x = 0
      let w = 0

      const mouseDownHandler = function (e) {
        // Get the current mouse position
        x = e.clientX

        // Calculate the current width of column
        const styles = window.getComputedStyle(col)
        w = parseInt(styles.width, 10)

        // Attach listeners for document's events
        document.addEventListener('mousemove', mouseMoveHandler)
        document.addEventListener('mouseup', mouseUpHandler)
      }

      const mouseMoveHandler = function (e) {
        // Determine how far the mouse has been moved
        const dx = e.clientX - x
        const colGrp = me.$refs.tableRef.$el.querySelector('colgroup > col:nth-child(' + (index + 1) + ')')
        // Update the width of column
        if (colGrp) {
          colGrp.style.width = `${w + dx}px`
        }
      }

      // When user releases the mouse, remove the existing event listeners
      const mouseUpHandler = function () {
        document.removeEventListener('mousemove', mouseMoveHandler)
        document.removeEventListener('mouseup', mouseUpHandler)
      }

      resizer.addEventListener('mousedown', mouseDownHandler)
    },
    checkBorder () {
      if (!this.tableData || this.tableData.length <= 0) {
        this.isBorderBottom = false
        return
      }
      const elm = this.$refs.tableRef
      const isHasScrollY = elm.maxHeight ? elm.tableHeight >= elm.maxHeight : false
      if (isHasScrollY && !elm.hasXScrollBar) {
        this.isBorderBottom = false
      } else if (isHasScrollY && elm.hasXScrollBar) {
        this.isBorderBottom = true
      } else if (!isHasScrollY && elm.hasXScrollBar) {
        this.isBorderBottom = true
      } else if (!isHasScrollY && !elm.hasXScrollBar) {
        if ((elm.tableHeight + 5) < elm.maxHeight) {
          this.isBorderBottom = true
        } else {
          this.isBorderBottom = false
        }
      } else {
        this.isBorderBottom = false
      }
    },
    setRowSpan (column, cssClass) {
      if (column.rowspan || column.rowspan === 0) {
        if (column.rowspan === 0) {
          return ' display-none'
        } else {
          column._rowspan = column.rowspan
        }
      }
    },
    setHighlightRow (rowKey) {
      this.$refs['tableRef'].setHighlightRow({ rowKey: rowKey })
    },
    setCheckboxSelected (arrRowSelected) {
      this.selectedRowKeys = arrRowSelected
    },
    getCheckboxSelected () {
      return this.selectedRowKeys
    },
    cellDataChange (row, column, cellValue) {
      const { submitData } = this

      let currentCell = submitData.find(
        (x) => x.rowKey === row['rowKey'] && x.field === column.field
      )

      if (currentCell) {
        currentCell.value = cellValue
      } else {
        let newCell = {
          rowKey: row['rowKey'],
          field: column.field,
          value: cellValue
        }
        this.submitData.push(newCell)
      }
    }
  }
}
</script>

<style>
.ve-table-container::-webkit-scrollbar {
  background: rgb(238, 233, 233) !important;
  width: 10px !important;
}
.ve-table-container::-webkit-scrollbar-thumb {
  background: rgba(144,147,153,.3);
}

.setHeight .ve-table-container, .setHeight .ve-table-container table {
  height: 100% !important;
}
.ve-table .ve-table-container table.ve-table-content thead.ve-table-header tr.ve-table-header-tr th.ve-table-header-th {
  background-color: #305496 !important;
  color: #fff !important;
}
.ve-table .ve-table-container table.ve-table-content tbody.ve-table-body tr.ve-table-body-tr td.ve-table-body-td, .ve-table .ve-table-container table.ve-table-content tbody.ve-table-body tr.ve-table-expand-tr td.ve-table-body-td,
.ve-table .ve-table-container table.ve-table-content thead.ve-table-header tr.ve-table-header-tr th.ve-table-header-th {
  padding: 5px;
  overflow-wrap: anywhere;
}
/* .ve-table .ve-table-container table.ve-table-content tbody.ve-table-body tr.ve-table-body-tr, .ve-table .ve-table-container table.ve-table-content tbody.ve-table-body tr.ve-table-expand-tr {
    height: 30px;
} */
.ve-checkbox {
  padding-left: 4px;
}
.cell-vertical-align-top {
  vertical-align: top;
}
.cell-vertical-align-middle {
  vertical-align: middle;
}
.cell-vertical-align-bottom {
  vertical-align: bottom;
}
td.ve-table-body-td a {
  cursor: pointer;
}
.ellipsis-text {
  overflow: hidden;
  display: block;
  white-space: nowrap;
  text-overflow: ellipsis;
  -webkit-line-clamp: 1;
  -webkit-box-orient: vertical;
}

.ve-table .ve-table-container .ve-table-border-y th,
.ve-table .ve-table-container .ve-table-border-y td {
  border-right: 1px solid #000 !important;
}

.ve-table .ve-table-container .ve-table-border-y td:last-child {
  border-right: 1px solid transparent !important;
}

.ve-table .ve-table-container {
   border-left: 1px solid #000 !important;
   border-right: 1px solid #000 !important;
   border-bottom: 1px solid #000 !important;
   border-top: 1px solid #000 !important;
}

.ve-table .ve-table-container .ve-table-border-x th:not(.merge-header-top), .ve-table .ve-table-container .ve-table-border-x tr:not(:last-child) td:not([style*="height: 0px"]) {
   border-bottom: 1px solid #000 !important;
}

/* .ve-table .ve-table-container {
    border: 1px solid #000 !important;
} */

.ve-table .ve-table-container tfoot tr td{
  border-top: 1px solid #000 !important;
}
/* .ve-table-container table {
    border-left: 1px solid #000 !important;
} */
.border-bottom .ve-table-content{
  border-bottom: 1px solid #000 !important;
}
.merge-header-top {
  border-bottom: 0px !important;
  vertical-align: bottom;
}
.merge-header-bottom {
  vertical-align: top;
}

.ve-table-container table tr:last-child td{
  border-bottom: 0px !important;
}

.ve-table .ve-table-container table.ve-table-content tbody.ve-table-body .ve-table-body-tr .ve-table-body-td.ve-table-cell-selection {
  border-top: unset !important;
  border-left: unset !important;
}

/* .ve-table .ve-table-container table.ve-table-content thead.ve-table-header tr.ve-table-header-tr:last-child > th {
  border-top: 1px solid black;
} */
/* .ve-table.ve-table-border-around .ve-table-container table.ve-table-content.ve-table-border-x tr:last-child > th {
  border-top: 1px solid black;
} */
/* .ve-table .ve-table-container .ve-table-border-x th:not(.merge-header-top), .ve-table .ve-table-container .ve-table-border-x tr:not(:last-child) td {
  border-bottom: none !important;
} */
.ve-table .ve-table-container table.ve-table-content thead.ve-table-header tr:nth-child(0) th {
  z-index: 10 !important;
}
.ve-table .ve-table-container table.ve-table-content thead.ve-table-header tr:nth-child(1) th {
  z-index: 9 !important;
}
.ve-table .ve-table-container table.ve-table-content thead.ve-table-header tr:nth-child(2) th {
  z-index: 8 !important;
}
.ve-table .ve-table-container table.ve-table-content thead.ve-table-header tr:nth-child(3) th {
  z-index: 7 !important;
}
.ve-table .ve-table-container table.ve-table-content thead.ve-table-header tr:nth-child(4) th {
  z-index: 6 !important;
}

.display-none {
  display: none !important;
}

.ve-table .ve-table-body tr[style*="height: 0px"] td{
  border-right: none !important;
}
.disable-row {
  background-color: #BFBFBF !important;
}
.warning-row {
  background-color: #FF9999 !important;
}

.nolink a {
  color: #1C1C1C !important;
  pointer-events: none;
  text-decoration: none;
}
.align-left{
  text-align: left;
}
.enable-checkbox {
  pointer-events: auto;
}
.disable-checkbox {
  pointer-events: none;
}
.ve-table .ve-table-container {
  overflow-x: auto;
}
.ve-table .ve-table-container::-webkit-scrollbar {
   background-color: rgba(0, 0, 0, 0.1);
   width: 10px;
   height: 10px;
}
.ve-table .ve-table-container::-webkit-scrollbar-thumb {
   background-color: rgba(82, 81, 81, 0.5);
   width: 10px;
   border-radius: 5px;
}
.ve-table .ve-table-container::-webkit-scrollbar-thumb:hover {
   background-color: rgba(0, 0, 0, 0.5);
}

.ve-table th {
    position: relative;
}
.resizer {
    /* Displayed at the right side of column */
    position: absolute;
    top: 0;
    right: 0;
    width: 4px;
    cursor: col-resize;
    user-select: none;
}
</style>
