package com.thetatechno.fluidadmin.ui;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.thetatechno.fluidadmin.R;
import com.thetatechno.fluidadmin.databinding.ConfirmAppointmentBookingDialogBinding;
import com.thetatechno.fluidadmin.listeners.OnConfirmAppointmentListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

public class ConfirmAppointment extends Fragment {
    private String providerName;
    private String bookedDate;
    private String bookedTime;
    private String specialityCode;
    private static String ARG_PROVIDER_NAME = "providerName";
    private static String ARG_BOOK_DATE = "bookDate";
    private static String ARG_BOOK_TIME = "bookedTime";
    private static String ARG_SPECIALITY_CODE = "specialityCode";
    NavController navController;
    OnConfirmAppointmentListener onConfirmAppointmentListener;
    private Button okBtn, takeScreenShotBtn;
    ConfirmAppointmentBookingDialogBinding binding;

    MediaPlayer mp;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof HomeActivity) {
            onConfirmAppointmentListener = (OnConfirmAppointmentListener) context;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mp = MediaPlayer.create(requireActivity(), R.raw.screen_shot);
        if (getArguments() != null) {
            bookedDate = getArguments().getString(ARG_BOOK_DATE);
            bookedTime = getArguments().getString(ARG_BOOK_TIME);
            providerName = getArguments().getString(ARG_PROVIDER_NAME);
            specialityCode = getArguments().getString(ARG_SPECIALITY_CODE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.confirm_appointment_booking_dialog, container, false);
        navController = Navigation.findNavController(getActivity(),R.id.nav_host_fragment);
        binding.appointmentProviderNameTxtView.setText(providerName);
        binding.timeTxtView.setText(bookedTime);
        binding.appointmentDateTxtView.setText(bookedDate);
        binding.specialityTxtView.setText(specialityCode);
        binding.okBtn.setOnClickListener(v -> {
//            if (onConfirmAppointmentListener != null) {
//                onConfirmAppointmentListener.onClickOnOkBtn();
//
//            }
navController.popBackStack();
        });

        binding.takeScreenShotBtn.setOnClickListener(v -> {
            screenshoot();
            mp.start();
        });

        if (!isStoragePermissionGranted()) {
            checkPermission();
        }

        return binding.getRoot();

    }

    @Override
    public void onDetach() {
        super.onDetach();
        onConfirmAppointmentListener = null;
    }

    private void takeScreenshot() {
        Date now = new Date();
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);

        try {
            // image naming and path  to include sd card  appending name you choose for file
            String mPath = Environment.getExternalStorageDirectory().toString() + "/" + now + ".jpg";

            // create bitmap screen capture
//            View v1 = requireActivity().getWindow().getDecorView().getRootView();
            View v1 = binding.consBookedAppointment;
            v1.setDrawingCacheEnabled(true);
            v1.buildDrawingCache(true);
            Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
            v1.setDrawingCacheEnabled(false);

            File imageFile = new File(mPath);

            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();

//            openScreenshot(imageFile);
        } catch (Throwable e) {
            // Several error may come out with file handling or DOM
            e.printStackTrace();
        }
    }

    private void screenshoot() {
        Date date = new Date();
        CharSequence now = android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", date);
        String filename = Environment.getExternalStorageDirectory() + "/ScreenShooter/" + now + ".jpg";

        View root = requireActivity().getWindow().getDecorView().getRootView();
        root.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(root.getDrawingCache());
        root.setDrawingCacheEnabled(false);


        File file = new File(filename);
        file.getParentFile().mkdirs();

        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openScreenshot(File imageFile) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(imageFile);
        intent.setDataAndType(uri, "image/*");
        startActivity(intent);
    }

    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkPermission()) {
                Log.v("TAG", "Permission is granted");
                return true;
            } else {

                Log.v("TAG", "Permission is revoked");
                ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v("TAG", "Permission is granted");
            return true;
        }
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

}
