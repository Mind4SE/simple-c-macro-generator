void toto(void) {
	//basic
	{
		char c=0;
		short s=0;
		int i=0;
		long l=0;
		float f=0.0;
		double d=0.0;
		CALL(basicTypesItf[0],v) (  );
		c = CALL(basicTypesItf[0],c) ( c);
		s = CALL(basicTypesItf[1],s) (s);
		i = CALL(basicTypesItf[2],i) ( i );
		f = CALL(basicTypesItf[3],_f) (f);
		d =CALL(basicTypesItf[4],_d) (d);
		CALL(basicTypesItf[i],many) (c,s,i,l);
	}

	//unsigned
	{
		unsigned char uc=0;
		unsigned short us=0;
		unsigned int ui=0;
		uc = CALL(basicTypesItf[5],uc) (uc);
		us = CALL(basicTypesItf[6],us) (us);
		ui = CALL(basicTypesItf[7],ui) (ui);
	}

	//pointers
	{
		int *pi=0;
		int **ppi=0;
		CALL(basicTypesItf[8],pi) (pi);
		CALL(basicTypesItf[9],ppi) (ppi);
	}
	//table
	{
		int ti[2];
		int tti[3][10];
		CALL(basicTypesItf[ti[0]],ti) (ti);
		CALL(basicTypesItf[5],tti) (tti);
	}

	//constant
	{
		const int ci=0;
		CALL(basicTypesItf[ci],ci) (ci);
	}
	//mixing
	{
		int * const cpi=0;
		const unsigned long * cpui=0;
		char * ptc[4];
		short * const ptcs[5];
		const long ** const **  const ppcppctttl [6][9][3];
		CALL(basicTypesItf[6],mix) ( cpi, cpui, ptc, ptcs, ppcppctttl);
	}
}
