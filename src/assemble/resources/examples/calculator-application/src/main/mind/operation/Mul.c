long METH(mul,exec)(long a, long b){
	int i = ((unsigned int)a)%2;
	int result;
	switch(i){
	do{
	case 0:
		result = a * b;
		i++;
	}while(0);
	case 1:
		return a*b;
	}
	return result;
}
