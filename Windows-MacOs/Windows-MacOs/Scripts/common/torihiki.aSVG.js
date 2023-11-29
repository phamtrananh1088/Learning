(function ($, global) {
    var SVG = function () {
        this._initialize.apply(this, arguments);
    };

    var defaults = {
        target: undefined
    };

    SVG.prototype = {
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
            } else if (!self.conf.target[0].nodeName === 'svg') {
                throw new Error("target control is not SVG");
            };

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

            var rect = $(`<rect x="${options.x}" y="${options.y}" width="${options.width}" height="${options.height}" stroke="${options.stroke}" fill="${options.fill}" stroke-width="${options.strokeWidth}" />`);
            rect.attr('data-width', options.width);
            rect.attr('data-height', options.height);
            if (options.rx) {
                rect.attr('rx', options.ry);
            }
            if (options.ry) {
                rect.attr('ry', options.ry);
            }

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

            rect.attr('x', options.x);
            rect.attr('y', options.y);
            var resfreshData = {
                oldValue: [Number(rect.attr('data-width')), Number(rect.attr('data-height'))],
                newValue: [options.width, options.height]
            }
            rect.attr('width', options.width);
            rect.attr('height', options.height);
            rect.attr('stroke', options.stroke);
            rect.attr('fill', options.fill);
            rect.attr('stroke-width', options.strokeWidth);
            if (options.rx) {
                rect.attr('rx', options.ry);
            }
            if (options.ry) {
                rect.attr('ry', options.ry);
            }
            var refreshEvent = new CustomEvent("refresh", { detail: resfreshData });
            // Dispatch the event.
            rect[0].dispatchEvent(refreshEvent);
            return rect;
        },
    };
    global.TorihikiSVG = SVG;

})(jQuery, this);