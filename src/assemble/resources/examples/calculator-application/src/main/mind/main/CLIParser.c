#include <stdio.h>
#include <string.h>

int main(int argc, char** argv){

	long result;
	long a = atol(argv[1]);
	long b = atol(argv[3]);
	if (!strcmp(argv[2],"+")) {
		result = CALL(add,exec)(a,b);
	}
	if (!strcmp(argv[2],"*")) {
		result = CALL(mul,exec)(a,b);
	}
	if (!strcmp(argv[2],"-")) {
		result = CALL(sub,exec)(a,b);
	}
	if (!strcmp(argv[2],"/")) {
		result = CALL(div,exec)(a,b);
	}
	printf("%ld\n", result);
	return 0;
}
