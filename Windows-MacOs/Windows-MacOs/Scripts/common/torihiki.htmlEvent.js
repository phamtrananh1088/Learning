(function ($, global) {
    var Enum = class {
        static ActionTypes = {
            none: 0,
            select: 1
        }
    };
    var Action = class {
        constructor() {
        }
        actionType = Enum.ActionTypes.select;
        p = new Selection(0, 0);
        select(evt) {
            if (evt.type === 'pointerdown') {
                this.actionType = Enum.ActionTypes.select;
                this.p = new Selection(evt.offsetX, evt.offsetY);
                console.log(this.p);
            } else if (evt.type === 'pointermove') {
                this.p.eX = evt.offsetX;
                this.p.eY = evt.offsetY;
            } else if (evt.type === 'pointerup') {
                this.p.eX = evt.offsetX;
                this.p.eY = evt.offsetY;
                this.actionType = Enum.ActionTypes.none;
                console.log(this.p);
            }

        }
    };
    var Selection = class {
        x = 0;
        y = 0;
        eX = 0;
        eY = 0;
        get height() {
            return eY - y;
        };
        get width() {
            return ex - x;
        }
        constructor(x, y) {
            this.x = x;
            this.y = y;
        }
    };
    var HtmlEvent = function () {
        this._initialize.apply(this, arguments);
    };

    var defaults = {
        target: undefined
    };

    HtmlEvent.prototype = {
        _initialize: function (conf) {
            this.conf = $.extend({}, defaults, conf);
            this._execute();
        },
        _execute: function () {
            var self = this;
            //var input = document.createElement('input');
            //input.type = "file";
            //input.id = "upload_file";
            //input.style = "display:none";
            ////input.accept = ".csv, application/vnd.openxmlformats-officedocument.spreadsheetml.sheet, application/vnd.ms-excel";
            //$(input).change(async function () {
            //    if (!$(input)[0].files[0]) {
            //        TorihikiUtils.hideLoading();
            //        return;
            //    }

            //    if ($(input)[0].files[0].size > self.conf.fileSizeLimit) {
            //        TorihikiMsg.alert("添付ファイルは10.0 MB以内でお願いします。");
            //        $(input).val('');                   
            //        return;
            //    }
            //    await self.conf.submit($(input)[0].files);
            //    $(input).val('');  
            //});

            //$(self.conf.uploadControl).click(function () {
            //    $(input).click();
            //});
            if (!self.conf.target) {
                throw new Error("target control is not set");
            };

            self.conf.target
                .bind('beforeinput', async function (evt) {
                    let res = await self.beforeinput(evt);
                    return res;
                })
                .bind('beforetoggle', async function (evt) {
                    let res = await self.beforetoggle(evt);
                    return res;
                })
                .bind("cancel", async function (evt) {
                    let res = await self.cancel(evt);
                    return res;
                })
                .bind("change", async function (evt) {
                    let res = await self.change(evt);
                    return res;
                })
                .bind("copy", async function (evt) {
                    let res = await self.copy(evt);
                    return res;
                })
                .bind("cut", async function (evt) {
                    let res = await self.cut(evt);
                    return res;
                })
                .bind("drag", async function (evt) {
                    let res = await self.drag(evt);
                    return res;
                })
                .bind("dragend", async function (evt) {
                    let res = await self.dragend(evt);
                    return res;
                })
                .bind("dragenter", async function (evt) {
                    let res = await self.dragenter(evt);
                    return res;
                })
                .bind("dragleave", async function (evt) {
                    let res = await self.dragleave(evt);
                    return res;
                })
                .bind("dragover", async function (evt) {
                    let res = await self.dragover(evt);
                    return res;
                })
                .bind("dragstart", async function (evt) {
                    let res = await self.dragstart(evt);
                    return res;
                })
                .bind("drop", async function (evt) {
                    let res = await self.drop(evt);
                    return res;
                })
                .bind("error", async function (evt) {
                    let res = await self.error(evt);
                    return res;
                })
                .bind("input", async function (evt) {
                    let res = await self.input(evt);
                    return res;
                })
                .bind("load", async function (evt) {
                    let res = await self.load(evt);
                    return res;
                })
                .bind("paste", async function (evt) {
                    let res = await self.paste(evt);
                    return res;
                })
                .bind("toggle", async function (evt) {
                    let res = await self.toggle(evt);
                    return res;
                })
                .bind("animationcancel", async function (evt) {
                    let res = await self.animationcancel(evt);
                    return res;
                })
                .bind("animationend", async function (evt) {
                    let res = await self.animationend(evt);
                    return res;
                })
                .bind("animationiteration", async function (evt) {
                    let res = await self.animationiteration(evt);
                    return res;
                })
                .bind("animationstart", async function (evt) {
                    let res = await self.animationstart(evt);
                    return res;
                })
                .bind("auxclick", async function (evt) {
                    let res = await self.auxclick(evt);
                    return res;
                })
                .bind("blur", async function (evt) {
                    let res = await self.blur(evt);
                    return res;
                })
                .bind("click", async function (evt) {
                    let res = await self.click(evt);
                    return res;
                })
                .bind("compositionend", async function (evt) {
                    let res = await self.compositionend(evt);
                    return res;
                })
                .bind("compositionstart", async function (evt) {
                    let res = await self.compositionstart(evt);
                    return res;
                })
                .bind("compositionupdate", async function (evt) {
                    let res = await self.compositionupdate(evt);
                    return res;
                })
                .bind("contextmenu", async function (evt) {
                    let res = await self.contextmenu(evt);
                    return res;
                })
                .bind("dblclick", async function (evt) {
                    let res = await self.dblclick(evt);
                    return res;
                })
                .bind("focus", async function (evt) {
                    let res = await self.focus(evt);
                    return res;
                })
                .bind("focusin", async function (evt) {
                    let res = await self.focusin(evt);
                    return res;
                })
                .bind("focusout", async function (evt) {
                    let res = await self.focusout(evt);
                    return res;
                })
                .bind("fullscreenchange", async function (evt) {
                    let res = await self.fullscreenchange(evt);
                    return res;
                })
                .bind("fullscreenerror", async function (evt) {
                    let res = await self.fullscreenerror(evt);
                    return res;
                })
                .bind("gotpointercapture", async function (evt) {
                    let res = await self.gotpointercapture(evt);
                    return res;
                })
                .bind("keydown", async function (evt) {
                    let res = await self.keydown(evt);
                    return res;
                })
                .bind("keyup", async function (evt) {
                    let res = await self.keyup(evt);
                    return res;
                })
                .bind("lostpointercapture", async function (evt) {
                    let res = await self.lostpointercapture(evt);
                    return res;
                })
                .bind("mousedown", async function (evt) {
                    let res = await self.mousedown(evt);
                    return res;
                })
                .bind("mouseenter", async function (evt) {
                    let res = await self.mouseenter(evt);
                    return res;
                })
                .bind("mouseleave", async function (evt) {
                    let res = await self.mouseleave(evt);
                    return res;
                })
                .bind("mousemove", async function (evt) {
                    let res = await self.mousemove(evt);
                    return res;
                })
                .bind("mouseout", async function (evt) {
                    let res = await self.mouseout(evt);
                    return res;
                })
                .bind("mouseover", async function (evt) {
                    let res = await self.mouseover(evt);
                    return res;
                })
                .bind("mouseup", async function (evt) {
                    let res = await self.mouseup(evt);
                    return res;
                })
                .bind("pointercancel", async function (evt) {
                    let res = await self.pointercancel(evt);
                    return res;
                })
                .bind("pointerdown", async function (evt) {
                    let res = await self.pointerdown(evt);
                    return res;
                })
                .bind("pointerenter", async function (evt) {
                    let res = await self.pointerenter(evt);
                    return res;
                })
                .bind("pointerleave", async function (evt) {
                    let res = await self.pointerleave(evt);
                    return res;
                })
                .bind("pointermove", async function (evt) {
                    let res = await self.pointermove(evt);
                    return res;
                })
                .bind("pointerout", async function (evt) {
                    let res = await self.pointerout(evt);
                    return res;
                })
                .bind("pointerover", async function (evt) {
                    let res = await self.pointerover(evt);
                    return res;
                })
                .bind("pointerup", async function (evt) {
                    let res = await self.pointerup(evt);
                    return res;
                })
                .bind("scroll", async function (evt) {
                    let res = await self.scroll(evt);
                    return res;
                })
                .bind("scrollend", async function (evt) {
                    let res = await self.scrollend(evt);
                    return res;
                })
                .bind("securitypolicyviolation", async function (evt) {
                    let res = await self.securitypolicyviolation(evt);
                    return res;
                })
                .bind("touchcancel", async function (evt) {
                    let res = await self.touchcancel(evt);
                    return res;
                })
                .bind("touchend", async function (evt) {
                    let res = await self.touchend(evt);
                    return res;
                })
                .bind("touchmove", async function (evt) {
                    let res = await self.touchmove(evt);
                    return res;
                })
                .bind("touchstart", async function (evt) {
                    let res = await self.touchstart(evt);
                    return res;
                })
                .bind("transitioncancel", async function (evt) {
                    let res = await self.transitioncancel(evt);
                    return res;
                })
                .bind("transitionend", async function (evt) {
                    let res = await self.transitionend(evt);
                    return res;
                })
                .bind("transitionrun", async function (evt) {
                    let res = await self.transitionrun(evt);
                    return res;
                })
                .bind("transitionstart", async function (evt) {
                    let res = await self.transitionstart(evt);
                    return res;
                })
                .bind("wheel", async function (evt) {
                    let res = await self.wheel(evt);
                    return res;
                })
                .bind("selectstart", async function (evt) {
                    let res = await self.selectstart(evt);
                    return res;
                })
                ;

            return false;
        },
        beforeinput: function (evt) {
            var self = this;
            return new Promise((re, rj) => {
                if (typeof self.conf.beforeinput !== 'undefined' && typeof self.conf.beforeinput === 'function') {
                    self.conf.beforeinput(evt, resolve, reject);
                } else {
                    //TODO
                    console.log('beforeinput', evt);
                    //evt.stopPropagation();
                    //evt.preventDefault();
                    re(true);
                }
            });
        },
        beforetoggle: function (evt) {
            var self = this;
            return new Promise((re, rj) => {
                if (typeof self.conf.beforetoggle !== 'undefined' && typeof self.conf.beforetoggle === 'function') {
                    self.conf.beforetoggle(evt, resolve, reject);
                } else {
                    //TODO
                    console.log('beforetoggle', evt);
                    //evt.stopPropagation();
                    //evt.preventDefault();
                    re(true);
                }
            });
        },
        cancel: function (evt) {
            var self = this;
            return new Promise((re, rj) => {
                if (typeof self.conf.cancel !== 'undefined' && typeof self.conf.cancel === 'function') {
                    self.conf.cancel(evt, resolve, reject);
                } else {
                    //TODO
                    console.log('cancel', evt);
                    //evt.stopPropagation();
                    //evt.preventDefault();
                    re(true);
                }
            });
        },
        change: function (evt) {
            var self = this;
            return new Promise((re, rj) => {
                if (typeof self.conf.change !== 'undefined' && typeof self.conf.change === 'function') {
                    self.conf.change(evt, resolve, reject);
                } else {
                    //TODO
                    console.log('change', evt);
                    //evt.stopPropagation();
                    //evt.preventDefault();
                    re(true);
                }
            });
        },
        copy: function (evt) {
            var self = this;
            return new Promise((re, rj) => {
                if (typeof self.conf.copy !== 'undefined' && typeof self.conf.copy === 'function') {
                    self.conf.copy(evt, resolve, reject);
                } else {
                    //TODO
                    console.log('copy', evt);
                    //evt.stopPropagation();
                    //evt.preventDefault();
                    re(true);
                }
            });
        },
        cut: function (evt) {
            var self = this;
            return new Promise((re, rj) => {
                if (typeof self.conf.cut !== 'undefined' && typeof self.conf.cut === 'function') {
                    self.conf.cut(evt, resolve, reject);
                } else {
                    //TODO
                    console.log('cut', evt);
                    //evt.stopPropagation();
                    //evt.preventDefault();
                    re(true);
                }
            });
        },
        drag: function (evt) {
            var self = this;
            return new Promise((re, rj) => {
                if (typeof self.conf.drag !== 'undefined' && typeof self.conf.drag === 'function') {
                    self.conf.drag(evt, resolve, reject);
                } else {
                    //TODO
                    console.log('drag', evt);
                    //evt.stopPropagation();
                    //evt.preventDefault();
                    re(true);
                }
            });
        },
        dragend: function (evt) {
            var self = this;
            return new Promise((re, rj) => {
                if (typeof self.conf.dragend !== 'undefined' && typeof self.conf.dragend === 'function') {
                    self.conf.dragend(evt, resolve, reject);
                } else {
                    //TODO
                    console.log('dragend', evt);
                    //evt.stopPropagation();
                    //evt.preventDefault();
                    re(true);
                }
            });
        },
        dragenter: function (evt) {
            var self = this;
            return new Promise((re, rj) => {
                if (typeof self.conf.dragenter !== 'undefined' && typeof self.conf.dragenter === 'function') {
                    self.conf.dragenter(evt, resolve, reject);
                } else {
                    //TODO
                    console.log('dragenter', evt);
                    //evt.stopPropagation();
                    //evt.preventDefault();
                    re(true);
                }
            });
        },
        dragleave: function (evt) {
            var self = this;
            return new Promise((re, rj) => {
                if (typeof self.conf.dragleave !== 'undefined' && typeof self.conf.dragleave === 'function') {
                    self.conf.dragleave(evt, resolve, reject);
                } else {
                    //TODO
                    console.log('dragleave', evt);
                    //evt.stopPropagation();
                    //evt.preventDefault();
                    re(true);
                }
            });
        },
        dragover: function (evt) {
            var self = this;
            return new Promise((re, rj) => {
                if (typeof self.conf.dragover !== 'undefined' && typeof self.conf.dragover === 'function') {
                    self.conf.dragover(evt, resolve, reject);
                } else {
                    //TODO
                    console.log('dragover', evt);
                    //evt.stopPropagation();
                    //evt.preventDefault();
                    re(true);
                }
            });
        },
        dragstart: function (evt) {
            var self = this;
            return new Promise((re, rj) => {
                if (typeof self.conf.dragstart !== 'undefined' && typeof self.conf.dragstart === 'function') {
                    self.conf.dragstart(evt, resolve, reject);
                } else {
                    //TODO
                    console.log('dragstart', evt);
                    //evt.stopPropagation();
                    //evt.preventDefault();
                    re(true);
                }
            });
        },
        drop: function (evt) {
            var self = this;
            return new Promise((re, rj) => {
                if (typeof self.conf.drop !== 'undefined' && typeof self.conf.drop === 'function') {
                    self.conf.drop(evt, resolve, reject);
                } else {
                    //TODO
                    console.log('drop', evt);
                    //evt.stopPropagation();
                    //evt.preventDefault();
                    re(true);
                }
            });
        },
        error: function (evt) {
            var self = this;
            return new Promise((re, rj) => {
                if (typeof self.conf.error !== 'undefined' && typeof self.conf.error === 'function') {
                    self.conf.error(evt, resolve, reject);
                } else {
                    //TODO
                    console.log('error', evt);
                    //evt.stopPropagation();
                    //evt.preventDefault();
                    re(true);
                }
            });
        },
        input: function (evt) {
            var self = this;
            return new Promise((re, rj) => {
                if (typeof self.conf.input !== 'undefined' && typeof self.conf.input === 'function') {
                    self.conf.input(evt, resolve, reject);
                } else {
                    //TODO
                    console.log('input', evt);
                    //evt.stopPropagation();
                    //evt.preventDefault();
                    re(true);
                }
            });
        },
        load: function (evt) {
            var self = this;
            return new Promise((re, rj) => {
                if (typeof self.conf.load !== 'undefined' && typeof self.conf.load === 'function') {
                    self.conf.load(evt, resolve, reject);
                } else {
                    //TODO
                    console.log('load', evt);
                    //evt.stopPropagation();
                    //evt.preventDefault();
                    re(true);
                }
            });
        },
        paste: function (evt) {
            var self = this;
            return new Promise((re, rj) => {
                if (typeof self.conf.paste !== 'undefined' && typeof self.conf.paste === 'function') {
                    self.conf.paste(evt, resolve, reject);
                } else {
                    //TODO
                    console.log('paste', evt);
                    //evt.stopPropagation();
                    //evt.preventDefault();
                    re(true);
                }
            });
        },
        toggle: function (evt) {
            var self = this;
            return new Promise((re, rj) => {
                if (typeof self.conf.toggle !== 'undefined' && typeof self.conf.toggle === 'function') {
                    self.conf.toggle(evt, resolve, reject);
                } else {
                    //TODO
                    console.log('toggle', evt);
                    //evt.stopPropagation();
                    //evt.preventDefault();
                    re(true);
                }
            });
        },
        animationcancel: function (evt) {
            var self = this;
            return new Promise((re, rj) => {
                if (typeof self.conf.animationcancel !== 'undefined' && typeof self.conf.animationcancel === 'function') {
                    self.conf.animationcancel(evt, resolve, reject);
                } else {
                    //TODO
                    console.log('animationcancel', evt);
                    //evt.stopPropagation();
                    //evt.preventDefault();
                    re(true);
                }
            });
        },
        animationend: function (evt) {
            var self = this;
            return new Promise((re, rj) => {
                if (typeof self.conf.animationend !== 'undefined' && typeof self.conf.animationend === 'function') {
                    self.conf.animationend(evt, resolve, reject);
                } else {
                    //TODO
                    console.log('animationend', evt);
                    //evt.stopPropagation();
                    //evt.preventDefault();
                    re(true);
                }
            });
        },
        animationiteration: function (evt) {
            var self = this;
            return new Promise((re, rj) => {
                if (typeof self.conf.animationiteration !== 'undefined' && typeof self.conf.animationiteration === 'function') {
                    self.conf.animationiteration(evt, resolve, reject);
                } else {
                    //TODO
                    console.log('animationiteration', evt);
                    //evt.stopPropagation();
                    //evt.preventDefault();
                    re(true);
                }
            });
        },
        animationstart: function (evt) {
            var self = this;
            return new Promise((re, rj) => {
                if (typeof self.conf.animationstart !== 'undefined' && typeof self.conf.animationstart === 'function') {
                    self.conf.animationstart(evt, resolve, reject);
                } else {
                    //TODO
                    console.log('animationstart', evt);
                    //evt.stopPropagation();
                    //evt.preventDefault();
                    re(true);
                }
            });
        },
        auxclick: function (evt) {
            var self = this;
            return new Promise((re, rj) => {
                if (typeof self.conf.auxclick !== 'undefined' && typeof self.conf.auxclick === 'function') {
                    self.conf.auxclick(evt, resolve, reject);
                } else {
                    //TODO
                    console.log('auxclick', evt);
                    //evt.stopPropagation();
                    //evt.preventDefault();
                    re(true);
                }
            });
        },
        blur: function (evt) {
            var self = this;
            return new Promise((re, rj) => {
                if (typeof self.conf.blur !== 'undefined' && typeof self.conf.blur === 'function') {
                    self.conf.blur(evt, resolve, reject);
                } else {
                    //TODO
                    console.log('blur', evt);
                    //evt.stopPropagation();
                    //evt.preventDefault();
                    re(true);
                }
            });
        },
        click: function (evt) {
            var self = this;
            return new Promise((re, rj) => {
                if (typeof self.conf.click !== 'undefined' && typeof self.conf.click === 'function') {
                    self.conf.click(evt, resolve, reject);
                } else {
                    //TODO
                    console.log('click', evt);
                    //evt.stopPropagation();
                    //evt.preventDefault();
                    re(true);
                }
            });
        },
        compositionend: function (evt) {
            var self = this;
            return new Promise((re, rj) => {
                if (typeof self.conf.compositionend !== 'undefined' && typeof self.conf.compositionend === 'function') {
                    self.conf.compositionend(evt, resolve, reject);
                } else {
                    //TODO
                    console.log('compositionend', evt);
                    //evt.stopPropagation();
                    //evt.preventDefault();
                    re(true);
                }
            });
        },
        compositionstart: function (evt) {
            var self = this;
            return new Promise((re, rj) => {
                if (typeof self.conf.compositionstart !== 'undefined' && typeof self.conf.compositionstart === 'function') {
                    self.conf.compositionstart(evt, resolve, reject);
                } else {
                    //TODO
                    console.log('compositionstart', evt);
                    //evt.stopPropagation();
                    //evt.preventDefault();
                    re(true);
                }
            });
        },
        compositionupdate: function (evt) {
            var self = this;
            return new Promise((re, rj) => {
                if (typeof self.conf.compositionupdate !== 'undefined' && typeof self.conf.compositionupdate === 'function') {
                    self.conf.compositionupdate(evt, resolve, reject);
                } else {
                    //TODO
                    console.log('compositionupdate', evt);
                    //evt.stopPropagation();
                    //evt.preventDefault();
                    re(true);
                }
            });
        },
        contextmenu: function (evt) {
            var self = this;
            return new Promise((re, rj) => {
                if (typeof self.conf.contextmenu !== 'undefined' && typeof self.conf.contextmenu === 'function') {
                    self.conf.contextmenu(evt, resolve, reject);
                } else {
                    //TODO
                    console.log('contextmenu', evt);
                    //evt.stopPropagation();
                    //evt.preventDefault();
                    re(true);
                }
            });
        },
        dblclick: function (evt) {
            var self = this;
            return new Promise((re, rj) => {
                if (typeof self.conf.dblclick !== 'undefined' && typeof self.conf.dblclick === 'function') {
                    self.conf.dblclick(evt, resolve, reject);
                } else {
                    //TODO
                    console.log('dblclick', evt);
                    //evt.stopPropagation();
                    //evt.preventDefault();
                    re(true);
                }
            });
        },
        focus: function (evt) {
            var self = this;
            return new Promise((re, rj) => {
                if (typeof self.conf.focus !== 'undefined' && typeof self.conf.focus === 'function') {
                    self.conf.focus(evt, resolve, reject);
                } else {
                    console.log('focus', evt);
                    //evt.stopPropagation();
                    //evt.preventDefault();
                    re(true);
                }
            });
        },
        focusin: function (evt) {
            var self = this;
            return new Promise((re, rj) => {
                if (typeof self.conf.focusin !== 'undefined' && typeof self.conf.focusin === 'function') {
                    self.conf.focusin(evt, resolve, reject);
                } else {
                    //TODO
                    console.log('focusin', evt);
                    //evt.stopPropagation();
                    //evt.preventDefault();
                    re(true);
                }
            });
        },
        focusout: function (evt) {
            var self = this;
            return new Promise((re, rj) => {
                if (typeof self.conf.focusout !== 'undefined' && typeof self.conf.focusout === 'function') {
                    self.conf.focusout(evt, resolve, reject);
                } else {
                    //TODO
                    console.log('focusout', evt);
                    //evt.stopPropagation();
                    //evt.preventDefault();
                    re(true);
                }
            });
        },
        fullscreenchange: function (evt) {
            var self = this;
            return new Promise((re, rj) => {
                if (typeof self.conf.fullscreenchange !== 'undefined' && typeof self.conf.fullscreenchange === 'function') {
                    self.conf.fullscreenchange(evt, resolve, reject);
                } else {
                    //TODO
                    console.log('fullscreenchange', evt);
                    //evt.stopPropagation();
                    //evt.preventDefault();
                    re(true);
                }
            });
        },
        fullscreenerror: function (evt) {
            var self = this;
            return new Promise((re, rj) => {
                if (typeof self.conf.fullscreenerror !== 'undefined' && typeof self.conf.fullscreenerror === 'function') {
                    self.conf.fullscreenerror(evt, resolve, reject);
                } else {
                    //TODO
                    console.log('fullscreenerror', evt);
                    //evt.stopPropagation();
                    //evt.preventDefault();
                    re(true);
                }
            });
        },
        gotpointercapture: function (evt) {
            var self = this;
            return new Promise((re, rj) => {
                if (typeof self.conf.gotpointercapture !== 'undefined' && typeof self.conf.gotpointercapture === 'function') {
                    self.conf.gotpointercapture(evt, resolve, reject);
                } else {
                    //TODO
                    console.log('gotpointercapture', evt);
                    //evt.stopPropagation();
                    //evt.preventDefault();
                    re(true);
                }
            });
        },
        keydown: function (evt) {
            var self = this;
            return new Promise((re, rj) => {
                if (typeof self.conf.keydown !== 'undefined' && typeof self.conf.keydown === 'function') {
                    self.conf.keydown(evt, resolve, reject);
                } else {
                    //TODO
                    console.log('keydown', evt);
                    //evt.stopPropagation();
                    //evt.preventDefault();
                    re(true);
                }
            });
        },
        keyup: function (evt) {
            var self = this;
            return new Promise((re, rj) => {
                if (typeof self.conf.keyup !== 'undefined' && typeof self.conf.keyup === 'function') {
                    self.conf.keyup(evt, resolve, reject);
                } else {
                    //TODO
                    console.log('keyup', evt);
                    //evt.stopPropagation();
                    //evt.preventDefault();
                    re(true);
                }
            });
        },
        lostpointercapture: function (evt) {
            var self = this;
            return new Promise((re, rj) => {
                if (typeof self.conf.lostpointercapture !== 'undefined' && typeof self.conf.lostpointercapture === 'function') {
                    self.conf.lostpointercapture(evt, resolve, reject);
                } else {
                    //TODO
                    console.log('lostpointercapture', evt);
                    //evt.stopPropagation();
                    //evt.preventDefault();
                    re(true);
                }
            });
        },
        mousedown: function (evt) {
            var self = this;
            return new Promise((re, rj) => {
                if (typeof self.conf.mousedown !== 'undefined' && typeof self.conf.mousedown === 'function') {
                    self.conf.mousedown(evt, resolve, reject);
                } else {
                    //TODO
                    console.log('mousedown', evt);
                    //evt.stopPropagation();
                    //evt.preventDefault();
                    re(true);
                }
            });
        },
        mouseenter: function (evt) {
            var self = this;
            return new Promise((re, rj) => {
                if (typeof self.conf.mouseenter !== 'undefined' && typeof self.conf.mouseenter === 'function') {
                    self.conf.mouseenter(evt, resolve, reject);
                } else {
                    //TODO
                    console.log('mouseenter', evt);
                    //evt.stopPropagation();
                    //evt.preventDefault();
                    re(true);
                }
            });
        },
        mouseleave: function (evt) {
            var self = this;
            return new Promise((re, rj) => {
                if (typeof self.conf.mouseleave !== 'undefined' && typeof self.conf.mouseleave === 'function') {
                    self.conf.mouseleave(evt, resolve, reject);
                } else {
                    //TODO
                    console.log('mouseleave', evt);
                    //evt.stopPropagation();
                    //evt.preventDefault();
                    re(true);
                }
            });
        },
        mousemove: function (evt) {
            var self = this;
            return new Promise((re, rj) => {
                if (typeof self.conf.mousemove !== 'undefined' && typeof self.conf.mousemove === 'function') {
                    self.conf.mousemove(evt, resolve, reject);
                } else {
                    //TODO
                    console.log('mousemove', evt);
                    //evt.stopPropagation();
                    //evt.preventDefault();
                    re(true);
                }
            });
        },
        mouseout: function (evt) {
            var self = this;
            return new Promise((re, rj) => {
                if (typeof self.conf.mouseout !== 'undefined' && typeof self.conf.mouseout === 'function') {
                    self.conf.mouseout(evt, resolve, reject);
                } else {
                    //TODO
                    console.log('mouseout', evt);
                    //evt.stopPropagation();
                    //evt.preventDefault();
                    re(true);
                }
            });
        },
        mouseover: function (evt) {
            var self = this;
            return new Promise((re, rj) => {
                if (typeof self.conf.mouseover !== 'undefined' && typeof self.conf.mouseover === 'function') {
                    self.conf.mouseover(evt, resolve, reject);
                } else {
                    //TODO
                    console.log('mouseover', evt);
                    //evt.stopPropagation();
                    //evt.preventDefault();
                    re(true);
                }
            });
        },
        mouseup: function (evt) {
            var self = this;
            return new Promise((re, rj) => {
                if (typeof self.conf.mouseup !== 'undefined' && typeof self.conf.mouseup === 'function') {
                    self.conf.mouseup(evt, resolve, reject);
                } else {
                    //TODO
                    console.log('mouseup', evt);
                    //evt.stopPropagation();
                    //evt.preventDefault();
                    re(true);
                }
            });
        },
        pointercancel: function (evt) {
            var self = this;
            return new Promise((re, rj) => {
                if (typeof self.conf.pointercancel !== 'undefined' && typeof self.conf.pointercancel === 'function') {
                    self.conf.pointercancel(evt, resolve, reject);
                } else {
                    //TODO
                    console.log('pointercancel', evt);
                    //evt.stopPropagation();
                    //evt.preventDefault();
                    re(true);
                }
            });
        },
        pointerdown: function (evt) {
            var self = this;
            return new Promise((re, rj) => {
                if (typeof self.conf.pointerdown !== 'undefined' && typeof self.conf.pointerdown === 'function') {
                    self.conf.pointerdown(evt, resolve, reject);
                } else {
                    //TODO
                    console.log('pointerdown', evt);
                    if (self.actionType === Enum.ActionTypes.select) {
                        self.action.select(evt)
                    }
                    //evt.stopPropagation();
                    //evt.preventDefault();
                    re(true);
                }
            });
        },
        pointerenter: function (evt) {
            var self = this;
            return new Promise((re, rj) => {
                if (typeof self.conf.pointerenter !== 'undefined' && typeof self.conf.pointerenter === 'function') {
                    self.conf.pointerenter(evt, resolve, reject);
                } else {
                    //TODO
                    console.log('pointereenter', evt);
                    //evt.stopPropagation();
                    //evt.preventDefault();
                    re(true);
                }
            });
        },
        pointerleave: function (evt) {
            var self = this;
            return new Promise((re, rj) => {
                if (typeof self.conf.pointerleave !== 'undefined' && typeof self.conf.pointerleave === 'function') {
                    self.conf.pointerleave(evt, resolve, reject);
                } else {
                    //TODO
                    console.log('pointerleave', evt);
                    //evt.stopPropagation();
                    //evt.preventDefault();
                    re(true);
                }
            });
        },
        pointermove: function (evt) {
            var self = this;
            return new Promise((re, rj) => {
                if (typeof self.conf.pointermove !== 'undefined' && typeof self.conf.pointermove === 'function') {
                    self.conf.pointermove(evt, resolve, reject);
                } else {
                    //TODO
                    console.log('pointermove', evt);
                    if (self.actionType === Enum.ActionTypes.select) {
                        self.action.select(evt)
                    }
                    //evt.stopPropagation();
                    //evt.preventDefault();
                    re(true);
                }
            });
        },
        pointerout: function (evt) {
            var self = this;
            return new Promise((re, rj) => {
                if (typeof self.conf.pointerout !== 'undefined' && typeof self.conf.pointerout === 'function') {
                    self.conf.pointerout(evt, resolve, reject);
                } else {
                    //TODO
                    console.log('pointerout', evt);
                    //evt.stopPropagation();
                    //evt.preventDefault();
                    re(true);
                }
            });
        },
        pointerover: function (evt) {
            var self = this;
            return new Promise((re, rj) => {
                if (typeof self.conf.pointerover !== 'undefined' && typeof self.conf.pointerover === 'function') {
                    self.conf.pointerover(evt, resolve, reject);
                } else {
                    //TODO
                    console.log('pointerover', evt);
                    //evt.stopPropagation();
                    //evt.preventDefault();
                    re(true);
                }
            });
        },
        pointerup: function (evt) {
            var self = this;
            return new Promise((re, rj) => {
                if (typeof self.conf.pointerup !== 'undefined' && typeof self.conf.pointerup === 'function') {
                    self.conf.pointerup(evt, resolve, reject);
                } else {
                    //TODO
                    console.log('pointerup', evt);
                    if (self.actionType === Enum.ActionTypes.select) {
                        self.action.select(evt)
                    }
                    //evt.stopPropagation();
                    //evt.preventDefault();
                    re(true);
                }
            });
        },
        scroll: function (evt) {
            var self = this;
            return new Promise((re, rj) => {
                if (typeof self.conf.scroll !== 'undefined' && typeof self.conf.scroll === 'function') {
                    self.conf.scroll(evt, resolve, reject);
                } else {
                    //TODO
                    console.log('scroll', evt);
                    //evt.stopPropagation();
                    //evt.preventDefault();
                    re(true);
                }
            });
        },
        scrollend: function (evt) {
            var self = this;
            return new Promise((re, rj) => {
                if (typeof self.conf.scrollend !== 'undefined' && typeof self.conf.scrollend === 'function') {
                    self.conf.scrollend(evt, resolve, reject);
                } else {
                    //TODO
                    console.log('scrollend', evt);
                    //evt.stopPropagation();
                    //evt.preventDefault();
                    re(true);
                }
            });
        },
        securitypolicyviolation: function (evt) {
            var self = this;
            return new Promise((re, rj) => {
                if (typeof self.conf.securitypolicyviolation !== 'undefined' && typeof self.conf.securitypolicyviolation === 'function') {
                    self.conf.securitypolicyviolation(evt, resolve, reject);
                } else {
                    //TODO
                    console.log('securitypolicyviolation', evt);
                    //evt.stopPropagation();
                    //evt.preventDefault();
                    re(true);
                }
            });
        },
        touchcancel: function (evt) {
            var self = this;
            return new Promise((re, rj) => {
                if (typeof self.conf.touchcancel !== 'undefined' && typeof self.conf.touchcancel === 'function') {
                    self.conf.touchcancel(evt, resolve, reject);
                } else {
                    //TODO
                    console.log('touchcancel', evt);
                    //evt.stopPropagation();
                    //evt.preventDefault();
                    re(true);
                }
            });
        },
        touchend: function (evt) {
            var self = this;
            return new Promise((re, rj) => {
                if (typeof self.conf.touchend !== 'undefined' && typeof self.conf.touchend === 'function') {
                    self.conf.touchend(evt, resolve, reject);
                } else {
                    //TODO
                    console.log('touchend', evt);
                    //evt.stopPropagation();
                    //evt.preventDefault();
                    re(true);
                }
            });
        },
        touchmove: function (evt) {
            var self = this;
            return new Promise((re, rj) => {
                if (typeof self.conf.touchmove !== 'undefined' && typeof self.conf.touchmove === 'function') {
                    self.conf.touchmove(evt, resolve, reject);
                } else {
                    //TODO
                    console.log('touchmove', evt);
                    //evt.stopPropagation();
                    //evt.preventDefault();
                    re(true);
                }
            });
        },
        touchstart: function (evt) {
            var self = this;
            return new Promise((re, rj) => {
                if (typeof self.conf.touchstart !== 'undefined' && typeof self.conf.touchstart === 'function') {
                    self.conf.touchstart(evt, resolve, reject);
                } else {
                    //TODO
                    console.log('touchstart', evt);
                    //evt.stopPropagation();
                    //evt.preventDefault();
                    re(true);
                }
            });
        },
        transitioncancel: function (evt) {
            var self = this;
            return new Promise((re, rj) => {
                if (typeof self.conf.transitioncancel !== 'undefined' && typeof self.conf.transitioncancel === 'function') {
                    self.conf.transitioncancel(evt, resolve, reject);
                } else {
                    //TODO
                    console.log('transitioncancel', evt);
                    //evt.stopPropagation();
                    //evt.preventDefault();
                    re(true);
                }
            });
        },
        transitionend: function (evt) {
            var self = this;
            return new Promise((re, rj) => {
                if (typeof self.conf.transitionend !== 'undefined' && typeof self.conf.transitionend === 'function') {
                    self.conf.transitionend(evt, resolve, reject);
                } else {
                    //TODO
                    console.log('transitionend', evt);
                    //evt.stopPropagation();
                    //evt.preventDefault();
                    re(true);
                }
            });
        },
        transitionrun: function (evt) {
            var self = this;
            return new Promise((re, rj) => {
                if (typeof self.conf.transitionrun !== 'undefined' && typeof self.conf.transitionrun === 'function') {
                    self.conf.transitionrun(evt, resolve, reject);
                } else {
                    //TODO
                    console.log('transitionrun', evt);
                    //evt.stopPropagation();
                    //evt.preventDefault();
                    re(true);
                }
            });
        },
        transitionstart: function (evt) {
            var self = this;
            return new Promise((re, rj) => {
                if (typeof self.conf.transitionstart !== 'undefined' && typeof self.conf.transitionstart === 'function') {
                    self.conf.transitionstart(evt, resolve, reject);
                } else {
                    //TODO
                    console.log('transitionstart', evt);
                    //evt.stopPropagation();
                    //evt.preventDefault();
                    re(true);
                }
            });
        },
        wheel: function (evt) {
            var self = this;
            return new Promise((re, rj) => {
                if (typeof self.conf.wheel !== 'undefined' && typeof self.conf.wheel === 'function') {
                    self.conf.wheel(evt, resolve, reject);
                } else {
                    //TODO
                    console.log('wheel', evt);
                    //evt.stopPropagation();
                    //evt.preventDefault();
                    re(true);
                }
            });
        },
        selectstart: function (evt) {
            var self = this;
            return new Promise((re, rj) => {
                if (typeof self.conf.selectstart !== 'undefined' && typeof self.conf.selectstart === 'function') {
                    self.conf.selectstart(evt, resolve, reject);
                } else {
                    //TODO
                    console.log('selectstart', evt);
                    //evt.stopPropagation();
                    //evt.preventDefault();
                    re(true);
                }
            });
        },
        xxx: function (evt) {
            var self = this;
            return new Promise((re, rj) => {
                if (typeof self.conf.xxx !== 'undefined' && typeof self.conf.xxx === 'function') {
                    self.conf.xxx(evt, resolve, reject);
                } else {
                    //TODO
                    console.log('xxx', evt);
                    //evt.stopPropagation();
                    //evt.preventDefault();
                    re(true);
                }
            });
        },
        actionType: Enum.ActionTypes.select,
        action: new Action(),
    };
    global.TorihikiHtmlEvent = HtmlEvent;

})(jQuery, this);