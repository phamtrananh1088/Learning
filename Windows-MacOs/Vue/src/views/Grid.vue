<template>
    <div class="home-contianer">
    <!-- １　単行表示　ページング機能使用　サンプル -->
    <!-- :single="true"　true:単行選択　false:複数行選択 デフォルト：false　表示-->
    <!-- :rowKey="rowKey"　TODO -->
    <!-- :tableData="tableData"　通常：テーブルデータを指定 -->
    <!--                         ページング機能を使用する場合：ページング計算されたデータを指定 -->
    <!--                                                    tableData.slice((pagination.page - 1) * pagination.size, pagination.page * pagination.size) -->
    <!-- :columns="columns"　テーブル列の設定 columns変数はscriptに参照してください-->
    <!-- :pagination="pagination"　ページング機能を使用する pagination変数はscriptに参照してください-->
    <!-- :pagination-hide="false"　右下ページングコントロールを使用する デフォルト：true(表示しない)-->
    <!-- :height　テーブルの高さ-->
    <!-- :max-height　テーブルの最大高さ-->
    <!-- :columnIndex = true　行番号列　デフォルト：true　表示-->
    <!-- :columnChkbox = true　チェックボックス列　デフォルト：true　表示-->
    <!-- @backData="pageBack"　ページング機能を使用する場合は必須：ページ目を指定-->
    <reafstable 
          ref="table"
          :single="single" 
          :row-Key="rowKey"
          :tableData="tableData.slice((pagination.page - 1) * pagination.size, pagination.page * pagination.size)"
          :columns="columns1"
          :pagination="pagination"
          :pagination-hide="false"
          :height= 400
          :max-height = 400
          :columnIndex = true
          :columnChkbox = true
          @backData="pageBack"
          >
    </reafstable>
    
    <br/>

    <!-- multirow　数値　複雑明細行数を指定-->
    <!-- spanColumns　結合を指定ルールを指定-->
    <reafstable 
          ref="table"
          :single="single" 
          :rowKey="rowKey"
          :tableData="tableData2.slice((pagination2.page - 1) * pagination2.size, pagination2.page * pagination2.size)"
          :columns="columns2"
          :pagination="pagination2"
          :pagination-hide="false"
          :height=400
          :columnIndex = false
          :columnChkbox = false
          @backData="pageBack"
          :multirow = 2
          :spanColumns = spanColumns2
          :spanHeaderColumns = spanHeaderColumns2
          >
    </reafstable>

    <br/>
    </div>
</template>
<script>

import reafstable from '@/components/table/Table.vue'
export default  {
  components: {
    reafstable,
  },
  data(){
    return{
        single: false, //SingleSelet
        rowKey: undefined, //Treetable PK
        pagination: { total: 22, size: 2, sortName: "", page:1 },
        pagination2: { total: 8, size: 2, sortName: "", page:1 },
        
        tableData: [{
            torihikisakicd: '0010-003',
            torihikisakinm: 'テスト',
            address: '2110041 神奈川県川崎市中原区0-00-00',
            amount: 10000,
            date: '2022/04/01 10:10:10'
        }, {
            torihikisakicd: '0010-001',
            torihikisakinm: 'テスト',
            address: '2110041 神奈川県川崎市中原区0-00-00',
            amount: 10000,
            date: '2022/04/01'
        }, {
            torihikisakicd: '0010-002',
            torihikisakinm: 'テスト',
            address: '2110041 神奈川県川崎市中原区0-00-00',
            amount: 10000,
            date: '2022/04/01'
        }, {
            torihikisakicd: '0010-003',
            torihikisakinm: 'テスト',
            address: '2110041 神奈川県川崎市中原区0-00-00',
            amount: 10000,
            date: '2022/04/01'
        }, {
            torihikisakicd: '0010-000',
            torihikisakinm: 'テスト',
            address: '2110041 神奈川県川崎市中原区0-00-00',
            amount: 10000,
            date: '2022/04/01'
        }, {
            torihikisakicd: '0010-000',
            torihikisakinm: 'テスト',
            address: '2110041 神奈川県川崎市中原区0-00-00',
            amount: 10000,
            date: '2022/04/01'
        }, {
            torihikisakicd: '0010-000',
            torihikisakinm: 'テスト',
            address: '2110041 神奈川県川崎市中原区0-00-00',
            amount: 10000,
            date: '2022/04/01'
        }, {
            torihikisakicd: '0010-000',
            torihikisakinm: 'テスト',
            address: '2110041 神奈川県川崎市中原区0-00-00',
            amount: 10000,
            date: '2022/04/01'
        },  {
            torihikisakicd: '0010-000',
            torihikisakinm: 'テスト',
            address: '2110041 神奈川県川崎市中原区0-00-00',
            amount: 10000,
            date: '2022/04/01'
        },  {
            torihikisakicd: '0010-000',
            torihikisakinm: 'テスト',
            address: '2110041 神奈川県川崎市中原区0-00-00',
            amount: 10000,
            date: '2022/04/01'
        },  {
            torihikisakicd: '0010-000',
            torihikisakinm: 'テスト',
            address: '2110041 神奈川県川崎市中原区0-00-00',
            amount: 10000,
            date: '2022/04/01'
        },  {
            torihikisakicd: '0010-000',
            torihikisakinm: 'テスト',
            address: '2110041 神奈川県川崎市中原区0-00-00',
            amount: 10000,
            date: '2022/04/01'
        },  {
            torihikisakicd: '0010-000',
            torihikisakinm: 'テスト',
            address: '2110041 神奈川県川崎市中原区0-00-00',
            amount: 10000,
            date: '2022/04/01'
        },  {
            torihikisakicd: '0010-000',
            torihikisakinm: 'テスト',
            address: '2110041 神奈川県川崎市中原区0-00-00',
            amount: 10000,
            date: '2022/04/01'
        },  {
            torihikisakicd: '0010-000',
            torihikisakinm: 'テスト',
            address: '2110041 神奈川県川崎市中原区0-00-00',
            amount: 10000,
            date: '2022/04/01'
        },  {
            torihikisakicd: '0010-000',
            torihikisakinm: 'テスト',
            address: '2110041 神奈川県川崎市中原区0-00-00',
            amount: 10000,
            date: '2022/04/01'
        },  {
            torihikisakicd: '0010-000',
            torihikisakinm: 'テスト',
            address: '2110041 神奈川県川崎市中原区0-00-00',
            amount: 10000,
            date: '2022/04/01'
        },  {
            torihikisakicd: '0010-000',
            torihikisakinm: 'テスト',
            address: '2110041 神奈川県川崎市中原区0-00-00',
            amount: 10000,
            date: '2022/04/01'
        },  {
            torihikisakicd: '0010-000',
            torihikisakinm: 'テスト',
            address: '2110041 神奈川県川崎市中原区0-00-00',
            amount: 10000,
            date: '2022/04/01'
        },  {
            torihikisakicd: '0010-000',
            torihikisakinm: 'テスト',
            address: '2110041 神奈川県川崎市中原区0-00-00',
            amount: 10000,
            date: '2022/04/01'
        },  {
            torihikisakicd: '0010-000',
            torihikisakinm: 'テスト',
            address: '2110041 神奈川県川崎市中原区0-00-00',
            amount: 10000,
            date: '2022/04/01'
        },  {
            torihikisakicd: '0010-000',
            torihikisakinm: 'テスト1',
            address: '2110041 神奈川県川崎市中原区0-00-00',
            amount: 10000,
            date: '2022/04/01'
        }, 
        ],
        
        tableData2: [{
            torihikisakicd: '0010-000',
            torihikisakinm: 'テスト',
            address: '2110041 神奈川県川崎市中原区0-00-00',
            amount: 10000,
            date: '2022/04/01 10:10:10',
            sagyoyotei_kaisi_time: '10:10:10'
        }, {
            torihikisakicd: '0010-001',
            torihikisakinm: 'テスト',
            address: '2110041 神奈川県川崎市中原区0-00-00',
            amount: 10000,
            date: '2022/04/01',
            sagyoyotei_kaisi_time: '10:10:10'
        }, {
            torihikisakicd: '0010-002',
            torihikisakinm: 'テスト',
            address: '2110041 神奈川県川崎市中原区0-00-00',
            amount: 10000,
            date: '2022/04/01',
            sagyoyotei_kaisi_time: '10:10:10'
        }, {
            torihikisakicd: '0010-003',
            torihikisakinm: 'テスト',
            address: '2110041 神奈川県川崎市中原区0-00-00',
            amount: 10000,
            date: '2022/04/01',
            sagyoyotei_kaisi_time: '10:10:10'
        }, 
        ],
        
        //１　単行表示　ページング機能使用　サンプル :columns="columns"　テーブル列の設定
        //field: Cellのデータフィールドを指定　「tableData」の項目名称（例：tableData.torihikisakicd）を使用してください
        //title：Cellのヘッダタイトルを指定
        //type：Cellの表示形を指定
        //                      string：文字列
        //                      date：日付
        //
        //formatter：Cellのデータ表示形を指定 
        //width：Cellの幅を指定　数値
        //align：Cellの整列を指定　left/center/right デフォルト：left
        //link：Cellをリンク形で指定　true/false デフォルト：true
        //fixed：列を固定　true/false/left/right
        columns1: [{field:'torihikisakicd',title: 'タイトル1',type:'string',link:true,width:90,readonly:true,align:'center',sort:true,fixed:true,},
                  {field:'checkbox',type:'selection',width:45,align:'center'},
                  {field:'torihikisakinm',title:'取引先名',type:'string',width:90,align:'left',sort:true },
                  {field:'torihikisakinm',title:'取引先名',type:'string',width:90,align:'left',sort:true ,click:true },
                  //セルカストマイズ方法１
                  {title:'カストマイズ1',width:120,align:'left',
                    render: (h, { row, column, index }) => {
                        return h("div",
                            { style: {} },
                            [
                                h(
                                    "reafsinputtext",
                                    {
                                        style: {},
                                        props: {append: "append", size: "small",width:'100px'},
                                    },
                                ),
                                h(
                                    "eigyoten",
                                    {
                                        style: {},
                                    },
                                ),
                                h(
                                    "p",row.torihikisakinm
                                ),
                                h(
                                    "p",row.torihikisakinm
                                ),
                            ]
                    );
                    }
                  },
                  //セルカストマイズ方法2
                  {title:'カストマイズ2',width:120,align:'left',
                    edit:{
                        reafsinputtext:[
                            {keep: true,required:true,maxlength:5,width:'100px',field:'torihikisakicd',ref:'reafsinputtext1'},
                            {keep: true,required:true,maxlength:5,width:'100px',field:'torihikisakicd',ref:'reafsinputtext2'},
                        ],
                        eigyoten:[
                            {keep: true,required:true,maxlength:5,width:'100px',field:'torihikisakinm',ref:'eigyoten'},
                        ],
                    },
                  },
                  {field:'address',title:'住所',type:'string',width:140,align:'left',hidden:true},
                  {field:'amount',title:'金額',type:'decimal',width:100,align:'right',formatter:(row, column) => { return this.base.formatCommaNumeric(row.amount.toString());}},
                  {field:'date',title:'日付',type:'date',width:100,align:'center',formatter:(row) => { return  (row.date || '').split(' ')[0].replace(/-/g, '.')}},
                ],

        //複数行ヘッダ、複数行明細
        columns2: [{field:'torihikisakicd',title:'取引先コード',type:'string',width:90,readonly:true,align:'center',sort:true,},
        
        { 
          title: '開始時間',
          width: 100,
          render: (h, { row, column, index }) => {
            var self = this
            return h("div", { style: {} }, [
              h("el-time-select", {
                style: {},
                props: { append: "append", size: "small", width: "100%" 
                        ,value :row.sagyoyotei_kaisi_time
                        },
                on: {
                        input: function (val) {
                            row.sagyoyotei_kaisi_time = val
                        }
                    }
              })
            ]);
          }
        },
                   {field:'torihikisakinm',title:'取引先名',type:'string',width:90,align:'left',sort:true,},
                   {title:'タイトル1',width:120,align:'left',
                    children:[{title:'タイトル2',
                        render: (h, { row, column, index }) => {
                            if (index % 2) {
                                return h("div",
                                    { style: {} },
                                    [
                                        h(
                                            "eigyoten",
                                            {
                                                style: {},
                                            },
                                        ),
                                    ]
                                );
                            }
                            return h("div",
                                { style: {} },
                                [
                                    h(
                                        "reafsinputtext",
                                        {
                                            style: {},
                                            props: {append: "append", size: "small",width:'100px',
                                            },
                                        },
                                    ),
                                ]
                            );
                        },
                    },
                    ]
                  },
                  {field:'date',title:'日付',type:'date',width:100,align:'center',formatter:(row) => { return  (row.date || '').split(' ')[0].replace(/-/g, '.')},
                     children:[{title:'タイトル3'}]
                  },
                ],
        spanColumns2 : [
            //1行目の結合を指定
            {rowIndex:0,columnIndex:0,rowspan:2,colspan:1},
            {rowIndex:0,columnIndex:1,rowspan:2,colspan:1},
            {rowIndex:1,columnIndex:0,rowspan:0,colspan:0},
            {rowIndex:1,columnIndex:1,rowspan:0,colspan:0},
            {rowIndex:1,columnIndex:2,rowspan:1,colspan:2},
            {rowIndex:1,columnIndex:3,rowspan:0,colspan:0},
        ],
        spanHeaderColumns2 : [
            {rowIndex:1,columnIndex:0,rowspan:1,colspan:2},
            {rowIndex:1,columnIndex:1,rowspan:0,colspan:0},
        ],

    }
  },
  created() {
  },
  mounted: function () {
    this.$nextTick(function () {
        //画面レンダー後行う
        document
            .getElementsByClassName('el-table__cell gutter')[1]
            .setAttribute('rowSpan', 2);
    })
   },
  methods: {
    pageBack(obj){
      this.pagination.page = obj
    },
  }
};
</script>
<style scoped>
  .home-contianer{
    padding: 10px;
  }
</style>
