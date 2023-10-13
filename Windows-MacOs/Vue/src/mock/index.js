import Mock from 'mockjs'

// interface data

//login
import { login } from './response/user'
Mock.mock('http://localhost:8888/api/user/login', 'post', login)

//menu ??
import { menu } from './response/menu'
Mock.mock('http://localhost:8888/api/menu/getTreeMenu', 'get', menu)


//検索項目　承認種類　
import { syouninSyurui } from './response/syouninSyurui'
Mock.mock('http://localhost:8888/api/syouninSyurui', 'get', syouninSyurui)


//検索項目　対象 　
import { taisyou } from './response/taisyou'
Mock.mock('http://localhost:8888/api/taisyou', 'get', taisyou)

//明細データ
import { searchSubmit } from './response/searchSubmit'
Mock.mock('http://localhost:8888/api/Reafs_W/Shonin/getMeisaiListRD00802', 'get', searchSubmit)
Mock.mock('http://localhost:8888/api/Reafs_W/Shonin/getMeisaiListRD00802', 'post', searchSubmit)

//担当者検索データ
import { searchTanto } from './response/searchTanto'
Mock.mock('http://localhost:8888/api/searchTanto', 'get', searchTanto)

//店検索データ
import { searchMise } from './response/searchMise'
Mock.mock('http://localhost:8888/api/searchMise', 'get', searchMise)

import { searchRD00803 } from './response/searchRD00803'
Mock.mock('http://localhost:8888/api/searchRD00803', 'get', searchRD00803)

import { errorMsgTest } from './response/errorMsgTest'
Mock.mock('http://localhost:8888/api/errorMsgTest', 'post', errorMsgTest)

export default Mock
