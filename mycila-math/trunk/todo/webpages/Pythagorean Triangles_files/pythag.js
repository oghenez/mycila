var outF="mnCalc";function isInt(i){return i.toString()!="" && i.toString().indexOf(".")==-1      &&i.toString().toLowerCase().indexOf("e")==-1      && !isNaN(i) && isFinite(i) && (i+1)!=i};function isNb(r){return r!="" && !isNaN(r) &&isFinite(r)};function numorder(a,b){return a-b};function clearmsg(F){ if(arguments.length==0)F=outF; document.getElementById('msg'+F).innerHTML=''};function putmsg(txt){     var olist = document.getElementById('msg'+outF);        olist.innerHTML += txt+"<br>";        olist.scrollTop = olist.scrollHeight;        olist.scrollLeft=0;};function firstcap(s){return s.substring(0,1).toUpperCase()+s.substring(1)};function issq(x){if(isNaN(x)||x<0)return false;   if(x.toString().indexOf(".")!=-1 || x.toString().indexOf("e")!=-1)return false;   var s=Math.round(Math.sqrt(x));  return s*s==x};function gcd(a,b){if(a<0)a=-a;if(b<0)b=-b;return (a==0?b:b==0?a:gcd(b%a,a))};function primFactor(a,b,h){  return gcd(gcd(a,b),h) };function ptmn(m,n){var s1=m*m-n*n, s2=2*m*n, s3 =m*m+n*n;     return [s1,s2,s3,primFactor(s1,s2,s3)]};function primpt(hmin,hmax,primonly,processFN){var mhi=Math.floor(Math.sqrt(hmax));   for(var m=2;m<=mhi;m++)      {//putmsg("m:"+m+"\r");       for(var n=1;n<m;n++)         {var isprim=(gcd(m,n)==1 && n%2!=m%2);           if(isprim)           {var h=m*m+n*n;            if(primonly){if(h<=hmax && h>=hmin)processFN(m*m-n*n,2*m*n,m*m+n*n,true)}            else for(var k=Math.ceil(hmin/h);k<=Math.floor(hmax/h);k++)               processFN(k*(m*m-n*n),k*(2*m*n),k*(m*m+n*n),isprim&&k==1)      }  } }};var ct;function print4(a,b,c,isp){var msg=a+","+b+","+c,g=(isp?1:primFactor(a,b,c));   msg+=(isp?" primitive":"="+g+"x("+a/g+","+b/g+","+c/g+")")         +" perimeter:"+(a+b+c)+" area:"+a*b/2         +"\r";   putmsg(msg);ct++};function count(){ct++}; function tri(){var msg="";   //document.mnCalc.msg.value = ""   var m = parseInt(document.mnCalc.mdata.value) ;    var n = parseInt(document.mnCalc.ndata.value)        if(isNaN(m))alert("The m value is not a number. Please re-enter it.")   else if(isNaN(n))alert("The n value is not a number. Please re-enter it.")   else if (n==m)alert("m and n must be different otherwise one side is 0")   else if(n<1||m<1)alert("m and n cannot be less than 1")   else if(n>m)alert("m must be bigger than n otherwise one side is negative")   else {var t=ptmn(m,n);     msg="m:"+m+" n:"+n+" generates triangle "+t[0]+","+t[1]+","+t[2];     if(t[3]>1)putmsg(msg+"= "+t[3]+"x["+t[0]/t[3]+","+t[1]/t[3]+","+t[2]/t[3]+"]\r")     else putmsg(msg+": primitive\r")   }};function intFW(i,fw){ //return string  var ilen=i.toString().length  if(ilen>fw) return i.toString()  else return "          ".substring(0,fw-ilen)+i}function strFW(s,fw){ // return string  var slen=s.length  if(slen>fw) return s  else return "          ".substring(0,fw-slen)+s}var a,b,h;function testit(hide){   if(arguments.length==0)hide=false;   var nbsides=0;   a=document[outF].A.value.replace(/\s/g,"");   b=document[outF].B.value.replace(/\s/g,"");   h=document[outF].H.value.replace(/\s/g,"");   a=  a==""?"":parseFloat(a);   b=  b==""?"":parseFloat(b);   h=  h==""?"":parseFloat(h);   if(a!=""&&isNaN(a)){alert("Side 'a' is not empty but is not a number");return false};   if(b!=""&&isNaN(b)){alert("Side 'b' is not empty but is not a number");return false};   if(h!=""&&isNaN(h)){alert("Side 'h' is not empty but is not a number");return false};   if(a!=""&&a<=0||b!=""&&b<=0||h!=""&&h<=0){alert("Sides must be positive numbers");return false};   if(h!="" && (b>h||a>h)){alert("h must be the longest side");return false};   if(a!="")nbsides++;if(b!="")nbsides++;if(h!="")nbsides++;   if(nbsides==1){alert("At least two sides must be given");return false};   var pyth,rtang;   if(nbsides==3){rtang=(h*h-a*a-b*b==0);pyth=rtang&&isInt(a)&&isInt(b)&&isInt(h)}   else if(a==""){a=Math.sqrt(h*h-b*b);pyth=isInt(a);rtang=true;}   else if(b==""){b=Math.sqrt(h*h-a*a);pyth=isInt(b);rtang=true;}   else if(h==""){h=Math.sqrt(a*a+b*b);pyth=isInt(h);rtang=true;}   if(!isInt(a)||!isInt(b)||!isInt(h))      {alert('All sides must be whole numbers: '+a+","+b+","+h);return};   if(a!=""&&!isInt(a*a) || b!=""&&!isInt(b*b) || h!=""&&!isInt(h*h))      {alert("The sides are to big to test if Pythagorean");return};   var g=gcd(a,gcd(b,h));   if(!hide)       putmsg(a+", "+b+", "+h+" is "+(rtang?"":"not ")+"right-angled"+         (h*h<a*a+b*b?": all angles are less than 90 deg"         :h*h>a*a+b*b?": one angle exceeds 90 deg"         :" and is "+(pyth?"":"not ")+"Pythagorean"         )+(pyth?(g==1?" and primitive":" = "+g+"&times;["+[a/g,b/g,h/g]+"]"):"")+"\r");   return pyth}function listTri(which){   if(arguments.length==0){which=document.mnCalc.listopt[document.mnCalc.listopt.selectedIndex].value};   var minMN=parseInt(document.mnCalc.mmin.value,10),maxMN=parseInt(document.mnCalc.mmax.value,10);    if(isNaN(minMN)||minMN<1){alert("M value is not a positive number");return};   if(document.mnCalc.mmax.value.replace(/\s/,"")=="")maxMN=minMN;   if(isNaN(maxMN)){alert("'up to m' value is not a number");return};   if(maxMN<minMN){alert("The 'up to m' value is less than the starting value for m");return};   putmsg("List of "+which+" Pythagorean Triples with m="+minMN+(maxMN>minMN?" to "+maxMN:"")+"\r");   var mnFW=maxMN.toString().length;   var abcFW=eval(maxMN*maxMN*2).toString().length;   var m, n, a, b, c, g;   putmsg(strFW("m",mnFW)+" "+strFW("n",mnFW)+" : "+strFW("a",abcFW)+" "+strFW("b",abcFW)+" "+strFW("c",abcFW)+"\r")   for(m=minMN;m<=maxMN;m++)   {  for(n=1;n<m;n++)      {var t=ptmn(m,n);       if(which=="all" || (which=="primitive" && t[3]==1))          putmsg(intFW(m,mnFW)+" "+intFW(n,mnFW)+" : "+intFW(t[0],abcFW)+" "+intFW(t[1],abcFW)+" "+intFW(t[2],abcFW)+                 (which!="primitive"? "   "+ (t[3]==1?"primitive":t[3]+"x("+t[0]/t[3]+","+t[1]/t[3]+","+t[2]/t[3]+")"):"")+"\r")   }  }  ;   return true}function primhyp(CorL){  var hmin=parseInt(document.pythCalc.hyplo.value),      hmax=parseInt(document.pythCalc.hyphi.value);  if(document.pythCalc.hyphi.value.replace(/\s/g,"")=="")hmax=hmin;  //alert(hmin+".."+hmax);  if(isNaN(hmin))halt("Hypotenuse starting value is not a number");  if(isNaN(hmax))halt("Hypotenuse final value is not a number");  if(hmin>hmax)halt("Hypotenuse ending value is less than the start value");  var PorA=document.pythCalc.paSEL[document.pythCalc.paSEL.selectedIndex].value;  putmsg((PorA=='A'?"All":"Primitive")+" triangles with hypotenuse "  //+"|"+hmin+"-"+hmax+"|"    +(hmax>hmin?"between "+hmin+" and "+hmax:hmin)+":"+"\r");  var sum=0;  for(var h=hmin;h<=hmax;h++)      {ct=0;primpt(h,h,PorA=='P',(CorL=='L'?print4:count));       if(CorL=='C'&&ct>0)putmsg("h="+h+": "+ct+" found\r");       sum+=ct};  putmsg(sum+" found.\r");}; // ----------------------- Two Fractions Method ----------function FRAC(t,b){      if(!isInt(t))halt("FRACT top is not an integer:"+t);   if(!isInt(b))halt("FRAC  bottom is not an integer:"+b);   if(b==0)halt("FRAC: cannot have 0 bottom");   this.top=t; this.bot=b;}function getFRAC(fldT,fldB,nm){ var t=checkinput(document[outF][fldT].value,nm+" numerator","emptyval=Number.NaN"),      b=checkinput(document[outF][fldB].value,nm+" denominator","emptyval=1");      //putmsg(t+"./."+b);      if(isNaN(t) ||isNaN(b))return false;      else return new FRAC(t,b) }; function twofrac(F1,F2,prnt,premsg,postmsg){var s;   if(arguments.length<2)      {F1=getFRAC('F1T','F1B',"first"); F2=getFRAC('F2T','F2B',"first")};   if(arguments.length<3)prnt=true;   if(arguments.length<4){premsg="";postmsg="";}  // if(! F1)halt("The first is neither a fraction nor a whole number");  // if(! F2)halt("The second is not a fraction nor a whole number");   if(F1.top<0 || F1.bot<=0)halt("The first fraction must contain only whole numbers > 0");   if(F2.top<0 || F2.bot<=0)halt("The second fraction must contain only whole numbers > 0");  if(F1.top*F2.top/F1.bot/F2.bot!=2)halt("Your two numbers do not have a product of 2");    s=premsg+    F1.top+(F1.bot>1?"/"+F1.bot:"")+" &amp; "+F2.top+(F2.bot>1?"/"+F2.bot:"")+" generate  "    ;     var  L1=(F1.top+2*F1.bot)*F2.bot,L2=(F2.top+2*F2.bot)*F1.bot;   var H=Math.round(Math.sqrt(L1*L1+L2*L2));   var g=gcd(L1,gcd(L2,H));      if(prnt)putmsg(s+(g==1?"the primitive triple ":"")+L1+" "+L2+" "+H+(g==1?"":" = "+g+" ["+L1/g+", "+L2/g+", "+H/g+"]"+postmsg));  return [g,L1/g,L2/g,H/g]}function twofractable(atlo,athi,ablo,abhi,k){ //AF is a FRAC for values in range top..bot, gcd(top,bot)=k, B similarly  var ab,at,outs;  if(arguments.length==0)  {atlo=checkinput(document.twofracF.atlo.value,"top start of range"),   athi=checkinput(document.twofracF.athi.value,"top end of range","emptyval="),   ablo=checkinput(document.twofracF.ablo.value,"bottom start of range"),   abhi=checkinput(document.twofracF.abhi.value,"bottom end of range","emptyval="),   k=checkinput(document.twofracF.k2f.value,"k","emptyval=1"),   factrize=document.twofracF.twoffctQ.checked,   reduce=document.twofracF.twoflowf.checked;  };  //putmsg(atlo+".."+athi+" / "+ablo+".."+abhi);  if(isNaN(atlo))halt("a's start of range is not a number");  if(atlo<1)halt("a's starting value must be at least 1");  if(ablo=='')ablo=1;  if(isNaN(ablo))halt("b's start of range is not a number");  if(ablo<1)halt("b's starting value must be at least 1");  if(athi=='')athi=atlo;  if(abhi=='')abhi=ablo;    outs="<span class=red>"+(k>1?k:"")+"a</span>/<span class=blue>"+(k>1?k:"")+"b</span> &times; <span class=green>2b/a</span> = 2<br><table cellspacing=0 cellpadding=3><!-- tr><th></th><td colspan="+2*(athi-atlo+1)+" class=red align=center>"+(k>1?k:"")+"a:</td></tr --><tr class=red><th class=blue>"+(k>1?k:"")+"b:</th>";  for(at=atlo;at<=athi;at++)outs+="<th colspan=2>"+(at==atlo?"<span class=red>"+(k>1?k:"")+"a=</span>":"")+k*at+"</th>";  outs+="</tr>";  for(ab=ablo;ab<=abhi;ab++)    {outs+="<tr><th class=blue>"+k*ab+"</th>";     for(at=atlo;at<=athi;at++)      {var F2=new FRAC(2*ab,at);       var F2g=gcd(F2.top,F2.bot);if(reduce && F2g>1){F2.top=F2.top/F2g;F2.bot=F2.bot/F2g};       var T=twofrac(new FRAC(k*at,k*ab),F2,false);       outs+=("<td class=green>"+F2.top+"/"+F2.bot+":</td><td>"+         (factrize?(T[0]>1?T[0]:"")+"["+T[1]+","+T[2]+","+T[3]+"]"                  :"["+T[0]*T[1]+","+T[0]*T[2]+","+T[0]*T[3]+"]")        +"</td>")      };      outs+=("</tr>")     };  outs+=("</table>");  putmsg(outs)}function find2frac(a,b,h,allforms){var H;if(arguments.length<3){    a=checkinput(document.twofracF.pt1.value,"first side of triple");    b=checkinput(document.twofracF.pt2.value,"second side of triple");    h=checkinput(document.twofracF.pt3.value,"third side of triple");    }; if(h=="")H=Math.round(Math.sqrt(a*a+b*b)) else H=h; if(H*H-a*a-b*b!=0)halt(a+", "+b+(h!=""?", "+h+" is not":" cannot be the sides of")+" a Pythagorean triple"); if(h=="")h=H; if(arguments.length<4)allforms=true; var g=gcd(a,gcd(b,h)),A,B,k,s; if(g>1){a=a/g;b=b/g;h=h/g}; if(a % 2 == 0){var x=a;a=b;b=x}; // ASSERT b is even var L=b/2;var Lfs=factors(L), gls=factors(g); for(var i=0;i<Lfs.length;i++)  {B=Lfs[i];    if(L/B>B)      {A=L/B-B;      if(A*(2*B+A)==a)      { var s="";        if(allforms && gls.length>1)           {            for(j=0;j<gls.length-1;j++)              {k=gls[j];s=s+A*k+"/"+B*k+" &amp; "+2*B*g/k+"/"+A*g/k+", "};           }  ;        twofrac(new FRAC(A*g,B*g),new FRAC(2*B,A),true,s,(s!=""?" ("+gls.length+" pairs)":""));             }}  }}// ------------------------ m,n Generators -----------------function findmn(t){  if(t[0]*t[0]+t[1]*t[1]-t[2]*t[2]!=0){return ["not right-angled"]};  //if(gcd(t[0],gcd(t[1],t[2]))!=1){return ["not primitive"]};  if(t[0]%2==1){var x=t[1];t[1]=t[0];t[0]=x};  //ASSERT t[0]=even=2mn, t[1]=odd=m^2-n^2, t[2]=m^2+n^2  var n=Math.round(Math.sqrt((t[2]-t[1])/2));  var m=Math.round(t[0]/2/n);  if(2*m*n!=t[0]||m*m-n*n!=t[1]||m*m+n*n!=t[2])    {n=Math.round(Math.sqrt((t[2]-t[0])/2));     m=Math.round(t[1]/2/n);     if(2*m*n!=t[1]||m*m-n*n!=t[0]||m*m+n*n!=t[2])          return ["no m,n generators"]    };  return [m,n]};function showmn(t){var mn=findmn(t);return "m="+mn[0]+",n="+mn[1]};function dofindmn(){  if(testit(true))    {var mn=findmn([a,b,h]);     if(mn.length==1)putmsg([a,b,h]+": "+mn[0]+"\r")     else putmsg([a,b,h]+" m="+mn[0]+" n="+mn[1]+"\r")    }};   var primesknown =new Array(2,3,5,7,11,13,17,19,23,29,31,37,41,43,47,53,59,61,67,71,73,79,83,89,97,101,103,107,109,113,127,131,137,139,149,151,157,163,167,173,179,181,191,193,197,199,211,223,227,229,233,239,241,251,257,263,269,271,277,281,283,293,307,311,313,317,331,337,347,349,353,359,367,373,379,383,389,397,401,409,419,421,431,433,439,443,449,457,461,463,467,479,487,491,499,503,509,521,523,541,547,557,563,569,571,577,587,593,599,601,607,613,617,619,631,641,643,647,653,659,661,673,677,683,691,701,709,719,727,733,739,743,751,757,761,769,773,787,797,809,811,821,823,827,829,839,853,857,859,863,877,881,883,887,907,911,919,929,937,941,947,953,967,971,977,983,991,997,1009,1013,1019,1021,1031,1033,1039,1049,1051,1061,1063,1069,1087,1091,1093,1097,1103,1109,1117,1123,1129,1151,1153,1163,1171,1181,1187,1193,1201,1213,1217,1223,1229,1231,1237,1249,1259,1277,1279,1283,1289,1291,1297,1301,1303,1307,1319,1321,1327,1361,1367,1373,1381,1399,1409,1423,1427,1429,1433,1439,1447,1451,1453,1459,1471,1481,1483,1487,1489,1493,1499,1511,1523,1531,1543,1549,1553,1559,1567,1571,1511,1523,1531,1543,1549,1553,1559,1567,1571,1579,1583,1597,1601,1607,1609,1613,1619,1621,1627,1637,1657,1663,1667,1669,1693,1697,1699,1709,1721,1723,1733,1741,1747,1753,1759,1777,1783,1787,1789,1801,1811,1823,1831,1847,1861,1867,1871,1873,1877,1879,1889,1901,1907,1913,1931,1933,1949,1951,1973,1979,1987,1993,1997,1999);function isknownprime(n){return knownprime(n)=='prime'};function knownprime(p){  var lo=0, hi=primesknown.length-1; var state='unknown';  if(p>primesknown[hi]){return 'not known'};  while(state=='unknown' && lo<=hi)  {  var mid=Math.floor((lo+hi)/2);     if(p<primesknown[mid])hi=mid-1     else if(p==primesknown[mid])return 'prime'     else lo=mid+1    };  return 'composite'};function isprime(n){  if(n<=primesknown[primesknown.length-1])      {return isknownprime(n)}  else {var lim=Math.round(Math.sqrt(n));        for(var i=1;i<primesknown.length&&primesknown[i]<=lim;i++)          if(n%primesknown[i]==0)return false;        for(var f=primesknown[primesknown.length-1]+2;f<=lim;f=f+2)          if(n%f==0)return false;        return true       }};function Factors(n){var PLIM=5000000,N=n;;  //if(n>Math.pow(primesknown[primesknown.length-1],2)){alert("Cannot factor "+n+" - too large!");return};  var N=n;  if(n<0)n=-n;  var nprime=knownprime(n)  if(nprime=='prime'){return [n]};  var maxp=Math.floor(Math.sqrt(n)+0.001), maxI=primesknown.length-1;  //if(maxp>lastknownprime){halt("factorsOf cannot use primes beyond those known");return};  var fs=[], lim=Math.sqrt(n);   if(lim>primesknown[primesknown.length-1])lim=primesknown[primesknown.length-1];  for(var i=0;i<=maxI && primesknown[i]<=lim;)  {  if(n%primesknown[i]==0)         {fs=fs.concat(primesknown[i]);  n=Math.round(n/primesknown[i]);lim=Math.round(Math.sqrt(n));          if(isknownprime(n))return fs.concat(n)}      else i++   };   //putmsg("-"+n+"-"+lim+"-");   if(n<=primesknown[primesknown.length-1]){fs=fs.concat(n); return fs};   maxp=Math.round(Math.sqrt(n));if(maxp>PLIM)maxp=PLIM;   for(i=primesknown[primesknown.length-1]+2;i<=maxp;i+=2)      if(n%i==0){fs=fs.concat(i);n=Math.round(n/i);                 if(isprime(n))return fs.concat(n);                   maxp=Math.round(Math.sqrt(n));if(maxp>PLIM)maxp=PLIM;                }   if(maxp==PLIM){//alert(N+": factors got too large, sorry!");                  return []};    //putmsg(N+": "+fs+"\r");    return fs.concat(n);};function nbinrad(r){  var fs=Factors(r);var n=fs.length;  if(fs[0]==2)n=n-1;  return Math.pow(2,(fs[0]==2?n-1:n))};function PYTHNBS(n,L,H,uniqprs,mod41){          this.Legs=L;    this.primLegs=(n%4==2?0:Math.pow(2,uniqprs-1));      this.compLegs=this.Legs-this.primLegs;     this.Hyps=H;    this.primHyps=(mod41<uniqprs?0:Math.pow(2,mod41-1)); this.compHyps=this.Hyps-this.primHyps;     this.Sides=L+H; this.primSides=this.primLegs+this.primHyps;          this.compSides=this.compLegs+this.compHyps};var NOPYTHNBS=new PYTHNBS(-1,-1,-1,-1,-1);function pnbs(n){  var res;     if(n==1||n==2)res=new PYTHNBS(2,0,0,0,-1)     else {		 var fs=Factors(n);if(fs.length==0)return NOPYTHNBS;		 var L=1,f=fs[0],pow=1,uniqprs=0,mod41=0,H=1;		 for(var i=1;i<fs.length;i++)		 {if(fs[i]==f)pow++		  else {if(f==2){L*=(2*pow-1)}else{L*=(2*pow+1)};uniqprs++;if(f%4==1){mod41++;H=H*(2*pow+1)};				f=fs[i];pow=1;			   } 		 };		 if(f==2){L*=(2*pow-1)}else{L*=(2*pow+1)};uniqprs++;if(f%4==1){mod41++;H=H*(2*pow+1)};		 var res=new PYTHNBS(n,(L-1)/2,(H-1)/2,uniqprs,mod41);     }     return res}/* function table(){   *//*   var prop=["primLegs","compLegs","Legs","primHyps","compHyps","Hyps","primSides","compSides","Sides"]; *//*   var ser=new Array(); *//*   for(p=0;p<9;p++) *//*      {putmsg(prop[p]+":");ser.length=0; *//*       for(n=2;ser.length<30;n++) *//*          {var P=pnbs(n);for(var j=1;j<=P[prop[p]];j++)ser[ser.length]=n} *//*       putmsg(ser+"\r") *//*      }; *//* }; */function pct(){     var nmin=parseInt(document.pCalc.min.value),nmax;  if(isNaN(nmin)){alert("Cannot find a number to count triads for");return};  if(document.pCalc.max.value.replace(/\s+/g,"")=="")nmax=nmin  else {nmax=parseInt(document.pCalc.max.value);        if(isNaN(nmax)){alert("'up to' value is not a number");return}       };  for(var n=nmin;n<=nmax;n++)  {var ps=pnbs(n);    if(ps.Legs>=0)       putmsg(n+" is in "+ps.Legs+" legs("+ps.primLegs+" prim)+"+ps.Hyps+" hyps("+ps.primHyps+" prim)="+ps.Sides+" sides("+ps.primSides+" prim)\r")    else putmsg(n+": unable to compute Pythagorean triple counts\r");   };};/* function fct(){ *//*   var n=parseInt(document.pCalc.p.value); *//*   if(isNaN(n)){alert("Cannot find a number to factor");return}; *//*   putmsg(n+":"+factors(n)+"\r") *//* }; */var U=   new Array([ 1,-2, 2],[ 2,-1 ,2],[ 2,-2,3]),    A=   new Array([ 1, 2, 2],[ 2, 1, 2],[ 2, 2,3]),    D=   new Array([-1, 2, 2],[-2, 1, 2],[-2, 2,3]),    Uinv=new Array([ 1, 2,-2],[-2,-1, 2],[-2,-2,3]),    Ainv=new Array([ 1, 2,-2],[ 2, 1,-2],[-2,-2,3]),    Dinv=new Array([-1,-2, 2],[ 2, 1,-2],[-2,-2,3]);    function mmult(v,M){var m=M;  switch(M.toLowerCase()){case "u":M=U;break;case "a":M=A;break;case "d":M=D;break   case "ui":M=Uinv;break;case "ai":M=Ainv;break;case "di":M=Dinv;break;};  //putmsg(v+" x "+m+M[0]+" "+M[1]+" "+M[2]+"\r");   var vM=new Array(3);   for(var i=0;i<3;i++)     {var s=0;      for(var j=0;j<3;j++) s=s+v[j]*M[i][j];      vM[i]=s;     // putmsg(i+"="+v+"x"+M[i]+"="+s+"\r");     };    //putmsg("<<"+vM+"\r");   return vM};var dbg=false;function uad(){var t,m,T=new Array(),stk=new Array([3,4,5]); while(stk.length>0) {t=stk[stk.length-1];stk.length--;  if(dbg)putmsg("try "+t+"\r");  if(found(t)){process(t);if(findCorL=="1")return};  //if(!confirm("Prim="+t))halt();  if(findPorA=="A")       {m=firstm(t);         if(dbg)putmsg("m from "+m);        do {T[0]=m*t[0];T[1]=m*t[1];T[2]=m*t[2];               if(found(T)){process(T);if(findCorL=="1")return};               m++          } while(more(T));         if(dbg)putmsg(" to "+m+"\r");       };   if(more(t)){stk[stk.length]=mmult(t,"a");stk[stk.length]=mmult(t,"d");stk[stk.length]=mmult(t,"u");      if(dbg)putmsg("push "+stk[stk.length-3]+" "+stk[stk.length-2]+" "+stk[stk.length-1]+" ("+stk.length+")\r")   }  };};function UADprev(v){   var stop=false,prev3,prev;		for(var i=0;i<3;i++&&!stop)		  {prev3=mmult(v,["ui","ai","di"][i]);		   if(prev3[0]>0&&prev3[1]>0&&prev3[2]>0)			{prev=["UAD".charAt(i),prev3[0],prev3[1],prev3[2]];stop=true;		   }};  if(stop)return prev;  return []};function findUADgiventri(which){  var ab,ub,db,prev3,prevs=new Array(),s="",path="";  if(!testit(true))return;  var v=new Array(a,b,h);  var g=gcd(a,gcd(b,h));  var prim=g==1;  if(!prim){alert(v+" is not primitive = "+g+"x["+a/g+","+b/g+","+h/g+"]: it is not in the UAD tree");return};  if(UADprev(v).length==0)v=[b,a,h];  switch(which){  case "pathto":	  while(v[0]!=3&&v[0]!=4)	  { prev3=UADprev(v);if(prev3.length==0){alert("@@ no prev "+v+" X"+path);return}	    path=prev3[0]+path; //if(showuadpath)prevs[prevs.length]=prev3;	    v[0]=prev3[1];v[1]=prev3[2];v[2]=prev3[3];	  };	  	  //if(showuadpath)for(i=prevs.length-1;i>=0;i--){path=path+prevs[i][0];s=s+prevs[i][1].join(',')+" -"+prevs[i][0]+"-> "};	  if(v[0]==4)	     {path=path.replace(/U/g,"X").replace(/D/g,"U").replace(/X/g,"D");	      i=b;b=a;a=i};	  putmsg("The path from <span class=tri>3, 4, 5</span> "+showmn([3,4,5])+" to <span class=tri>"+a+", "+b+", "+h+"</span> "+showmn([a,b,h])+	      " is "+path);	  break;  case'next':     v=[a,b,h];var uv=mmult(v,"u"),av=mmult(v,"a"),dv=mmult(v,"d");     putmsg("<table>"+           "<tr><td rowspan=3 valign=middle><span class=tri>"+v.join(", ")+"</span> "+showmn(v)+"</td>"+                "<td valign=baseline>U&uarr;</td><td class=tri>"+uv.join(", ")+"</td><td valign=baseline>"+showmn(uv)+"</td></tr>"+            "<tr valign=baseline><td valign=baseline>A&rarr;</td><td class=tri>"+av.join(", ")+"</td><td valign=baseline>"+showmn(av)+"</td></tr>"+            "<tr valign=baseline><td valign=baseline>D&darr;</td><td class=tri>"+dv.join(", ")+"</td><td valign=baseline>"+showmn(dv)+"</td></tr></table>")            //putmsg(v+"["+findmn(v)+"]: U->"+uv+"["+findmn(uv)+"]; A->"+av+"["+findmn(av)+"]; D->"+dv+"["+findmn(dv)+"]\r");     break;  }};  function findUADpath(){var s="";  var p=document[outF].path.value.replace(/\s/g,"");  if(p.replace(/[UADuad]/g,"")!="")    {alert("Path does not contain only U,A or D");return};  var v=[3,4,5];s=p.toUpperCase()+" ";  for(var i=0;i<p.length;i++)    {v=mmult(v,p.charAt(i)); //if(showuadpath)s=s+" "+p.charAt(i)+" "+v     if(v[2]+1==v[2]){alert("!! Triple has become too large at the "+(i+1)+"-th letter ");return}    };  putmsg(s+" is the path to <span class=tri>"+v.join(", ")+'</span> '+showmn(v));};  var findPorA,findProp,findMin,findMax,finds=new Array();function pnbsOf(sides,n){   var ps=pnbs(n);    //putmsg(n+" is in "+ps.Legs+" legs("+ps.primLegs+" prim)+"+ps.Hyps+" hyps("+ps.primHyps+" prim)="+ps.Sides+" sides("+ps.primSides+" prim)\r");   switch(sides){   case "L":return (findPorA=="P"?ps.primLegs:ps.Legs);   case "H":return (findPorA=="P"?ps.primHyps:ps.Hyps);   case "S":return (findPorA=="P"?ps.primSides:ps.Sides);}  };  function allpyth(){ //putmsg("Allpyth\r");    return  uad()};function firstm(t){var m;  switch(findProp){   case "Ls":m= Math.round(findMin/Math.min(t[0],t[1]));break;   case "S":m= Math.round(findMin/Math.max(t[0],t[1],t[2]));break;   case "L":   case "Ll":   case "L2":m= Math.round(findMin/Math.max(t[0],t[1]));break;   case "S3":   case "H":m= Math.round(findMin/t[2]);break;   case "P":m= Math.round(findMin/(t[0]+t[1]+t[2]));break;   case "A":m= Math.round(Math.sqrt(findMin*2/t[0]/t[1]));break;   case "r":m=Math.round(findMin*2/(t[0]+t[1]-t[2]));break;   case "P3":m=Math.round(findMin/(t[0]*t[1]*t[2]));break;   }   return Math.max(m,2)};function Prop(P){switch(P){  case "Ls":return "the shortest leg";  case "Ll":return "the longest leg";  case "L":return "a leg";  case "L2":return "both legs";  case "S":return "a side";  case "S3":return "all sides";  case "H":return "hypotenuse";  case "P":return "perimeter";  case "A":return "area";  case "r":return "inradius";  case "P3":return "product of all 3 sides";} };function inrng(x){//putmsg("inrng "+x+"\r");       return x>=findMin && x<=findMax};     function found(t){   //putmsg("found? "+t+"\r");  switch(findProp)  { case "L": return inrng(t[0])||inrng(t[1]);    case "Ll":return inrng(Math.max(t[0],t[1]));    case "Ls":return inrng(Math.min(t[0],t[1]));    case "L2": return inrng(t[0])&&inrng(t[1]);    case "H":return inrng(t[2]);    case "S":return inrng(t[0])||inrng(t[1])||inrng(t[2]);    case "S3":return inrng(t[0])&&inrng(t[1])&&inrng(t[2]);    case "P":return inrng(t[0]+t[1]+t[2]);    case "A":return inrng(t[0]*t[1]/2);    case "r":return inrng(Math.round((t[0]+t[1]-t[2])/2));    case "P3":return inrng(t[0]*t[1]*t[2]); }};// function propsizes(t){//   switch(findProp)//   { case "L": //     case "L2": //     case "Ls":return Math.min(t[0],t[1]);//     case "Ll":return Math.max(t[0],t[1]);//     case "S":return Math.min(t[0],Math.min(t[1],t[2]));//     case "H": case "S3":return t[2];//     case "P":return t[0]+t[1]+t[2];//     case "A":return t[0]*t[1]/2;//     case "r":return t[0]*t[1]/(t[0]+t[1]+t[2]);//  } };function save(sz,t){ct++;finds[sz][finds[sz].length]=t.slice(0);};function process(t){ //putmsg("process "+t+"\r");    switch(findProp)    { case "Ls":      case "L2":      case "S3":save(Math.min(t[0],t[1]),t);break;      case "Ll":save(Math.max(t[0],t[1]),t);break;      case "L":if(inrng(t[0]))save(t[0],t);  if(inrng(t[1]))save(t[1],t);break;      case "H":save(t[2],t);break;      case "S":if(inrng(t[0]))save(t[0],t);  if(inrng(t[1]))save(t[1],t);               if(inrng(t[2]))save(t[2],t); break;      case "P":save(t[0]+t[1]+t[2],t); break;      case "A":save(t[0]*t[1]/2,t); break;      case "r":save((t[0]+t[1]-t[2])/2,t); break;      case "P3":save(t[0]*t[1]*t[2],t);break;     }};function inradius(t){return (t[0]*t[1])/(t[0]+t[1]+t[2])};function area(t){return t[0]*t[1]/2};function rev(s){var r="";for(var i=0;i<s.length;i++)r=s.charAt(i)+r;return r};function reportTri(cnt,t,prod){ putmsg(TriSTR(cnt,t,prod)) };function PT(a,b,h){  this.A=Math.min(a,b); this.B=Math.max(a,b);this.H=h;  this.tri=new Array(this.A,this.B,this.H);  this.primFactor=primFactor(a,b,h);  this.Primitive=this.primFactor==1;  this.PrimPT=new Array(this.A/this.primFactor,this.B/this.primFactor,this.H/this.primFactor);  this.Perim=a+b+h;    this.Area=this.A*this.B/2;if(this.Area+1==this.Area)this.Area="too big";  this.Inradius=inradius([a,b,h]);       if(issq((a+h)/2)){this.m=Math.sqrt((a+h)/2);this.n=b/2/this.m}  else if(issq((b+h)/2)){this.m=Math.sqrt((b+h)/2);this.n=a/2/this.m}  else {this.m=0;this.n=0};};function TriSTR(cnt,t,prod){ //alert("TriSTR "+cnt+" "+t+" "+prod);  if(arguments.length==0){cnt=0;t=[a,b,h]}  else if(arguments.length==1){t=cnt;cnt=0};  if(arguments.length<3)prod=false;    var P=new PT(t[0],t[1],t[2]);    var k=P.primFactor;  var p=new Array(P.a/k,P.b/k,P.h/k),m=P.m,n=P.n,Area=P.Area;  var prodfct=Math.round(t[0]*t[1]/60*t[2]);  return (cnt>0?cnt+") ":cnt==0?"":-cnt+": ")+"<span class=tri>"+P.A+", "+P.B+", "+P.H+"</span> "         +(k==1?"primitive":"="+k+"&times; <span class=tri>"+P.PrimPT.join(", ")+"</span>")+" P="+P.Perim         +" A="+P.Area+" r="+P.Inradius+         (prod?" abh="+prodfct+"x"+"60":"")         +(P.m>0?" m="+P.m+" n="+P.n:" (no m,n)")};//r=Area/semiperimeter=legs product/sides sum//R=H/2 since semicircles subtend angle of 90 degfunction more(t){//putmsg("More? "+t+"\r");  switch(findProp)  {  case "Ls":return Math.min(t[0],t[1])<=findMax;     case "Ll":return Math.min(t[0],t[1])<=findMax;     case "L":return t[0]<findMax || t[1]<=findMax;     case "L2":return t[0]<=findMax && t[1]<=findMax;     case "H":return t[2]<=findMax;     case "S":return t[0]<=findMax || t[1]<=findMax || t[2]<=findMax;     case "S3":return t[0]<=findMax && t[1]<=findMax && t[2]<=findMax;     case "P":return t[0]+t[1]+t[2]<=findMax;     case "A":return t[0]*t[1]/2<=findMax;     case "r":return t[0]+t[1]-t[2]<=2*findMax;     case "P3":return t[0]*t[1]*t[2]<=findMax;} };function pfind(sel){ var txt;  findCorL=sel;  findPorA=document.pCalc.paSEL[document.pCalc.paSEL.selectedIndex].value;  findProp=document.pCalc.sideSEL[document.pCalc.sideSEL.selectedIndex].value;  findMin=eval(document.pCalc.min.value);  if( isNaN(findMin)){alert("The first size is not a number:"+findMin);return};  if(!isInt(findMin)){alert("The first size is too big for this Calculator");return};  findMax=(document.pCalc.max.value==""?findMin:eval(document.pCalc.max.value));  if( isNaN(findMax)){alert("The upper size limit is not a number:"+findMax);return};  if(!isInt(findMax)){alert("The upper size limit is too big for this Calculator");return};  if(findMax<findMin){alert("The upper size is less than the starting size value");return};  if(findCorL=="c"&&(findProp=="L2"||findProp=="S3"))    {alert("If more than one side is in the range I cannot do separate counts. "+           "Choose another count/list option please.");     return};  if((findProp=="S3"||findProp=="L2")&&(findMin==findMax))    {alert("No Pythagorean triangle has 2 sides of the same length");return};      ct=0;finds.length=0;  //putmsg("\rX");  for(var i=findMin;i<=findMax;i++)finds[i]=[];    //putmsg("C/L="+findCorL+" P/A="+findPorA+" Prop="+findProp+" "+findMin+"-"+findMax+"\r");    if("L1".indexOf(findCorL)!=-1)    putmsg(firstcap((findPorA=="P"?"primitive ":"")+"triples with "+Prop(findProp)             +(findMax>findMin?" in range "+findMin+"-"+findMax:"="+findMin)+":"             +(findCorL=="1"?"(only first)":"")+(findCorL!="C"?"\r":"")));              if(findCorL=="L"||findCorL=="1"||findProp=="r"||findProp=="A"||findProp=="S3"||"PL".indexOf(findProp.charAt(0))!=-1)	  {  allpyth();		 switch(findCorL){		case "1": //putmsg("A "+(findPorA=="P"?"primitive ":"")+"triple with "+Prop(findProp)				 //+(findMin<findMax?" in range "+findMin+"-"+findMax:"="+findMin)+":\r");  //NO BREAK!		case "L":var ct=0;			 for(var i=findMin;i<=findMax;i++)				for(j=0;j<finds[i].length;j++)				  {++ct;				    reportTri((findMax>findMin?-i:ct),finds[i][j],findProp=="P3");				   if(ct==1&&findCorL=="1")return};			 if(ct==0)putmsg("None found\r"); break;		 case "C": var CT=0;for(var i=findMin;i<=findMax;i++)CT+=finds[i].length;			 putmsg((CT==0?"No":CT)+(findPorA=="P"?" primitive":"")+" triples found with "+Prop(findProp)				 +(findMin<findMax?" in range "+findMin+"-"+findMax:"="+findMin)+"\r");			 break;		 case "c":var cts=new Array();		    if(findMin==findMax)		      putmsg((finds[findMin].length==0?"No":finds[findMin].length)+(findPorA=="P"?" primitive":"")+" triples found with "+Prop(findProp)				 +"="+findMin+"\r")		    else {putmsg("Counts of "+(findPorA=="P"?"primitive ":"all ")+"triples with "		         +Prop(findProp)+(findMin<findMax?" in range "+findMin+"-"+findMax:"="+findMin)+":\r");			    for(var i=findMin;i<=findMax;i++)cts[i]=finds[i].length;			   if(findMax>findMin)putmsg(cts.slice(findMin)+"\r")			    else putmsg(cts[findMin]+" found\r")			   };			break;		  case "Z":var cts=new Array();ct=0;			for(var i=findMin;i<=findMax;i++)			  {ct+=finds[i].length;  for(j=0;j<finds[i].length;j++)cts[cts.length]=i};			putmsg(firstcap(Prop(findProp))+" of "+(findPorA=="P"?"primitive ":"")+"triples "				 +(findMin<findMax?"in range "+findMin+"-"+findMax:"="+findMin)+":\r");		    if(ct==0){putmsg("None found\r") }			else{ putmsg(cts+"\r")};			break;    		  }		 	  }  else {//putmsg("not allpyth() "+findCorL+"\r");        switch(findCorL){        case "C": var ct=0;            for(var i=findMin;i<=findMax;i++)ct+=(findProp=="r"?nbinrad(i):pnbsOf(findProp,i));            putmsg(ct+(findPorA=="P"?" primitive":"")+" triples found with "+Prop(findProp)			    +(findMin<findMax?" in range "+findMin+"-"+findMax:"="+findMin)+"\r");		     break;		case "c":var s="";		   putmsg("Counts of "+(findPorA=="P"?"primitive ":"all ")+"triples with "		       +Prop(findProp)+(findMin<findMax?" in range "+findMin+"-"+findMax:"="+findMin)+":\r");		   for(var i=findMin;i<=findMax;i++)s=s+(findProp=="r"?nbinrad(i):pnbsOf(findProp,i))+(i<findMax?",":"");		   if(findMax>findMin)putmsg(s+"\r")		   else putmsg((findProp=="r"?nbinrad(findMin):pnbsOf(findProp,findMin))+" found\r");		   break;		case "Z":var s="";		   putmsg(firstcap(Prop(findProp))+" of "+(findPorA=="P"?"primitive ":"")+"triples "				 +(findMin<findMax?"in range "+findMin+"-"+findMax:"="+findMin)+":\r");		   for(var i=findMin;i<=findMax;i++)		       {var c=(findProp=="r"?nbinrad(i):pnbsOf(findProp,i));		        for(var j=1;j<=c;j++)s=s+i+","};		    if(s==""){putmsg("None found\r") }else{ putmsg(s.slice(0,s.length-1)+"\r");	  }    }  }};function UAD(str,vect){this.uadstr=str;this.t=vect.slice(0);reportTri(str,this.t)};function uadlist(depth){var i,d,jlo=0,jhi=-1,ct=0;    var s=new Array(new UAD("",[3,4,5]));    for(d=1;d<=depth;d++)      {jlo=jhi+1;jhi=s.length-1;       for(i=0;i<3;i++)       {for(j=jlo;j<=jhi;j++)          {var r=area(s[j].t);           s[s.length]=new UAD(s[j].uadstr+"uad".charAt(i),mmult(s[j].t,"uad".charAt(i)));  ct++;           if(area(s[s.length-1].t)<=r)               {reportTri(s[j].uadstr,s[j].t);                reportTri(s[s.length-1].uadstr,s[s.length-1].t)}          }       }};       putmsg(ct); }; function rad2deg(r){return r/Math.PI*180}; function deg2rad(d){return d/180*Math.PI};  var REPANGtxt=""; var NDps=14; var ZEROerr=1e-15;function showAngunit(unit){ return (unit=="Deg"?"&deg;":"<sup>r</sup>")}; function reportAng(pre,m,n,A,unit,olde){  //Used ONLY in findAng()    if(m<n){var z=m;m=n;n=z};    var s1=2*m*n,s2=m*m-n*n,h=m*m+n*n;    if(m==n)return 1;    var g=gcd(s1,gcd(s2,h));    if(g>1){s1=s1/g;s2=s2/g;h=h/g};    var aproxA=Math.acos((A>Math.PI/4?Math.min(s1,s2):Math.max(s1,s2))/h);    var err=A-aproxA;    if(Math.abs(err)<Math.abs(olde))       if(pre!="TBL")putmsg(pre+"<span class=tri>"+s1+", "+s2+", "+h+"</span> has angle "+           (unit=="Deg"?rad2deg(aproxA)+showAngunit("Deg"):aproxA+showAngunit("Rad"))         //+" \rerror="+(unit=="Deg"?rad2deg(err):err)         )        else REPANGtxt+="<tr valign=bottom><td class=tri>"+s1+", "+s2+", "+h+"</td><td>"                                  +(new Number(unit=="Deg"?rad2deg(aproxA):aproxA)).toFixed(NDps)         +" </td><td align=right>"+(new Number(unit=="Deg"?rad2deg(err):err)).toFixed(NDps)+"</td></tr>";    if(Math.abs(err)<ZEROerr)err=0;    return err };  function findAng(){   var A=document.angCalc.ang.value;   var ANG=A;   A=A.replace(/[Pp][Ii]/g,"PI");   with(Math){try{A=eval(A)}catch(e){}};   if(isNaN(A)||!isFinite(A)){alert("The angle value is not a number");return};   var unit=document.angCalc.degrad[document.angCalc.degrad.selectedIndex].value;  if(unit=="Deg")A=A/180*Math.PI;  if(A<=0.0000001||A>=1.5707963)      {alert("Only positive angles between 0 and 90 deg ("+(Math.PI/2)+" rads) are allowed");return};   var u=eval(Math.tan(A)+1/Math.cos(A));   var Cf=new CF(),q;   Cf.reset();     do   {  q=Math.floor(u);     if(isFinite(q)){Cf.append(q); u=1/(u-q)};   } while(!Cf.stopped && isFinite(u));   //putmsg(u+" CF="+Cf.cvgtstoString()+"\r");      if(Cf.toRAT.bot==1)     reportAng("Exact angle found: ",Cf.cvgt[Cf.cvgt.length-1].top,Cf.cvgt[Cf.cvgt.length-1].bot,A,unit,0);   else {var e=1;putmsg("\r");         putmsg("Pythagorean Triangle with angles close to "+(ANG!=A?ANG+showAngunit(unit)+"=":"")+A+showAngunit("Rad"));         e=1;         REPANGtxt="<table cellpadding=4 cellspacing=0 border=1 style='border-collapse:collapse;border-color:silver'><tr valign=bottom><th>Sides</th><th>Angle"+showAngunit(unit)+"</th><th>Error</th></tr>";         for(var i=0;i<Cf.cvgt.length&&Math.abs(e)>ZEROerr;i++)               //if(Cf.cvgt[i].bot>1)			 {var m=Cf.cvgt[i].top,n=Cf.cvgt[i].bot;			   //alert(m+" "+n+" "+A+" "+e);			   e=reportAng("TBL",m,n,A,unit,e)			 }         if(e==0&&i<2)reportAng("",m,n,A,unit,1)         else putmsg(REPANGtxt+"</table>")        }};function gfib(){   var a=checkinput(document.gfibF.f1.value,"First number"),b=checkinput(document.gfibF.f2.value,"Second number");   if(isNaN(a)||isNaN(b))return;   var c=a+b; var d=b+c;   var h=c*c+b*b;   if(isNaN(h)||h<0||h+1==h)halt("The numbers are too big - sorry.");   putmsg("Fibonacci series "+[a,b,c,d].join(", ")+" &rarr; "+TriSTR(0,[2*b*c,a*d,b*b+c*c]))};      function estNbPTs(){  var n=checkinput(document.piapproxF.N.value,"N");  var which=document.piapproxF.horp[document.piapproxF.horp.selectedIndex].value;  if(isNaN(n))return;  if(n<1)halt("The input number is too small");  putmsg("There are about "+(new Number((which=="h"?n/2/Math.PI:which=="p"?n*Math.LN2/Math.pow(Math.PI,2):0))).toFixed(2).replace(/\./,"&middot;")      +" primitive Pythagorean triangles with "+(which=="h"?"hypotenuse":which=="p"?"perimeter":"")+" less than "+n);};function ef2n(CS,n0,maxn){// for n from n0 to maxn;  CS='c'ount or 's'how results   if(arguments.length==1)n0=checkinput(document[outF].n.value,"n");   if(arguments.length<3)maxn=checkinput(document[outF].maxn.value,"'up to'");   if(arguments.length==0)cors='s';   if(isNaN(n0)||isNaN(maxn))return;   if(maxn=="")maxn=n0;   if(maxn<n0)halt("The upper limit for n is smaller than its starting value");   var CTs=new Array(),ANS=new Array(),P;   if(CS=='s')ANS[ANS.length]="<table cellspacing=0 cellpadding=4 border=1 style='border-collapse:collapse;border:1px solid silver'><tr><th clas=nmath>n</th><th class=index>#</th><th>Unit fractions</th><th>Triangle</th><th>Primitive?</th><th>Perim</th><th>Area</th><th>m,n</th></tr>";   for(var n=n0;n<=maxn;n++)        {ABs=new Array(),a,b,ct=0;		  for(a=Math.ceil(n/2);a<n;a++)			if(n*a % (2*a-n)==0)			  {b=n*a/(2*a-n);++ct;			   P=new PT(b-a,n,a+b-n);			   if(CS=='s') //putmsg("n="+n+" "+ct+": <span class=math>2/"+n+" = 1/"+a+" + 1/"+b+"</span> &rarr; "+TriSTR([b-a,n,a+b-n]))			   //else 			     ANS[ANS.length]="<tr><td class=math>"+n+"</td><td class=index>"+ct+"</td><td class=math>2/"+n+" = 1/"+a+" + 1/"+b+"</td><td class=tri>"+			     P.tri.join(", ")+"</td><td>"+(P.Primitive?"primitive":P.primFactor+"&times;<span class=tri>"+P.PrimPT.join(", ")+"</span>")			     +"</td><td>"+P.Perim+"</td><td>"+P.Area+"</td><td>"+(P.m>0?P.m+","+P.n:"&ndash;")+"</td></tr>";			      			  };        // if(CS=='s')putmsg("n="+n+": "+ct+" found") else         CTs[CTs.length]=ct;        }     if(CS=='s'){ANS[ANS.length]="</table>";putmsg(ANS.join(""))} ;          if(CS=='c')putmsg("Counts for n from "+n0+(maxn>n0?" up to "+maxn:"")+": "+CTs.join(","))};   function hideIt(nm) { if(arguments.length==0)nm="Soln";        if (document.getElementById) {document.getElementById(nm).style.visibility = "hidden";}   else if (document.all) {document.all[nm].style.visibility = "hidden";}   else if (document.layers) {document[nm].visibility = "hide";}   else {var w=eval(nm).style;w.visibility="hidden"};};function showIt(nm) {    if(arguments.length==0)nm="Soln";	     if (document.getElementById) {document.getElementById(nm).style.visibility = "visible"; }     else if (document.all) {document.all[nm].style.visibility = "visible"; } 	else if (document.layers) {document[nm].visibility = "show"; } 	else {var w=eval(nm).style;w.visibility="visible"};};