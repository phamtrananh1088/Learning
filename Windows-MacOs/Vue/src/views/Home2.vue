<template>
  <div class="home-contianer">
    <el-card shadow="never">
      <div
        slot="header"
        class="clearfix card-header"
      >
        <span class="card-title">ステータス</span>
        <el-button
          style="float: right; padding: 3px 0"
          type="text"
        >再読込</el-button>
      </div>
      <div>
        <el-row :gutter="20">
          <el-col :span="24">
            <div class="grid-content bg-purple-light">
              <el-table
                v-loading="loading"
                :data="tableData"
                style="width: 100%"
              >
                <el-table-column
                  prop="pattern"
                  label=""
                  width="120"
                >
                </el-table-column>
                <el-table-column
                  prop="col1"
                  label="依頼承認(緊急)"
                  width="120"
                >
                </el-table-column>
                <el-table-column
                  prop="col2"
                  label="依頼承認(高額)"
                 
                >
                </el-table-column>
              </el-table>
            </div>
          </el-col>
        </el-row>
      </div>
    </el-card>
    <el-card shadow="never">
      <div
        slot="header"
        class="clearfix card-header"
      >
        <span class="card-title">お知らせ通知</span>
        <el-button
          style="float: right; padding: 3px 0"
          type="text"
        >再読込</el-button>
      </div>
      <div>
        <el-row :gutter="20">
          <el-col :span="24">
            <div class="grid-content bg-purple-light">
              <el-table
                v-loading="loading"
                :data="noticeData"
                style="width: 100%"
              >
                <el-table-column
                  prop="col1"
                  label="掲載日"
                  width="120"
                >
                </el-table-column>
                <el-table-column
                  prop="col2"
                  label="登録システム"
                  width="150"
                >
                </el-table-column>
                <el-table-column
                  prop="col3"
                  label="登録者部署"
                  width="300"
                >
                </el-table-column>
                <el-table-column
                  prop="col4"
                  label="件名"
                >
                </el-table-column>
              </el-table>
            </div>
          </el-col>
        </el-row>
      </div>
    </el-card>
  </div>
</template>
<script>

export default {
  data() {
    return {
      value1: "1"
      ,tableData: [{
          pattern: '承認待ち',
          col1: "99,999",
          col2: "99,999"        
        }],
        noticeData: [{
          col1: "YYYY/MM/DD",
          col2: "Reafs-R",
          col3: "資産管理部 資産管理課",
          col4: "件名NNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNN"
        }, {
          col1: "YYYY/MM/DD",
          col2: "Reafs-R",
          col3: "資産管理部 資産管理課",
          col4: "件名NNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNN"
        }, {
          col1: "YYYY/MM/DD",
          col2: "Reafs-R",
          col3: "資産管理部 資産管理課",
          col4: "件名NNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNN"
        }, {
          col1: "YYYY/MM/DD",
          col2: "Reafs-R",
          col3: "資産管理部 資産管理課",
          col4: "件名NNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNN"
        }, {
          col1: "YYYY/MM/DD",
          col2: "Reafs-W",
          col3: "資産管理部 資産管理課",
          col4: "件名NNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNN"
        }, , {
          col1: "YYYY/MM/DD",
          col2: "Reafs-W",
          col3: "資産管理部 資産管理課",
          col4: "件名NNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNN"
        }, , {
          col1: "YYYY/MM/DD",
          col2: "Reafs-W",
          col3: "資産管理部 資産管理課",
          col4: "件名NNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNN"
        }, , {
          col1: "YYYY/MM/DD",
          col2: "Reafs-W",
          col3: "資産管理部 資産管理課",
          col4: "件名NNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNN"
        }],

        loading: false
    };
  },
  mounted() {
  }
};
</script>
<style lang="less" scoped>
.home-contianer {
  background: #efefef;
}
.home-app {
  display: inline-block;
  padding: 15px;
  padding-top: 5px;
}
.home-app > div {
  float: left;
  width: 33.33333%;
  padding-left: 8px;
  padding-right: 8px;
}
.ivu-card-body {
  text-align: center;
  padding: 25px 13px;
  padding-left: 80px;
}
.demo-color-name {
  color: #fff;
  font-size: 16px;
}
.demo-color-desc {
  color: #fff;
  opacity: 0.7;
}
.ivu-card {
  position: relative;
}
.ivu-card .icon-left {
  border-right: 1px solid;
  padding: 10px 24px;
  height: 100%;
  position: absolute;
  font-size: 50px;
  color: white;
}
.ivu-row {
  display: flex;
  border-bottom: 2px dotted #eee;
  padding: 0px 5px;
  .ivu-col {
    border-radius: 4px;
    background: white;
    flex: 1;
    margin: 13px 8px;
    padding: 20px 20px;
    padding-bottom: 15px;
  }
  .total {
    word-break: break-all;
    color: #282727;
    font-size: 30px;
    padding-bottom: 12px;
    font-family: -apple-system, BlinkMacSystemFont, Segoe UI, PingFang SC,
      Hiragino Sans GB, Microsoft YaHei, Helvetica Neue, Helvetica, Arial,
      sans-serif, Apple Color Emoji, Segoe UI Emoji, Segoe UI Symbol;
  }
  .item-name {
    position: relative;
    .icon {
      position: absolute;
      right: 0;
      top: -1px;
    }
  }
  .item-name,
  .rate,
  .bottom {
    color: #5f5f5f;
    font-size: 14px;
  }
  .rate {
    border-bottom: 1px solid #e6e6e6;
    padding-bottom: 10px;
  }
  .bottom {
    padding-top: 8px;
  }
  .down {
    color: #10d310;
  }
  .up {
    color: red;
  }
}

.h5-desc {
  padding-top: 10px;
}
</style>
<style lang="less">
.charts-line {
  background: white;
  padding-top: 10px;
}
.charts {
  margin: 25px 13px;
  display: inline-block;
  width: 100%;
  .left {
    padding: 25px;
    background: white;
    height: 360px;
    width: 49%;
    float: left;
    margin-right: 1%;
    background: white;
  }
  .right {
    padding: 25px 45px;
    background: white;
    height: 360px;
    width: 49%;
    float: left;
    margin-left: 1%;
    .badge-count {
      padding: 3px 7px;
      position: relative;
      border: 1px solid #eee;
      border-radius: 50%;

      margin-right: 11px;
    }
    .badge {
      background: #e2e2e2;
      color: #3a3535;
    }
    .top3 {
      background: #2db7f5;
      color: white;
    }
    .cell {
      position: relative;
      display: flex;
      padding: 10px 0;
      border-bottom: 1px dotted #eee;
    }
    .primary {
      flex: 1;
    }
    .title {
      font-size: 16px;
      padding-bottom: 6px;
      border-bottom: 1px solid #eee;
      margin-bottom: 11px;
    }
    .name {
      font-size: 15px;
      position: relative;
      top: 5px;
      color: #303133;
      left: 12px;
    }
    .desc {
      margin-left: 27px;
      font-size: 12px;
      color: #b3b3b3;
      position: relative;
      top: 5px;
    }
  }
}
</style>
<style lang="less" scoped>
.numbers {
    margin: 0 12px 12px 12px;
  border-radius: 5px;
  border: 1px solid #eaeaea;
  background: white;
  display: flex;

  padding: 20px 0px;
  .item {
    flex: 1;
    text-align: center;
    border-right: 1px solid #e5e5e5;
  }
  .item > 　div:first-child {
    word-break: break-all;
    color: #282727;
    font-size: 30px;
  }
  .item > 　div:last-child {
    font-size: 13px;
    color: #777;
  }
  .item:last-child {
    border-right: none;
  }
  .number {
    cursor: pointer;
    transition: transform 0.8s;
  }
  .number:hover {
    transform: scale(1.2);
    color: #03c10b !important;
  }
}
</style>
<style lang="less" scoped>
  .el-row {
    margin-bottom: 20px;
    &:last-child {
      margin-bottom: 0;
    }
  }
  .el-col {
    border-radius: 4px;
  }
  .bg-purple-dark {
    background: #99a9bf;
  }
  .bg-purple {
    background: #d3dce6;
  }
  .bg-purple-light {
    background: #e5e9f2;
  }
  .grid-content {
    border-radius: 4px;
    min-height: 36px;
  }
  .row-bg {
    padding: 10px 0;
    background-color: #f9fafc;
  }

  .el-card__header {
    padding: 0 0;
    border:0 0;
    background: #777777;
    color: #ececec;
  }

  .card-title {
    font-size: 1.4em;
  }

</style>

