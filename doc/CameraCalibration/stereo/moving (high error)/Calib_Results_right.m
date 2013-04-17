% Intrinsic and Extrinsic Camera Parameters
%
% This script file can be directly excecuted under Matlab to recover the camera intrinsic and extrinsic parameters.
% IMPORTANT: This file contains neither the structure of the calibration objects nor the image coordinates of the calibration points.
%            All those complementary variables are saved in the complete matlab data file Calib_Results.mat.
% For more information regarding the calibration model visit http://www.vision.caltech.edu/bouguetj/calib_doc/


%-- Focal length:
fc = [ 1731.802197193351503 ; 1728.891770184598727 ];

%-- Principal point:
cc = [ 682.183464880831821 ; 446.704818451193603 ];

%-- Skew coefficient:
alpha_c = 0.000000000000000;

%-- Distortion coefficients:
kc = [ -0.323610181827860 ; -0.137949157626961 ; -0.001461114672375 ; 0.003255302986105 ; 0.000000000000000 ];

%-- Focal length uncertainty:
fc_error = [ 15.785786961298550 ; 15.641743292586712 ];

%-- Principal point uncertainty:
cc_error = [ 20.094761221512648 ; 16.234846416733387 ];

%-- Skew coefficient uncertainty:
alpha_c_error = 0.000000000000000;

%-- Distortion coefficients uncertainty:
kc_error = [ 0.029138572249099 ; 0.097447144675358 ; 0.001946712025084 ; 0.003251800547689 ; 0.000000000000000 ];

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
omc_1 = [ 2.074302e+00 ; 2.003083e+00 ; -3.084216e-01 ];
Tc_1  = [ -2.522329e+02 ; -8.985789e+01 ; 7.786090e+02 ];
omc_error_1 = [ 6.299261e-03 ; 9.494808e-03 ; 1.614131e-02 ];
Tc_error_1  = [ 9.038931e+00 ; 7.407195e+00 ; 7.538282e+00 ];

%-- Image #2:
omc_2 = [ 1.957838e+00 ; 1.819552e+00 ; -4.219222e-01 ];
Tc_2  = [ -2.709234e+02 ; -3.134907e+00 ; 7.984615e+02 ];
omc_error_2 = [ 6.563337e-03 ; 9.508938e-03 ; 1.439001e-02 ];
Tc_error_2  = [ 9.247720e+00 ; 7.650100e+00 ; 7.631636e+00 ];

%-- Image #3:
omc_3 = [ 1.935768e+00 ; 1.821408e+00 ; -4.155549e-01 ];
Tc_3  = [ -2.849539e+02 ; -5.999151e+00 ; 7.957378e+02 ];
omc_error_3 = [ 6.463926e-03 ; 9.645772e-03 ; 1.428163e-02 ];
Tc_error_3  = [ 9.218721e+00 ; 7.646496e+00 ; 7.701268e+00 ];

%-- Image #4:
omc_4 = [ 1.631064e+00 ; 1.873475e+00 ; -5.392413e-01 ];
Tc_4  = [ -2.506104e+02 ; -6.059572e+01 ; 8.455571e+02 ];
omc_error_4 = [ 5.703896e-03 ; 1.042714e-02 ; 1.325308e-02 ];
Tc_error_4  = [ 9.797897e+00 ; 8.055906e+00 ; 7.773428e+00 ];

%-- Image #5:
omc_5 = [ 1.361544e+00 ; 2.136852e+00 ; -7.076672e-01 ];
Tc_5  = [ -2.372896e+02 ; -3.949762e+01 ; 8.532534e+02 ];
omc_error_5 = [ 4.422447e-03 ; 1.115535e-02 ; 1.309032e-02 ];
Tc_error_5  = [ 9.853490e+00 ; 8.133207e+00 ; 7.686534e+00 ];

%-- Image #6:
omc_6 = [ 1.649961e+00 ; 1.625864e+00 ; -2.118397e-01 ];
Tc_6  = [ -2.208403e+02 ; -6.948582e+00 ; 6.746407e+02 ];
omc_error_6 = [ 7.013518e-03 ; 8.998091e-03 ; 1.253588e-02 ];
Tc_error_6  = [ 7.839692e+00 ; 6.463149e+00 ; 6.525214e+00 ];

%-- Image #7:
omc_7 = [ 1.665197e+00 ; 1.583635e+00 ; -2.068897e-01 ];
Tc_7  = [ -2.411035e+02 ; 3.180979e+00 ; 6.713128e+02 ];
omc_error_7 = [ 6.980652e-03 ; 8.916590e-03 ; 1.236863e-02 ];
Tc_error_7  = [ 7.822630e+00 ; 6.463630e+00 ; 6.579995e+00 ];

%-- Image #8:
omc_8 = [ NaN ; NaN ; NaN ];
Tc_8  = [ NaN ; NaN ; NaN ];
omc_error_8 = [ NaN ; NaN ; NaN ];
Tc_error_8  = [ NaN ; NaN ; NaN ];

%-- Image #9:
omc_9 = [ 1.965303e+00 ; 2.290854e+00 ; 8.849794e-02 ];
Tc_9  = [ -2.416521e+02 ; -1.214157e+02 ; 6.855970e+02 ];
omc_error_9 = [ 8.072139e-03 ; 1.124896e-02 ; 2.001447e-02 ];
Tc_error_9  = [ 8.013534e+00 ; 6.569779e+00 ; 7.026057e+00 ];

%-- Image #10:
omc_10 = [ 1.992044e+00 ; 2.275824e+00 ; 1.012823e-01 ];
Tc_10  = [ -2.449249e+02 ; -1.286787e+02 ; 6.832905e+02 ];
omc_error_10 = [ 8.040882e-03 ; 1.112279e-02 ; 2.031100e-02 ];
Tc_error_10  = [ 7.995902e+00 ; 6.554351e+00 ; 7.058557e+00 ];

%-- Image #11:
omc_11 = [ 1.971013e+00 ; 2.298547e+00 ; 2.599765e-01 ];
Tc_11  = [ -2.316501e+02 ; -1.712438e+02 ; 6.378778e+02 ];
omc_error_11 = [ 7.937183e-03 ; 1.127171e-02 ; 2.085825e-02 ];
Tc_error_11  = [ 7.578759e+00 ; 6.161830e+00 ; 6.899397e+00 ];

%-- Image #12:
omc_12 = [ 1.714645e+00 ; 1.696648e+00 ; -3.184441e-01 ];
Tc_12  = [ -2.762242e+02 ; -1.727532e+02 ; 7.830905e+02 ];
omc_error_12 = [ 6.291534e-03 ; 1.038090e-02 ; 1.294614e-02 ];
Tc_error_12  = [ 9.178570e+00 ; 7.496808e+00 ; 7.905433e+00 ];

%-- Image #13:
omc_13 = [ 1.828389e+00 ; 1.132278e+00 ; -7.796230e-02 ];
Tc_13  = [ -2.691278e+02 ; 4.337569e+01 ; 7.481641e+02 ];
omc_error_13 = [ 8.262290e-03 ; 8.093542e-03 ; 1.203877e-02 ];
Tc_error_13  = [ 8.756502e+00 ; 7.205745e+00 ; 7.528131e+00 ];

%-- Image #14:
omc_14 = [ -1.791368e+00 ; -2.086696e+00 ; -1.964422e-01 ];
Tc_14  = [ -2.552731e+02 ; -1.438108e+02 ; 6.674436e+02 ];
omc_error_14 = [ 7.728500e-03 ; 8.715755e-03 ; 1.523603e-02 ];
Tc_error_14  = [ 7.809722e+00 ; 6.418640e+00 ; 7.079416e+00 ];

%-- Image #15:
omc_15 = [ 2.160188e+00 ; 2.203928e+00 ; -1.712605e-01 ];
Tc_15  = [ -2.379764e+02 ; -1.228774e+02 ; 8.327467e+02 ];
omc_error_15 = [ 7.140779e-03 ; 9.783815e-03 ; 1.935631e-02 ];
Tc_error_15  = [ 9.668305e+00 ; 7.898298e+00 ; 8.029672e+00 ];

%-- Image #16:
omc_16 = [ 1.985379e+00 ; 2.220368e+00 ; -4.346146e-01 ];
Tc_16  = [ -2.413325e+02 ; -1.951456e+02 ; 9.784918e+02 ];
omc_error_16 = [ 5.154809e-03 ; 1.042125e-02 ; 1.770073e-02 ];
Tc_error_16  = [ 1.144951e+01 ; 9.276822e+00 ; 9.261160e+00 ];

%-- Image #17:
omc_17 = [ -1.982130e+00 ; -2.160104e+00 ; -3.682854e-01 ];
Tc_17  = [ -2.504780e+02 ; -1.208571e+02 ; 7.837772e+02 ];
omc_error_17 = [ 8.268885e-03 ; 9.598377e-03 ; 1.802523e-02 ];
Tc_error_17  = [ 9.160305e+00 ; 7.481913e+00 ; 8.059305e+00 ];

%-- Image #18:
omc_18 = [ -1.111969e+00 ; -2.923332e+00 ; 2.296086e-01 ];
Tc_18  = [ -1.592508e+02 ; -1.513770e+02 ; 9.143393e+02 ];
omc_error_18 = [ 6.421114e-03 ; 1.240979e-02 ; 1.775039e-02 ];
Tc_error_18  = [ 1.060383e+01 ; 8.594643e+00 ; 8.795914e+00 ];

