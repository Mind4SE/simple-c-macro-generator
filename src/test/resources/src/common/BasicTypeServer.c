//basic
void METH(basicTypesItf,v) ( void ){
	return;
}
char METH(basicTypesItf,c) ( char c){
	return 0;
}
short METH(basicTypesItf,s) (short s){
	return 0;
}
int METH(basicTypesItf,i) ( int i){
	return 0;
}
float METH(basicTypesItf,_f) (float f){
	return 0.0;
}
double METH(basicTypesItf,_d) (double d){
	return 0.0;
}
void METH(basicTypesItf,many) (char c,short s,int i,long l){
	return;
}

//unsigned
unsigned char METH(basicTypesItf,uc) ( unsigned char uc){
	return 0;
}
unsigned short METH(basicTypesItf,us) (unsigned short us){
	return 0;
}
unsigned int METH(basicTypesItf,ui) ( unsigned int ui){
	return 0;
}

//pointers
int* METH(basicTypesItf,pi) ( int * pi){
	return 0;
}
int** METH(basicTypesItf,ppi) ( int ** ppi){
	return 0;
}

//table
int METH(basicTypesItf,ti) (int ti[] ){
	return 0;
}
int METH(basicTypesItf,tti) (int tti[][10]){
	return 0;
}

//constant
int METH(basicTypesItf,ci) (const int ci){
	return 0;
}

//mixing
void METH(basicTypesItf,mix) ( int * const cpi, const unsigned long * cpui, char * ptc[], short * const ptcs[], const long ** const **  const ppcppctttl [][9][3]){
	return;
}
