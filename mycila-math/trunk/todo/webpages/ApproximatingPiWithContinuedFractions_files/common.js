/* 
Code to show/hide source code within Demonstrations  
*/
function showhide(id) {
  var divElement = document.getElementsByName(id+'Group');
  for(i=0; i<divElement.length; i++) {
    if (divElement[i].className == id+'Group') {
      divElement[i].className = id+'Group_Hide';
    } else if (divElement[i].className == id+'Group_Hide') {
      divElement[i].className = id+'Group';
    } 
  }
  img = document.getElementById(id+'Icon');
  if (img.src.indexOf('showsourceicon.gif') > 0) {
    img.src = img.src.replace ('show', 'hide');
  } else {
    img.src = img.src.replace ('hide', 'show');
  }
}

function codeshowhide(id) {
  var divElement = document.getElementsByName(id+'Group');
  for(i=0; i<divElement.length; i++) {
    if(divElement[i].className == id+'Group'){
      divElement[i].className = id+'Group_Hide';
    } else if (divElement[i].className == id+'Group_Hide') {
      divElement[i].className = id+'Group';
    }
  }

  var img = document.getElementById(id+'Text');
  if(img.src.indexOf('sourcecodeicon-show.gif') > 0) {
    img.src = img.src.replace('show', 'hide');
  } else {
    img.src = img.src.replace('hide', 'show');
  }
  return true;
}

function initshowhide(id) {
  var divElement = document.getElementsByName(id+'Group');
  for(i=0; i<divElement.length; i++) {
    if (divElement[i].className == id+'Group') {
      divElement[i].className = id+'Group_Hide';
    } else if (divElement[i].className == id+'Group_Hide') {
      divElement[i].className = id+'Group';
    } 
  }
  var divElement = document.getElementsByName(id+'GroupDiv');
  for(i=0; i<divElement.length; i++) {
    if (divElement[i].className == id+'GroupDiv') {
      divElement[i].className = id+'GroupDiv_Hide';
    } else if (divElement[i].className == id+'GroupDiv_Hide') {
      divElement[i].className = id+'GroupDiv';
    } 
  }
  var int = document.getElementById(id+'CodeText');
  if (int.innerHTML == 'Show Initialization Code') {
    int.innerHTML = 'Hide Initialization Code';
  } else {
    int.innerHTML = 'Show Initialization Code';
  }
  img = document.getElementById(id+'Icon');
  if (img.src.indexOf('showsourceicon.gif') > 0) {
    img.src = img.src.replace ('show', 'hide');
  } else {
    img.src = img.src.replace ('hide', 'show');
  }
}



/*
Floating layer popups
*/

x = 100;
y = 100;
function setVisible(id)
{
	obj = document.getElementById('popup' + id);
	obj.style.visibility = (obj.style.visibility == 'visible') ? 'hidden' : 'visible';
	obj.style.display = (obj.style.displayy == 'inline') ? 'none' : 'inline';
	this.zIndexvalue=(this.zIndexvalue)? this.zIndexvalue+1 : 100;
	obj.style.zIndex=this.zIndexvalue;
	Drag.init(obj);
	//placeIt('popup' + id);	
}
function placeIt(id)
{
	obj = document.getElementById('popup' + id);
	if (document.documentElement)
	{
		theLeft = document.documentElement.scrollLeft;
		theTop = document.documentElement.scrollTop;
	}
	else if (document.body)
	{
		theLeft = document.body.scrollLeft;
		theTop = document.body.scrollTop;
	}

	theLeft += x;
	theTop += y;
	obj.style.left = theLeft + 'px' ;
	obj.style.top = theTop + 'px' ;
	setTimeout("placeIt('popup1')",500);

}
//window.onscroll = setTimeout("placeIt('layer1')",500);



/**************************************************
 * dom-drag.js
 * 09.25.2001
 * www.youngpup.net
 **************************************************
 * 10.28.2001 - fixed minor bug where events
 * sometimes fired off the handle, not the root.
 **************************************************/

var Drag = {

	obj : null,

	init : function(o, oRoot, minX, maxX, minY, maxY, bSwapHorzRef, bSwapVertRef, fXMapper, fYMapper)
	{
		o.onmousedown	= Drag.start;

		o.hmode			= bSwapHorzRef ? false : true ;
		o.vmode			= bSwapVertRef ? false : true ;

		o.root = oRoot && oRoot != null ? oRoot : o ;

		if (o.hmode  && isNaN(parseInt(o.root.style.left  ))) o.root.style.left   = "0px";
		if (o.vmode  && isNaN(parseInt(o.root.style.top   ))) o.root.style.top    = "0px";
		if (!o.hmode && isNaN(parseInt(o.root.style.right ))) o.root.style.right  = "0px";
		if (!o.vmode && isNaN(parseInt(o.root.style.bottom))) o.root.style.bottom = "0px";

		o.minX	= typeof minX != 'undefined' ? minX : null;
		o.minY	= typeof minY != 'undefined' ? minY : null;
		o.maxX	= typeof maxX != 'undefined' ? maxX : null;
		o.maxY	= typeof maxY != 'undefined' ? maxY : null;

		o.xMapper = fXMapper ? fXMapper : null;
		o.yMapper = fYMapper ? fYMapper : null;

		o.root.onDragStart	= new Function();
		o.root.onDragEnd	= new Function();
		o.root.onDrag		= new Function();
	},

	start : function(e)
	{
		var o = Drag.obj = this;
		e = Drag.fixE(e);
		var y = parseInt(o.vmode ? o.root.style.top  : o.root.style.bottom);
		var x = parseInt(o.hmode ? o.root.style.left : o.root.style.right );
		o.root.onDragStart(x, y);

		o.lastMouseX	= e.clientX;
		o.lastMouseY	= e.clientY;

		if (o.hmode) {
			if (o.minX != null)	o.minMouseX	= e.clientX - x + o.minX;
			if (o.maxX != null)	o.maxMouseX	= o.minMouseX + o.maxX - o.minX;
		} else {
			if (o.minX != null) o.maxMouseX = -o.minX + e.clientX + x;
			if (o.maxX != null) o.minMouseX = -o.maxX + e.clientX + x;
		}

		if (o.vmode) {
			if (o.minY != null)	o.minMouseY	= e.clientY - y + o.minY;
			if (o.maxY != null)	o.maxMouseY	= o.minMouseY + o.maxY - o.minY;
		} else {
			if (o.minY != null) o.maxMouseY = -o.minY + e.clientY + y;
			if (o.maxY != null) o.minMouseY = -o.maxY + e.clientY + y;
		}

		document.onmousemove	= Drag.drag;
		document.onmouseup		= Drag.end;

		return false;
	},

	drag : function(e)
	{
		e = Drag.fixE(e);
		var o = Drag.obj;

		var ey	= e.clientY;
		var ex	= e.clientX;
		var y = parseInt(o.vmode ? o.root.style.top  : o.root.style.bottom);
		var x = parseInt(o.hmode ? o.root.style.left : o.root.style.right );
		var nx, ny;

		if (o.minX != null) ex = o.hmode ? Math.max(ex, o.minMouseX) : Math.min(ex, o.maxMouseX);
		if (o.maxX != null) ex = o.hmode ? Math.min(ex, o.maxMouseX) : Math.max(ex, o.minMouseX);
		if (o.minY != null) ey = o.vmode ? Math.max(ey, o.minMouseY) : Math.min(ey, o.maxMouseY);
		if (o.maxY != null) ey = o.vmode ? Math.min(ey, o.maxMouseY) : Math.max(ey, o.minMouseY);

		nx = x + ((ex - o.lastMouseX) * (o.hmode ? 1 : -1));
		ny = y + ((ey - o.lastMouseY) * (o.vmode ? 1 : -1));

		if (o.xMapper)		nx = o.xMapper(y)
		else if (o.yMapper)	ny = o.yMapper(x)

		Drag.obj.root.style[o.hmode ? "left" : "right"] = nx + "px";
		Drag.obj.root.style[o.vmode ? "top" : "bottom"] = ny + "px";
		Drag.obj.lastMouseX	= ex;
		Drag.obj.lastMouseY	= ey;

		Drag.obj.root.onDrag(nx, ny);
		return false;
	},

	end : function()
	{
		document.onmousemove = null;
		document.onmouseup   = null;
		Drag.obj.root.onDragEnd(	parseInt(Drag.obj.root.style[Drag.obj.hmode ? "left" : "right"]), 
									parseInt(Drag.obj.root.style[Drag.obj.vmode ? "top" : "bottom"]));
		Drag.obj = null;
	},

	fixE : function(e)
	{
		if (typeof e == 'undefined') e = window.event;
		if (typeof e.layerX == 'undefined') e.layerX = e.offsetX;
		if (typeof e.layerY == 'undefined') e.layerY = e.offsetY;
		return e;
	}
};



/*
Popup layers 
*/

var y1 = 50;   // change the # on the left to adjust the Y co-ordinate
(document.getElementById) ? dom = true : dom = false;

function hideIt(id) {
  if (dom) {document.getElementById('popup' + id).style.visibility='hidden';}
}

function showIt(id) {
  if (dom) {
	document.getElementById('popup' + id).style.visibility='visible';
	// Call timer to move textbox in case they scroll the window
	//t = setInterval('move_window();', 500); }
	move_window(id); }
}

function toggleSlide(id) {
	el = document.getElementById(id);
	if(el.style.display == "none"){
		el.style.display == "";
	} else {
		el.style.display == "none";
	}
}

function getCookie(c_name) {
	if(document.cookie.length > 0) {
		c_start = document.cookie.indexOf(c_name + "=");
		if(c_start != -1) {
			c_start = c_start + c_name.length + 1;
			c_end = document.cookie.indexOf(";",c_start);
			if(c_end == -1) {
				c_end = document.cookie.length;
			}
			return unescape(document.cookie.substring(c_start,c_end).replace(/\+/g,' '));
		}
	}
	return "";
}
