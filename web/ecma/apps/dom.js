/* Diese Datei: dom.js

 Im HTML-Header wird diese ergänzte ECMAScript-Bibliothek
 dom.js eingebunden mit:

 <script type="text/javascript" src="dom.js></script>

 und z.B. aufgerufen mit
 dom.show("Hallo Welt");
 Wichtig! Siehe auch:
 */



var dom = (function (win) {"use strict";

    var doc = win.document,
        popup_win = null,
        gespeicherte_strings = {};

// === von app1 ===
    function show(str) {
        win.alert(str);
    }

    function popup(str, url, o) {
        o = o || {
            top: 20,
            left: 150,
            width: 800,
            height: 600
        };
        var flags = "resizable=yes,scrollbars=yes,top=" + o.top + ",left=" + o.left + ",width=" + o.width + ",height=" + o.height;
        if (popup_win) {
            popup_win.close();
        }
        popup_win = null;
        if (url) {
            popup_win = window.open(url, "popup", flags);
        } else { // global: popup_win
            popup_win = win.open("", "popup", flags);
            popup_win.document.write(str);
        }
        if (popup_win.opener) {
            popup_win.opener = self;
        }
        if (popup_win.focus) {
            popup_win.focus();
        }
        return popup_win;
    }

    function get_store_str(key) {
        return gespeicherte_strings[key];
    }

    function set_store_str(key, val) {
        gespeicherte_strings[key] = val;
    }

    function get_dom_str(id) {
        var node = doc.getElementById(id),
            tn = node.tagName.toLowerCase(),
            r = "";
        if (tn === 'input' || tn === 'textarea') {
            r = node.value;
        } else {
            r = node.innerHTML;
        }
        return r; // liefert String zurück
    }

    function set_dom_str(id, str) {
        var node = doc.getElementById(id),
            tn = node.tagName.toLowerCase();
        if (tn === 'input' || tn === 'textarea') {
            node.value = str;
        } else {
            node.innerHTML = str;
        }
    }
// === ende ===

// === von app2 ===

    function getAllLinkTags(str) {
        var links  = str.match(/<a([^>]*)>(.+?)<\/a>/g),
            output = '',
            i;
        for (i in links) {
            output += links[i] + '\n';
        }
        return output;
    }

    function getContentOfSpecificXMLTag(str, tagName) {
        var regex = new RegExp("<" + tagName + "[^>]*>(.+?)</" + tagName + ">","g"),
        match,
        output = '';
        while (match = regex.exec(str)) {
            output += match[1] + '\n';
        }
        return output;
    }

    function getJSONKey(str, keyName) {
        var regex = new RegExp('"' + keyName + '": *"?([\\w\\säÄöÖüÜ,]*)"?,?\\n','g'),
            match,
            output = '';
        while (match = regex.exec(str)) {
            output += match[1] + '\n';
        }
        return output;
    }


// === ende ===


// === neue Funktionen für node-List === 

// get_node_list(selektoren_str, parent_ele)
// hole zur Laufzeit die DOM-List-Elemente gemäß dem CSS-"selektoren_string"
// Bsp: var node_list = get_node_list(selektoren_str, parent_ele);
// Bsp: var node_list_ele0 = get_node_list(selektoren_str, parent_ele)[0];

    function get_node_list(selektoren_str, parent_ele) {
        return (parent_ele || doc).querySelectorAll(selektoren_str);
    }

// get_node_strings(node_list)
// hole zur Laufzeit aus einer DOM-Liste die Strings in Array-Elemente
// Bsp: var string_arr = get_node_strings(selektoren_str, parent_ele);
    function get_node_strings(node_list) {
        var i, tn, node, r = []; // r = Rückgabe-Array der innerHTML-Strings
        for (i = 0; i < node_list.length; i += 1) {
            node = node_list[i]; tn = node.tagName.toLowerCase();
            if (tn === 'input' || tn === 'textarea') { r.push(node.value); } else { r.push(node.innerHTML); }
        } return r;
    }

 //set_node_strings(nodes_list, string_arr)
 //... muß meist neu und angepaßt programmiert werden:
 //put Array-Strings als innerHTML in die DOM-Liste
 function set_node_strings(nodes_list, string_arr) {
     var i, tn, str, node;
     if (nodes_list.length < string_arr.length) { show("ERR: string_arr.length !== nodes_list.length"); return; }
     for (i = 0; i < string_arr.length; i += 1) {
         try {
             node = nodes_list[i]; tn = node.tagName.toLowerCase(); str = string_arr[i];
             if (tn === 'input' || tn === 'textarea') { node.value = str; } else { node.innerHTML = str; }
         } catch (e) { show("ERR: nodes_list, string_arr"); }
     }
 }

// === ende ===

    return { // Rückgabe-Objekt der Bib-Funktionen
        get_store_str:get_store_str,
        set_store_str:set_store_str,
        get_dom_str:get_dom_str,
        set_dom_str:set_dom_str,
        show:show,
        popup:popup,
        get_node_list:get_node_list,
        get_node_strings:get_node_strings,
        getAllLinkTags:getAllLinkTags,
        getContentOfSpecificXMLTag:getContentOfSpecificXMLTag,
        getJSONKey:getJSONKey,
        set_node_strings:set_node_strings,
        version: '2015'
    };
} (window));// ende von dom