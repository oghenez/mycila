function createCookie(name,value,days) {
	if (days) {
		var date = new Date();
		date.setTime(date.getTime()+(days*24*60*60*1000));
		var expires = "; expires="+date.toGMTString();
	}
	else var expires = "";
	document.cookie = name+"="+value+expires+"; path=/";
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

function eraseCookie(name) {
	createCookie(name,"",-1);
}

function initSelect()
{
	var theSelect = document.getElementById("chooselang");

    if (theSelect) {
        theSelect.changed = false;
        theSelect.onfocus = selectFocused;
        theSelect.onchange = selectChanged;
        theSelect.onkeydown = selectKeyed;
        theSelect.onclick = selectClicked;
    }
	
	return true;
}

function selectChanged(theElement)
{
	var theSelect;
	
	if (theElement && theElement.value)
	{
		theSelect = theElement;
	}
	else
	{
		theSelect = this;
	}
	
	if (!theSelect.changed)
	{
		return false;
	}

	document.sitelang.submit();
	
	return true;
}




function selectClicked()
{
	this.changed = true;
}




function selectFocused()
{
	this.initValue = this.value;
	
	return true;
}




function selectKeyed(e)
{
	var theEvent;
	var keyCodeTab = "9";
	var keyCodeEnter = "13";
	var keyCodeEsc = "27";
	
	if (e)
	{
		theEvent = e;
	}
	else
	{
		theEvent = event;
	}

	if ((theEvent.keyCode == keyCodeEnter || theEvent.keyCode == keyCodeTab) && this.value != this.initValue)
	{
		this.changed = true;
		selectChanged(this);
	}
	else if (theEvent.keyCode == keyCodeEsc)
	{
		this.value = this.initValue;
	}
	else
	{
		this.changed = false;
	}
	
	return true;
}


function toggle( idStr ) {
	var node = getNode( idStr );
	
	if( node == null ) return;

	if( node.style.display == "" || node.style.display == "none" )
    node.style.display = "block"; 
	else
	  node.style.display = "none"; 
	
}


function getNode( nodeId ) {
	if( document.getElementById ) {
		return document.getElementById( nodeId );
	}
	else if( document.all && document.all( nodeId ) ) {
		return document.all( nodeId );
	}
	else if( document.layers && document.layers[ nodeId ] ) {
		return document.layers[ nodeId ];
	}
	else {
		return false;
	}
}

window.onload = initSelect;
