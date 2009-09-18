/*
** Pascal Sebah : July 1999
** 
** Subject:
**
**    A very easy program to compute Log(2) with many digits.
**    No optimisations, no tricks, just a basic program to learn how
**    to compute in multiprecision.  
**
** Formula:
**
**    Log(2) = 2*atanh(1/3) with
**      atanh(x) = 1/2*Log((1+x)/(1-x)) = x + x^3/3 + x^5/5 + ...
**
** Data:
**
**    A big real (or multiprecision real) is defined in base B as:
**      X = x(0) + x(1)/B^1 + ... + x(n-1)/B^(n-1)
**      where 0<=x(i)<B
**
** Results: (PentiumII, 450Mhz)
**    
**    1000   decimals :   0.06seconds
**    10000  decimals :   7.4s
**    100000 decimals : 749.0s
**
** With a little work it's possible to reduce those computation
** times by a factor 3 and more.
*/
#include <stdio.h>
#include <malloc.h>
long B=10000; /* Working base */
long LB=4;    /* Log10(base)  */
/*
** Set the big real x to the small integer Integer 
*/
void SetToInteger (long n, long *x, long Integer) {
  long i;
  for (i=1; i<n; i++) x[i] = 0;
  x[0] = Integer;
}
/*
** Is the big real x equal to zero ?
*/
long IsZero (long n, long *x) {
  long i;
  for (i=0; i<n; i++)  
    if (x[i])	return 0;
	return 1;
}
/*
** Addition of big reals : x += y
**  Like school addition with carry management
*/
void Add (long n, long *x, long *y) {
  long carry=0, i;
  for (i=n-1; i>=0; i--) {
    x[i] += y[i]+carry;
    if (x[i]<B) carry = 0;
    else {
      carry = 1;
      x[i] -= B;
    }
  }  
}
/*
** Multiplication of the big real x by the integer q 
** x = x*q.
**  Like school multiplication with carry management
*/
void Mul (long n, long *x, long q) {
  long carry = 0, xi, i;
  for (i=n-1; i>=0; i--) {
    xi  = x[i]*q;		
    xi += carry;		
    if (xi>=B) {
      carry = xi/B;
      xi -= (carry*B);
    }
    else 
      carry = 0;
    x[i] = xi;
	}  
}
/*
** Division of the big real x by the integer d 
** The result is y=x/d.
**  Like school division with carry management
*/
void Div (long n, long *x, long d, long *y) {
  long carry=0, xi, q, i;
  for (i=0; i<n; i++) {
    xi    = x[i]+carry*B;
    q     = xi/d;
    carry = xi-q*d;   
    y[i]  = q;        
  }  
}
/*
** Print the big real x
*/
void Print (long n, long *x) {
  long i; 
  printf ("%d.", x[0]);
  for (i=1; i<n; i++) {
    printf ("%.4d", x[i]);
    if (i%25==0) printf ("%8d\n", i*4);
  }
  printf ("\n");
}
/*
** Computation of the constant Log(2)
*/
void main () {
  long NbDigits=10000, size=1+NbDigits/LB;
  long *l2 = (long *)malloc(size*sizeof(long));
  long *uk = (long *)malloc(size*sizeof(long));
  long *vk = (long *)malloc(size*sizeof(long)); 
  long k=3, p=3, p2=p*p; 
  /*
  ** Formula used:
  **
  **    Log(2) = 2*atanh(1/3) = Log((1+1/3)/(1-1/3)) with
  **     atanh(x) = 1/2*Log((1+x)/(1-x)) = x + x^3/3 + x^5/5 + ...
  **     and x=1/p (here p=3)
  **
  ** You may also try:
  **
  **    Log(2) = 4*atanh(1/6)+2*atanh(1/99)
  **    (two terms one with p=6 and the other with p=99)
  */
  SetToInteger (size, l2, 1);	/* l2 = x = 1/p */
  Div (size, l2, p, l2);
  SetToInteger (size, uk, 1);	/* uk = x = 1/p */
  Div (size, uk, p, uk);
  while (!IsZero(size, uk)) {
    Div (size, uk, p2, uk);   /* uk = u(k-1)/(p^2) */
    Div (size, uk, k, vk);    /* vk = uk/k         */
    Add (size, l2, vk);       /* l2 = l2+vk        */
    k+=2;
  }
  Mul (size, l2, 2); /* l2 = 2*l2 */
  Print (size, l2);  /* Print out of Log(2) */
  free (l2);
 	free (uk);
 	free (vk);
}

