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
            rect.attr('data-width', options.width);
            rect.attr('data-height', options.height);

            $(self.conf.target).append(rect);
            // Listen for the event.
            rect.bind('refresh', async function (evt) {
                console.log(evt);
                //if (Math.abs(evt.detail.newValue[0] - evt.detail.oldValue[0]) >= 4 || Math.abs(evt.detail.newValue[1] - evt.detail.oldValue[1]) >= 4) {
                //    $(this).remove();
                //    $(self.conf.target).append($(this));
                //}
            });
            self.curRect = rect;
            
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
            var rect = $(`<div style="position: absolute;left: ${options.x}px; top: ${options.y}px; width: ${options.width}px; height: ${options.height}px; border: ${options.strokeWidth}px solid ${options.stroke};background-color: ${options.fill}" />`);

            rect.css('left', options.x);
            rect.css('top', options.y);
            var resfreshData = {
                oldValue: [Number(rect.attr('data-width')), Number(rect.attr('data-height'))],
                newValue: [options.width, options.height]
            }
            rect.css('width', options.width);
            rect.css('height', options.height);
            rect.css('stroke', options.stroke);
            rect.css('border', `${options.strokeWidth}px solid ${options.stroke}`);
            rect.css('background-color', options.fill);

            var refreshEvent = new CustomEvent("refresh", { detail: resfreshData });
            // Dispatch the event.
            rect[0].dispatchEvent(refreshEvent);
        },
    };
    global.TorihikiDIV = DIV;

})(jQuery, this);