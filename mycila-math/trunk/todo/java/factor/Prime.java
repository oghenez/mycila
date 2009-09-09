import java.util.Vector;

/**
 * This class implements the Pratt prime number test.
 *
 * Pratt test:
 * A number p>1 is prime, if a number r exists, with 1<r<p, and conditions
 * 1) r^(p-1) mod p = 1
 * 2) for all prime factors q of p-1 is:  r^((p-1)/q) mod p <> 1. 
 * are met.
 * This is tested recursive for all q.
 *
 * @author   Holger Schmid
 * @version  07.01.2000
 */
class Prime
{
   int p;                       // numer to test
	boolean isPrime = false;     // test result
   Vector rList = new Vector(); // list of r, that meet condition 1)
   Vector qList = new Vector(); // prime factors of p-1
   int rZeuge;
	
   /** Constructs a new <code>number</code> and test if it's prime.
    *  @param   number   the number to test
    */
	public Prime(int number)
   {
      p=number;
   	LucasFindR();
   	PFZ();
   	LucasTestFast();
      return;
   }
   
   /** Returns if this number is prime.
    *  @return   result of prime test
    */
   public boolean isPrime()
   {
      return isPrime;
   }

	public int getRZeuge()
   {
   	return rZeuge;
   }
	
   /** Returns all prime factors of p-1
    *  @return   list of prime factors
    */
	public Vector getQList()
   {
   	return qList;
   }
	
   /** Returns all r, that meet condition 1)
    *  @return   list of r
    */
   public Vector getRList()
   {
      return rList;
   }
	
   /** Executes a full Lucas test.
    *  @return   list of results for output purpose (one list of all q for each r)
    */
   public Vector LucasComplete()
   {
   	Vector Result = new Vector();
   	
      int i, j, q, r;
   		
   	q=0;
   	
   	for (i=0; i<rList.size(); i++)
      {// test all r 
         Result.addElement(new Vector());
      	r = ((Integer)rList.elementAt(i)).intValue();
         for (j=0; j<qList.size(); j++)
         {// and all q
               q=((Integer)qList.elementAt(j)).intValue();
               if (Formel(r,(p-1)/q,p)==1)
               {// condition 2 is not met
                  ((Vector)Result.lastElement()).addElement("-");
               }
            	else
               {
               	((Vector)Result.lastElement()).addElement("+");
               }
         }
      }
   	return Result;
   }
	
   /** Finds all r that meet condition 1)
    *  @return   list of r, that meet condition 1)
    */
	public Vector LucasFindR()
   {
   	int r;

   	for (r=2; r<p; r++)
      {// test alle possible r with 1<r<p
         if (Bedingung1(r))
         {     
         	rList.addElement(new Integer(r));
         }
      }
   	return rList;
   }
	
   /** Compute all prime factors q of p-1
    *  @return   list of prime factors
    */
   public Vector PFZ()
   {
   	qList = PFZ(p-1);
   	return qList;
   }
	
   /** Executes a fast Lucas test. Only the result p is (not) prime is stored.
    */
	private void LucasTestFast()
   {
   	int r;
   	
      for (r=2; r<p; r++)
      {// test alle possible r with 1<r<p
         if (Bedingung1(r))
         {// check condition 2) only if condition 1) is met
            if (Bedingung2(r))
            {// p is prime
            	rZeuge = r;
               isPrime = true;
            	return;
            }
         }
      	else
         {// abort test
         	return;
         }
      }
   	return;
   }

	/** Check condition 1)
    *  @return   true if condition is met
    */
	private boolean Bedingung1(int r)
   {
      if (Formel(r,p-1,p)==1)
         return true;
      else
         return false;
   }

   /** Check condition 2)
    *  @return   true if condition is met
    */
   private boolean Bedingung2(int r)
   {
      int i, q=0;
   	
      for (i=0; i<qList.size(); i++)
      {// test condition 2) for all q
      	if (q!=((Integer)qList.elementAt(i)).intValue())
         {// test only if new prime factor
      	   q=((Integer)qList.elementAt(i)).intValue();
            if (Formel(r,(p-1)/q,p)==1)
               return false;
         }
      }
      return true;
   }

   /** Compute all prime factors of <code>x</code>
    *  @return   list of prime factors
    */
	private Vector PFZ(int x)
   {
   	int q;
   	Vector PFTemp = new Vector(10);

      for (q=2; q<x; q++)
      {// test all possible factors
         if (x%q == 0)
         {// q is prime factor of x
            PFTemp.addElement(new Integer(q));
            // eliminate this factor
            x/=q;
         	q--;
         }
      }
   	PFTemp.addElement(new Integer(x));
   	return PFTemp;
   }

   /** Compute <code>(a^b)%p</code>
    *  @param   a   value for formula
    *  @param   b   value for formula
    *  @param   p   value for formula
    */
	int Formel(int a, int b, int c)
   {
      int k, bitmask, d, i;
      k=30; bitmask=0x40000000;
      d=1;
      for (i=k;i>=0;i--)
      {
         d = (d*d)%c;
         if ((b & bitmask) > 0)
            {
               d=(d*a)%c;
            }
         bitmask /= 2;
      }
   return (d);
   }
}