(function (global, factory) {
  if (typeof define === 'function' && define.amd) {
    define('VETable/lang/enUS', ['exports'], factory)
  } else if (typeof exports !== 'undefined') {
    factory(exports)
  } else {
    var mod = {
      exports: {}
    }
    factory(mod.exports)
    global.VETable = global.VETable || {}
    global.VETable.lang = global.VETable.lang || {}
    global.VETable.lang.enUS = mod.exports.default
  }
})(typeof globalThis !== 'undefined' ? globalThis : typeof self !== 'undefined' ? self : this, function (_exports) {
  'use strict'

  Object.defineProperty(_exports, '__esModule', {
    value: true
  })
  _exports.default = void 0
  var _default = {
    pagination: {
      goto: 'に行く',
      page: '',
      itemsPerPage: ' / ページ',
      total: function total (_total) {
        return '合計 ' + _total
      },
      prev5: '前の5ページ',
      next5: '次の5ページ'
    },
    table: {
      confirmFilter: '確認',
      resetFilter: 'リセット',
      insertRowAbove: '上に行を挿入',
      insertRowBelow: '下に行を挿入',
      removeRow: '行を削除',
      hideColumn: '列を非表示'
    }
  }
  _exports.default = _default
})
