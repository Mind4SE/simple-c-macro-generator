long METH(add,exec)(long a, long b){
	double i, result=a;
	if (b>0) {
		result = CALL(add,exec)(a,b-1);
		result +=1;
	}
	return result;
}
