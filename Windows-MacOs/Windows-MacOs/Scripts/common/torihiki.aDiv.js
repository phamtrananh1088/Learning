(function ($, global) {
    var DIV = function () {
        this._initialize.apply(this, arguments);
    };

    var defaults = {
        target: undefined
    };

    DIV.prototype = {
        curRect: null,
        _initialize: function (conf) {
            if (conf !== undefined) {
                this.conf = $.extend({}, defaults, conf);
                this._execute();
            }
        },
        _execute: function () {
            var self = this;
            
            if (!self.conf.target) {
                throw new Error("target control is not setting");
            }

            return true;
        },

        addRect: function (conf) {
            var self = this;
            var options = {
                x: 0,
                y: 0,
                width: 0,
                height: 0,
                stroke: 'black',
                fill: 'transparent',
                strokeWidth: 1
            };
            $.extend(options, conf, true);

            var rect = $(`<div style="position: absolute;left: ${options.x}px; top: ${options.y}px; width: ${options.width}px; height: ${options.height}px; border: ${options.strokeWidth}px solid ${options.stroke};background-color: ${options.fill}" />`);

            $(self.conf.target).append(rect);
            self.curRect = rect;
            return rect;
        },

        editRect: function (conf) {
            var self = this;
            var rect = self.curRect;

            if (rect == null) {
                return;
            }
            var options = {
                x: 0,
                y: 0,
                width: 0,
                height: 0,
                stroke: 'black',
                fill: 'transparent',
                strokeWidth: 1
            };
            $.extend(options, conf, true);

            rect.css('left', `${options.x}px`);
            rect.css('top', `${options.y}px`);

            rect.css('width', `${options.width}px`);
            rect.css('height', `${options.height}px`);
            rect.css('border', `${options.strokeWidth}px solid ${options.stroke}`);
            rect.css('background-color', options.fill);
            return rect;
        },
    };
    global.TorihikiDIV = DIV;

})(jQuery, this);