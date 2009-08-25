var locurl;

function downloadNotebook(url) {
  //Get location as string
  var loc = document.location + '';

  //replace preview with data for internal
  loc = loc.replace(/preview\.html\?/, 'data/');
  loc = loc.replace(/preview\.html/, 'data');
  if(readCookie('gotmathematica') != null || url.match(/.*-source\.nb/) != null) {
    //next release of mathematica player we'll need to change this.  
  	if(readCookie('gotmathematica') == 'true' && url.matches("*-source.nb") == null) {
  		window.open('/player_upgrade.html?url=' + loc + '/' + url,'PlayerPopup','toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=no,resizable=no,width=456,height=284,left=50,top=50');
  	} else {
    	document.location = loc + '/' + url;
    }
  } else {
			var box =  document.getElementById("outerBorder");
		  	var downloadbutton = $(".demodownloadbutton");
		  	box.style.display = "block";
		  	box.style.top = (downloadbutton[0].offsetTop - 10) + "px";
		  	box.style.left = (downloadbutton[0].offsetLeft + 210) + "px";
		  	Drag.init(box);
				locurl = loc + '/' + url;
  }   
}

function alreadyInstalled() {
	var date = new Date();
    createCookie('gotmathematica',date.getTime(),365);

		if (document.getElementById("outerBorder")) {
      document.getElementById("outerBorder").style.display = "none";
			document.location = locurl;
    } else {
			opener.location.href = getURLParam('url');
      window.close();
    }
		return false;
}

function gotoMathematica6() {
    window.open('http://www.wolfram.com/products/mathematica/','Mathematica6Info','toolbar=yes,location=yes,directories=yes,status=yes,menubar=yes,scrollbars=yes,resizable=yes');
		window.close();
}

function downloadPlayer() {
    window.open('http://www.wolfram.com/products/player/download.cgi','MathematicaPlayerDownload','toolbar=yes,location=yes,directories=yes,status=yes,menubar=yes,scrollbars=yes,resizable=yes');
		if (document.getElementById("outerBorder")) {
	    document.getElementById("outerBorder").style.display = "none"
	  } else {
		  window.close();
	  } 
		return false;
}

function createCookie(name,value,days) {
	if (days) {
		var date = new Date();
		date.setTime(date.getTime()+(days*24*60*60*1000));
		var expires = "; expires="+date.toGMTString();
	}
	else var expires = "";
	document.cookie = name+"="+value+expires+"; path=/; domain=.wolfram.com;";
}

function readCookie(name) {
	var nameEQ = name + "=";
	var ca = document.cookie.split(';');
	for(var i=0;i < ca.length;i++) {
		var c = ca[i];
		while (c.charAt(0)==' ') c = c.substring(1,c.length);
		if (c.indexOf(nameEQ) == 0) return c.substring(nameEQ.length,c.length);
	}
	return null;
}

function getURLParam(strParamName){
  var strReturn = "";
  var strHref = window.location.href;
  if ( strHref.indexOf("?") > -1 ){
    var strQueryString = strHref.substr(strHref.indexOf("?"))
    var strQueryLower = strQueryString.toLowerCase();
    var aQueryString = strQueryString.split("&");
    var aLower = strQueryLower.split("&");
    for ( var iParam = 0; iParam < aLower.length; iParam++ ){
      if (aLower[iParam].indexOf(strParamName + "=") > -1 ){
        var aParam = aQueryString[iParam].split("=");
        strReturn = aParam[1];
        break;
      }
    }
  }
  return strReturn;
}

function isLinux(){
	if(navigator.userAgent.indexOf("Linux") != -1 || navigator.appVersion.indexOf("Linux") != -1)
	  return true;
	return false;
	
}

$(document).ready(function(){
	$('.NoteContinue').click(function() {
		$('.NoteContinue').parent().after('<div class="NotePlease">Please make a selection.</div>');
		$('.NoteContinue').unbind("click");
	});
	$(".NoteChoose input").bind("click change",function() { 
		choose = $(this).val();
		$('.NoteContinue').unbind('click');
		$('.NoteContinue').css('background-position','0 0').click(function() { 
			if(choose == 'dl') {
				downloadPlayer();
			} else if (choose == 'alreadyhave') {
				alreadyInstalled();
			}
		}); 
		$('.NotePlease').remove();
	});
});
