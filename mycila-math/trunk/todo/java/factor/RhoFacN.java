import java.util.Vector;

/**
 * This class implemtents the Rho Factoring Algorithm by Pollard
 *
 *
 *
 *
 * @author Kristijan Dragicevic
 * @version xx.xx.04
 * changed by Klaus Reinhardt July 2 2007
 */

class RhoFacN
{
    long n;     // number to factorize
    long a = 2, b = 2;
    long d, result;     // greatest common divisor of (a-b) and n
    Vector aList = new Vector();
    Vector bList = new Vector();
    Vector fList = new Vector();
    Vector gList = new Vector();
    Vector hList = new Vector();
    Vector iList = new Vector();
    Vector dList = new Vector();
    Vector dcList = new Vector();
    boolean success = false, b1 = true;
    
    public RhoFacN(long number, long coeff) {
	success = false;
	b1 = true;
	result = Factorize(2, 2, number, coeff);
	return;
    }

    public boolean succeeded(){
	return success;
    }

    public long sizeof(Vector v){
	return v.size();
    }

    private long Factorize(long a,long  b,long n, long c){

        long z = 36656891 % n;
	z=(z*10000 )% n;
	z=(z*10000 + 34307200) % n;
	while(b1 == true) {
	    a = (a * a + c) % n;
	    b = (b * b + c) % n;
	    b = (b * b + c) % n;
	    long e = (a - b) % n;     /** changed here  */
	    if (e<0) e+=n;
	    long f = e * (e + 367) % n;
	    long g = f * (f + 33642) % n;
	    long h = (g * ((g + 125167320) % n)) % n;
	    long i = (h * ((h + z) % n )) % n;
	    d = GCD(h, n);
	    aList.addElement(new Long(a));
	    bList.addElement(new Long(b));
	    fList.addElement(new Long(f));
	    gList.addElement(new Long(g));
	    hList.addElement(new Long(h));
	    iList.addElement(new Long(i));
	    dList.addElement(new Long(d));
	    
	    if(d > 1) {;
	      b1 = false;
	      success = true;
	      if( d == n) { d = GCD(h, n);
		if( d == 1) { d = GCD(g+46739880, n);
		  if( d == 1) { d = GCD(f+2520, n);
		    if( d == 1) { d = GCD(e+133, n);}
		    else if( d == n) { d = GCD(e+7, n);}
		  }
		  else if( d == n) { d = GCD(f+1452, n);
		    if( d == 1) { d = GCD(e+145, n);}
		    else if( d == n) { d = GCD(e+4, n);}
		} }
	        else if( d == n) { d = GCD(g, n);
		  if( d == 1) { d = GCD(f+4260, n);
		    if( d == 1) { d = GCD(e+118, n);}
		    else if( d == n) { d = GCD(e+12, n);}
		  }
		  else if( d == n) { d = GCD(f, n);
		    if( d == 1) { d = GCD(e+178, n);}
		    else if( d == n) { d = GCD(e, n);}
	      } } }
	      if( d == n) { d = GCD(e, n);}
	      if( d == n) { success = false;}
	      if( d == 1) { b1 = true;}			
	    }

	    /** long f = e * (e + 11) % n;
	    long g = f * (f + 28) % n;
	    long h = g * (g + 180) % n;
	    d = GCD(h, n);
	    
	    if(d > 1) {;
		b1 = false;
		success = true;
		if( d == n) { d = GCD(g, n);
		  if( d == 1) { d = GCD(f+10, n);
		    if( d == 1) { d = GCD(e+2, n);}
		    else if( d == n) { d = GCD(e+1, n);}
		  }
		  else if( d == n) { d = GCD(f, n);
		    if( d == 1) { d = GCD(e+4, n);}
		    else if( d == n) { d = GCD(e, n);}
		} }
		if( d == n) { d = GCD(e, n);}
		if( d == n) { success = false;}
		if( d == 1) { b1 = true;}  */
	    
	    dcList.addElement(new Long(d));
	    
	}
	return d;
    }
    
    private long GCD(long amb, long num){
	long x1 = 0, x2 = 1, y1 = 1, y2 = 0, q, r, x, y;
	if(amb == 0)
	    return num;

	while(amb > 0){
	    q = num / amb;
	    r = num - q * amb;
	    x = x2 - q * x1;
	    y = y2 - q * y1;
	    num = amb;
	    amb = r;
	    x2 = x1;
	    x1 = x;
	    y2 = y1;
	    y1 = y;
	}
	return num;
    }

}
