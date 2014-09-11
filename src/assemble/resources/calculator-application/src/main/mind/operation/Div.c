#include <stdio.h>

long METH(div,exec)(long a, long b){
	long result;
	if (b!=0)
		result = a/b;
		return result;
	printf("Cannot divide by zero !");
	return 0;
}

