/**
 * Functions.js v1.0: General javascript functions used throughout Wolfram's websites
 *
 * This file requires flashobject.js in order to be of any use. 
 */
var iedom=document.all||document.getElementById;
var hightraffic=0;

function writit(text,id)
{
	if (document.getElementById)
	{
		if (!document.all) {
			//NS6 code - works with Firefox
			dynamiccontentNS6(id,text);
		} else {
			x = document.getElementById(id);
			x.innerHTML = '';
			x.innerHTML = text;
		}
	}
	else if (document.all)
	{
		x = document.all[id];
		x.innerHTML = text;
	}
	else if (document.layers)
	{
		x = document.layers[id];
		text2 = '<P CLASS="body">' + text + '</P>';
		x.document.open();
		x.document.write(text2);
		x.document.close();
	}
}

function dynamiccontentNS6(elementid,content){
	if (document.getElementById && !document.all){
		var rng = document.createRange();
		var el = document.getElementById(elementid);
		rng.setStartBefore(el);
		var htmlFrag = rng.createContextualFragment(content);
		while (el.hasChildNodes())
			el.removeChild(el.lastChild);
		el.appendChild(htmlFrag);
	}
}

// the code below adds a Flash script into a DIV, replacing the DIV's content
function loadDivWithFlash(divId, flashPath, flashName, flashWidth, flashHeigth, flashVersionRequired, flashBgColor, flashQuality, flashTransparency) {
   var fo = new FlashObject(flashPath, flashName, flashWidth, flashHeigth, flashVersionRequired, flashBgColor);

   if (flashQuality) {fo.addParam("quality", flashQuality);}
   if (flashTransparency) {fo.addParam("wmode","transparent");}

   //added by jeremyd
   fo.addParam("allowScriptAccess","sameDomain");
   fo.addParam("loop","false");
   fo.addParam("menu","false");
   fo.addParam("scale","noscale");
   fo.addParam("salign","lt");
   //end added code
  
   writit(fo.getFlashHTML(), divId);
}

// this code if for wolfram demonstrations only
function loadFlash(flashwidth,flashheight,nbname) {

	var thePath=window.location.href;
	
	//find root web directory of html page (remove any trailing filenames that contail .html)
	str = thePath;
	re = /\/[^$\/]*\.html$/i;
	found =str.match(re);
	theurl=str; if (found != null) {theurl=(str.split(found))[0]+"/"};

	//hack for preview.html (adjust url to true location of page)
	if (str.match("/preview\.html/") != null) {theurl="/data/"+(str.split("/preview\.html/"))[1]+"/";}
	else if (str.match("/preview\.html\?") != null) {theurl="/data/"+(str.split("/preview\.html\?"))[1]+"/";}
	thePath=theurl;

	var playerwidth=(flashwidth+10)+"";
	var playerheight=(flashheight+34)+"";
	var flashpath="/includes/playback.swf?src="+thePath+"flash.swf&srcwidth="+flashwidth+"&srcheight="+flashheight+"&preimage="+thePath+"preloadGraphic.jpg&notebookurl="+thePath+nbname+".nbp&hightraffic="+hightraffic;
	if (flashinstalled > 0 && flashversion >= 7 ) {
	loadDivWithFlash("mainFlash",flashpath, "mainFlash", playerwidth, playerheight, "7", "#ffffff","high","transparent");} else {
	writit("<a href='"+nbname+".nbp'><img src='"+thePath+"preloadGraphic.jpg' style='margin:14px 0px 14px 5px;' border=0><br><a href='http://adobe.com/go/getflash/ ' target=new><img src='/images/flashrequired.gif' style='margin:0px 0px 24px 7px;' border=0></a>", "mainFlash");}
}


function newpop(url,theWidth,theHeight) {
  var strHref = window.location.href;
  var newUrl = url;
  if(strHref.indexOf("/preview.html?") > -1 || strHref.indexOf("/preview.html/") > -1){
    newUrl = "/data/" + strHref.substr(strHref.indexOf("/preview.html")+14) + "/" + url;   
  } 
  win=window.open(newUrl,'Screenshot','width='+theWidth+',height='+theHeight+',resizable=yes,scrollbars=yes,status=no,toolbar=no,menubar=no,titlebar=no,statusbar=no');
  if (win.focus) { win.focus(); }
}

function addThumbnail() {
  var strHref = window.location.href;
  if(strHref.indexOf("/preview.html?") > -1 || strHref.indexOf("/preview.html/") > -1){
    var titleid = strHref.substr(strHref.indexOf("/preview.html")+26);   
  } else {
    var titleidarr = strHref.split('/');
	var titleid = titleidarr[titleidarr.length - 2];	
  }
	title = document.title;
  window.open('/ext/addthumbnail.jsp?title=' + title + '&titleid=' + titleid, 'Thumbnail', 'toolbar=0,scrollbars=1,location=0,statusbar=0,menubar=0,resizable=0,width=640,height=600');
}

function openInAdmin(id) {
  var idVals = id.split('/');
  if(idVals[0].match(/\d+/)) {
    window.location="/admin/view.jsp?id=" + idVals[0];
  }
}

function openDetails(id) {
  window.location="/int/details.jsp?id=" + id + "&limit=10";
}

function checkForDefaultInput(fieldId, defaultInput) {
    var fieldContent;
	var w3 = document.getElementById ? 1 : 0;
	var ie = document.all ? 1 : 0;
    if (w3) {
        fieldContent = document.getElementById(fieldId).value;
    } else if (ie) {
        fieldContent = eval("document.all." + fieldId + ".value");
    } else {
        fieldContent = eval("document.layers." + fieldId + ".value");
    }
    return fieldContent == defaultInput;
}

function initTextInput(element, action, text, cssname, checkForDefault) {
    var txt = text;
    if (text == null || text == "") {
        txt = "SEARCH";
    }
    if (element != null) {
        switch (action) {
          case "clear":
            cl = cssname;
            if (cssname == null) {
                cl = "searchinput-on";
            }
            if (element.value == txt) {
                element.value = "";
                element.className = cl;
            }
            break;
          case "setDefault":
            cl = cssname;
            if (cssname == null) {
                cl = "searchinput";
            }
            if (element.value == "") {
                element.value = txt;
                element.className = cl;
            } else if(element.value != txt) {
		 element.className = "searchinput-on";
	    }
            break;
          default:
            break;
        }
    }
}

$(document).ready(function(){
  $(".extl").click(function() {
    $.get("/ext.html",{ url: $(this).attr("href") });
  });
});
	  
