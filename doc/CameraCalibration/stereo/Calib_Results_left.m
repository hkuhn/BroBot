% Intrinsic and Extrinsic Camera Parameters
%
% This script file can be directly excecuted under Matlab to recover the camera intrinsic and extrinsic parameters.
% IMPORTANT: This file contains neither the structure of the calibration objects nor the image coordinates of the calibration points.
%            All those complementary variables are saved in the complete matlab data file Calib_Results.mat.
% For more information regarding the calibration model visit http://www.vision.caltech.edu/bouguetj/calib_doc/


%-- Focal length:
fc = [ 1934.034272076026809 ; 1937.716276793101088 ];

%-- Principal point:
cc = [ 680.282670756759671 ; 455.198813607095701 ];

%-- Skew coefficient:
alpha_c = 0.000000000000000;

%-- Distortion coefficients:
kc = [ -0.548898183840007 ; 1.256610243630957 ; 0.011135472412838 ; 0.006453973062970 ; 0.000000000000000 ];

%-- Focal length uncertainty:
fc_error = [ 18.670298610449592 ; 18.297198415243304 ];

%-- Principal point uncertainty:
cc_error = [ 18.751742411453741 ; 20.399430338730433 ];

%-- Skew coefficient uncertainty:
alpha_c_error = 0.000000000000000;

%-- Distortion coefficients uncertainty:
kc_error = [ 0.045210431810039 ; 0.458943406910189 ; 0.003239175918224 ; 0.001589282976939 ; 0.000000000000000 ];

%-- Image size:
nx = 1296;
ny = 964;


%-- Various other variables (may be ignored if you do not use the Matlab Calibration Toolbox):
%-- Those variables are used to control which intrinsic parameters should be optimized

n_ima = 18;						% Number of calibration images
est_fc = [ 1 ; 1 ];					% Estimation indicator of the two focal variables
est_aspect_ratio = 1;				% Estimation indicator of the aspect ratio fc(2)/fc(1)
center_optim = 1;					% Estimation indicator of the principal point
est_alpha = 0;						% Estimation indicator of the skew coefficient
est_dist = [ 1 ; 1 ; 1 ; 1 ; 0 ];	% Estimation indicator of the distortion coefficients


%-- Extrinsic parameters:
%-- The rotation (omc_kk) and the translation (Tc_kk) vectors for every calibration image and their uncertainties

%-- Image #1:
omc_1 = [ 2.040339e+00 ; 1.974559e+00 ; -3.679614e-01 ];
Tc_1  = [ -1.253925e+02 ; -3.593646e+01 ; 8.029452e+02 ];
omc_error_1 = [ 7.729460e-03 ; 9.088816e-03 ; 1.704450e-02 ];
Tc_error_1  = [ 7.796355e+00 ; 8.493174e+00 ; 7.914128e+00 ];

%-- Image #2:
omc_2 = [ 1.937487e+00 ; 1.811379e+00 ; -4.636645e-01 ];
Tc_2  = [ -1.352865e+02 ; 3.172014e+01 ; 8.238049e+02 ];
omc_error_2 = [ 7.484904e-03 ; 8.546757e-03 ; 1.447295e-02 ];
Tc_error_2  = [ 7.993702e+00 ; 8.737913e+00 ; 7.896274e+00 ];

%-- Image #3:
omc_3 = [ 1.914813e+00 ; 1.814093e+00 ; -4.379337e-01 ];
Tc_3  = [ -1.597491e+02 ; 2.188440e+01 ; 8.173191e+02 ];
omc_error_3 = [ 7.265950e-03 ; 8.643281e-03 ; 1.438705e-02 ];
Tc_error_3  = [ 7.928663e+00 ; 8.697493e+00 ; 7.952883e+00 ];

%-- Image #4:
omc_4 = [ 1.634956e+00 ; 1.863335e+00 ; -5.528951e-01 ];
Tc_4  = [ -1.238614e+02 ; -4.773191e+01 ; 8.684447e+02 ];
omc_error_4 = [ 6.734883e-03 ; 9.438682e-03 ; 1.336091e-02 ];
Tc_error_4  = [ 8.428158e+00 ; 9.186499e+00 ; 8.326214e+00 ];

%-- Image #5:
omc_5 = [ 1.366707e+00 ; 2.131627e+00 ; -7.213019e-01 ];
Tc_5  = [ -1.088340e+02 ; -2.520049e+01 ; 8.770294e+02 ];
omc_error_5 = [ 5.308535e-03 ; 1.008089e-02 ; 1.353624e-02 ];
Tc_error_5  = [ 8.491020e+00 ; 9.272815e+00 ; 8.171833e+00 ];

%-- Image #6:
omc_6 = [ 1.673217e+00 ; 1.608168e+00 ; -2.090353e-01 ];
Tc_6  = [ -9.863493e+01 ; -6.604118e+00 ; 6.931402e+02 ];
omc_error_6 = [ 8.134517e-03 ; 7.876021e-03 ; 1.199067e-02 ];
Tc_error_6  = [ 6.715603e+00 ; 7.326264e+00 ; 6.867322e+00 ];

%-- Image #7:
omc_7 = [ 1.676470e+00 ; 1.575924e+00 ; -2.120601e-01 ];
Tc_7  = [ -1.130371e+02 ; 8.783325e+00 ; 6.913613e+02 ];
omc_error_7 = [ 8.083229e-03 ; 7.750761e-03 ; 1.183446e-02 ];
Tc_error_7  = [ 6.703441e+00 ; 7.330211e+00 ; 6.866459e+00 ];

%-- Image #8:
omc_8 = [ NaN ; NaN ; NaN ];
Tc_8  = [ NaN ; NaN ; NaN ];
omc_error_8 = [ NaN ; NaN ; NaN ];
Tc_error_8  = [ NaN ; NaN ; NaN ];

%-- Image #9:
omc_9 = [ 1.936607e+00 ; 2.260722e+00 ; -1.271338e-02 ];
Tc_9  = [ -1.149647e+02 ; -4.241788e+01 ; 7.112023e+02 ];
omc_error_9 = [ 8.860349e-03 ; 9.865322e-03 ; 1.963147e-02 ];
Tc_error_9  = [ 6.908204e+00 ; 7.535979e+00 ; 7.484559e+00 ];

%-- Image #10:
omc_10 = [ 1.958554e+00 ; 2.242243e+00 ; -1.016053e-02 ];
Tc_10  = [ -1.187887e+02 ; -4.625456e+01 ; 7.104764e+02 ];
omc_error_10 = [ 8.890497e-03 ; 9.831708e-03 ; 1.973064e-02 ];
Tc_error_10  = [ 6.902733e+00 ; 7.530076e+00 ; 7.484216e+00 ];

%-- Image #11:
omc_11 = [ 1.941135e+00 ; 2.263986e+00 ; 1.296245e-01 ];
Tc_11  = [ -1.067831e+02 ; -9.158555e+01 ; 6.687907e+02 ];
omc_error_11 = [ 8.676649e-03 ; 9.535930e-03 ; 1.756324e-02 ];
Tc_error_11  = [ 6.523296e+00 ; 7.095662e+00 ; 7.153606e+00 ];

%-- Image #12:
omc_12 = [ 1.721314e+00 ; 1.698771e+00 ; -3.172189e-01 ];
Tc_12  = [ -1.465652e+02 ; -1.695458e+02 ; 7.997405e+02 ];
omc_error_12 = [ 7.990767e-03 ; 9.668992e-03 ; 1.323020e-02 ];
Tc_error_12  = [ 7.866862e+00 ; 8.514091e+00 ; 8.385965e+00 ];

%-- Image #13:
omc_13 = [ 1.845113e+00 ; 1.128128e+00 ; -8.630973e-02 ];
Tc_13  = [ -1.386647e+02 ; 4.225031e+01 ; 7.717613e+02 ];
omc_error_13 = [ 9.146460e-03 ; 6.879182e-03 ; 1.109098e-02 ];
Tc_error_13  = [ 7.511718e+00 ; 8.209559e+00 ; 7.897220e+00 ];

%-- Image #14:
omc_14 = [ -1.864618e+00 ; -2.150632e+00 ; -4.538959e-02 ];
Tc_14  = [ -1.254414e+02 ; -3.687091e+01 ; 6.993187e+02 ];
omc_error_14 = [ 7.136619e-03 ; 8.691452e-03 ; 1.592021e-02 ];
Tc_error_14  = [ 6.779496e+00 ; 7.418609e+00 ; 7.157807e+00 ];

%-- Image #15:
omc_15 = [ 2.131156e+00 ; 2.179876e+00 ; -2.402208e-01 ];
Tc_15  = [ -1.091469e+02 ; -6.686229e+01 ; 8.601243e+02 ];
omc_error_15 = [ 1.031649e-02 ; 1.128340e-02 ; 2.403734e-02 ];
Tc_error_15  = [ 8.363462e+00 ; 9.078837e+00 ; 8.704589e+00 ];

%-- Image #16:
omc_16 = [ 1.973116e+00 ; 2.201448e+00 ; -4.536138e-01 ];
Tc_16  = [ -1.216693e+02 ; -1.572778e+02 ; 1.003608e+03 ];
omc_error_16 = [ 7.490448e-03 ; 1.140561e-02 ; 2.009649e-02 ];
Tc_error_16  = [ 9.862401e+00 ; 1.065729e+01 ; 1.011795e+01 ];

%-- Image #17:
omc_17 = [ -1.995275e+00 ; -2.183088e+00 ; -3.343420e-01 ];
Tc_17  = [ -1.240990e+02 ; -8.165863e+01 ; 8.078673e+02 ];
omc_error_17 = [ 7.883671e-03 ; 9.987174e-03 ; 1.739389e-02 ];
Tc_error_17  = [ 7.878493e+00 ; 8.569297e+00 ; 8.634545e+00 ];

%-- Image #18:
omc_18 = [ 1.121840e+00 ; 2.910236e+00 ; -3.024692e-01 ];
Tc_18  = [ -3.126299e+01 ; -1.036346e+02 ; 9.450785e+02 ];
omc_error_18 = [ 6.003484e-03 ; 1.536497e-02 ; 2.475456e-02 ];
Tc_error_18  = [ 9.170838e+00 ; 9.960692e+00 ; 9.682338e+00 ];

