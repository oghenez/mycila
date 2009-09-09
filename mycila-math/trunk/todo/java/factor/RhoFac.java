import java.util.Vector;

/**
 * This class implemtents the Rho Factoring Algorithm by Pollard
 *
 *
 *
 *
 * @author Kristijan Dragicevic
 * @version xx.xx.04
 */

class RhoFac
{
    int n;     // number to factorize
    int a = 2, b = 2;
    int d, result;     // greatest common divisor of (a-b) and n
    Vector aList = new Vector();
    Vector bList = new Vector();
    Vector dList = new Vector();
    boolean success = false, b1 = true;
    
    public RhoFac(int number, int coeff) {
	success = false;
	b1 = true;
	result = Factorize(2, 2, number, coeff);
	return;
    }

    public boolean succeeded(){
	return success;
    }

    public int sizeof(Vector v){
	return v.size();
    }

    private int Factorize(int a,int  b,int n, int c){

	while(b1 == true) {
	    a = (a * a + c) % n;
	    b = (b * b + c) % n;
	    b = (b * b + c) % n;
	    if (a - b > 0)	    
		d = GCD(a - b, n);
	    else
		d = GCD(-(a - b), n);
	    
	    aList.addElement(new Integer(a));
	    bList.addElement(new Integer(b));
	    dList.addElement(new Integer(d));
	    
	    if(d > 1) {;
		b1 = false;
		
		if( d < n)
		    success = true;
		else
		    success = false;			
	    }
	    
	}
	return d;
    }
    
    private int GCD(int amb, int num){
	int x1 = 0, x2 = 1, y1 = 1, y2 = 0, q, r, x, y;
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
