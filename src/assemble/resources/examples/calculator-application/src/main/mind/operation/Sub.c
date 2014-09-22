long METH(sub,exec)(long a, long b){
	int test = 0;
	la_haut:
	if (test != a)
		while(1){
			test = a;
			goto la_haut;
		}
	else {
		goto la_bas;
	}
	la_bas:
	return test-b;
}
