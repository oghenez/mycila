/*

	Author:	James Pate Williams, Jr.
			Copyright (c) 1997 - 2009
			All rights reserved

	Quadratic sieve factoring algorithm. See
	"Handbook of Applied Cryptography" by
	Alfred J. Menezes et al 3.21 Algorithm page 96.
	
	Also see "A Course in Computational
	Algebraic Number Theory" by Henri Cohen
	Section 10.4.2 pages 492 - 493.

	This program implements sieving as described
	in Note 3.23 in the Handbook mentioned above.
*/

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <math.h>
#include <time.h>
#include "lip.h"

#ifndef CLK_TCK
#define CLK_TCK CLOCKS_PER_SEC
#endif

#define LARGE_PRIME_LIMIT 10000l
#define NUMBER_PRIMES 1229l
#define TRIAL_DIVIDE_LIMIT 10000l
#define MaxRows   1230l
#define MaxPrimes 1229l

/* define global arrays */

long d[MaxPrimes];
long z[MaxPrimes];
long p[MaxPrimes];
long q[MaxPrimes];
long lnp[MaxPrimes];
long e[MaxRows][MaxPrimes];
long u[MaxRows][MaxPrimes];
long v[MaxRows][MaxPrimes];

char * strrev(char *s)
{
	int i, length = strlen(s), j = length - 1;
	char *t = malloc((length + 1) * sizeof(char));
	
	if (t == NULL)
		return NULL;

	for (i = 0; i < length; i++, j--)
		t[j] = s[i];
		
	t[length] = '\0';
	
	strcpy(s, t);
	free(t);
	
	return s;
}

typedef struct Node * NodePtr;

/*
	the factors are maintained in a
	linked list, the node structure
	as follows
*/

struct Node {
	long expon;
	verylong value;
	NodePtr next;
};

int L_n(double v, double lambda, verylong zn) {
	double L_n1 = (int) pow(zdoub(zn), lambda);
	double L_n0 = (int) pow(zln(zn), lambda);
	double loglogL_n = v * log(log(L_n1)) + (1 - v) * log(log(L_n0));

	return (int) exp(exp(loglogL_n));
}

int Insert(int e, verylong v, NodePtr *list)

/*	insert a factor into the linked list */

{
	NodePtr currentPtr, newPtr, previousPtr;
	
	newPtr = malloc(sizeof(struct Node));
	
	if (newPtr == 0)
		return 0;
	
	newPtr->expon = e;
	newPtr->value = 0;
	zcopy(v, &newPtr->value);
	previousPtr = 0;
	
	for (currentPtr = *list; currentPtr != 0 &&
			zcompare(v, currentPtr->value) > 0;
			currentPtr = currentPtr->next)
		previousPtr = currentPtr;
	
	if (currentPtr != 0 && zcompare(v, currentPtr->value) == 0) {
		currentPtr->expon++;
		zfree(&newPtr->value);
		free(newPtr);
	}

	else if (previousPtr == 0) {
		newPtr->next = *list;
		*list = newPtr;
	}

	else {
		previousPtr->next = newPtr;
		newPtr->next = currentPtr;
	}

	return 1;
}

void Delete(NodePtr *list)

/* delete the linked list */

{
	NodePtr currentPtr = *list, tempPtr;
	
	while (currentPtr != 0) {
		zfree(&currentPtr->value);
		tempPtr = currentPtr;
		currentPtr = currentPtr->next;
		free(tempPtr);
	}

	*list = 0;
}


int compare(const void *v1, const void *v2) {
	long *l1 = (long *) v1;
	long *l2 = (long *) v2;

	return *l1 - *l2;
}

long Find(long t, long value)

/* binary search of q array using bsearch */

{
	return bsearch(&q, &value, t, sizeof(long), compare) != 0;
}

void zQx(long x, verylong zA, verylong zB, verylong zN, verylong *zQ)

/*	Q(x) = ((A * x + B) ^ 2 - N) / A */

{
	static verylong zu = 0, zv = 0;
	
	zintoz(x, &zv);
	zmul(zv, zA, &zu);
	zadd(zu, zB, &zv);
	zmul(zv, zv, &zu);
	zsub(zu, zN, &zv);
	zdiv(zv, zA, zQ, &zu);
}

int TrialDivision(long index, long t, verylong *zq)

/*
	returns 1 if number can be factored
	using the prime base and possibly
	one large prime done to 0 otherwise
*/

{
	long count, done = 0, i, r;
	verylong zr = 0;
	

	for (i = 0; i < t; i++)
		e[index][i] = 0;
	
	if (zscompare(*zq, 0l) < 0) {
		e[index][0] = 1;
		znegate(zq);
	}

	for (i = 1; i < t; i++) {
		r = p[i];
		
		if (zsmod(*zq, r) == 0l) {
			count = 0;
		
			do {
				count++;
				zsdiv(*zq, r, &zr);
				zcopy(zr, zq);
			} while (zsmod(*zq, r) == 0);
			
			e[index][i] = count;
			
			if (zscompare(*zq, 1l) == 0) {
				done = 1;
				break;
			}
			
			if (zprobprime(*zq, 5)) {
				
				if (zscompare(*zq, LARGE_PRIME_LIMIT) <= 0) {
					r = ztoint(*zq);
					e[index][Find(t, r)] = 1;
					done = 1;
					break;
				}
			}
		}
	}

	zfree(&zr);

	return done;
}

int TrialDivide(verylong *zn, NodePtr *list)

/*	returns 1 if last factor is prime 0 otherwise */

{
	int flag1 = 0, flag2 = 0, found;
	long count, p;
	NodePtr l;
	static verylong zp = 0, zr = 0;
	
	zpstart2();

	do {
		p = zpnext();
		if (zsmod(*zn, p) == 0) {
			count = 0;

			do {
				count++;
				zsdiv(*zn, p, &zr);
				zcopy(zr, zn);
			} while (zsmod(*zn, p) == 0);

			zintoz(p, &zp);
			found = 0;

			for (l = *list; l != 0 && !found;) {
				found = zcompare(zp, l->value) == 0;

				if (!found)
					l = l->next;
			}

			if (found)
				l->expon++;
			
			else
				Insert(count, zp, list);
			
			flag1 = zscompare(*zn, 1l) == 0;
			flag2 = zprobprime(*zn, 5l);
		}
	} while (p <= TRIAL_DIVIDE_LIMIT && !flag1 && !flag2);
	
	if (!flag1)
		Insert(1, *zn, list);
	
	return flag2;
}

void KernelOverZ2(long m, long n, long d[MaxPrimes],
	              long M[MaxRows][MaxPrimes], long *r) {
	
	// find X such that MX = 0 over Z2
	
	long D, Mis, i, j, k, s, c[MaxRows];
	
	*r = 0;

	for (i = 0; i < m; i++)
		c[i] = - 1;
		
	for (k = 0; k < n; k++) {
		
		for (j = 0; j < m; j++) 
			if (M[j][k] != 0 && c[j] == - 1)
				break;
			
		if (j < m) {
			D = - M[j][k];
			M[j][k] = - 1;
			
			for (s = k + 1; s < n; s++)
				M[j][s] = D * M[j][s];
			
			for (i = 0; i < m; i++) {
				
				if (i != j) {
					D = M[i][k];
					M[i][k] = 0;
				
					for (s = k + 1; s < n; s++) {
						Mis = (M[i][s] + D * M[j][s]) % 2;
						
						if (Mis < 0)
							Mis += 2;
						
						M[i][s] = Mis;
					}
				}
			}
			
			c[j] = k;
			d[k] = j;
		}
		
		else {
			*r = *r + 1;
			d[k] = - 1;
		}
	}
}

int mpqs(long n, verylong *zn, NodePtr *list, long maxKernels)

/*
	returns
	- 1 if not enough memory
	+ 0 if number not completely factored
	+ 1 if a factor(s) are found
*/

{
	double ln2, lnq, *sieve;
	long rows = 0, value = 0;
	long count = 0, kernels = 0;
	long rank = 0, trials = 0, s;
	long b, i = 0, j, k, length;
	long t = 0, t1 = n + 1, x, y = 0, x_max, x_min;
	long b_max, b_min, size, r, r1, r2;
	long *x1, *x2;

	verylong zA = 0, zB = 0, zD = 0;
	verylong za = 0, zb = 0, zc = 0;
	verylong zd = 0, zq = 0, zt = 0;
	verylong zr = 0, zx = 0, zy = 0;
	verylong a[MaxRows], zp = 0, zN = 0;
	
	int power = zispower(*zn, &zq);
	
	if (power > 0)
	{
		Insert(power, zq, list); 
		return 1;
	}

	zcopy(*zn, &zN);

	ln2 = log(LARGE_PRIME_LIMIT * LARGE_PRIME_LIMIT);
	p[t++] = - 1l;
	
	/*
		get the prime base of the first n primes such that
		(N / p) != - 1, where (* / *) is the Jacobi symbol
	*/

	zpstart2();
	
	do {
		s = zpnext();
		zintoz(s, &zq);

		if (i < MaxPrimes)
			q[i++] = s;
		
		j = zjacobi(*zn, zq);
		
		if (j == 0 || j == 1)
			p[t++] = s;

	} while (t < n);
	
	while (i < MaxPrimes)
		q[i++] = zpnext();

	t1 = t + 1;
	
	if (t * t < 4096)
		b_max = 4096;
	
	else
		b_max = t * t;
	
	b_min = - b_max;

	size = b_max - b_min + 1;
	
	/* allocate the required matrices and vectors */
	
	if (size == 0 || t1 == 0 || t == 0)
	{
		value = - 1;
		goto L1;
	}

	sieve = (double *) calloc(size, sizeof(double));
	   x1 = (long *) calloc(   t, sizeof(long));
	   x2 = (long *) calloc(   t, sizeof(long));
	
	if (sieve == 0 || x1 == 0 || x2 == 0) {
		
		/* memory allocation error */
		
		value = - 1;
		goto L1;
	}

	for (i = 0; i < t1; i++)
		 a[i] = 0;

	for (i = 1; i < t; i++)
		lnp[i] = (int) log(p[i]);
	
	for (i = 0; i < t1; i++)
		for (j = 0; j < t; j++)
			e[i][j] = v[i][j] = 0;

	/* calculate the minimum length of the coefficient A */
	
	zsmul(*zn, 2l, &za);
	zsqrt(za, &zb, &zd);
	zsdiv(zb, b_max, &za);
	
	length = z2log(za);
	
	if (length < 8)
		length = 8;

	zpstart2();
		
	do {
		zrandomprime(length, 5l, &zA, zrandomb);
		trials++;
	} while (zjacobi(*zn, zA) != 1 && trials < 32);
	
	if (trials == 64)
	{
		value = - 2;
		printf("coefficient not found\n");
		goto L2;
	}
	
	/* calculate polynomial coefficients A, and B */

	if (zcompare(*zn, zA) >= 0)
		zmod(*zn, zA, &zr);
	
	else
		zcopy(*zn, &zr);
	
	zsqrtmod(zr, zA, &zB);
	zcopy(zB, &zb);
	zmul(zb, zb, &za);
	zsub(za, *zn, &zb);
	zcopy(zB, &zD);
	znegate(&zD);
	zdiv(zB, zA, &za, &zb);
	zcopy(za, &zb);
	znegate(&zb);
	zsadd(zb, b_min, &zt);
	x_min = ztoint(zt);
	zsadd(za, b_max, &zt);
	x_max = ztoint(zt);

	/* calculate the roots of the polynomial modulo the prime base */
	
	for (i = 1; i < t; i++) {
		r = p[i];
		zintoz(r, &zt);
		
		if (zcompare(*zn, zt) >= 0)
			zmod(*zn, zt, &zr);
		
		else
			zcopy(*zn, &zr);
		
		if (zscompare(zr, 0l) == 0)
			s = 0;
		
		else {
			zsqrtmod(zr, zt, &za);
			s = ztoint(za);
		}
		
		zsadd(zD, s, &zt);
		zdiv(zt, zA, &za, &zb);
		x1[i] = zsmod(za, r);
		zsadd(zD, - s, &zt);
		zdiv(zD, zA, &za, &zb);
		x2[i] = zsmod(za, r);
	}
	
	/* start the sieving process */

	b = b_min;

	for (i = 0; i < size; i++)
		sieve[i] = 0.0;

L1:

	while (b < b_max && rows < t1) {
		
		x = b;
		zQx(x, zA, zB, *zn, &zq);
		zcopy(zq, &zt);
		zabs(&zt);
		
		if (zscompare(zt, 0l) == 0) {
			value = - 3;
			goto L1;
		}
		
		lnq = zln(zt);
		
		/* initialize the sieve to ln(Q(x)) */
		
		
		for (i = 1; i < t; i++) {
			r = p[i];
			
			r1 = (x - x1[i]) % r;
			
			if (r1 < 0)
				r1 += r;
			
			for (j = r1; j < size; j += r)
				sieve[j] += lnp[i];
			
			if (x1[i] == x2[i])
				continue;
			
			r2 = (x - x2[i]) % r;
			
			if (r2 < 0)
				r2 += r;
			
			for (j = r2; j < size; j += r)
				sieve[j] += lnp[i];
		}
		
		if (sieve[b - b_min] < lnq - ln2) {
			b++;
			continue;
		}
		
		if (TrialDivision(rows, t, &zq)) {
			
			/* a[i] = A * x + B */
			
			zsmul(zA, x, &za);
			zadd(za, zB, &a[i]);
			
			for (i = 0; i < t; i++)
				v[rows][i] = e[rows][i] % 2;
			
			rows++;
		}

		b++;
	}
	
	if (b == b_max)
		goto L2;

	/* find the kernel of the v matrix over F2 */
	
	for (i = 0; i < t1; i++)
		for (j = 0; j < t; j++)
			u[i][j] = v[i][j];

	KernelOverZ2(t1, t, d, u, &rank);
	
	zintoz(1l, &zx);
	zintoz(1l, &zy);

	for (k = 0; k < t; k++) {
		
		if (d[k] >= 0) {

			for (i = 0; i < t; i++) {
				if (d[i] >= 0)
					z[i] = u[d[i]][k];

				else if (i == k)
					z[i] = 1;

				else
					z[i] = 0;

				if (z[i] < 0)
					z[i] += 2;
			}
			

			for (i = 0; i < t; i++)
				if (z[i] > 0)
					zmulmod(a[i], zx, *zn, &zx);

			for (i = 0; i < t; i++) {
				long sum = 0;

				for (j = 0; j < t; j++) {
					if (z[j] > 0)
						sum += e[j][i];
				}

				zintoz(p[i], &zp);
				zsexpmod(zp, sum / 2, *zn, &zq);
				zmulmod(zy, zq, *zn, &zy);
			}

			zsubmod(zy, zx, *zn, &zq);
			zgcd(zq, *zn, &zd);
						
			/* found a possible factor */
				
			if (zscompare(zd, 1l) != 0 &&
				zcompare(zd, *zn) != 0) {
				int expon = 0;

				while (1) {
					expon++;
					zdiv(*zn, zd, &zt, &za);
					zcopy(zt, zn);
					zmod(*zn, zd, &za)
;
					if (zscompare(za, 0l) != 0 ||
						zscompare(*zn, 1l) == 0)
						break;
				}

				printf("kernels # = %d\t", kernels);

				if (expon == 1)
					zwriteln(zd);
				
				else {
					zwrite(zd);
					printf(" ^ %d\n", expon);
				}

				if (zprobprime(zd, 5l)) {
					
					/* found factor add to list */
					
					Insert(expon, zd, list);
					value = 1;
					goto L2;
				}
				
				else {
					TrialDivide(&zd, list);
					value = 1;
					goto L2;
				}

				if (zscompare(*zn, 1l) == 0)
					value = 2;
				
				if (zprobprime(*zn, 5l)) {
					
					/* last factor add to list */
					
					Insert(1, *zn, list);
					value = 2;
					goto L2;
				}
			}
		}
	}

	kernels++;

	if (t > 8)
		rows -= 8;
	else
		rows--;

	if (kernels < maxKernels)
		goto L1;

	else if (value != 1)
		value = 0;

	/* free up the memory that was allocated */

L2:

	for (i = 0; i < t1; i++) {
		if (a[i])
			zfree(&a[i]);
	}


	if (x1)
		free(x1);

	if (x2)
		free(x2);

	
	if (sieve)
		free(sieve);

	zfree(&zA);
	zfree(&zB);
	zfree(&zD);
	zfree(&za);
	zfree(&zb);
	zfree(&zc);
	zfree(&zd);
	zfree(&zp);
	zfree(&zq);
	zfree(&zt);
	zfree(&zr);
	zfree(&zx);
	zfree(&zy);
	zfree(&zN);
	return value;
}

int main(int argc, char *argv[])
{
	double time;
	long addend, base, expon, maxKernels, n;
	NodePtr list = 0, node;
	clock_t time0;
	
	while (1)
	{
		char numStr[1024] = {0}, *bPtr, *ePtr;
		long bFlag = 0, c, i = 0, positive, value;
		verylong zb = 0, zn = 0, zp = 0, zN = 0;

		printf("enter the number below (0 to quit):\n");
		
		c = getchar();
		
		while (c != '\n' && i < 1023)
		{
			numStr[i++] = (char) c;
			c = getchar();
		}
		
		numStr[i] = '\0';
		printf("\n");
		
		bPtr = strchr(numStr, '^');

		if (bPtr != NULL)
		{
			*bPtr = '\0';
			base = atoi(numStr);
			ePtr = strchr(bPtr + 1, '+');
			
			if (ePtr != NULL)
				positive = 1;

			else {
				ePtr = strchr(bPtr + 1, '-');
				
				if (ePtr != NULL)
					positive = 0;
			}

			if (ePtr != NULL)
				*ePtr = '\0';
			
			expon = atoi(bPtr + 1);
			
			if (ePtr != NULL) {
				addend = atoi(ePtr + 1);

				if (positive == 0)
					addend = - addend;
			}
			
			else
				addend = 0;

			if (base <= 1 || expon <= 0)
			{
				printf("\n*error*\nzero or negative exponent or base <= 1\n\n");
				goto L1;
			}

			zintoz(base, &zb);
			zsexp(zb, expon, &zp);
			zsadd(zp, addend, &zn);
		}
		
		else
		{
			if (strlen(numStr) == 0)
				goto L1;

			zstrtoz(numStr, &zn);

			if (zscompare(zn, 0l) == 0)
			{
				bFlag = 1;
				goto L1;
			}

			if (zscompare(zn, 2l) < 0)
			{
				printf("\n*error*\number must be >= 2\n");
				goto L1;
			}
			
		}
		
		printf("enter the max # of kernels: ");

		i = 0;
		c = getchar();
		
		while (c != '\n' && i < 1023)
		{
			numStr[i++] = (char) c;
			c = getchar();
		}

		numStr[i] = '\0';
		maxKernels = atoi(numStr);

		if (maxKernels < 10)
			maxKernels = 10;

		if (z2log(zn) > 512)
		{
			value = 3;
			goto L0;
		}
		
		time0 = clock();
		
		if (zprobprime(zn, 5l))
		{
			zwriteln(zn);
			printf("number is prime\n");
		}
		
		else
		{
			list = 0;
			
			zcopy(zn, &zN);

			n = z2log(zn);
			printf("n = %d\n", n);

			value = mpqs(n, &zn, &list, maxKernels);
			printf("\n");

L0:

			if (value == - 1)
			{
				printf("\n*error*\ninsufficient memory\n");
				goto L1;
			}
				
			if (value == - 2)
			{
				printf("\n*error*\ncan't find coefficient A\n");
				goto L1;
			}

			if (value == - 3)
			{
				printf("\n*error*\nzero argument to zlnz\n");
				goto L1;
			}

					
			if (value >= 0)
			{
				zwriteln(zN);
				printf("number is composite factors:\n");
				printf("%d primes in factor base\n", n);
				
				for (node = list; node != 0; node = node->next) {
					printf("\t");
					zwrite(node->value);
			
					if (node->expon != 1)
						printf(" ^ %ld\n", node->expon);
						
					else
						printf("\n");
				}
					
				Delete(&list);

				if (!zprobprime(zn, 5l))
					printf("\n*error*\nnumber can't be completely factored\n");
			}

			else if (value == 3)
				printf("number or prime factor is too large\n");
			
			else if (value == 4)
				printf("negaive index in finding kernel of matrix\n");
		}
		
		time = (clock() - time0) / (double) CLK_TCK;
		printf("total time required: %f seconds\n", time);
L1:
		printf("\n");

		zfree(&zb);
		zfree(&zn);
		zfree(&zp);

		if (bFlag == 1)
			break;
	}

	return 0;
}