import store from '../store/index'
import http from '../api/http'

let base = {
  isPhone (val) {
    return /^[1][3,4,5,6,7,8,9][0-9]{9}$/.test(val)
  },
  isDecimal (val) {
    return /(^[\-0-9][0-9]*(.[0-9]+)?)$/.test(val)
  },
  isNumber (val) {
    return /(^[\-0-9][0-9]*([0-9]+)?)$/.test(val)
  },
  isMail (val) {
    // return /^(\w-*\.*)+@(\w-?)+(\.\w{2,})+$/.test(val)
    // return /^(\w|[+-])+(\.(\w|[+-])+)*@(\w|[-+])+((\.(\w|[+-])+)+)$/.test(val)
    return /^(([^<>()\[\]\.,;:\s@\"]+(\.[^<>()\[\]\.,;:\s@\"]+)*)|(\".+\"))@(([^<>()[\]\.,;:\s@\"]+\.)+[^<>()[\]\.,;:\s@\"]{2,})$/.test(val)
  },
  isUrl (url) {
    return this.checkUrl(url)
  },
  checkUrl (url) {
    // url= 协议://(ftp的登录信息)[IP|域名](:端口号)(/或?请求参数)
    var strRegex =
      '^((https|http|ftp)://)?' + // (https或http或ftp):// 可有可无
      "(([\\w_!~*'()\\.&=+$%-]+: )?[\\w_!~*'()\\.&=+$%-]+@)?" + // ftp的user@  可有可无
      '(([0-9]{1,3}\\.){3}[0-9]{1,3}' + // IP形式的URL- 3位数字.3位数字.3位数字.3位数字
      '|' + // 允许IP和DOMAIN（域名）
      '(localhost)|' + // 匹配localhost
      "([\\w_!~*'()-]+\\.)*" + // 域名- 至少一个[英文或数字_!~*\'()-]加上.
      '\\w+\\.' + // 一级域名 -英文或数字  加上.
      '[a-zA-Z]{1,6})' + // 顶级域名- 1-6位英文
      '(:[0-9]{1,5})?' + // 端口- :80 ,1-5位数字
      '((/?)|' + // url无参数结尾 - 斜杆或这没有
      "(/[\\w_!~*'()\\.;?:@&=+$,%#-]+)+/?)$" // 请求参数结尾- 英文或数字和[]内的各种字符
    var re = new RegExp(strRegex, 'i') // i不区分大小写
    // 将url做uri转码后再匹配，解除请求参数中的中文和空字符影响
    if (re.test(encodeURI(url))) {
      return true
    }
    return false
  },
  checkPass (val, val2) {
    // （英文字 （ 大文字、小文字 ）
    const ralpha = 'a-z'
    const ralphaUp = 'A-Z'

    // 数字
    const rnumber = '0-9'

    // refer https://www.touki-kyoutaku-online.moj.go.jp/password/password_available.html
    // "`" : // アクサングラーブ
    // "˜" : // 波線，波線符号
    // "!" : // 感嘆符
    // "@" : // 単価記号
    // "#" : // 番号記号，井桁
    // "$" : // ドル記号
    // "%" : // パーセント
    // "^" : // アクサンシルコンフレックス，キャロット
    // "&" : // アンパサンド
    // "*" : // アスタリスク
    // "(" : // 左小括弧
    // ")" : // 右小括弧
    // "_" : // アンダライン
    // "+" : // 正符号
    // "-" : // ハイフン，負符号
    // "=" : // 等号
    // "{" : // 左中括弧
    // "}" : // 右中括弧
    // "[" : // 左大括弧
    // "]" : // 右大括弧
    // "￥，\\" : // 円記号，バックスラッシュ（※）
    // "|" : // 縦線
    // ":" : // コロン
    // ";" : // セミコロン
    // '"' : // 引用符
    // "'" : // シングルクォート，アポストロフィー
    // "<" : // 不等号（より小）
    // ">" : // 不等号（より大）
    // "," : // コンマ，セディユ
    // "." : // 終止符
    // "?" : // 疑問符
    // "/" : // 斜線
    const rsym = '`\\˜!@#\\$%\\^&\\*\\(\\)_\\+\\-=\\{\\}\\[\\]￥，\\\\\|:;"\'<>,\\.\\?\\/'
    const rmix = ralpha + ralphaUp + rnumber + rsym

    if (val2 === 1) {
      const mix = new RegExp('[^' + rmix + ']').test(val)
      return !mix
    } else if (val2 === 2) {
      var chkCnt = 0
      const alpha = new RegExp('[' + ralpha + ']').test(val)
      const alphaUp = new RegExp('[' + ralphaUp + ']').test(val)
      const number = new RegExp('[' + rnumber + ']').test(val)
      const sym = new RegExp('[' + rsym + ']').test(val)
      if (alpha)
        chkCnt++
      if (alphaUp)
        chkCnt++
      if (number)
        chkCnt++
      if (sym)
        chkCnt++
      if(chkCnt >= 3){
        return true
      }else{
        return false
      }
      //return alpha && alphaUp && number && sym
    }
    return true
  },
  formatCurrDate () {
    var today = new Date()
    var dd = String(today.getDate()).padStart(2, '0')
    var mm = String(today.getMonth() + 1).padStart(2, '0') // January is 0!
    var yyyy = today.getFullYear()
    today = yyyy + '/' + mm + '/' + dd
    return today
  },
  matchUrlIp (url, ip) { // url使用是否使用的当前ip
    if (!url || !ip) {
      return false
    }
    return url.indexOf(ip.replace('https://', '').replace('http://', '')) >= 0
  },
  getImgSrc (src, httpUrl) {
    if (this.isUrl(src)) {
      return src
    }
    if (httpUrl) {
      return httpUrl + src
    }
    return src
  },
  // 下载文件 $element 标签, url完整url, fileName 文件名, header 以key/value传值
  // backGroundUrl 后台url，如果后台url直接从后台下载，其他全部通过点击a标签下载
  dowloadFile (url, fileName, header, backGroundUrl) {
    if (!url) return alert('此文件没有url不能下载')
    if (!this.isUrl(url)) {
      url = backGroundUrl + url
    }
    window.open(url)
  },
  base64ToBlob (b64Data, contentType = '', sliceSize = 512) {
    const byteCharacters = atob(b64Data)
    const byteArrays = []

    for (let offset = 0; offset < byteCharacters.length; offset += sliceSize) {
      const slice = byteCharacters.slice(offset, offset + sliceSize)

      const byteNumbers = new Array(slice.length)
      for (let i = 0; i < slice.length; i++) {
        byteNumbers[i] = slice.charCodeAt(i)
      }

      const byteArray = new Uint8Array(byteNumbers)
      byteArrays.push(byteArray)
    }

    const blob = new Blob(byteArrays, {type: contentType})
    return blob
  },
  downloadNormal (blob, fileName) {
    const url = window.URL.createObjectURL(blob)
    const link = document.createElement('a')
    link.href = url
    link.setAttribute('download', fileName)
    document.body.appendChild(link)
    link.click()
    document.body.removeChild(link)
    return true
  },
  async saveFileDownload (data, fileName, contentType = '') {
    let blob = data
    if (contentType !== '') {
      blob = this.base64ToBlob(data, contentType)
    }
    if (window.location.protocol == 'https:') {
      const fileHandle = await window.showSaveFilePicker({
        suggestedName: fileName
      }).catch(ex => {
        return this.downloadNormal(blob, fileName)
      })
      if (fileHandle) {
        const fileStream = await fileHandle.createWritable()
        await fileStream.write(blob)
        await fileStream.close()
        return true
      }
      return false
    } else {
      return this.downloadNormal(blob, fileName)
    }
  },
  // 将普通对象转换为tree结构
  // data数据格式[
  //     { name: 'tree1', id: 1, parentId: 0 },
  //     { name: 'tree2', id: 2, parentId: 0 }]

  // 1、id与parentId这两个字段必须有
  // 2、树形tree需要注意Id与parentId循环依赖的问题
  // 3、callback每次生成一新的节点的时回调的方法

  convertTree (data, callback) {
    var treeIds = []
    var root_data = []
    data.forEach(x => {
      if (!x.children) {
        x.children = []
      }
      if (!x.hidden && x.id !== undefined && x.id !== x.parentId && !data.some(s => {
        return x.parentId == s.id
      })) {
        x.isRoot = true
        callback && callback(x, data, true, treeIds)
        root_data.push(x)
        getTree(x.id, x, data, callback, treeIds)
      }
    })
    var exceptionNodes = data.filter(f => {
      return treeIds.indexOf(f.id) == -1 && !f.hidden
    })

    root_data.push(...exceptionNodes)
    return root_data
  },
  getTreeAllParent (id, data) { // 获取某个节点的所有父节点信息2020.11.01
    var nodes = []
    if (!(data instanceof Array)) {
      return nodes
    }

    var _child = data.find(x => { return x.id === id })
    if (!_child) {
      return []
    }
    nodes.push(_child)
    var _parentIds = [_child.parentId]
    for (let index = 0; index < _parentIds.length; index++) {
      var _node = data.find(x => { return x.id === _parentIds[index] && x.id !== x.parentId })
      if (!_node) {
        return nodes
      }
      _parentIds.push(_node.parentId)
      nodes.unshift(_node)
    }
    return nodes
  },
  // Obsolete, get IPAddress under server side
  getUpdateHost () {
    return new Promise((resolve) => {
      const ipAddress = store.getters.getIpAddress()
      if (ipAddress) {
        return resolve(ipAddress)
      }
      http
        .get('/api/User/getClientIpAddress')
        .then((res) => {
          store.commit('setIpAddress', res.data)
          return resolve(res.data)
        })
    })
  },

  // Name:		isNull
  // Purpose:		文字列が空文字であるか調べます。（正確にはnullではない）
  // Parameters:
  //				s : 文字列
  // Return:		bool
  //				nullである場合はfalseです
  // Ex:			isNull(document.MyForm.Num)
  // Date:		2005/04/08
  // Modified:
  isNull (s) {
    if (s.length == 0) { return true } else { return false }
  },

  // Name:		getSimpleByte
  // Purpose:		文字列をShift-JISで表現する場合に必要なバイト数を返します。
  //				すなわち、Unicode時であっても、半角を１バイトとして解釈するようなものです。
  // Parameters:
  //				s:	文字列
  // Return:		number
  // Example:		getSimpleByte(document.Form.Text1.value)
  // Date:		2005/04/08
  // Modified:    2017/11/22：改行文字コードを２バイトとして判定するよう修正
  //
  getSimpleByte (s) {
    var i
    var cnt = 0

    for (i = 0; i < s.length; i++) {
	    //* *** 2019/11/22 DHK.YWJ ADD  【TOKAI器具販売】Begin******************
	    // 2017/11/22
	    // 事象：改行文字が1バイトとしてカウントされると、データ登録時にOracle側でエラーになる可能性がある(Oracle[S-JIS]では改行文字を2バイトであつかう)
	    // 対応：CRとLFのいずれかに合致した場合（CR+LRも含まれる）2バイトとしてカウントする。
	    if (escape(s.charAt(i)).match(/%0D/) || escape(s.charAt(i)).match(/%0A/)) {
	        cnt += 2
	    }
	    // DHK.ROCWANG 2020/02/14 ×÷がよく使うので特別に処理します。
	    else if (escape(s.charAt(i)).match(/%D7/) || escape(s.charAt(i)).match(/%F7/)) {
	        cnt += 2
	    }
	    //* *** 2019/11/22 DHK.YWJ ADD  【TOKAI器具販売】End  ******************
	    else if (escape(s.charAt(i)).length >= 4) {
        // ｡､｢｣を許可するため範囲を広げる 2020/10/29
	    	if (escape(s.charAt(i)) >= '%uFF61' && escape(s.charAt(i)) <= '%uFF9F')
	        // if (escape(s.charAt(i)) >= "%uFF65" && escape(s.charAt(i)) <= "%uFF9F")
	            { cnt++ } else { cnt += 2 }
	    } else {
	        cnt++
	    }
    }
    return cnt
  },

  // Name:		isSimpleByte
  // Purpose:		文字列をShift-JISで表現した場合に、SingleByteとなるものだけで構成されているかを調べます。
  //				すなわち、Unicode時であっても、半角文字列のみで構成されているかを知るためのものです。
  // Parameters:
  //				s:	文字列
  // Return:		bool
  // Example:		isSimpleByte(document.Form.Text1.value)
  // Date:		2005/04/08
  // Modified:
  isSimpleByte (s) {
    if (this.getSimpleByte(s) == s.length) { return true } else { return false }
  },

  // Name:		isAlphabet
  // Purpose:		文字列がAlphabetで構成されているかを調べます。
  // Parameters:
  //				s:	文字列
  // Return:		bool
  // Example:		isAlphabet(document.Form.Text1.value)
  // Date:		2005/04/08
  // Modified:
  isAlphabet (s) {
    if (this.isSimpleByte(s)) {
      if (s.match(/[^A-Za-z]/)) { return false } else { return true }
    } else { return false }
  },

  // Name:		isSimpleChar
  // Purpose:		文字列がSimpleなCharで構成されているかを調べます。
  //				ここで言うSimpleなCharとは、英数字のみです
  // Parameters:
  //				s:	文字列
  // Return:		bool
  // Example:		getByte(document.Form.Text1.value)
  // Date:		2005/04/08
  // Modified:
  isSimpleChar (s) {
    if (this.isSimpleByte(s)) {
      if (s.match(/[^A-Za-z0-9]/)) { return false } else { return true }
    } else { return false }
  },

  // Name:		convertSimpleCharToSBCS
  // Purpose:		全角英数字を半角にします。
  //				全角英数字以外は半角にされません。そのまま返ります。必要であれば、isSimpleByteで内容をチェックすべきです。
  // Parameters:
  //				s:	文字列
  // Return:		string
  // Example:		convertToSBCS(document.Form.Text1.value)
  // Date:		2005/04/08
  // Modified:
  convertSimpleCharToSBCS (s) {
    return s.replace(/([Ａ-Ｚａ-ｚ０-９])/g, function ($0) { return String.fromCharCode($0.charCodeAt(0) - 65248) })
  },

  // Name:		convertSimpleCharToDBCS
  // Purpose:		半角英数字を全角にします。
  //				半角英数字以外は全角にされません。そのまま返ります。必要であれば、getSimpleByteの1/2のlengthであるかチェックすべきです。
  // Parameters:
  //				s:	文字列
  // Return:		string
  // Example:		convertToSBCS(document.Form.Text1.value)
  // Date:		2005/04/08
  // Modified:
  convertSimpleCharToDBCS (s) {
    return s.replace(/(\w)/g, function ($0) { return String.fromCharCode($0.charCodeAt(0) + 65248) })
  },

  // Name:		putZeroForward
  // Purpose:		指定された長さに0詰した文字列を返します。0は前に付きます。
  // Parameters:
  //				s:	文字列（数値列も文字列として処理されます。意識する必要はありません）
  //				length:	桁数
  // Return:		string
  //				指定された長さよりすでに大きい時は、何もされません。
  // Example:		putZeroForward(document.Form.Text1.value)
  // Date:		2005/04/08
  // Modified:
  putZeroForward (s, length) {
    if (s.length == 0) return ''
    var put = this.getPutZero(s, length)
    return (put + s)
  },

  // Name:		putZeroBackward
  // Purpose:		指定された長さに0詰した文字列を返します。0は後ろに付きます。
  // Parameters:
  //				s:		文字列（数値列も文字列として処理されます。意識する必要はありません）
  //				length:	桁数
  // Return:		string
  //				指定された長さよりすでに大きい時は、何もされません。
  // Example:		putZeroBackward(document.Form.Text1.value)
  // Date:		2005/04/08
  // Modified:
  putZeroBackward (s, length) {
    if (s.length == 0) return ''
    var put = this.getPutZero(s, length)
    return (s + put)
  },

  // Name:		putZeroBackward
  // Purpose:		指定された長さに0詰するのに必要な"0"文字列を返します。これをそのまま使う事はほぼありません。
  // Parameters:
  //				s:	文字列（数値列も文字列として処理されます。意識する必要はありません）
  //				length:	桁数
  // Return:		string
  //				指定された長さよりすでに大きい時は、空文字を返します。
  // Example:		getPutZero(document.Form.Text1.value)
  // Date:		2005/04/08
  // Modified:
  getPutZero (s, length) {
    var src = new String(s)
    var cnt = length - src.length
    if (cnt <= 0) return ''
    var str = new String()
    while (cnt-- > 0) str += '0'
    return str
  },

  // Name:		isPureNumeric
  // Purpose:		文字列sが数値として妥当であるかを調べます。
  //				123.45も1,234.5も-1も+0も、数値としては妥当です。すなわちそれは数値型にキャストできます。
  //				特殊なパターンとして、.1は0.1評価され、妥当です。+.4も0.4と評価されますが、+は妥当ではありません。（-も同様です）
  //				例外として、1,23.4も妥当と評価されます。それは123.4と評価されます。
  // Parameters:
  //				s:	文字列
  // Return:		bool
  //				以下の条件を満たす時、trueです。
  //				全ての文字が数値か、+ - , . であり、
  //				文字列の頭は数値か、+ - . であり（つまり、.012という入力も可能で、これは0.012とされる）、
  //				.は文字列中に１つしかなく、それ以外はすべて数値である時
  // Example:		isPureNumeric(document.Form.Text1.value)
  // Date:		2005/04/08
  // Modified:
  isPureNumeric (s) {
    var src = this.getPureNumeric(s)

    if (this.isNull(src)) { return false } else { return true }
  },

  // Name:		getPureNumeric
  // Purpose:		文字列sが数値として妥当であるかを調べ、そこからカンマを除いた文字列として返します。
  //				isPureNumericの本体でもあります。
  // Parameters:
  //				s:	文字列
  // Return:		string
  //				変換不可能な場合、空文字を返します。
  // Example:		getPureNumeric(document.Form.Text1.value)
  // Date:		2005/04/08
  // Modified:
  getPureNumeric (s) {
    if (this.isNull(s)) { return '' }

    if (s.match(/[^0-9\+\-\,\.]/)) { return '' }

    var src = new String(s)
    var isdot = new Boolean(false)
    var purenum = new String()

    if (src.match(/^[^0-9\+\-\.]/)) { return '' }

    if (src.match(/^\./)) { purenum = '0' }

    if (src.match(/^[\+\-]/)) {
      if (src.length == 1) { return '' }
    }

    if (src.match(/^\+/)) { purenum = '' } else { purenum += src.substr(0, 1) }

    for (var i = 1; i < src.length; i++) {
      if (src.substr(i, 1).match(/[\+\-]/)) { return '' }

      if (src.substr(i, 1).match(/\./)) {
        if (isdot == true) { return '' }

        if (src.substr(i - 1, 1).match(/[^0-9]/)) { purenum += '0' }

        isdot = true
      } else {
        if (src.substr(i, 1).match(/,/)) { continue }

        if (src.substr(i, 1).match(/[^0-9]/)) { return '' }
      }
      purenum += src.substr(i, 1)
    }
    if (purenum.match(/[\.]$/)) { purenum = purenum.substr(0, purenum.length - 1) }

    return purenum
  },

  // Name:		formatCommaNumeric
  // Purpose:		文字列sが数値として妥当であるかを調べ、そこから#,##0な数値書式を摘要した文字列を返します。
  // Parameters:
  //				s:	文字列
  // Return:		string
  // Example:		formatCommaNumeric(document.Form.Text1.value)
  // Date:		2005/07/30
  // Modified:
  formatCommaNumeric (s) {
    return this.formatCommaNumeric(s, -1)
  },

  // Name:		formatCommaNumeric
  // Purpose:		文字列sが数値として妥当であるかを調べ、そこから#,##0な数値書式を摘要した文字列を返します。
  //			第二引数に指定される桁数分、小数部を0詰します。
  // Parameters:
  //				s:	文字列
  //				d:	数値
  //
  // Return:		string
  // Example:		formatCommaNumeric(document.Form.Text1.value)
  // Date:		2005/07/30
  // Modified:
  formatCommaNumeric (s, d) {
    s = s + ''
    var src = this.getPureNumeric(s)

    var minus = new String('')
    var commanum = new String('')
    var integers = new String()
    var decimals = new String()

    if (this.isNull(src)) { return s }

    if (src.match(/\./)) {
      var split = src.split('.')
      integers = split[0]
      decimals = '.' + split[1]
    } else {
      integers = src
      decimals = ''
    }

    integers = (integers - 0) + ''

    if (integers.match(/^\-/)) {
      minus = '-'
      integers = integers.substr(1, integers.length - 1)
    }

    var loopcnt = new Number(integers.length)
    var cnt = new Number(0)
    while (loopcnt != -1) {
      if (cnt > 3) {
        cnt = 1
        commanum = ',' + commanum
      }
      commanum = integers.substr(loopcnt, 1) + commanum
      cnt++
      loopcnt--
    }

    if (d > 0 && decimals.length == 0) { decimals = '.' }

    for (var i = decimals.length - 1; i < d; i++) {
      decimals += '0'
    }
    if (d == -1) { decimals = decimals.substr(0, d) }

    commanum = minus + commanum + decimals

    return commanum
  },

  // Name:		isScaleNumeric
  // Purpose:		文字列sが長さlength以下の、小数部decimalsな文字列であるか調べます。この時、.は一文字と換算されません。
  //				すなわち、(12.345, 5, 3)はtrueです。その値は、多くのDBでのNumeric(5,3)なFieldに収まるでしょう。すなわち-12.345もtrueです。
  //				*注：与えられた数値は、PureNumericに変換されて処理されます。
  // Parameters:
  //				s:			文字列
  //				length:		許可される全体の長さ（.を含まない）
  //				decimals:	小数部
  // Return:		bool
  //				PureNumericに変換できないものはfalseです。（すなわち、空文字はfalseにされます）
  // Example:		isScaleNumeric(document.Form.Text1.value, 5, 2)
  // Date:		2005/04/08
  // Modified:
  isScaleNumeric (s, length, decimals) {
    var src = this.getPureNumeric(s)

    if (this.isNull(src)) { return false }

    var integers = parseInt(length) - parseInt(decimals)

    if (src.match(/^\-/)) { src = src.substr(1, src.length - 1) }

    if (src.match(/\./)) {
      var split = src.split('.')
      if (split[0].length > integers) { return false }

      if (split[1].length > decimals) { return false }
    } else {
      if (src.length > integers) { return false }
    }
    return true
  },

  // Name:		isScaleText
  // Purpose:		文字列sが長さlength以下であるかを調べます。その時の長さは文字数ではなく、SimpleByteです。すなわち、Unicodeであっても、半角を1Byteとして考えます。
  // Parameters:
  //				s:			文字列
  //				length:		許可される全体の長さ。これはSimpleByteです。
  // Return:		bool
  // Example:		isScaleText(document.Form.Text1.value)
  // Date:		2005/04/08
  // Modified:
  isScaleText (s, length) {
    var dst = this.getSimpleByte(s)

    if (dst > length) { return false } else { return true }
  },

  // Name:		substrSimpleByte
  // Purpose:		文字列sをSimpleByte換算でsubstrします。すなわち、あいうえお のstart = 0, lenght = 5 は あい です。
  // Parameters:
  //				s:			文字列
  //				start:		開始位置。これはSimpleByteです。
  //				length:		許可される全体の長さ。これはSimpleByteです。
  // Return:		bool
  // Example:		substrSimpleByte(document.Form.Text1.value, 0, 5)
  // Date:		2005/05/11
  // Modified:
  substrSimpleByte (s, start, length) {
    var i
    var dst = new String(s)
    var rtn = new String()

    for (i = start; i < dst.length; i++) {
      if (this.isSimpleByte(dst.substr(i, 1))) { length-- } else { length -= 2 }

      if (length < 0) break
      else rtn += dst.substr(i, 1)
    }
    return rtn
  },

  // Date:		2006/02/17
  // Name:        CalcShoSuuKbn
  // Purpose:     小数計算区分により、端数の計算を行う
  // Parameters:
  //              s:      小数計算区分（0：切捨て、1：四捨五入、2：切上げ）
  //              d:      対象値
  //              i:      小数第何位か
  // Return:      端数計算を行った値
  CalcShoSuuKbn (s, d, i) {
    if (d == 0) return 0	// 数値の0を戻すように変更
    // if ( d == 0 ) return '0';

    if (s == '0') {
      return this.ToRoundDown(d, i)
    } else if (s == '1') {
      return this.ToHalfAjust(d, i)
    } else if (s == '2') {
      return this.ToRoundUp(d, i)
    }

    return 0	// 数値の0を戻すように変更
    // return '0';
  },

  // Date:		2006/02/17
  // Name:        ToRoundUp
  // Purpose:     切上げ関数
  // Parameters:
  //              dValue:    対象値
  //              iDigits:   小数第何位か
  // Return:      計算を行った値
  // Modified:    2017/12/04：マイナスの場合の丸め方をExcelに合わせる
  ToRoundUp (dValue, iDigits) {
    var dCoef = 1
    for (let i = 0; i < iDigits; i++) dCoef = dCoef * 10

    //* *** 2019/11/22 DHK.YWJ UPD  【TOKAI器具販売】Begin******************
    // Math.ceilだとマイナスの場合に-1.1→-1になるので、Math.floorを使用して-1.1→-2になるようにする 2017/12/04
    if (dValue >= 0) {
      return Math.ceil(dValue * dCoef) / dCoef
    } else {
      return Math.floor(dValue * dCoef) / dCoef
    }
    // return Math.ceil(dValue * dCoef) / dCoef;
    //* *** 2019/11/22 DHK.YWJ UPD  【TOKAI器具販売】End  ******************
  },

  // Date:		2006/02/17
  // Name:        ToHalfAjust
  // Purpose:     四捨五入関数
  // Parameters:
  //              dValue:    対象値
  //              iDigits:   小数第何位か
  // Return:      計算を行った値
  // Modified:    2017/12/04：マイナスの場合の丸め方をExcelに合わせる
  ToHalfAjust (dValue, iDigits) {
    var dCoef = 1

    //* *** 2019/11/22 DHK.YWJ UPD  【TOKAI器具販売】Begin******************
    var sign = (dValue < 0) ? -1 : 1
    // var sign = Math.sign(dValue);	//Math.signはIE11未対応
    for (let i = 0; i < iDigits; i++) dCoef = dCoef * 10

    // 従来の処理だとマイナスの場合に-1.5→-1になるため、一度プラスにしてからマイナスに戻して-1.5→-2になるようにする 2017/12/04
    return Math.floor((dValue * sign * dCoef) + 0.5) / dCoef * sign
    // return Math.floor((dValue * dCoef) + 0.5) / dCoef;
    /// /return Math.round(dValue);
    //* *** 2019/11/22 DHK.YWJ UPD  【TOKAI器具販売】End  ******************
  },

  // Date:		2006/02/17
  // Name:        ToRoundDown
  // Purpose:     切捨て関数
  // Parameters:
  //              dValue:    対象値
  //              iDigits:   小数第何位か
  // Return:      計算を行った値
  // Modified:    2017/12/04：マイナスの場合の丸め方をExcelに合わせる
  ToRoundDown (dValue, iDigits) {
    var dCoef = 1
    for (let i = 0; i < iDigits; i++) dCoef = dCoef * 10
    //* *** 2019/11/22 DHK.YWJ UPD  【TOKAI器具販売】Begin******************
    // Math.floorだとマイナスの場合に-1.1→-2になるので、Math.ceilを使用して-1.1→-1になるようにする 2017/12/04
    if (dValue >= 0) {
      return Math.floor(dValue * dCoef) / dCoef
    } else {
      return Math.ceil(dValue * dCoef) / dCoef
    }
    // return Math.floor(dValue * dCoef) / dCoef;
    //* *** 2019/11/22 DHK.YWJ UPD  【TOKAI器具販売】End  ******************
  },

  // Date:		2006/02/17
  // Name:        CalcRiekiRtnTanka
  // Purpose:     利益率より単価再計算時の端数計算
  // Parameters:
  //              dValue:    対象値
  //              sKbn:      小数区分（0：切捨て 1：四捨五入 2：切上げ 3：処理なし）
  //              iDigits:   対象の位（1：1の位 2：10の位 3：100の位 4：1000の位 5：10000の位）
  // Return:      計算を行った値
  CalcRiekiRtnTanka (dValue, sKbn, iDigits) {
    var rtnValue

    if (sKbn == '3') return dValue
    if (dValue == 0) return dValue

    var dCoef = 1
    for (i = 0; i < iDigits; i++) dCoef = dCoef * 10

    if (dValue > 0) {
      if (dValue < dCoef) return dValue
    } else {
      if (dValue > dCoef) return dValue
    }

    if (sKbn == '0') {
      return Math.floor(dValue / dCoef) * dCoef
    } else if (sKbn == '1') {
      return Math.round((dValue / dCoef)) * dCoef
    } else if (sKbn == '2') {
      return Math.ceil(dValue / dCoef) * dCoef
    }

    return rtnValue
  },

  // Date:		2006/12/28
  // Name:        changeZip
  // Purpose:     テキストボックスの値を郵便番号の形式に変換（文字数が足りない場合は右側に0詰めします）
  // Parameters:
  //              objTextBox : テキストボックスのオブジェクト(通常はthisを指定)
  // Return:      変換不可の場合にはfalse
  changeZip (objTextBox) {
    var zip = objTextBox.value
    if (zip.length == 0) return

    zip = zip.replace('-', '')

    if (zip.match(/[^0-9]/)) {
	    alert('郵便番号に変換できない値が入力されました。')
	    this.SetObjValue(objTextBox, '')// 2019/01/14 DHK.YWJ ADD  【FCフロンティアコンサルティング】
      objTextBox.focus()
      return false
    } else {
      zip = (zip + '0000000').substr(0, 7)
      objTextBox.value = zip.substr(0, 3) + '-' + zip.substr(3, 4)
    }
  },

  // Date:		2007/08/20
  // Name:        Get_ZeiKomi
  // Purpose:     税抜金額から税込金額を計算
  // Parameters:
  //              Ymd				: 消費税改定日と比較するのに使用()
  //              Value			: 税抜きの金額
  //              TaxRitsu		: 消費税率
  //              KaiteiTaxRitsu	: 改定後消費税率
  //              KaiteiYmd		: 消費税率改定日
  //              TaxKbn			: 消費税計算区分
  // Return:       税込みの金額を返します。
  // 税区分マスタ(M024)追加に伴い仕様変更 2013/08/27 Oshinomi
  Get_ZeiKomi (Value, TaxRitsu, TaxKbn) {
    CalTaxRitsu = 0
    // 税率改定日を超えている日付か同じ日付の場合は改定後税率を使う
    CalTaxRitsu = (TaxRitsu * 1) / 100 + 1	// 2013/08/27 Oshinomi

    // 税込み金額を計算
    RetValue = this.CalcShoSuuKbn_M(TaxKbn, Value * CalTaxRitsu, 0)

    // 税込み金額を返す
    return RetValue
  },

  // Date:		2007/08/20
  // Name:        Get_ZeiNuki
  // Purpose:     税込金額から税抜金額を計算
  // Parameters:
  //              Ymd				: 消費税改定日と比較するのに使用
  //              Value			: 税込みの金額
  //              TaxRitsu		: 消費税率
  //              KaiteiTaxRitsu	: 改定後消費税率
  //              KaiteiYmd		: 消費税率改定日
  //              TaxKbn			: 消費税計算区分
  // Return:       税抜きの金額を返します。
  // 税区分マスタ(M024)追加に伴い仕様変更 2013/08/27 Oshinomi
  Get_ZeiNuki (Value, TaxRitsu, TaxKbn)
  //, Get_ZeiNuki(Ymd, Value, TaxRitsu, KaiteiTaxRitsu, KaiteiYmd, TaxKbn)
  {
    CalTaxRitsu = 0
    // 税率改定日を超えている日付か同じ日付の場合は改定後税率を使う
    CalTaxRitsu = (TaxRitsu * 1) / 100 + 1
    // 税抜き金額を求めるときは、消費税率区分は切捨て->切上げ、切上げ->切捨てとする。
    var CalTaxKbn = TaxKbn
    if (TaxKbn == '0') {
      CalTaxKbn = '2'
    }
    if (TaxKbn == '2') {
      CalTaxKbn = '0'
    }

    // 税込み金額を計算
    RetValue = this.CalcShoSuuKbn_M(CalTaxKbn, Value / CalTaxRitsu, 0)

    // 税込み金額を返す
    return RetValue
  },

  // Date:		2007/08/20
  // Name:        CalcShoSuuKbn_M
  // Purpose:     小数計算区分により、端数の計算を行う
  //              マイナス値の場合は、絶対値で計算し、符号を付加する。
  // Parameters:
  //              s:      小数計算区分（0：切捨て、1：四捨五入、2：切上げ）
  //              d:      対象値
  //              i:      小数第何位か
  // Return:      端数計算を行った値
  CalcShoSuuKbn_M (s, d, i) {
    var value = Math.abs(d)
    // CalcShoSuuKbn(value);	 //2007/09/13 wada rep
    value = this.CalcShoSuuKbn(s, value, i)
    if (d < 0) {
      value = value * -1
    }

    return this.parseInt(value)
  },

  // Date:		2007/12/19
  // Name:        KataKanaCheck
  // Purpose:     stringが全角カナ、半角カナ、全角スペース、半角スペースで構成されているか調べます。
  // Parameters:
  //              s:      確認する文字列
  // Return:      条件を満たしている場合はtrue
  KataKanaCheck (s) {
    if (s.length != 0) {
      if (s.match('[^ァ-ヴーｦ-ﾟ 　]')) {
        return false
      }
    }

    return true
  },

  // Date:		2008/01/29
  // Name:        KakezanGosaTyosei
  // Purpose:     小数の乗算を行った際に演算誤差が出ないように四捨五入して調整
  // Parameters:
  //              r1:      引数1
  //              r2:      引数2
  //              i:       小数第何位の調整を行うか（1.23 * 4.56なら小数第4位がいいかと）
  // Return:      演算誤差の調整を行った値
  KakezanGosaTyosei (r1, r2, i) {
    var rtn = r1 * r2 * Math.pow(10, i)
    rtn = this.ToHalfAjust(rtn, 0)	// 小数点以下を四捨五入
    rtn = rtn / Math.pow(10, i)
    return rtn
  },

  // Date:		2013/08/20
  // Name:        KasanGosaTyosei
  // Purpose:     小数の加算を行った際に演算誤差が出ないように四捨五入して調整
  // Parameters:
  //              r1:      引数1
  //              r2:      引数2
  //              i:       小数第何位の調整を行うか
  // Return:      演算誤差の調整を行った値
  KasanGosaTyosei (r1, r2, i) {
    var rtn = r1 * Math.pow(10, i) + r2 * Math.pow(10, i)
    rtn = this.ToHalfAjust(rtn, 0)	// 小数点以下を四捨五入
    rtn = rtn / Math.pow(10, i)
    return rtn
  },

  // Date:		2013/08/20
  // Name:        GensanGosaTyosei
  // Purpose:     小数の減産を行った際に演算誤差が出ないように四捨五入して調整
  // Parameters:
  //              r1:      引数1
  //              r2:      引数2
  //              i:       小数第何位の調整を行うか
  // Return:      演算誤差の調整を行った値
  GensanGosaTyosei (r1, r2, i) {
    var rtn = r1 * Math.pow(10, i) - r2 * Math.pow(10, i)
    rtn = this.ToHalfAjust(rtn, 0)	// 小数点以下を四捨五入
    rtn = rtn / Math.pow(10, i)
    return rtn
  },

  // Date:		2013/08/20
  // Name:        JyosanGosaTyosei
  // Purpose:     小数の除算を行った際に演算誤差が出ないように四捨五入して調整
  // Parameters:
  //              r1:      引数1
  //              r2:      引数2
  //              i:       小数第何位の調整を行うか（1.23 / 4.56なら小数第4位がいいかと）
  // Return:      演算誤差の調整を行った値
  JyosanGosaTyosei (r1, r2, i) {
    var rtn = (r1 * Math.pow(10, i)) / (r2 * Math.pow(10, i))
    rtn = this.ToHalfAjust(rtn, 0)	// 小数点以下を四捨五入
    rtn = rtn / Math.pow(10, i)
    return rtn
  },

  // メッセージ
  openMsg (vueObj, options, callback) {
    options.confirmButtonText = 'はい(Y)'
    options.closeOnClickModal = false
    return vueObj.$msgbox(options).then(() => {
      if (callback) {
        callback()
      }
    }).catch(() => { })
  },
  openConfirm (vueObj, options) {
    options.confirmButtonText = 'はい(Y)'
    options.cancelButtonText = 'いいえ(N)'
    options.showCancelButton = true
    options.cancelButtonClass = 'btn-custom-cancel'
    options.closeOnClickModal = false
    return vueObj.$msgbox(options).catch(() => { })
  },
  openConfirmMsg (vueObj, options, thenfunc, catchfunc) {
    options.showCancelButton = true
    options.confirmButtonText = 'はい(Y)'
    options.cancelButtonText = 'いいえ(N)'
    options.cancelButtonClass = 'btn-custom-cancel'
    options.closeOnClickModal = false

    if (typeof thenfunc != 'function') { thenfunc = () => {} }
    if (typeof catchfunc != 'function') { catchfunc = () => {} }

    return vueObj.$msgbox(options).then(thenfunc).catch(catchfunc)
  }

}
export default base

// 将普通对象转换为tree结构
function getTree (id, node, data, callback, treeIds) {
  if (treeIds.indexOf(id) == -1) {
    treeIds.push(id)
  }
  data.forEach(x => {
    if (!x.hidden && x.parentId == id) {
      if (!node.children) node.children = []
      callback && callback(x, node, false)
      node.children.push(x)
      getTree(x.id, x, data, callback, treeIds)
    }
  })
}
