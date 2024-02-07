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
            css1: 'sql-1',
            css2: 'sql-1',
            css3: 'sql-3',
        };
        $.extend(options, conf, true);
        var key1Rg = new RegExp('\bSET\b|\bANSI_NULLS\b|\bQUOTED_IDENTIFIER\b|\bCREATE\b|\bTABLE\b|\bNOT\b|\bNULL\b|\bCONSTRAINT\b|\bPRIMARY\b|\bKEY\b|\bCLUSTERED\b|\bASC\b|\bWITH\b|\bPAD_INDEX\b|\bOFF\b|\bSTATISTICS_NORECOMPUTE\b|\bIGNORE_DUP_KEY\b|\bALLOW_ROW_LOCKS\b|\bALLOW_PAGE_LOCKS\b|\bON\b|\bANSI_PADDING\b|\bNONCLUSTERED\b|\bINDEX\b|\bSORT_IN_TEMPDB\b|\bDROP_EXISTING\b|\bONLINE\b|\bALTER\b|\bADD\b|\bDEFAULT\b|\bFOR\b', 'gi');
        var key2Rg = new RegExp('[\[\]\(\)]');
        console.log(key1Rg.exec(sql));
        console.log(key2Rg.exec(sql));
        var content = $(`<div class="sql-data text-prewrap">${sql}</div>`);
        return content;
    },
}

$(document).ready(function () {
    viewfile.init();

});
