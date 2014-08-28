void toto(void) {
	//basic
	{
		char c;
		short s;
		int i;
		long l;
		float f;
		double d;
		CALL(basicTypesItf,v) (  );
		c = CALL(basicTypesItf,c) ( c);
		s = CALL(basicTypesItf,s) (s);
		i = CALL(basicTypesItf,i) ( i );
		f = CALL(basicTypesItf,_f) (f);
		d =CALL(basicTypesItf,_d) (d);
		CALL(basicTypesItf,many) (c,s,i,l);
	}

	//unsigned
	{
		unsigned char uc;
		unsigned short us;
		unsigned int ui;
		uc = CALL(basicTypesItf,uc) (uc);
		us = CALL(basicTypesItf,us) (us);
		ui = CALL(basicTypesItf,ui) (ui);
	}

	//pointers
	{
		int *pi;
		int **ppi;
		CALL(basicTypesItf,pi) (pi);
		CALL(basicTypesItf,ppi) (ppi);
	}
	//table
	{
		int ti[];
		int tti[][10];
		CALL(basicTypesItf,ti) (ti);
		CALL(basicTypesItf,tti) (tti);
	}

	//constant
	{
		const int ci;
		CALL(basicTypesItf,ci) (ci);
	}
	//mixing
	{
		int * const cpi;
		const unsigned long * cpui;
		char * ptc[];
		short * const ptcs[];
		const long ** const **  const ppcppctttl [][9][3];
		CALL(basicTypesItf,mix) ( cpi, cpui, ptc, ptcs, ppcppctttl);
	}
}
