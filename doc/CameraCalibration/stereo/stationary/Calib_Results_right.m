% Intrinsic and Extrinsic Camera Parameters
%
% This script file can be directly excecuted under Matlab to recover the camera intrinsic and extrinsic parameters.
% IMPORTANT: This file contains neither the structure of the calibration objects nor the image coordinates of the calibration points.
%            All those complementary variables are saved in the complete matlab data file Calib_Results.mat.
% For more information regarding the calibration model visit http://www.vision.caltech.edu/bouguetj/calib_doc/


%-- Focal length:
fc = [ 1709.091332433365096 ; 1712.753648285152622 ];

%-- Principal point:
cc = [ 647.251386978112009 ; 463.765456354879007 ];

%-- Skew coefficient:
alpha_c = 0.000000000000000;

%-- Distortion coefficients:
kc = [ -0.415982053240950 ; 0.344324602056727 ; -0.001417505359457 ; -0.003481545361538 ; 0.000000000000000 ];

%-- Focal length uncertainty:
fc_error = [ 10.134447576246835 ; 9.864407505321173 ];

%-- Principal point uncertainty:
cc_error = [ 14.060192797378704 ; 11.044154169424141 ];

%-- Skew coefficient uncertainty:
alpha_c_error = 0.000000000000000;

%-- Distortion coefficients uncertainty:
kc_error = [ 0.021455446995552 ; 0.119623968880715 ; 0.001023301252308 ; 0.001632386565247 ; 0.000000000000000 ];

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
est_dist = [ 1 ; 1 ; 1 ; 1 ; 0 ];	% Estimation indicator of the distortion coefficients


%-- Extrinsic parameters:
%-- The rotation (omc_kk) and the translation (Tc_kk) vectors for every calibration image and their uncertainties

%-- Image #1:
omc_1 = [ 2.108570e+00 ; 2.045011e+00 ; 1.998912e-01 ];
Tc_1  = [ -1.618167e+02 ; -1.131067e+02 ; 6.434666e+02 ];
omc_error_1 = [ 6.530913e-03 ; 6.866037e-03 ; 1.392949e-02 ];
Tc_error_1  = [ 5.336086e+00 ; 4.206086e+00 ; 4.236831e+00 ];

%-- Image #2:
omc_2 = [ 2.115039e+00 ; 2.225033e+00 ; 1.792701e-01 ];
Tc_2  = [ -1.747988e+02 ; -1.097353e+02 ; 6.148208e+02 ];
omc_error_2 = [ 6.707679e-03 ; 7.521251e-03 ; 1.518600e-02 ];
Tc_error_2  = [ 5.111405e+00 ; 4.049726e+00 ; 4.168660e+00 ];

%-- Image #3:
omc_3 = [ -1.853968e+00 ; -2.117038e+00 ; 3.512579e-01 ];
Tc_3  = [ -1.420653e+02 ; -1.185956e+02 ; 7.096370e+02 ];
omc_error_3 = [ 6.228365e-03 ; 5.800632e-03 ; 1.092133e-02 ];
Tc_error_3  = [ 5.844521e+00 ; 4.611751e+00 ; 4.226265e+00 ];

%-- Image #4:
omc_4 = [ 1.830328e+00 ; 2.143311e+00 ; -1.136894e-01 ];
Tc_4  = [ -1.673907e+02 ; -1.032760e+02 ; 7.083975e+02 ];
omc_error_4 = [ 4.870155e-03 ; 7.153440e-03 ; 1.245379e-02 ];
Tc_error_4  = [ 5.842185e+00 ; 4.617002e+00 ; 4.416877e+00 ];

%-- Image #5:
omc_5 = [ -1.846468e+00 ; -2.111521e+00 ; -6.859357e-01 ];
Tc_5  = [ -1.615084e+02 ; -1.335491e+02 ; 6.611364e+02 ];
omc_error_5 = [ 4.520631e-03 ; 6.967735e-03 ; 1.126840e-02 ];
Tc_error_5  = [ 5.505777e+00 ; 4.357968e+00 ; 4.634793e+00 ];

%-- Image #6:
omc_6 = [ -1.612535e+00 ; -1.992883e+00 ; 8.424880e-02 ];
Tc_6  = [ -1.520445e+02 ; -1.626427e+02 ; 7.534895e+02 ];
omc_error_6 = [ 5.494875e-03 ; 6.370600e-03 ; 9.836625e-03 ];
Tc_error_6  = [ 6.237653e+00 ; 4.904745e+00 ; 4.732741e+00 ];

%-- Image #7:
omc_7 = [ 1.682492e+00 ; 2.215142e+00 ; 9.995720e-01 ];
Tc_7  = [ -1.518106e+02 ; -1.436867e+02 ; 6.595774e+02 ];
omc_error_7 = [ 7.366959e-03 ; 5.973173e-03 ; 1.019229e-02 ];
Tc_error_7  = [ 5.518129e+00 ; 4.350680e+00 ; 4.687711e+00 ];

%-- Image #8:
omc_8 = [ -2.072325e+00 ; -2.202838e+00 ; 9.527493e-02 ];
Tc_8  = [ -1.838630e+02 ; -1.252124e+02 ; 6.130231e+02 ];
omc_error_8 = [ 6.488400e-03 ; 5.788786e-03 ; 1.306801e-02 ];
Tc_error_8  = [ 5.073012e+00 ; 4.028287e+00 ; 4.101231e+00 ];

%-- Image #9:
omc_9 = [ -1.900187e+00 ; -1.957182e+00 ; -6.040242e-01 ];
Tc_9  = [ -1.940580e+02 ; -1.235578e+02 ; 5.568115e+02 ];
omc_error_9 = [ 4.816467e-03 ; 6.275973e-03 ; 1.068987e-02 ];
Tc_error_9  = [ 4.643460e+00 ; 3.721046e+00 ; 4.147245e+00 ];

%-- Image #10:
omc_10 = [ -1.795573e+00 ; -2.111215e+00 ; 6.844094e-02 ];
Tc_10  = [ -1.526745e+02 ; -1.530996e+02 ; 7.256368e+02 ];
omc_error_10 = [ 5.734308e-03 ; 6.215139e-03 ; 1.117967e-02 ];
Tc_error_10  = [ 6.004050e+00 ; 4.727368e+00 ; 4.591420e+00 ];

%-- Image #11:
omc_11 = [ 1.990446e+00 ; 2.135476e+00 ; -1.081635e-01 ];
Tc_11  = [ -1.875112e+02 ; -3.582738e+01 ; 7.519983e+02 ];
omc_error_11 = [ 6.736528e-03 ; 8.103409e-03 ; 1.551342e-02 ];
Tc_error_11  = [ 6.202221e+00 ; 4.911006e+00 ; 4.785680e+00 ];

%-- Image #12:
omc_12 = [ -1.840689e+00 ; -2.005447e+00 ; 5.371342e-01 ];
Tc_12  = [ -1.691340e+02 ; -9.796523e+01 ; 6.716226e+02 ];
omc_error_12 = [ 6.763959e-03 ; 5.620965e-03 ; 1.024017e-02 ];
Tc_error_12  = [ 5.535215e+00 ; 4.394549e+00 ; 3.933146e+00 ];

%-- Image #13:
omc_13 = [ 2.122512e+00 ; 2.297426e+00 ; -1.287692e-01 ];
Tc_13  = [ -1.828840e+02 ; -1.063861e+02 ; 8.753642e+02 ];
omc_error_13 = [ 7.637537e-03 ; 9.410181e-03 ; 1.874157e-02 ];
Tc_error_13  = [ 7.208593e+00 ; 5.696533e+00 ; 5.442040e+00 ];

%-- Image #14:
omc_14 = [ -1.851556e+00 ; -1.994265e+00 ; -4.274859e-01 ];
Tc_14  = [ -1.562433e+02 ; -1.253420e+02 ; 8.440427e+02 ];
omc_error_14 = [ 5.222399e-03 ; 7.065471e-03 ; 1.148031e-02 ];
Tc_error_14  = [ 6.978695e+00 ; 5.499213e+00 ; 5.532305e+00 ];

%-- Image #15:
omc_15 = [ 1.975678e+00 ; 2.290336e+00 ; 8.071678e-01 ];
Tc_15  = [ -2.333220e+02 ; -1.289526e+02 ; 8.787473e+02 ];
omc_error_15 = [ 8.013670e-03 ; 6.454213e-03 ; 1.314070e-02 ];
Tc_error_15  = [ 7.305450e+00 ; 5.808052e+00 ; 6.209169e+00 ];

%-- Image #16:
omc_16 = [ -1.783395e+00 ; -2.032007e+00 ; 6.429710e-03 ];
Tc_16  = [ -8.363265e+01 ; -1.664902e+02 ; 8.820057e+02 ];
omc_error_16 = [ 5.648521e-03 ; 6.749150e-03 ; 1.137833e-02 ];
Tc_error_16  = [ 7.294225e+00 ; 5.714964e+00 ; 5.482066e+00 ];

%-- Image #17:
omc_17 = [ -1.653676e+00 ; -1.832250e+00 ; -8.789169e-01 ];
Tc_17  = [ -1.721916e+02 ; -1.290030e+02 ; 5.455656e+02 ];
omc_error_17 = [ 4.559397e-03 ; 6.797166e-03 ; 9.561463e-03 ];
Tc_error_17  = [ 4.561319e+00 ; 3.640367e+00 ; 4.113590e+00 ];

%-- Image #18:
omc_18 = [ 1.741171e+00 ; 2.018033e+00 ; -7.884365e-01 ];
Tc_18  = [ -1.537566e+02 ; -7.513616e+01 ; 7.169459e+02 ];
omc_error_18 = [ 3.567217e-03 ; 7.270848e-03 ; 1.063622e-02 ];
Tc_error_18  = [ 5.898954e+00 ; 4.668944e+00 ; 4.050246e+00 ];

%-- Image #19:
omc_19 = [ 1.883502e+00 ; 1.969501e+00 ; -2.551761e-01 ];
Tc_19  = [ -1.977665e+02 ; -4.463093e+01 ; 7.495840e+02 ];
omc_error_19 = [ 4.779741e-03 ; 6.709108e-03 ; 1.145921e-02 ];
Tc_error_19  = [ 6.171966e+00 ; 4.903475e+00 ; 4.677973e+00 ];

%-- Image #20:
omc_20 = [ -1.844202e+00 ; -2.091970e+00 ; 4.690965e-01 ];
Tc_20  = [ -1.774954e+02 ; -8.783407e+01 ; 8.387827e+02 ];
omc_error_20 = [ 6.688501e-03 ; 6.079403e-03 ; 1.095610e-02 ];
Tc_error_20  = [ 6.897855e+00 ; 5.468857e+00 ; 4.925157e+00 ];

%-- Image #21:
omc_21 = [ 1.858104e+00 ; 2.193251e+00 ; 2.991633e-01 ];
Tc_21  = [ -2.617743e+02 ; -1.250346e+02 ; 9.579361e+02 ];
omc_error_21 = [ 8.228677e-03 ; 1.110140e-02 ; 1.896076e-02 ];
Tc_error_21  = [ 7.991861e+00 ; 6.309026e+00 ; 6.619671e+00 ];

%-- Image #22:
omc_22 = [ -1.549415e+00 ; -2.035153e+00 ; 5.602062e-01 ];
Tc_22  = [ -1.067007e+02 ; -1.351724e+02 ; 1.153050e+03 ];
omc_error_22 = [ 6.443360e-03 ; 6.646004e-03 ; 1.004161e-02 ];
Tc_error_22  = [ 9.486009e+00 ; 7.448709e+00 ; 6.677912e+00 ];

