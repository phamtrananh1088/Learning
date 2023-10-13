(function ($, global) {
    var UploadFile = function () {
        this._initialize.apply(this, arguments);
    };

    var defaults = {
        dropArea: undefined,
        fileSizeLimit: 10485760 * 5,
        submit: undefined,
        uploadControl: '#btnUpload'
    };
    UploadFile.prototype = {
        _initialize: function (settings) {
            this.settings = $.extend({}, defaults, settings);
            this._execute();
        },
        _execute: function () {
            var self = this;
            var input = document.createElement('input');
            input.type = "file";
            input.id = "upload_file";
            input.style = "display:none";
            //input.accept = ".csv, application/vnd.openxmlformats-officedocument.spreadsheetml.sheet, application/vnd.ms-excel";
            $(input).change(async function () {
                if (!$(input)[0].files[0]) {
                    TorihikiUtils.hideLoading();
                    return;
                }

                if ($(input)[0].files[0].size > self.settings.fileSizeLimit) {
                    TorihikiMsg.alert("添付ファイルは10.0 MB以内でお願いします。");
                    $(input).val('');                   
                    return;
                }
                await self.settings.submit($(input)[0].files);
                $(input).val('');  
            });

            $(this.settings.uploadControl).click(function () {
                $(input).click();
            });

            this.settings.dropArea.bind('dragenter dragover', function (evt) {
                if (!self.settings.dropArea.hasClass('is-dragover')) {
                    self.settings.dropArea.addClass('is-dragover');
                }
                evt.stopPropagation();
                evt.preventDefault();
                return false;
            })
                .bind('drop', async function (evt) {
                    if (self.settings.dropArea.hasClass('is-dragover')) {
                        self.settings.dropArea.removeClass('is-dragover');
                    }
                    evt.stopPropagation();
                    evt.preventDefault();
                    var files = evt.originalEvent.dataTransfer.files;
                    if (!files[0]) {
                        TorihikiUtils.hideLoading();
                        return;
                    }
                    if (files[0].size > self.settings.fileSizeLimit) {
                        TorihikiMsg.alert("添付ファイルは10.0 MB以内でお願いします。");
                        return;
                    }
                    await self.settings.submit(files);

                    return false;
                })
                .bind("dragleave", function (evt) {
                    if (self.settings.dropArea.hasClass('is-dragover')) {
                        self.settings.dropArea.removeClass('is-dragover');
                    }
                    evt.stopPropagation();
                    evt.preventDefault();
                });

            return false;
        }
    }
    global.TorihikiUploadFile = UploadFile;

})(jQuery, this);