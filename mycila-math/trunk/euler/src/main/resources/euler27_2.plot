set terminal svg
set output "euler27_2.svg"
set title "Euler's quadratic and composites"
set xlabel "x"
set ylabel "p(x)"
set xrange [0:1000]
set yrange [0:900000]
set xrange [0:80]
set yrange [0:1650]
plot 	x*x+x+41 		title "p(x)=x*x+x+41" with lines linewidth 2, \
 		x*x-79*x+1601	title "p(x)=x*x-79*x+1601" with lines linewidth 2, \
 		x*x-61*x+971 	title "p(x)=x*x-61*x+971" with lines linewidth 2

