#ifndef PAPER_MATHUNIT1_MATRIXMODIFIER
#define PAPER_MATHUNIT1_MATRIXMODIFIER
#ifndef M_PI
#define M_PI 3.14159265358979323846
#endif
#include "octave/oct.h"
class paper_mathUnit1_matrixModifier{
public:
Matrix mat1;
Matrix mat2;
Matrix mat3;
Matrix mat4;
double factor;
Matrix matOut;
void init()
{
mat1=Matrix(1000,200);
mat2=Matrix(1000,200);
mat3=Matrix(200,10);
mat4=Matrix(10,100);
matOut=Matrix(1000,100);
}
void execute()
{
Matrix a = mat1+mat2;
Matrix b = mat3*mat4;
matOut = ((a*mat3)*mat4)*factor;
}

};
#endif