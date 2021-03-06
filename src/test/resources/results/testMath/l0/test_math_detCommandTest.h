#ifndef TEST_MATH_DETCOMMANDTEST
#define TEST_MATH_DETCOMMANDTEST
#ifndef M_PI
#define M_PI 3.14159265358979323846
#endif
#include "octave/oct.h"
#include "Helper.h"
#include "octave/builtin-defun-decls.h"
class test_math_detCommandTest{
public:
Matrix CONSTANTCONSTANTVECTOR0;
void init()
{
CONSTANTCONSTANTVECTOR0 = Matrix(3,3);
CONSTANTCONSTANTVECTOR0(0,0) = 1;
CONSTANTCONSTANTVECTOR0(0,1) = 0;
CONSTANTCONSTANTVECTOR0(0,2) = 0;
CONSTANTCONSTANTVECTOR0(1,0) = 0;
CONSTANTCONSTANTVECTOR0(1,1) = 1;
CONSTANTCONSTANTVECTOR0(1,2) = 0;
CONSTANTCONSTANTVECTOR0(2,0) = 0;
CONSTANTCONSTANTVECTOR0(2,1) = 0;
CONSTANTCONSTANTVECTOR0(2,2) = 1;
}
void execute()
{
Matrix mat = CONSTANTCONSTANTVECTOR0;
double a = (Helper::getDoubleFromOctaveListFirstResult(Fdet(Helper::convertToOctaveValueList(mat),1)));
}

};
#endif
