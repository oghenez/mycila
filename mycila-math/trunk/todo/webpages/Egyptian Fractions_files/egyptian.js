/* EGYPTIAN FRACTIONS: www.mcs.surrey.ac.uk/Personal/R.Knott/FRACTIONS/egyptian.html */function gcd(a,b){var g;  if(arguments.length==1)    {g=gcd1(a[0],a[1]);      for(var i=2;g>1&&i<a.length;i++)g=gcd1(g,a[i]);   }else{      g=gcd1(arguments[0],arguments[1]);      for(var i=2;g>1&&i<arguments.length;i++)g=gcd1(g,arguments[i]);  };  return g}; function gcd1(a,b){    a=Math.abs(a);   b=Math.abs(b);   while(a>0 && b>0){var aa=a;a=b%a;b=aa};   return (a==0?b:a) };function toEgypt(outp){if(arguments.length==0)outp=true;  var t=parseInt(document.ef1.top.value), b=parseInt(document.ef1.bot.value);  var tt=t,bb=b;  if(isNaN(t))halt('The numerator (top) is not a number');  if(isNaN(b))halt('The denominator (bottom) is not a number');  if(b==0)halt('The denominator cannot be 0');  var efs=new Array(),g,d,pre=tt+"/"+bb+" = ";  if(b<0){b=-b};  if(t<0){t=-t};  if(t==0){if(outp)putmsg(tt+"/"+bb+"=0");return 0};  g=gcd(t,b);  if(g>1){t=t/g;b=b/g;pre += t+"/"+b+(t>1?" = ":"")};  if(b==1){if(outp)halt(tt+"/"+bb+"="+t+" is not a fraction")};  if(t==1){if(outp)halt(tt+"/"+bb+" is a unit fraction already and is "+     "1/"+(bb*2)+" + 1/"+(bb*3)+" + 1/"+(bb*6))};  var w=Math.floor(t/b);if(w>0){t=t-w*b;pre += w+"+"+t+"/"+b+" = "+w+"+"};  while(t>1)  {d=Math.ceil(b/t);   if(d.toString().indexOf("e")!=-1){b="too large";break;};   efs[efs.length]=d;   t=t*d-b;b=b*d;g=gcd(t,b);if(g>1){t=t/g;b=b/g};   //document.ef1R.res.value += t+"/"+b+"\r";  };  if(b.toString().indexOf("e")!=-1)b="too large";  efs[efs.length]=b;  if(outp)putmsg(pre+(efs.length>1?"1/"+efs.join("+1/"):""));  return efs.length; }; function tablLENS(){   for(var tt=2;tt<=29;tt++)     {document.ef1R.res.value += (tt<10?" ":"")+tt+"| ";      for(var bb=3;bb<=30;bb++)         {  if(tt>=bb){document.ef1R.res.value += ". "}            else if(gcd(tt,bb)>1){document.ef1R.res.value +="- ";}            else {var l=0;				var t=tt,b=bb;				while(t>1)				{var d=Math.ceil(b/t);				  if(d.toString().indexOf("e")!=-1){b="too large";break;};				  l++				 t=t*d-b;b=b*d;g=gcd(t,b);if(g>1){t=t/g;b=b/g};				};l++				document.ef1R.res.value += l+" "				}         };      document.ef1R.res.value += "\r"     }};      var RES="ef2R";      var foundct=0, printing =true, find1=false,     maxtofind, maxden,     soln=new Array();var ok;var found=function(solen){foundct++;  if(printing)putmsg( //foundct+") " +      soln[0]+" = 1/"+soln.slice(1,solen+1).join(" + 1/"))    if(foundct==maxtofind){ok=false}};var recipsumRAT=new Array(),recipRAT=new Array();recipsumRAT[1]=new RAT(0,1);oneRAT=new RAT(1,1);var recipUPB=100;for(var i=2;i<=recipUPB;i++){ recipRAT[i]=new RAT(1,i); recipsumRAT[i]=RATadd(recipsumRAT[i-1],recipRAT[i]);};function trydownfrom(maxd,targetRAT,ind,len){  //putmsg("tdf("+maxd+","+targetRAT+","+ind);       if(targetRAT.top==0 && ind==0)        found(len)  else if( ind==1 && targetRAT.num==1&&targetRAT.den<=maxd)        {soln[ind]= targetRAT.den;found(len)}  else if(maxd==1 || ind==0 )return  else if( RATis(targetRAT,"<",RATsub(recipsumRAT[maxd],recipsumRAT[maxd-ind])))       {//putmsg("too small");       return}  else if(RATis(targetRAT,">",recipsumRAT[ind+1])    )        {//putmsg("too big");       return}  else {soln[ind]=maxd;trydownfrom(maxd-1,RATsub(targetRAT,recipRAT[maxd]),ind-1,len);           if(maxden>1)trydownfrom(maxd-1,       targetRAT,                ind  ,len)       }};  function alllen(mind,targetRAT,ind){  putmsg("alllen "+mind+" "+targetRAT+" "+ind);       if(targetRAT.top==0)found(ind+1)  else if(maxden>0&&mind>maxden || targetRAT.top<0)return  else if( RATis(targetRAT,">",RATsub(recipsumRAT[maxden],recipsumRAT[mind-1])))       {//putmsg("too big");        return}      else if(RATis(targetRAT,"<",recipRAT[maxden])    )        {//putmsg("too small");       return}        else {soln[ind]=mind;        alllen(mind+1,RATsub(targetRAT,recipRAT[mind]),ind+1);        alllen(mind+1,targetRAT,ind)       }};function egylen(tt,bb,l,ind){var g; /// ind= last index filled in soln //document.ef2R.res.value += "egylen("+tt+","+bb+","+l+")\r";  if(l==1){if(tt==1&&(ind==0||bb>soln[ind])&&(maxden>0?maxden>=bb:true))      {soln[ind+1]=bb;found( ind+1)};return};  //if(maxden>0&&maxden<bb)return;  var t=tt,      b=bb,      lo=Math.ceil(b/t),      hi=Math.floor(l*bb/tt);  lo=(ind>1&&soln[ind]+1>lo?soln[ind]+1:lo);  if(maxden>0&&maxden>hi)hi=maxden;  if((maxden>0&&lo>maxden)||lo>hi)return;  //putmsg(tt+"/"+bb+" ["+soln.slice(0,ind)+"] l="+l+" "+lo+".."+hi+" by "+(down?-1:1)+" maxden="+maxden);  var down=false; for(var q=(down?hi:lo);ok&&(down?q>=lo:q<=hi);q=q+(down?-1:1))	  {  //try 1/q... rest is t/b-1/q=(tq-b)/(bq);		 t=tt*q-bb;		 if(t>0)		 { b=bb*q;g=gcd(t,b);if(g>1){t=t/g;b=b/g};		   //document.ef2R.res.value += "try q="+q+" "+t+"/"+b+" g="+g+"\r";		   if( ind==0||q>soln[ind]){soln[ind+1]=q;egylen(t,b,l-1,ind+1)}		 }	  }  };function efFXDlen(L,t,b){  if(arguments.length<3){t=parseInt(document[outF].top.value);     b=parseInt(document[outF].bot.value)};  maxtofind=(document[outF].ctlim?document[outF].ctlim.value:"");  if(maxtofind=="")maxtofind=0  else parseInt(maxtofind);  if(isNaN(maxtofind)){alert("count cutoff is not a number");    maxtofind=1000};  var tt=t,bb=b;  if(isNaN(t))halt('The numerator (top) is not a number');  if(isNaN(b))halt('The denominator (bottom) is not a number');  if(arguments.length==0)    {L=parseInt(document[outF].len.value);     if(isNaN(L)||!isInt(L))halt("Egyptian fraction length must be a whole number")     else if(L<0)halt("Egyptian fraction length must be at least 1")  //else if(l>maxlen)halt("Searching for Egyptian fraction lengths bigger than "  //    +maxlen+" takes too long. Try again");  //if(arguments.length<1)L=document.ef3.len[document.ef3.len.selectedIndex].value;    };   maxden=document[outF].maxden.value;   if(maxden.replace(/\s/g,"")=="")maxden=0   else{maxden=parseInt(maxden);       if(isNaN(maxden))halt("denominators limit: input is not a number")       };  if(b<0){b=-b};  if(t<0){t=-t};  //if(b<=t){halt("The fraction is bigger than 1.  Try again.")};  g=gcd(t,b);  if(g>1){tt=t;bb=b;t=t/g;b=b/g;};  soln[0]=t+(b>1?"/"+b:"");  foundct=0;ok=true; //if(b==1)//trydownfrom(maxden,new RAT(t,b),1,L)  //         { if(maxden<=0)halt("Set the maximum denominator please");  //           if(maxden>recipUPB)halt("Denominator max size is too large; max is "+recipUPB);  //            alllen(2,new RAT(t,b),1)   //         }  //else      egylen(t,b,L,0);   putmsg((g>1?tt+(bb>1?"/"+bb:"")+" = ":"")+t+(b>1?"/"+b:"")+": "    +(foundct>0?(ok?"":"stopped at ")+foundct:"none")+" found of length "+L    + (maxden>0?" with denominator up to "+maxden:""));  return foundct };function eflen(){  var maxlen=7;  var t=parseInt(document[outF].top.value), b=parseInt(document[outF].bot.value);  var tt=t,bb=b;  if(isNaN(t))halt('The numerator (top) is not a number');  if(isNaN(b))halt('The denominator (bottom) is not a number');  //var l=parseInt(document.ef2.len.value); // if(isNaN(l))l=0 // else if(l<0)halt("Egyptian fraction length must be at least 1")  //else if(l>maxlen)halt("Searching for Egyptian fraction lengths bigger than "+maxlen+" takes too long. Try again");  var ll=0; //ll=l;  if(b<0){b=-b};  if(t<0){t=-t};  if(b<=t){halt("The fraction is bigger than 1.  Try again.")};  if(t==0)putmsg(tt+"/"+bb+"=0")  else if(t==b)putmsg(tt+"/"+bb+"=1");  g=gcd(t,b);  if(g>1){t=t/g;b=b/g;putmsg(tt+"/"+bb+"="+t+"/"+b+(t>1?" = ":""))};  if(b==1)putmsg(tt+"/"+bb+"="+tt);  soln[0]=t+(b>1?"/"+b:"");  foundct=0; // if(l==0)  {while(++ll<=maxlen){ok=true;egylen(t,b,ll,0); if(foundct>0){break;}}}  putmsg((ll>maxlen?tt+(bb>1?"/"+bb:"")+": none found up to length "      +maxlen:"  "+foundct+" found of length "+ll));};function tablNB(which){var maxlen=7,max; printing=false;ok=true;   switch(which){case 'nb':max=20;break;   case 'minlen':max=30;break };   for(var t=2;t<=max-1;t++)   {document.ef2R.res.value += "\r"+(t<10?" ":"")+t+"| ";    for(var b=3;b<=max;b++)    {if(t>=b)document.ef2R.res.value +=(which=='nb'?" ":"")+ " .";    else if(gcd(t,b)>1)document.ef2R.res.value += (which=='nb'?" ":"")+" -"     else {foundct=0;var ll=0;           while(foundct==0 && ++ll<=maxlen){egylen(t,b,ll,0)};           if(which=='nb')             document.ef2R.res.value += (foundct<10?"  ":" ")+foundct+(ll>maxlen?"*":"")           else if (which=='minlen')             document.ef2R.res.value += " "+ll+(ll>maxlen?"*":"")          }   }};printing=true;};function tableUnder(Len,M){var c=new Array();printing=true;ok=true;   for(var i=5;i<=M;i++)c[i]=efFXDlen(Len,4,i);   document.ef3R.res.value+="\r"+c;};