var welt = (function (win) {
    "use strict";
    var doc = win.document,  //, gespeicherte_strings = {};
        ATTR_DEFAULT = { // default-Device-Context
            font: '10px sans-serif',
            fillStyle: 'transparent',
            strokeStyle: 'black', shadowColor: 'rgba(0, 0, 0, 0.0)',
            lineWidth: 1, miterLimit: 10, lineCap: 'butt', lineJoin: 'miter',
            shadowBlur: 0,
            shadowOffsetX: 0, shadowOffsetY: 0,
            textAlign: 'start', textBaseline: 'alphabetic',
            globalAlpha: 1, globalCompositeOperation: 'source-over',
            width: 400, height: 400,
            xmin: -100, ymin: -100, xmax: 100, ymax: 100
        },
        ATTR_BUF = {},    // Attribute-Buffer für alle Canvas
        ATTR = null,      // aktuelle Canvas-Attribute = ATTR_BUF.id_canvas,
        MOUSE_BUF = {},   // Maus-Buffer für alle Canvas
        MOUSE_XY = [],    // Maus-Buffer für i,j Koordinaten
        XY_MAX_LEN = 2*4-1, // = 2*nPkte - 1
        IS_DOWN = false,  // für multi-Maus-events
        IDC = null,       // aktuelle Canvas id="IDC'
        CTX = null;       // aktueller Canvas-Device-Context


    function show(str_oder_obj) { // Hilfsfunktion
        var k, keys, so = str_oder_obj, r = [];
        if (typeof so === 'object') {
            keys = Object.keys(so);
            for (k = 0; k < keys.length; k += 1) { r.push(keys[k] + ":" + so[keys[k]]); }
            win.alert(r.join('\n'));
        } else { win.alert(so); }
        //win.alert("ERR: " + str_oder_obj); }
    }

    /*function polygon_flaeche(koor_arr) {
        var j0, j1, k0, k1, dim = 2, h, A, az = 0, mx = 0, my = 0, ap = koor_arr, anz = ap.length, n = anz - dim + 1;
        if (!ap) { show("ERR:polygon_flaeche(koor_arr?)"); return null; }
        for (j0 = 0; j0 < n; j0 += dim) {
            k0 = j0 + dim; if (k0 > n) { k0 = 0; }
            j1 = j0 + 1; k1 = k0 + 1;
            h = (ap[j0] * ap[k1] - ap[j1] * ap[k0]);
            az += h;
            mx += (ap[j0] + ap[k0]) * h;
            my += (ap[j1] + ap[k1]) * h;
        } A = Math.sqrt(az * az) / 2;
        return { a: A, xs: mx / (6 * A), ys: my / (6 * A) };
    }*/


    // Bsp.: ATTR = get_ctx_atts_von()    // bewirkt ATTR = ATTR_DEFAULT
    // Bsp.: ATTR = get_ctx_atts_von(id); // bewirkt ATTR = ATTR_BUF[id]
    function get_ctx_atts_von(id) {
        var attr;
        if (!ATTR_BUF[id]) { ATTR_BUF[id] = Object.create(ATTR_DEFAULT); }

        attr = ATTR_BUF[id];
        //alert(attr.width);
        return attr;
    }

    // Bsp.: CTX = get_ctx_von(id);// bewirkt CTX = Canvas Context von id
    function get_ctx_von(id) {
        var el = doc.getElementById(id), ctx = el.getContext('2d'); return ctx;
    }

    // Bsp.: set_ctx_atts(); //setzt CTX mit ATTR_DEFAULT
    // Bsp.: set_ctx_atts({ lineWidth:5, strokeStyle: "#500",strokeStyle: '#0a0' });

    function set_ctx_atts(atts) {
        var i; if (!CTX) { show("ERR: kein glob CTX-Obj vorhanden"); }
        if (!atts) { atts = ATTR_DEFAULT; }
        for (i in atts) {
            if (atts.hasOwnProperty(i) && ATTR_DEFAULT.hasOwnProperty(i)) { CTX[i] = atts[i];}
        }
    }

    function add_event(id, evt_name, fn) {
        var m, el = doc.getElementById(id);
        if (!MOUSE_BUF[id]) { MOUSE_BUF[id] = Object.create({ is_down: false, xy: [] }); }
        m = MOUSE_BUF[id];

        el.addEventListener(evt_name, function (ev) {
            var a, el = doc.getElementById(id),
                rect = el.getBoundingClientRect(),
                ev = ev || window.event;
            m.id = id;
            m.type = ev.type;
            m.i = parseInt(ev.clientX - rect.left, 10);
            m.j = parseInt(ev.clientY - rect.top, 10);
            m.w = el.width;
            m.h = el.height; m.inside = !((m.i < 0) || (m.j < 0) || (m.i > m.w) || (m.j > m.h));
            if (m.type === 'mousemove' && m.is_down) { // && m.inside
                if (MOUSE_XY.length > XY_MAX_LEN) { MOUSE_XY.shift(); MOUSE_XY.shift(); }
                MOUSE_XY.push(m.i); MOUSE_XY.push(m.j);
            }
            MOUSE_BUF[id] = m;
            if (fn) { fn(ev); }
        }, false);
    }

    // Bsp.: init_canvas( "my_canvas_id", { fillStyle: "#f00" } );
    function init_canvas(id, atts) {
        var el, w, h, cssText = "margin:0;padding:0;border:none;";
        if (!MOUSE_BUF[id]) { MOUSE_BUF[id] = Object.create({ is_down: false, xy: [] }); }
        //if (!ATTR_BUF[id]) { ATTR_BUF[id] = Object.create(ATTR_DEFAULT); }

        IDC = id;                   // setzt  IDC  global
        CTX = get_ctx_von(IDC);     // setzt CTX  global
        ATTR = get_ctx_atts_von(IDC);  // setzt ATTR global
        for (var attrname in atts) { ATTR[attrname] = atts[attrname]; }
        if (ATTR) { set_ctx_atts(ATTR);}

        el = CTX.canvas;
        el.style.cssText = cssText;
        //el.style.cursor = 'pointer';
        //alert(atts.shadowBlur);
        if (ATTR.width > 0) { w = ATTR.width; el.width = w; el.style.width = w + 'px'; }
        if (ATTR.height > 0) { h = ATTR.height; el.height = h; el.style.height = h + 'px'; }
        w = el.width;
        h = el.height;

        add_event(id, "mousedown", function () { MOUSE_BUF[id].is_down = true; MOUSE_XY = null; MOUSE_XY = []; });
        add_event(id, "mouseup", function () { MOUSE_BUF[id].is_down = false;});
        add_event(id, "mouseout", function () { MOUSE_BUF[id].is_down = false;});

        CTX.clearRect(0, 0, w, h);
        //if (atts) { set_ctx_atts(atts); CTX.fillRect(0, 0, w, h); }
    }

    // ungeprüft:
    //function do_canvas_hidden_visible(id, hide) {
    //  var s, ctx = get_ctx_von(id);if (hide) { s = 'hidden'; } else { s = 'visible'; } ctx.canvas.visibility = s;
    //}

    function save() {
        CTX.save();
    }

    function beginPath() {
        CTX.beginPath();
    }

    function moveTo(x, y) {
        var i, j;
        if(isInt(x)) i = Math.round(ATTR.width * (x - ATTR.xmin)/(ATTR.xmax - ATTR.xmin))+0.5;
        else i = ATTR.width * (x - ATTR.xmin)/(ATTR.xmax - ATTR.xmin);

        if(isInt(y)) j = Math.round(ATTR.height - ATTR.height * (y - ATTR.ymin)/(ATTR.ymax - ATTR.ymin))+0.5;
        else j = ATTR.height - ATTR.height * (y - ATTR.ymin)/(ATTR.ymax - ATTR.ymin);

        CTX.moveTo(i, j)
    }

    function lineTo(x, y) {
        var i, j;
        if(isInt(x)) i = Math.round(ATTR.width * (x - ATTR.xmin)/(ATTR.xmax - ATTR.xmin))+0.5;
        else i = ATTR.width * (x - ATTR.xmin)/(ATTR.xmax - ATTR.xmin);

        if(isInt(y)) j = Math.round(ATTR.height - ATTR.height * (y - ATTR.ymin)/(ATTR.ymax - ATTR.ymin))+0.5;
        else j = ATTR.height - ATTR.height * (y - ATTR.ymin)/(ATTR.ymax - ATTR.ymin);

        CTX.lineTo(i, j)
    }

    function stroke() {
        CTX.stroke();
    }

    function restore() {
        CTX.restore();
    }

    function fillText(string, x, y) {
        var i, j;
        if(isInt(x)) i = Math.round(ATTR.width * (x - ATTR.xmin)/(ATTR.xmax - ATTR.xmin))+0.5;
        else i = ATTR.width * (x - ATTR.xmin)/(ATTR.xmax - ATTR.xmin);

        if(isInt(y)) j = Math.round(ATTR.height - ATTR.height * (y - ATTR.ymin)/(ATTR.ymax - ATTR.ymin))+0.5;
        else j = ATTR.height - ATTR.height * (y - ATTR.ymin)/(ATTR.ymax - ATTR.ymin);
        CTX.fillText(string, i, j);
    }

    function closePath() {
        CTX.closePath();
    }

    function fill() {
        CTX.fill();
    }


    function convertToWelt(i, j) {
        var x = ATTR.xmin + i*(ATTR.xmax - ATTR.xmin)/ATTR.width;
        var y = -((j*(ATTR.ymax - ATTR.ymin)/ATTR.height)-ATTR.ymax) ;

        return { x: x, y: y };
    }


    // helper function
    function isInt(n) {
        return n % 1 === 0;
    }

    return {
        init_canvas: init_canvas,
        get_ctx_von: get_ctx_von,
        add_event: add_event,
        //polygon_flaeche: polygon_flaeche, // berechne {a,xs,ys}
        MOUSE_BUF: MOUSE_BUF, // globale Maus-Daten
        save: save,
        beginPath: beginPath,
        moveTo: moveTo,
        lineTo: lineTo,
        stroke: stroke,
        restore: restore,
        fillText: fillText,
        closePath: closePath,
        fill: fill,
        convertToWelt: convertToWelt,
        version: '2015'
    };
} (window));