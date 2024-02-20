var viewfile = {

    init: function () {
        let self = this;
        //self.research = TorihikiUtils.initPageSession(sessionKey);
        this.setEvent();
        this.start();
    },
    setEvent: function () {
        var self = this;


    },
    start: function () {
        var self = this;
        var content = self.viewSQL($("#Content").val(), {});
        $("#sql-data").append(content);
        $("#btnSave").click(function () {
            TorihikiUtils.saveFileDownload($("#Content").val(), $("#FileName").val(), $("#ContentType").val())
        });
    },
    viewSQL: function (sql, conf) {
        var options = {
            css0: 'sql-0',
            css1: 'sql-1',
            css2: 'sql-2',
            css3: 'sql-3',
            css4: 'sql-4',
            css5: 'sql-5',
        };
        $.extend(options, conf, true);
        var key1Rg = /(\bSET\b|\bNOCOUNT\b|\bANSI_NULLS\b|\bQUOTED_IDENTIFIER\b|\bCREATE\b|\bPROCEDURE\b|\bTABLE\b|\bNOT\b|\bNULL\b|\bCASE\b|\bWHEN\b|\bTHEN\b|\bELSE\b|\bEND\b|\bIS\b|\bFROM\b|\bLEFT\b|\bINNER\b|\bJOIN\b|\bAS\b|\bBEGIN\b|\bSELECT\b|\bTOP\b|\bCONSTRAINT\b|\bPRIMARY\b|\bKEY\b|\bCLUSTERED\b|\bASC\b|\bWITH\b|\bPAD_INDEX\b|\bOFF\b|\bSTATISTICS_NORECOMPUTE\b|\bIGNORE_DUP_KEY\b|\bALLOW_ROW_LOCKS\b|\bALLOW_PAGE_LOCKS\b|\bON\b|\bANSI_PADDING\b|\bNONCLUSTERED\b|\bINDEX\b|\bSORT_IN_TEMPDB\b|\bDROP_EXISTING\b|\bONLINE\b|\bALTER\b|\bADD\b|\bDEFAULT\b|\bFOR\b)|([\[\]\(\)])|(\bFORMAT\b|\bGETDATE\b)|('.*')|(--.*\n)/gi;
        var m;
        var i = 0, j = 0;
        var fmtSql = [];
        do {
            m = key1Rg.exec(sql);
            console.log(m);
            if (m) {
                j = m.index;
                fmtSql.push(`<span class="${options.css0}">${sql.substring(i, j)}</span>`)
                if (m[0] === m[1]) {
                    fmtSql.push(`<span class="${options.css1}">${m[0]}</span>`)
                } else if (m[0] === m[2]) {
                    fmtSql.push(`<span class="${options.css2}">${m[0]}</span>`)
                } else if (m[0] === m[3]) {
                    fmtSql.push(`<span class="${options.css3}">${m[0]}</span>`)
                } else if (m[0] === m[4]) {
                    fmtSql.push(`<span class="${options.css4}">${m[0]}</span>`)
                } else if (m[0] === m[5]) {
                    fmtSql.push(`<span class="${options.css5}">${m[0]}</span>`)
                }
                i = j + m[0].length;
            } else {
                fmtSql.push(`<span class="${options.css0}">${sql.substring(i)}</span>`)
            }
        } while (m);
        var content = $(`<div class="sql-data text-prewrap">${fmtSql.join('')}</div>`);
        return content;
    },
}

$(document).ready(function () {
    viewfile.init();

});
