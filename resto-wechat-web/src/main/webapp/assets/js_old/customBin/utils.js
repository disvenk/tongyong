$(document).ready(function() {
    $(".dialog-close").click(function() {
        $(this).parents(".custom-dialog").hide()
    })
});
var loading_div_default = $("<div></div>");
loading_div_default.css({
    margin: 0,
    padding: 0,
    position: "absolute",
    top: 0,
    height: "100%",
    width: "100%",
    background: "rgba(0,0,0,0.3)",
    textAlign: "center",
    color: "#FFF",
    paddingTop: "20%",
    cursor: "wait",
    zIndex: 99999999
});
var defaultDivClass = "utils-loading-div-class";
loading_div_default.addClass(defaultDivClass);
loading_div_default.html("<img src='assets/img/loadding.gif' />");
function loading(t) {
    var e = loading_div_default.clone();
    e.height($(t).height());
    $(t).append(e)
}
function loaded(t) {
    $(t).find("." + defaultDivClass).remove()
}
function showMessage(t, e) {
    var n = $("<div>").css({
        position: "fixed",
        zIndex: 99999999,
        padding: "15px",
        top: "50%",
        width: "100%",
        textAlign: "center"
    });
    var i = $("<span>").css({
        background: "rgba(0,0,0,.5)",
        color: "#fff",
        padding: "10px 30px",
        borderRadius: "3px"
    });
    i.text(t);
    n.html(i);
    n.appendTo("body");
    setTimeout(function() {
        n.animate({
            opacity: 0
        },
        500, 
        function() {
            n.remove()
        })
    },
    e || 800)
}
$.fn.setAjaxParam = function(t) {
    $(this).find("*[data-key]").each(function() {
        var e = $(this);
        var n = e.data("type");
        var i = e.data("key");
        var a = t[i];
        switch (n) {
        case "IMG":
            try {
                if (a.indexOf("http://") == -1 && a.indexOf("https://") == -1) {
                    a = IMAGE_SERVICE + a
                }
            } catch(r) {
                a = ""
            }
            e.attr("src", a);
            break;
        case "DATE":
            var s = new Date(a);
            var o = e.data("format") || "yyyy-MM-dd hh:mm";
            var d = s.format(o);
            e.html(d);
            break;
        default:
            if (a == undefined) {
                a = ""
            }
            e.html(a);
            break
        }
    })
};
function cAjax(t, e, n, i) {
    if (!i) {
        i = function(t, e, n) {
            log.error(t, e, n);
            showMessage("出错了！" + t + e + n)
        }
    }
    $.ajax({
        type: "post",
        url: t,
        data: e,
        async: true,
        success: n,
        error: i
    })
}
function HashMap() {
    var t = 0;
    var e = new Object;
    this.put = function(n, i) {
        if (!this.containsKey(n)) {
            t++
        }
        e[n] = i
    };
    this.get = function(t) {
        return this.containsKey(t) ? e[t] : null
    };
    this.remove = function(n) {
        if (this.containsKey(n) && delete e[n]) {
            t--
        }
    };
    this.containsKey = function(t) {
        return t in e
    };
    this.containsValue = function(t) {
        for (var n in e) {
            if (e[n] == t) {
                return true
            }
        }
        return false
    };
    this.getValues = function() {
        var t = new Array;
        for (var n in e) {
            t.push(e[n])
        }
        return t
    };
    this.getKeys = function() {
        var t = new Array;
        for (var n in e) {
            t.push(n)
        }
        return t
    };
    this.getSize = function() {
        return t
    };
    this.clear = function() {
        t = 0;
        e = new Object
    }
}
function getParam(t) {
    var e = new RegExp("(^|&)" + t + "=([^&]*)(&|$)");
    var n = window.location.search.substr(1).match(e);
    if (n != null) return unescape(n[2]);
    return null
}
Date.prototype.format = function(t) {
    var e = {
        "M+": this.getMonth() + 1,
        "d+": this.getDate(),
        "h+": this.getHours(),
        "m+": this.getMinutes(),
        "s+": this.getSeconds(),
        "q+": Math.floor((this.getMonth() + 3) / 3),
        S: this.getMilliseconds()
    };
    if (/(y+)/.test(t)) t = t.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var n in e) if (new RegExp("(" + n + ")").test(t)) t = t.replace(RegExp.$1, RegExp.$1.length == 1 ? e[n] : ("00" + e[n]).substr(("" + e[n]).length));
    return t
};