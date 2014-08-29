void toto(void) {
	//basic
	{
		char c=0;
		short s=0;
		int i=0;
		long l=0;
		float f=0.0;
		double d=0.0;
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
		unsigned char uc=0;
		unsigned short us=0;
		unsigned int ui=0;
		uc = CALL(basicTypesItf,uc) (uc);
		us = CALL(basicTypesItf,us) (us);
		ui = CALL(basicTypesItf,ui) (ui);
	}

	//pointers
	{
		int *pi=0;
		int **ppi=0;
		CALL(basicTypesItf,pi) (pi);
		CALL(basicTypesItf,ppi) (ppi);
	}
	//table
	{
		int ti[2];
		int tti[3][10];
		CALL(basicTypesItf,ti) (ti);
		CALL(basicTypesItf,tti) (tti);
	}

	//constant
	{
		const int ci=0;
		CALL(basicTypesItf,ci) (ci);
	}
	//mixing
	{
		int * const cpi=0;
		const unsigned long * cpui=0;
		char * ptc[4];
		short * const ptcs[5];
		const long ** const **  const ppcppctttl [6][9][3];
		CALL(basicTypesItf,mix) ( cpi, cpui, ptc, ptcs, ppcppctttl);
	}
}
