var ppLegal = 'PagePeel : Copyright Visual Steel, all rights reserved (http://visualsteel.net)'; // v5.4.6

if (!ppCount) {
    var ppCount = true;
    var ppLck = Math.floor(Math.random() * 99999999);
    var ppTag = new Image(0, 0);
    var ppImp = 'http://ad.doubleclick.net/ad/N5744.119726.8874489406321/B3464344.13;sz=1x1;ord=' + ppLck + '?';
    var ppName = 'emb_ti_090824';
    var ppPath = 'http://i.cmpnet.com/ads/graphics/as5/pagepeel/';
    var clickstream = escape(clickstream);
    var userAgent = navigator.userAgent.toLowerCase();
    var appName = navigator.appName.toLowerCase();
    if (userAgent.indexOf('opera') != -1) {
        var ppCkop = true
    }
    if (userAgent.indexOf('nd') != -1) {
        var ppCkwn = true
    }
    if (appName.indexOf('ro') != -1 && !ppCkwn) {
        var ppTerm = true
    }
    if (appName.indexOf('ro') != -1 && ppCkwn && userAgent.indexOf('3.1') == -1) {
        var ppCkfs = true
    }
    if (!ppTerm) {
        var ppVars = 'clickstream=' + clickstream + '&ckop=' + ppCkop + '&legal=' + escape(ppLegal);
        var ppObject = '<style type="text/css"> #pp_l{position:absolute;top:0px;width:100px;height:75px;right:0px;z-index:999999;} </style><div id="pp_l"></div>';
        if (ppCkfs) {
            ppObject = ppObject + '<script language=\"VBScript\"\>\n' + 'On Error Resume Next\n' + 'Sub pObj_FSCommand(ByVal command, ByVal args)\n' + 'Call pp_fs(command, args)\n' + 'End Sub\n' + '</script\>\n';
        }
        var ppCont = '<object classid="clsid:d27cdb6e-ae6d-11cf-96b8-444553540000" codebase="http://fpdownload.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=8,0,0,0" id="pObj" width="100%" height="100%"><param name="allowScriptAccess" value="always" /><param name="movie" value="' + ppPath + ppName + '_pp.swf?keyLock=' + ppLck + '" /><param name="wmode" value="transparent" /><param name="FlashVars" value="' + ppVars + '" /><embed src="' + ppPath + ppName + '_pp.swf?keyLock=' + ppLck + '" wmode="transparent" width="100%" height="100%" id="pObj" name="pObj" flashvars="' + ppVars + '" allowscriptaccess="always" type="application/x-shockwave-flash" pluginspage="http://www.macromedia.com/go/getflashplayer" /></object>';
        document.write(ppObject);
        if (ppCkfs) {
            document.getElementById('pp_l').style.display = 'inline'
        }
        if (ppImp) {
            ppTag.src = ppImp
        }
        document.getElementById('pp_l').innerHTML = ppCont;
    }
}
function pp_call(ppVA, ppVB, ppVC, ppVD, ppVE, ppVF, ppVG, ppVH, ppVI, ppVJ, ppVK, ppVL, ppVM) {
    ppP1 = eval(ppVA + ppVE + 'ppVC');
    ppP2 = eval(ppVA + ppVF + 'ppVD');
    if (ppCkfs && eval(ppVK + '.length')) {
        for (var ppFcount = 0; ppFcount < eval(ppVK + '.length'); ppFcount++) {
            for (var ppSfcount = 0; ppSfcount < eval(ppVK + '[ppFcount].length'); ppSfcount++) {
                if (eval(ppVK + '[ppFcount]' + ppVL + '[ppSfcount]' + ppVM)) {
                    ppV2 = eval(ppVK + '[ppFcount]' + ppVL + '[ppSfcount]' + ppVG + 'ppVH')
                }
            }
        }
    }
    if (ppCkwn && !ppCkfs && eval(ppVI + '(ppVJ).length')) {
        for (var ppIcount = 0; ppIcount < eval(ppVI + '(ppVJ).length'); ppIcount++) {
            eval(ppVI + '(ppVJ)[ppIcount]' + ppVG + 'ppVH')
        }
    }
    ppP1 = null;
    ppP2 = null;
    ppV1 = null;
}
function pp_fs(ppC, ppA) {
    var obj_pp = ppCkfs ? document.all.pObj : document.pObj;
    var ppAs = ppA.split(',');
    if (ppC == 'pp_call') {
        pp_call(ppAs[0], ppAs[1], ppAs[2], ppAs[3], ppAs[4], ppAs[5], ppAs[6], ppAs[7], ppAs[8], ppAs[9], ppAs[10], ppAs[11], ppAs[12])
    }
}