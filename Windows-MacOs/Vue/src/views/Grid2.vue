<template>
    <reafstable
          ref="table"
          :tableData="tableData"
          :columns="columns"
          :height= 200
          :cellStyleOption="cellStyleOption"
          :rowStyleOption="rowStyleOption"
          :rowKeyFieldName="rowKeyFieldName"
          :editOption="editOption"
          ></reafstable>
</template>
<script>

import reafstable from '@/components/table/Grid.vue'
export default {
  components: {
    reafstable
  },
  data () {
    return {
      rowKeyFieldName: 'rowKey',
      tableData: [
        {
          'rowKey': 1,
          '物件名': '荒川',
          '契約名称': '渋谷店　ＳＧＭ　建物管理業務',
          '契約No': '000020',
          '作業名': '給水設備',
          '作業月': '20/06',
          '回目': '1',
          '作業予定日_自_date': '20/06/08',
          '作業予定日_至_date': '20/06/08',
          '予定申請': '済',
          '作業予定_備考': '予定内容記入',
          '作業完了日_自_date': '20/06/08',
          '作業完了日_至_date': '20/06/08',
          '完了報告': '済',
          '添付修正': '',
          '契約管理番号': '10000001-001',
          'ステータス': '完了承認',
          '作業予定日_自_time': '13:00',
          '作業予定日_至_time': '16:00',
          '承認日': '99/99/99'
        },
        {
          'rowKey': 2,
          '物件名': '荒川',
          '契約名称': '渋谷店　ＳＧＭ　建物管理業務',
          '契約No': '000020',
          '作業名': '給水設備',
          '作業月': '20/06',
          '回目': '1',
          '作業予定日_自_date': '20/06/08',
          '作業予定日_至_date': '20/06/08',
          '予定申請': '済',
          '作業予定_備考': '予定内容記入',
          '作業完了日_自_date': '20/06/08',
          '作業完了日_至_date': '20/06/08',
          '完了報告': '済',
          '添付修正': '',
          '契約管理番号': '10000001-001',
          'ステータス': '完了承認',
          '作業予定日_自_time': '13:00',
          '作業予定日_至_time': '16:00',
          '承認日': '99/99/99'
        },
        {
          'rowKey': 3,
          '物件名': '荒川',
          '契約名称': '渋谷店　ＳＧＭ　建物管理業務',
          '契約No': '000020',
          '作業名': '給水設備',
          '作業月': '20/06',
          '回目': '1',
          '作業予定日_自_date': '20/06/08',
          '作業予定日_至_date': '20/06/08',
          '予定申請': '済',
          '作業予定_備考': '予定内容記入',
          '作業完了日_自_date': '20/06/08',
          '作業完了日_至_date': '20/06/08',
          '完了報告': '済',
          '添付修正': '',
          '契約管理番号': '10000001-001',
          'ステータス': '完了承認',
          '作業予定日_自_time': '13:00',
          '作業予定日_至_time': '16:00',
          '承認日': '99/99/99'
        },
        {
          'rowKey': 4,
          '物件名': '荒川',
          '契約名称': '渋谷店　ＳＧＭ　建物管理業務',
          '契約No': '000020',
          '作業名': '給水設備',
          '作業月': '20/06',
          '回目': '1',
          '作業予定日_自_date': '20/06/08',
          '作業予定日_至_date': '20/06/08',
          '予定申請': '済',
          '作業予定_備考': '予定内容記入',
          '作業完了日_自_date': '20/06/08',
          '作業完了日_至_date': '20/06/08',
          '完了報告': '済',
          '添付修正': '有',
          '契約管理番号': '10000001-001',
          'ステータス': '完了承認',
          '作業予定日_自_time': '13:00',
          '作業予定日_至_time': '16:00',
          '承認日': '99/99/99'
        },
        {
          'rowKey': 5,
          '物件名': 'NNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNN',
          '契約名称': 'NNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNN',
          '契約No': '999999',
          '作業名': 'ＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮ',
          '作業月': '99/99',
          '回目': '9',
          '作業予定日_自_date': '20/06/08',
          '作業予定日_至_date': '20/06/08',
          '予定申請': '済',
          '作業予定_備考': 'ＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮ',
          '作業完了日_自_date': '20/06/08',
          '作業完了日_至_date': '20/06/08',
          '完了報告': '済',
          '添付修正': '有',
          '契約管理番号': '99999999-999',
          'ステータス': '完了承認',
          '作業予定日_自_time': '13:00',
          '作業予定日_至_time': '16:00',
          '承認日': '99/99/99'
        },
        {
          'rowKey': 6,
          '物件名': 'NNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNN',
          '契約名称': 'NNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNN',
          '契約No': '999999',
          '作業名': 'ＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮ',
          '作業月': '99/99',
          '回目': '9',
          '作業予定日_自_date': '20/06/08',
          '作業予定日_至_date': '20/06/08',
          '予定申請': '済',
          '作業予定_備考': 'ＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮ',
          '作業完了日_自_date': '20/06/08',
          '作業完了日_至_date': '20/06/08',
          '完了報告': '済',
          '添付修正': '有',
          '契約管理番号': '99999999-999',
          'ステータス': '完了承認',
          '作業予定日_自_time': '13:00',
          '作業予定日_至_time': '16:00',
          '承認日': '99/99/99'
        },
        {
          'rowKey': 7,
          '物件名': 'NNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNN',
          '契約名称': 'NNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNN',
          '契約No': '999999',
          '作業名': 'ＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮ',
          '作業月': '99/99',
          '回目': '9',
          '作業予定日_自_date': '20/06/08',
          '作業予定日_至_date': '20/06/08',
          '予定申請': '済',
          '作業予定_備考': 'ＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮ',
          '作業完了日_自_date': '20/06/08',
          '作業完了日_至_date': '20/06/08',
          '完了報告': '済',
          '添付修正': '有',
          '契約管理番号': '99999999-999',
          'ステータス': '完了承認',
          '作業予定日_自_time': '13:00',
          '作業予定日_至_time': '16:00',
          '承認日': '99/99/99'
        },
        {
          'rowKey': 8,
          '物件名': 'NNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNN',
          '契約名称': 'NNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNN',
          '契約No': '999999',
          '作業名': 'ＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮ',
          '作業月': '99/99',
          '回目': '9',
          '作業予定日_自_date': '20/06/08',
          '作業予定日_至_date': '20/06/08',
          '予定申請': '済',
          '作業予定_備考': 'ＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮ',
          '作業完了日_自_date': '20/06/08',
          '作業完了日_至_date': '20/06/08',
          '完了報告': '済',
          '添付修正': '有',
          '契約管理番号': '99999999-999',
          'ステータス': '完了承認',
          '作業予定日_自_time': '13:00',
          '作業予定日_至_time': '16:00',
          '承認日': '99/99/99'
        },
        {
          'rowKey': 9,
          '物件名': 'NNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNN',
          '契約名称': 'NNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNN',
          '契約No': '999999',
          '作業名': 'ＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮ',
          '作業月': '99/99',
          '回目': '9',
          '作業予定日_自_date': '20/06/08',
          '作業予定日_至_date': '20/06/08',
          '予定申請': '済',
          '作業予定_備考': 'ＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮ',
          '作業完了日_自_date': '20/06/08',
          '作業完了日_至_date': '20/06/08',
          '完了報告': '済',
          '添付修正': '有',
          '契約管理番号': '99999999-999',
          'ステータス': '完了承認',
          '作業予定日_自_time': '13:00',
          '作業予定日_至_time': '16:00',
          '承認日': '99/99/99'
        },
        {
          'rowKey': 10,
          '物件名': 'NNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNN',
          '契約名称': 'NNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNN',
          '契約No': '999999',
          '作業名': 'ＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮ',
          '作業月': '99/99',
          '回目': '9',
          '作業予定日_自_date': '20/06/08',
          '作業予定日_至_date': '20/06/08',
          '予定申請': '済',
          '作業予定_備考': 'ＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮ',
          '作業完了日_自_date': '20/06/08',
          '作業完了日_至_date': '20/06/08',
          '完了報告': '済',
          '添付修正': '有',
          '契約管理番号': '99999999-999',
          'ステータス': '完了承認',
          '作業予定日_自_time': '13:00',
          '作業予定日_至_time': '16:00',
          '承認日': '99/99/99'
        },
        {
          'rowKey': 11,
          '物件名': 'NNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNN',
          '契約名称': 'NNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNN',
          '契約No': '999999',
          '作業名': 'ＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮ',
          '作業月': '99/99',
          '回目': '9',
          '作業予定日_自_date': '20/06/08',
          '作業予定日_至_date': '20/06/08',
          '予定申請': '済',
          '作業予定_備考': 'ＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮ',
          '作業完了日_自_date': '20/06/08',
          '作業完了日_至_date': '20/06/08',
          '完了報告': '済',
          '添付修正': '有',
          '契約管理番号': '99999999-999',
          'ステータス': '完了承認',
          '作業予定日_自_time': '13:00',
          '作業予定日_至_time': '16:00',
          '承認日': '99/99/99'
        },
        {
          'rowKey': 12,
          '物件名': 'NNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNN',
          '契約名称': 'NNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNN',
          '契約No': '999999',
          '作業名': 'ＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮ',
          '作業月': '99/99',
          '回目': '9',
          '作業予定日_自_date': '20/06/08',
          '作業予定日_至_date': '20/06/08',
          '予定申請': '済',
          '作業予定_備考': 'ＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮ',
          '作業完了日_自_date': '20/06/08',
          '作業完了日_至_date': '20/06/08',
          '完了報告': '済',
          '添付修正': '有',
          '契約管理番号': '99999999-999',
          'ステータス': '完了承認',
          '作業予定日_自_time': '13:00',
          '作業予定日_至_time': '16:00',
          '承認日': '99/99/99'
        },
        {
          'rowKey': 13,
          '物件名': 'NNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNN',
          '契約名称': 'NNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNN',
          '契約No': '999999',
          '作業名': 'ＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮ',
          '作業月': '99/99',
          '回目': '9',
          '作業予定日_自_date': '20/06/08',
          '作業予定日_至_date': '20/06/08',
          '予定申請': '済',
          '作業予定_備考': 'ＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮ',
          '作業完了日_自_date': '20/06/08',
          '作業完了日_至_date': '20/06/08',
          '完了報告': '済',
          '添付修正': '有',
          '契約管理番号': '99999999-999',
          'ステータス': '完了承認',
          '作業予定日_自_time': '13:00',
          '作業予定日_至_time': '16:00',
          '承認日': '99/99/99'
        },
        {
          'rowKey': 14,
          '物件名': 'NNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNN',
          '契約名称': 'NNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNN',
          '契約No': '999999',
          '作業名': 'ＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮ',
          '作業月': '99/99',
          '回目': '9',
          '作業予定日_自_date': '20/06/08',
          '作業予定日_至_date': '20/06/08',
          '予定申請': '済',
          '作業予定_備考': 'ＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮ',
          '作業完了日_自_date': '20/06/08',
          '作業完了日_至_date': '20/06/08',
          '完了報告': '済',
          '添付修正': '有',
          '契約管理番号': '99999999-999',
          'ステータス': '完了承認',
          '作業予定日_自_time': '13:00',
          '作業予定日_至_time': '16:00',
          '承認日': '99/99/99'
        },
        {
          'rowKey': 15,
          '物件名': 'NNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNN',
          '契約名称': 'NNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNN',
          '契約No': '999999',
          '作業名': 'ＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮ',
          '作業月': '99/99',
          '回目': '9',
          '作業予定日_自_date': '20/06/08',
          '作業予定日_至_date': '20/06/08',
          '予定申請': '済',
          '作業予定_備考': 'ＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮＮ',
          '作業完了日_自_date': '20/06/08',
          '作業完了日_至_date': '20/06/08',
          '完了報告': '済',
          '添付修正': '有',
          '契約管理番号': '99999999-999',
          'ステータス': '完了承認',
          '作業予定日_自_time': '13:00',
          '作業予定日_至_time': '16:00',
          '承認日': '99/99/99'
        }
      ],
      columns: [
        { field: '物件名',
          key: '1',
          title: '物件名',
          align: 'left',
          vAlign: 'top',
          ellipsis: {
            showTitle: true,
            lineClamp: 2
          }
        },
        { field: '契約名称',
          key: '2',
          title: '契約名称',
          align: 'left',
          vAlign: 'top',
          renderBodyCell: ({ row }) => {
            return (
              <div style="display: grid">
                <a class="ellipsis-text" href="http://localhost:8888/syuzenIraiSansyoShonin">
                  {row.契約名称}
                </a></div>
            )
          },
          ellipsis: {
            showTitle: true,
            lineClamp: 2
          } },
        { key: '3',
          title: '契約№',
          align: 'left',
          children: [
            {
              key: '4',
              title: '契約管理番号',
              align: 'left',
              renderBodyCell: ({ row }) => {
                return (
                  <div style="display: grid">
                    <span>
                      {row.契約No}
                    </span><span >
                      {row.契約管理番号}
                    </span></div>
                )
              },
              width: 135,
              ellipsis: {
                showTitle: true,
                lineClamp: 2
              }
            }] },
        { field: '作業名',
          key: '5',
          title: '作業名',
          align: 'left',
          vAlign: 'top',
          width: 150,
          ellipsis: {
            showTitle: true,
            lineClamp: 2
          }
        },
        { key: '6',
          title: '作業月／回目',
          align: 'left',
          children: [
            {
              key: '7',
              title: 'ステータス',
              align: 'left',
              width: 125,
              renderBodyCell: ({ row }) => {
                return (
                  <div style="display: grid">
                    <div style="display: flex;justify-content: space-between;"><span >
                      {row.作業月}
                    </span>
                    <span >
                      {row.回目}
                    </span>
                    </div>
                    <span >
                      {row.ステータス}
                    </span></div>
                )
              }

            }] },
        { key: '9',
          title: '作業予定日',
          width: '10%',
          children: [
            {
              key: '10',
              title: '（自）',
              width: 90,
              renderBodyCell: ({ row }) => {
                return (
                  <div style="display: grid"><span style="text-align: left">
                    {row.作業予定日_自_date}
                  </span>
                  <span >
                    {row.作業予定日_自_time}
                  </span>
                  </div>
                )
              }

            },
            {
              key: '11',
              title: '（至)',
              width: 90,
              renderBodyCell: ({ row }) => {
                return (
                  <div style="display: grid"><span style="text-align: left">
                    {row.作業予定日_至_date}
                  </span>
                  <span >
                    {row.作業予定日_至_time}
                  </span>
                  </div>
                )
              }

            }] },
        { key: '12',
          title: '予定',
          children: [
            {
              key: '13',
              title: '申請',
              width: 40,
              renderBodyCell: ({ row }) => {
                return (
                  <div style="display: grid">
                    <a href="http://localhost:8888/syuzenIraiSansyoShonin">
                      {row.予定申請}
                    </a></div>
                )
              }
            }] },
        { field: '作業予定_備考',
          key: '14',
          title: '作業予定／備考',
          align: 'left',
          vAlign: 'top',
          ellipsis: {
            showTitle: true,
            lineClamp: 2
          },
          edit: true
        },
        { key: '15',
          title: '作業完了日',
          width: '10%',
          children: [
            {
              key: '16',
              title: '（自）',
              width: 90,
              renderBodyCell: ({ row }) => {
                return (
                  <div style="display: grid"><span style="text-align: left">
                    {row.作業完了日_自_date}
                  </span>
                  <span >
                   &nbsp;
                  </span>
                  </div>
                )
              }
            },
            {
              key: '17',
              title: '（至)',
              width: 90,
              renderBodyCell: ({ row }) => {
                return (
                  <div style="display: grid"><span style="text-align: left">
                    {row.作業完了日_至_date}
                  </span>
                  <span >
                   &nbsp;
                  </span>
                  </div>
                )
              }

            }] },
        { key: '18',
          title: '完了',
          children: [
            {
              key: '19',
              title: '報告',
              width: 40,
              renderBodyCell: ({ row }) => {
                return (
                  <div style="display: grid">
                    <a href="http://localhost:8888/syuzenIraiSansyoShonin">
                      {row.完了報告}
                    </a></div>
                )
              }
            }] },
        { key: '20',
          title: '添付',
          children: [
            {
              key: '21',
              title: '修正',
              width: 40,
              renderBodyCell: ({ row }) => {
                return (
                  <div style="display: grid">
                    <a href="http://localhost:8888/syuzenIraiSansyoShonin">
                      {row.添付修正}
                    </a></div>
                )
              }
            }] },
        { key: '22',
          title: '作業結果',
          align: 'left',
          children: [
            {
              key: '23',
              title: '承認日',
              align: 'left',
              width: 90,
              renderBodyCell: ({ row }) => {
                return (
                  <div style="display: grid">
                    <span >
                      &nbsp;
                    </span><span >
                      {row.承認日}
                    </span></div>
                )
              }

            }] }
      ],
      cellStyleOption: {
        bodyCellClass: ({ row, column, rowIndex }) => {
          if (rowIndex === 6) {
            return 'highlight-row'
          }
        }
      },
      // https://happy-coding-clans.github.io/vue-easytable/#/en/doc/table/cell-edit
      editOption: {
        // cell value change
        cellValueChange: ({ row, column }) => {
        }
      },
      rowStyleOption: {
        clickHighlight: true
      }
    }
  },
  created () {
    this.columns.splice(1, 0, {
      title: '入力エリア',
      width: 100,
      render: (h) => {
        return h(
          'div',
          { style: {} },
          [
            h(
              'reafsinputtext',
              {
                style: {},
                props: {}
              }
            ),
            h(
              'eigyoten',
              {
                style: {},
                props: {append: "'append'"}
              }
            )
          ]
        )
      }})
  },
  methods: {
    pageBack (obj) {
      this.pagination.page = obj
    }
  }
}
</script>
<style>
  .highlight-row {
    background-color: #FF9999 !important;
  }
</style>
