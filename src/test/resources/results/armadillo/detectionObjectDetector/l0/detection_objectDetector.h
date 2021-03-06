#ifndef DETECTION_OBJECTDETECTOR
#define DETECTION_OBJECTDETECTOR
#ifndef M_PI
#define M_PI 3.14159265358979323846
#endif
#include "armadillo.h"
#include "detection_objectDetector_spectralClusterer_1_.h"
using namespace arma;
class detection_objectDetector{
public:
mat imgFront;
mat imgBack;
mat imgLeft;
mat imgRight;
mat clusters[4];
detection_objectDetector_spectralClusterer_1_ spectralClusterer[4];
void init()
{
imgFront=mat(2500,3);
imgBack=mat(2500,3);
imgLeft=mat(2500,3);
imgRight=mat(2500,3);
clusters[0]=mat(2500,1);
clusters[1]=mat(2500,1);
clusters[2]=mat(2500,1);
clusters[3]=mat(2500,1);
spectralClusterer[0].init();
spectralClusterer[1].init();
spectralClusterer[2].init();
spectralClusterer[3].init();
}
void execute()
{
spectralClusterer[0].imgMatrix = imgFront;
spectralClusterer[0].execute();
spectralClusterer[1].imgMatrix = imgRight;
spectralClusterer[1].execute();
spectralClusterer[2].imgMatrix = imgLeft;
spectralClusterer[2].execute();
spectralClusterer[3].imgMatrix = imgBack;
spectralClusterer[3].execute();
clusters[0] = spectralClusterer[0].clusters;
clusters[1] = spectralClusterer[1].clusters;
clusters[2] = spectralClusterer[2].clusters;
clusters[3] = spectralClusterer[3].clusters;
}

};
#endif
