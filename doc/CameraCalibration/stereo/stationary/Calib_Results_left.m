% Intrinsic and Extrinsic Camera Parameters
%
% This script file can be directly excecuted under Matlab to recover the camera intrinsic and extrinsic parameters.
% IMPORTANT: This file contains neither the structure of the calibration objects nor the image coordinates of the calibration points.
%            All those complementary variables are saved in the complete matlab data file Calib_Results.mat.
% For more information regarding the calibration model visit http://www.vision.caltech.edu/bouguetj/calib_doc/


%-- Focal length:
fc = [ 1898.922297959615435 ; 1900.008025459087094 ];

%-- Principal point:
cc = [ 625.063850467349198 ; 455.931126929623986 ];

%-- Skew coefficient:
alpha_c = 0.000000000000000;

%-- Distortion coefficients:
kc = [ -0.365178767370410 ; 0.334663429542135 ; 0.000000000000000 ; 0.000000000000000 ; 0.000000000000000 ];

%-- Focal length uncertainty:
fc_error = [ 11.225960253881089 ; 11.000788824861109 ];

%-- Principal point uncertainty:
cc_error = [ 16.642760583007263 ; 12.190972695497345 ];

%-- Skew coefficient uncertainty:
alpha_c_error = 0.000000000000000;

%-- Distortion coefficients uncertainty:
kc_error = [ 0.019140972545712 ; 0.146476100951561 ; 0.000000000000000 ; 0.000000000000000 ; 0.000000000000000 ];

%-- Image size:
nx = 1296;
ny = 964;


%-- Various other variables (may be ignored if you do not use the Matlab Calibration Toolbox):
%-- Those variables are used to control which intrinsic parameters should be optimized

n_ima = 22;						% Number of calibration images
est_fc = [ 1 ; 1 ];					% Estimation indicator of the two focal variables
est_aspect_ratio = 1;				% Estimation indicator of the aspect ratio fc(2)/fc(1)
center_optim = 1;					% Estimation indicator of the principal point
est_alpha = 0;						% Estimation indicator of the skew coefficient
est_dist = [ 1 ; 1 ; 0 ; 0 ; 0 ];	% Estimation indicator of the distortion coefficients


%-- Extrinsic parameters:
%-- The rotation (omc_kk) and the translation (Tc_kk) vectors for every calibration image and their uncertainties

%-- Image #1:
omc_1 = [ 2.111271e+00 ; 2.048915e+00 ; 1.765491e-01 ];
Tc_1  = [ -1.899152e+01 ; -1.025844e+02 ; 6.566963e+02 ];
omc_error_1 = [ 6.969945e-03 ; 6.113263e-03 ; 1.322721e-02 ];
Tc_error_1  = [ 5.788680e+00 ; 4.202464e+00 ; 4.093999e+00 ];

%-- Image #2:
omc_2 = [ 2.118440e+00 ; 2.227365e+00 ; 1.552865e-01 ];
Tc_2  = [ -3.240613e+01 ; -9.920891e+01 ; 6.278172e+02 ];
omc_error_2 = [ 7.072018e-03 ; 6.435900e-03 ; 1.441222e-02 ];
Tc_error_2  = [ 5.532874e+00 ; 4.021635e+00 ; 3.929303e+00 ];

%-- Image #3:
omc_3 = [ -1.850212e+00 ; -2.103810e+00 ; 3.757613e-01 ];
Tc_3  = [ 1.467664e+00 ; -1.074990e+02 ; 7.233744e+02 ];
omc_error_3 = [ 6.242011e-03 ; 6.897321e-03 ; 1.183215e-02 ];
Tc_error_3  = [ 6.354836e+00 ; 4.637000e+00 ; 4.153263e+00 ];

%-- Image #4:
omc_4 = [ 1.831513e+00 ; 2.148751e+00 ; -1.345761e-01 ];
Tc_4  = [ -2.354464e+01 ; -9.201316e+01 ; 7.221753e+02 ];
omc_error_4 = [ 5.687715e-03 ; 7.012042e-03 ; 1.224240e-02 ];
Tc_error_4  = [ 6.333159e+00 ; 4.632318e+00 ; 4.285936e+00 ];

%-- Image #5:
omc_5 = [ -1.852092e+00 ; -2.108624e+00 ; -6.593870e-01 ];
Tc_5  = [ -1.850572e+01 ; -1.228810e+02 ; 6.762831e+02 ];
omc_error_5 = [ 3.741123e-03 ; 7.389765e-03 ; 1.210188e-02 ];
Tc_error_5  = [ 5.983376e+00 ; 4.347337e+00 ; 4.594188e+00 ];

%-- Image #6:
omc_6 = [ -1.612881e+00 ; -1.982094e+00 ; 1.071688e-01 ];
Tc_6  = [ -7.420459e+00 ; -1.510677e+02 ; 7.690088e+02 ];
omc_error_6 = [ 5.071502e-03 ; 6.946171e-03 ; 1.062886e-02 ];
Tc_error_6  = [ 6.800245e+00 ; 4.958199e+00 ; 4.637452e+00 ];

%-- Image #7:
omc_7 = [ 1.693727e+00 ; 2.223617e+00 ; 9.810715e-01 ];
Tc_7  = [ -8.917420e+00 ; -1.327865e+02 ; 6.748175e+02 ];
omc_error_7 = [ 7.774503e-03 ; 6.035532e-03 ; 1.056983e-02 ];
Tc_error_7  = [ 5.975713e+00 ; 4.336249e+00 ; 4.648162e+00 ];

%-- Image #8:
omc_8 = [ -2.068020e+00 ; -2.194487e+00 ; 1.231120e-01 ];
Tc_8  = [ -4.149424e+01 ; -1.147149e+02 ; 6.268633e+02 ];
omc_error_8 = [ 6.738401e-03 ; 7.440527e-03 ; 1.498077e-02 ];
Tc_error_8  = [ 5.525924e+00 ; 4.030426e+00 ; 3.922165e+00 ];

%-- Image #9:
omc_9 = [ -1.905604e+00 ; -1.952139e+00 ; -5.773658e-01 ];
Tc_9  = [ -5.266901e+01 ; -1.137241e+02 ; 5.712619e+02 ];
omc_error_9 = [ 3.907812e-03 ; 6.755087e-03 ; 1.146415e-02 ];
Tc_error_9  = [ 5.070817e+00 ; 3.682859e+00 ; 3.900129e+00 ];

%-- Image #10:
omc_10 = [ -1.791691e+00 ; -2.098995e+00 ; 9.669209e-02 ];
Tc_10  = [ -8.376085e+00 ; -1.426721e+02 ; 7.404795e+02 ];
omc_error_10 = [ 5.573977e-03 ; 7.224902e-03 ; 1.274997e-02 ];
Tc_error_10  = [ 6.544515e+00 ; 4.778666e+00 ; 4.527904e+00 ];

%-- Image #11:
omc_11 = [ 1.994180e+00 ; 2.139218e+00 ; -1.240545e-01 ];
Tc_11  = [ -4.294129e+01 ; -2.469712e+01 ; 7.659705e+02 ];
omc_error_11 = [ 7.051265e-03 ; 7.012257e-03 ; 1.475284e-02 ];
Tc_error_11  = [ 6.711937e+00 ; 4.904360e+00 ; 4.656819e+00 ];

%-- Image #12:
omc_12 = [ -1.832883e+00 ; -1.991414e+00 ; 5.600398e-01 ];
Tc_12  = [ -2.564135e+01 ; -8.718219e+01 ; 6.843215e+02 ];
omc_error_12 = [ 6.546265e-03 ; 6.254997e-03 ; 1.043934e-02 ];
Tc_error_12  = [ 6.000761e+00 ; 4.379033e+00 ; 3.741112e+00 ];

%-- Image #13:
omc_13 = [ 2.129227e+00 ; 2.301026e+00 ; -1.543457e-01 ];
Tc_13  = [ -3.612426e+01 ; -9.378107e+01 ; 8.922391e+02 ];
omc_error_13 = [ 1.090123e-02 ; 1.113859e-02 ; 2.360581e-02 ];
Tc_error_13  = [ 7.826042e+00 ; 5.722183e+00 ; 5.562766e+00 ];

%-- Image #14:
omc_14 = [ -1.854123e+00 ; -1.986472e+00 ; -4.082402e-01 ];
Tc_14  = [ -1.044944e+01 ; -1.130352e+02 ; 8.607077e+02 ];
omc_error_14 = [ 4.636963e-03 ; 7.492419e-03 ; 1.259248e-02 ];
Tc_error_14  = [ 7.577581e+00 ; 5.529229e+00 ; 5.591932e+00 ];

%-- Image #15:
omc_15 = [ 1.986190e+00 ; 2.295362e+00 ; 7.840661e-01 ];
Tc_15  = [ -8.686750e+01 ; -1.164675e+02 ; 8.982895e+02 ];
omc_error_15 = [ 8.013186e-03 ; 5.803074e-03 ; 1.314025e-02 ];
Tc_error_15  = [ 7.936536e+00 ; 5.792969e+00 ; 6.141739e+00 ];

%-- Image #16:
omc_16 = [ -1.782973e+00 ; -2.023824e+00 ; 2.756444e-02 ];
Tc_16  = [ 6.294857e+01 ; -1.544215e+02 ; 8.990974e+02 ];
omc_error_16 = [ 5.721748e-03 ; 8.061923e-03 ; 1.356266e-02 ];
Tc_error_16  = [ 7.938149e+00 ; 5.833606e+00 ; 5.628727e+00 ];

%-- Image #17:
omc_17 = [ -1.651045e+00 ; -1.820632e+00 ; -8.521556e-01 ];
Tc_17  = [ -3.121452e+01 ; -1.190085e+02 ; 5.628282e+02 ];
omc_error_17 = [ 3.808588e-03 ; 7.237257e-03 ; 1.024319e-02 ];
Tc_error_17  = [ 5.005936e+00 ; 3.640504e+00 ; 3.973810e+00 ];

%-- Image #18:
omc_18 = [ 1.738027e+00 ; 2.023275e+00 ; -8.095363e-01 ];
Tc_18  = [ -9.616911e+00 ; -6.395309e+01 ; 7.300356e+02 ];
omc_error_18 = [ 4.509224e-03 ; 7.149081e-03 ; 1.079655e-02 ];
Tc_error_18  = [ 6.388397e+00 ; 4.663447e+00 ; 3.926985e+00 ];

%-- Image #19:
omc_19 = [ 1.886435e+00 ; 1.973989e+00 ; -2.741256e-01 ];
Tc_19  = [ -5.350510e+01 ; -3.270227e+01 ; 7.637533e+02 ];
omc_error_19 = [ 5.575887e-03 ; 6.527925e-03 ; 1.145842e-02 ];
Tc_error_19  = [ 6.686448e+00 ; 4.885234e+00 ; 4.509545e+00 ];

%-- Image #20:
omc_20 = [ -1.835526e+00 ; -2.079653e+00 ; 4.957039e-01 ];
Tc_20  = [ -3.122869e+01 ; -7.561530e+01 ; 8.537967e+02 ];
omc_error_20 = [ 6.816337e-03 ; 7.163512e-03 ; 1.173087e-02 ];
Tc_error_20  = [ 7.480436e+00 ; 5.472370e+00 ; 4.805364e+00 ];

%-- Image #21:
omc_21 = [ 1.864101e+00 ; 2.198921e+00 ; 2.820019e-01 ];
Tc_21  = [ -1.131278e+02 ; -1.112373e+02 ; 9.775134e+02 ];
omc_error_21 = [ 7.041343e-03 ; 8.108132e-03 ; 1.474664e-02 ];
Tc_error_21  = [ 8.651261e+00 ; 6.300014e+00 ; 6.431391e+00 ];

%-- Image #22:
omc_22 = [ -1.545075e+00 ; -2.022270e+00 ; 5.802393e-01 ];
Tc_22  = [ 4.418815e+01 ; -1.205827e+02 ; 1.172275e+03 ];
omc_error_22 = [ 6.650995e-03 ; 7.566558e-03 ; 1.069578e-02 ];
Tc_error_22  = [ 1.028476e+01 ; 7.525540e+00 ; 6.767086e+00 ];

