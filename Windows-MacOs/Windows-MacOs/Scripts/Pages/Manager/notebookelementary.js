(function ($, global) {
    var SVG = function () {
        this._initialize.apply(this, arguments);
    };

    var defaults = {
        target: undefined
    };

    SVG.prototype = {
        name: 'TorihikiSVGPath',
        curPath: null,
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
        add: function (conf) {
            var self = this;
            var Path = self.curPath;
            return Path;
        },
        edit: function (conf) {
            var self = this;
            var Path = self.curPath;
            return Path;
        },
    };
    global.TorihikiSVGPath = SVG;

})(jQuery, this);

(function ($, global) {
    var SVG = function () {
        this._initialize.apply(this, arguments);
        this.name = 'TorihikiSVGPathGuide';
    };

    SVG.prototype = Object.create(TorihikiSVGPath.prototype);
    SVG.prototype.add = function (conf) {
        var self = this;
        var options = {
            x: 0,
            y: 0,
            x1: 0,
            y1: 0,
            x2: 0,
            y2: 0,
            ex: 0,
            ey: 0,
            width: 0,
            height: 0,
            stroke: 'black',
            fill: 'transparent',
            strokeWidth: 1
        };
        $.extend(options, conf, true);

        var Path = $(`<path d="M ${options.x} ${options.y}" stroke="${options.stroke}" fill="${options.fill}" stroke-width="${options.strokeWidth}" />`);
        //Path.attr('data-d', options);
        //Path.data('l', [1,1]);
        Path.data('p', options);
        $(self.conf.target).append(Path);
        // Listen for the event.
        Path.bind('refresh', async function (evt) {
            console.log(evt);
            //if (Math.abs(evt.detail.newValue[0] - evt.detail.oldValue[0]) >= 4 || Math.abs(evt.detail.newValue[1] - evt.detail.oldValue[1]) >= 4) {
            //    $(this).remove();
            //    $(self.conf.target).append($(this));
            //}
        });
        self.curPath = Path;
        return Path;
    };
    SVG.prototype.edit = function (conf) {
        var self = this;
        var Path = self.curPath;
        if (Path == null) {
            return;
        }
        var options = {
            x: 0,
            y: 0,
            x1: 0,
            y1: 0,
            x2: 0,
            y2: 0,
            ex: 0,
            ey: 0,
            width: 0,
            height: 0,
            stroke: 'black',
            fill: 'transparent',
            strokeWidth: 1
        };
        $.extend(options, conf, true);
        var p0 = Path.data('p');
        options.x1 = 2 * p0.ex - p0.x2, options.x2 = options.x1;
        options.y1 = 2 * p0.ey - p0.y2, options.y2 = options.y1;

        Path.attr('d', `${Path.attr('d')} C ${options.x1} ${options.y1}, ${options.x2} ${options.y2}, ${options.ex} ${options.ey}`);
        //Path.data('l', [l1, l2]);
        Path.data('p', options);

        var iframe = document.getElementById('iframeSVG');
        var html = `<html><body>
            ${$(self.conf.target).prop('outerHTML')}
            </body></html>`;
        iframe.src = 'data:text/html;charset=utf-8,' + encodeURIComponent(html);
        //var resfreshData = {
        //    oldValue: [Number(Path.attr('data-width')), Number(Path.attr('data-height'))],
        //    newValue: [options.width, options.height]
        //}
        //var refreshEvent = new CustomEvent("refresh", { detail: resfreshData });
        // Dispatch the event.
        //Path[0].dispatchEvent(refreshEvent);
        return Path;
    };
    global.TorihikiSVGPathGuide = SVG;

})(jQuery, this);

(function ($, global) {
    var SVG = function () {
        this._initialize.apply(this, arguments);
        this.name = 'TorihikiSVGPath1';
    };

    SVG.prototype = Object.create(TorihikiSVGPath.prototype);
    SVG.prototype.add = function (conf) {
        var self = this;
        var options = {
            x: 0,
            y: 0,
            x1: 0,
            y1: 0,
            x2: 0,
            y2: 0,
            ex: 0,
            ey: 0,
            width: 0,
            height: 0,
            stroke: 'red',
            fill: 'transparent',
            strokeWidth: 1
        };
        $.extend(options, conf, true);

        var Path = $(`<path d="M ${options.x} ${options.y}" stroke="${options.stroke}" fill="${options.fill}" stroke-width="${options.strokeWidth}" />`);

        $(self.conf.target).append(Path);
        // Listen for the event.
        Path.bind('refresh', async function (evt) {
            console.log(evt);
            //if (Math.abs(evt.detail.newValue[0] - evt.detail.oldValue[0]) >= 4 || Math.abs(evt.detail.newValue[1] - evt.detail.oldValue[1]) >= 4) {
            //    $(this).remove();
            //    $(self.conf.target).append($(this));
            //}
        });
        self.curPath = Path;
        return Path;
    };
    SVG.prototype.edit = function (conf) {
        var self = this;
        var Path = self.curPath;
        if (Path == null) {
            return;
        }
        var options = {
            x: 0,
            y: 0,
            x1: 0,
            y1: 0,
            x2: 0,
            y2: 0,
            ex: 0,
            ey: 0,
            width: 0,
            height: 0,
            stroke: 'black',
            fill: 'transparent',
            strokeWidth: 1
        };
        $.extend(options, conf, true);

        Path.attr('d', `${Path.attr('d')} L ${options.ex} ${options.ey}`);

        var iframe = document.getElementById('iframeSVG');
        var html = `<html><body style="margin: 0;">
            ${$(self.conf.target).prop('outerHTML')}
            </body></html>`;
        iframe.src = 'data:text/html;charset=utf-8,' + encodeURIComponent(html);
        //var resfreshData = {
        //    oldValue: [Number(Path.attr('data-width')), Number(Path.attr('data-height'))],
        //    newValue: [options.width, options.height]
        //}
        //var refreshEvent = new CustomEvent("refresh", { detail: resfreshData });
        // Dispatch the event.
        //Path[0].dispatchEvent(refreshEvent);
        return Path;
    };
    global.TorihikiSVGPath1 = SVG;

})(jQuery, this);

(function ($, global) {
    var DIV = function () {
        this._initialize.apply(this, arguments);
    };

    var defaults = {
        target: undefined
    };

    DIV.prototype = {
        curPath: null,
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

        add: function (conf) {
            var self = this;
            var options = {
                x: 0,
                y: 0,
                x1: 0,
                y1: 0,
                x2: 0,
                y2: 0,
                ex: 0,
                ey: 0,
                width: 0,
                height: 0,
                stroke: 'black',
                fill: 'transparent',
                strokeWidth: 1
            };
            $.extend(options, conf, true);

            var Path = $(`<div style="position: absolute;left: ${options.x}px; top: ${options.y}px; width: ${options.width}px; height: ${options.height}px; border: ${options.strokeWidth}px solid ${options.stroke};background-color: ${options.fill}" >
            
            </div>`);
            Path.data('p', options);

            $(self.conf.target).append(Path);
            self.curPath = Path;
            return Path;
        },

        edit: function (conf) {
            var self = this;
            var Path = self.curPath;

            if (Path == null) {
                return;
            }
            var options = {
                x: 0,
                y: 0,
                x1: 0,
                y1: 0,
                x2: 0,
                y2: 0,
                ex: 0,
                ey: 0,
                width: 0,
                height: 0,
                stroke: 'black',
                fill: 'transparent',
                strokeWidth: 1
            };
            $.extend(options, conf, true);
            var p0 = Path.data('p');
            options.x1 = 2 * p0.ex - p0.x2, options.x2 = options.x1;
            options.y1 = 2 * p0.ey - p0.y2, options.y2 = options.y1;

            Path.css('left', `${options.width >= 0 ? options.x : options.x1}px`);
            Path.css('top', `${options.height >= 0 ? options.y : options.y1}px`);

            Path.css('width', `${options.width >= 0 ? options.width : -1 * options.width}px`);
            Path.css('height', `${options.height >= 0 ? options.height : -1 * options.height}px`);

            Path.css('border', `${options.strokeWidth}px solid ${options.stroke}`);
            Path.css('background-color', options.fill);

            Path.data('p', options);

            return Path;
        },
    };
    global.TorihikiDIV = DIV;

})(jQuery, this);

var notebookelementary01 = {

    init: function () {
        let self = this;

        // çƒåüçıîªíf
        //self.research = TorihikiUtils.initPageSession(sessionKey);

        // ÉCÉxÉìÉgê›íË
        this.setEvent();

        const canvas = document.getElementById("myCanvas");
        const ctx = canvas.getContext("2d");

        // Start a new Path
        ctx.beginPath();
        ctx.moveTo(0, 0);
        ctx.lineTo(300, 150);

        // Draw the Path
        ctx.stroke();
    },
    setEvent: function () {

    }
};

class PathAction extends Action {
    svg;
    svgGuide;
    content;
    actionType;
    p;
    pathTypes;
    constructor(svgTarget, divTarget, pathTypes) {
        super();
        this.svg = new TorihikiSVGPath1({
            target: svgTarget,
        });
        this.svgGuide = new TorihikiSVGPathGuide({
            target: svgTarget,
        });
        this.content = new TorihikiDIV({
            target: divTarget,
        })
        this.actionType = Enum.ActionTypes.select;
        this.pathTypes = [PathTypes.L, PathTypes.C]
        this.p = null;
    }

    doEvent(evt) {
        super.doEvent(evt);
        if (evt.type === 'pointerdown') {
            //this.actionType = Enum.ActionTypes.select;
            this.p = new Selection(evt.offsetX, evt.offsetY);
            const cPath = this.content.add({
                x: this.p.x,
                y: this.p.y,
                x1: this.p.x,
                y1: this.p.y,
                x2: this.p.x,
                y2: this.p.y,
                ex: this.p.x,
                ey: this.p.y,
            });
            const svgPath = this.svg.add({
                x: this.p.x,
                y: this.p.y,
                x1: this.p.x,
                y1: this.p.y,
                x2: this.p.x,
                y2: this.p.y,
                ex: this.p.x,
                ey: this.p.y,
            });
            const svgPathGuide = this.svgGuide.add({
                x: this.p.x,
                y: this.p.y,
                x1: this.p.x,
                y1: this.p.y,
                x2: this.p.x,
                y2: this.p.y,
                ex: this.p.x,
                ey: this.p.y,
            });

            BasicFunction.addHistory([cPath, svgPath]);
        } else if (evt.type === 'pointermove' || evt.type === 'pointerup') {
            if (this.p == null) {
                return;
            }
            this.p.eX = evt.offsetX;
            this.p.eY = evt.offsetY;
            const cPath = this.content.edit({
                x: this.p.x,
                y: this.p.y,
                x1: 0,
                y1: 0,
                x2: 0,
                y2: 0,
                width: this.p.width,
                height: this.p.height,
                ex: this.p.eX,
                ey: this.p.eY,
            });
            const svgPath = this.svg.edit({
                x: this.p.x,
                y: this.p.y,
                x1: 0,
                y1: 0,
                x2: 0,
                y2: 0,
                width: this.p.width,
                height: this.p.height,
                ex: this.p.eX,
                ey: this.p.eY,
            });
            const svgPathGuide = this.svgGuide.edit({
                x: this.p.x,
                y: this.p.y,
                x1: 0,
                y1: 0,
                x2: 0,
                y2: 0,
                width: this.p.width,
                height: this.p.height,
                ex: this.p.eX,
                ey: this.p.eY,
            });
        //    var iframe = document.getElementById('iframeSVG');
        //    var html = `<html><body style="margin: 0;">
        //${$(this.svgTarget).prop('outerHTML')}
        //</body></html>`;
        //    iframe.src = 'data:text/html;charset=utf-8,' + encodeURIComponent(html);
            BasicFunction.editHistory([cPath, svgPath]);
        }
        if (evt.type === 'pointerup' || evt.type === 'pointerout') {
            this.p = null;
            console.log(null);
        }

    };

};
const PathTypes = {
    L: 1,
    C: 2
}
class Selection {
    x = 0;
    y = 0;
    eX = 0;
    eY = 0;

    get height() {
        return this.eY - this.y;
    };
    get width() {
        return this.eX - this.x;
    }
    constructor(x, y) {
        this.x = x;
        this.y = y;
    }
};
$(document).ready(function () {
    notebookelementary01.init();
    var action = new PathAction($('#mySVG'), $('#myContent'));
    var HTMLElementEvents = new TorihikiHtmlEvent(action, $('#myCanvas'), {});
});
