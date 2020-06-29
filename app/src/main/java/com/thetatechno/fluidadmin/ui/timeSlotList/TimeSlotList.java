package com.thetatechno.fluidadmin.ui.timeSlotList;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.thetatechno.fluidadmin.R;
import com.thetatechno.fluidadmin.databinding.FragmentTimeSlotListBinding;
import com.thetatechno.fluidadmin.listeners.OnItemClickedListener;
import com.thetatechno.fluidadmin.model.ConfirmAppointmentResponse;
import com.thetatechno.fluidadmin.model.appointment_model.AppointmentDayDetails;
import com.thetatechno.fluidadmin.model.staff_model.Staff;
import com.thetatechno.fluidadmin.model.Status;
import com.thetatechno.fluidadmin.model.time_slot_model.TimeSlot;
import com.thetatechno.fluidadmin.utils.Constants;


import java.util.ArrayList;


public class TimeSlotList extends Fragment implements OnItemClickedListener {
    private NavController navController;
    private TimeSlotListViewModel timeSlotListViewModel;
    private String bookDate;
    private String specialityCode;
    private int selectedProviderIndex;
    private Button cancelBtn, confirmBtn;
    private ImageButton previousBtn, nextBtn;
    private TextView providerNameTxtView;
    private ImageView providerImgView;
    private final float STROKE_WIDTH = 5f;
    private final float CENTER_RADIUS = 30f;
    private TimeSlot selectedTimeSlot = new TimeSlot();
    private FragmentTimeSlotListBinding binding;
    private ArrayList<AppointmentDayDetails> appointmentDayDetailsForSpecificProviderArrayList = new ArrayList<>();
    private ArrayList<AppointmentDayDetails> providerWorkingInSelectedDayList = new ArrayList<>();
    private ViewPager2 timeSlotsViewPager;
    private int docNameHeight, dateHeight, actionBarHeight, controlHeight, screenHeight, bottomNavigationHeight, androidBottomNavigator;
    private static String ARG_PROVIDER_LIST = "providerList";
    private static String ARG_BOOK_DATE = "bookDate";
    private static String ARG_SPECIALITY_CODE = "specialityCode";
    private static String ARG_PROVIDER_NAME = "providerName";
    private static String ARG_BOOK_TIME = "bookedTime";
    private static String ARG_CLIENT_ID = "clientId";
    private String clientId;
    private CircularProgressDrawable circularProgressDrawable;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            bookDate = getArguments().getString(ARG_BOOK_DATE);
            providerWorkingInSelectedDayList = getArguments().getParcelableArrayList(ARG_PROVIDER_LIST);
            specialityCode = getArguments().getString(ARG_SPECIALITY_CODE);
            clientId = getArguments().getString(ARG_CLIENT_ID);
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_time_slot_list, container, false);
        timeSlotListViewModel = new ViewModelProvider(this).get(TimeSlotListViewModel.class);
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                navController.popBackStack();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
        cancelBtn = binding.btnCancel;
        confirmBtn = binding.btnConfirm;
        previousBtn = binding.previousBtn;
        nextBtn = binding.nextBtn;
        providerNameTxtView = binding.providerNameTxtView;
        providerImgView = binding.providerImgView;
        timeSlotsViewPager = binding.timeSlotsViewPager;
        selectedProviderIndex = 0;
        getProviderDataAndDisplayIt();
        getScheduledCalenderDaysList();

        timeSlotsViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);

            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

                if (confirmBtn.isEnabled()) {
                    confirmBtn.setEnabled(false);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);

            }
        });
        if (providerWorkingInSelectedDayList.size() == 0) {
            previousBtn.setVisibility(View.GONE);
            nextBtn.setVisibility(View.GONE);
        } else {
            setHiddenForPreviousBtn();
            setHiddenForNextBtn();
        }
        return binding.getRoot();
    }

    private void handlingPreviousAndNextPages() {
        binding.nextAppointmentSlotsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.previousAppointmentSlotsBtn.setVisibility(View.VISIBLE);
                if (timeSlotsViewPager.getCurrentItem() < appointmentDayDetailsForSpecificProviderArrayList.size()) {
                    timeSlotsViewPager.setCurrentItem(timeSlotsViewPager.getCurrentItem() + 1);
                    if (timeSlotsViewPager.getCurrentItem() == appointmentDayDetailsForSpecificProviderArrayList.size() - 1) {
                        binding.nextAppointmentSlotsBtn.setVisibility(View.INVISIBLE);
                    }
                }
            }
        });

        binding.previousAppointmentSlotsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.nextAppointmentSlotsBtn.setVisibility(View.VISIBLE);
                if (timeSlotsViewPager.getCurrentItem() > 0) {
                    timeSlotsViewPager.setCurrentItem(timeSlotsViewPager.getCurrentItem() - 1);
                }
                if (timeSlotsViewPager.getCurrentItem() == 0) {
                    binding.previousAppointmentSlotsBtn.setVisibility(View.INVISIBLE);
                }
            }
        });

        binding.previousAppointmentSlotsBtn.setVisibility(View.INVISIBLE);
    }

    private int getAndroidBottomNavigatorHeight() {
        Resources resources = getActivity().getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return resources.getDimensionPixelSize(resourceId);
        }
        return 0;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onStart() {
        super.onStart();

        Toast.makeText(getActivity(), bookDate, Toast.LENGTH_SHORT).show();
        TypedValue tv = new TypedValue();
        if (getActivity().getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
            Log.e("actionBarHeight", actionBarHeight * 3 + "");
        }
        // Calculate Window height
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenHeight = displayMetrics.heightPixels;
        Log.e("screenHeight", screenHeight + "");


        LinearLayout docNameLayout = binding.providerDataLayout;
        ViewTreeObserver vto1 = docNameLayout.getViewTreeObserver();
        vto1.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                docNameLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                docNameHeight = docNameLayout.getMeasuredHeight();
                Log.e("docNameHeight", docNameHeight * 3 + "");

            }
        });

        ConstraintLayout conLayout = binding.control;
        ViewTreeObserver vto2 = conLayout.getViewTreeObserver();
        vto2.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                conLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                controlHeight = conLayout.getMeasuredHeight();
                Log.e("controlHeight", controlHeight * 3 + "");
            }
        });

        bottomNavigationHeight = actionBarHeight;
        androidBottomNavigator = getAndroidBottomNavigatorHeight();
        Log.e("androidBottomNavigator", androidBottomNavigator + "");

        ViewGroup.LayoutParams layoutParams = binding.timeSlotsViewPager.getLayoutParams();
        layoutParams.height = (int) (screenHeight - (actionBarHeight + docNameHeight + controlHeight + bottomNavigationHeight / 2) * 3) /*+ androidBottomNavigator*/; // is in pixels
        Log.e("ViewPager Height", layoutParams.height + "");
        binding.timeSlotsViewPager.setLayoutParams(layoutParams);

        previousBtn.setOnClickListener(v -> {
            if (selectedProviderIndex > 0 && selectedProviderIndex < providerWorkingInSelectedDayList.size()) {
                selectedProviderIndex--;
                updateDataWhileSwipingOnProviderList();
                setHiddenForPreviousBtn();
                setVisibilityForNextBtn();
            }

        });

        nextBtn.setOnClickListener(v -> {
            if (selectedProviderIndex >= 0 && selectedProviderIndex + 1 < providerWorkingInSelectedDayList.size()) {
                selectedProviderIndex++;
                updateDataWhileSwipingOnProviderList();
                setVisibilityForPreviousBtn();
                setHiddenForNextBtn();
            }


        });

        cancelBtn.setOnClickListener(v -> {
            navController.popBackStack();
        });
        confirmBtn.setOnClickListener(v -> confirmAppointmentBooking(selectedTimeSlot.getTime()));
    }

    private void updateDataWhileSwipingOnProviderList() {
        timeSlotListViewModel.getProviderData(specialityCode, providerWorkingInSelectedDayList.get(selectedProviderIndex).getProviderId());
        timeSlotListViewModel.getScheduledCalenderDaysListForSpecificProvider(bookDate, specialityCode, providerWorkingInSelectedDayList.get(selectedProviderIndex).getProviderId(), Constants.APPOINTMENT_LENGTH, "N");
    }


    private void setVisibilityForNextBtn() {

        nextBtn.setVisibility(View.VISIBLE);
    }

    private void setVisibilityForPreviousBtn() {

        previousBtn.setVisibility(View.VISIBLE);
    }

    private void setHiddenForPreviousBtn() {
        if (selectedProviderIndex == 0)
            previousBtn.setVisibility(View.INVISIBLE);
    }

    private void setHiddenForNextBtn() {
        if (selectedProviderIndex == providerWorkingInSelectedDayList.size() - 1)
            nextBtn.setVisibility(View.INVISIBLE);
    }


    private void getProviderDataAndDisplayIt() {
        timeSlotListViewModel.getProviderData(specialityCode, providerWorkingInSelectedDayList.get(selectedProviderIndex).getProviderId()).observe(getViewLifecycleOwner(), new Observer<Staff>() {
            @Override
            public void onChanged(Staff staff) {
                if (staff != null) {
                    circularProgressDrawable = new CircularProgressDrawable(requireContext());
                    circularProgressDrawable.setStrokeWidth(STROKE_WIDTH);
                    circularProgressDrawable.setCenterRadius(CENTER_RADIUS);
                    circularProgressDrawable.start();
                    if (staff.getFirstName().isEmpty()) {
                        providerNameTxtView.setText("");
                    } else {
                        providerNameTxtView.setText(String.format("%s %s", staff.getFirstName(), staff.getFamilyName()));
                    }
                    if (staff.getSpeciality().isEmpty()) {
                        binding.providerClinicTxtView.setText("");
                    } else {
                        binding.providerClinicTxtView.setText(staff.getSpeciality());
                    }
                    if (staff.getImageLink() != null) {
                        Glide.with(getContext())
                                .load(Constants.BASE_URL + Constants.BASE_EXTENSION_FOR_PHOTOS + staff.getImageLink())
                                .circleCrop()
                                .placeholder(circularProgressDrawable)
                                .into(providerImgView);
                    } else {
                        providerImgView.setImageResource(R.drawable.man);

                    }

                } else {
                    providerImgView.setImageResource(R.drawable.man);
                    providerNameTxtView.setText("name 3f3f4f4f");
                    binding.providerClinicTxtView.setText("");


                }
            }
        });

    }

    private void getScheduledCalenderDaysList() {
        timeSlotListViewModel.getScheduledCalenderDaysListForSpecificProvider(bookDate, specialityCode, providerWorkingInSelectedDayList.get(selectedProviderIndex).getProviderId(), Constants.APPOINTMENT_LENGTH, "N").observe(getViewLifecycleOwner(), appointmentCalenderDaysListData -> {

            if (appointmentCalenderDaysListData != null && appointmentCalenderDaysListData.getDayDetailsList() != null)
                appointmentDayDetailsForSpecificProviderArrayList = appointmentCalenderDaysListData.getDayDetailsList();

            setUpViewPagerWithAllTimeSlots();


        });

    }

    private void setUpViewPagerWithAllTimeSlots() {
        int scrooled_position = -1;
        for (AppointmentDayDetails appointmentDayDetails : appointmentDayDetailsForSpecificProviderArrayList) {
            if (appointmentDayDetails.getDate().equals(bookDate)) {
                scrooled_position = appointmentDayDetailsForSpecificProviderArrayList.indexOf(appointmentDayDetails);
            }
        }
        ViewPagerAdapter mViewPagerAdapter = new ViewPagerAdapter(requireActivity(), appointmentDayDetailsForSpecificProviderArrayList);
        timeSlotsViewPager.setAdapter(mViewPagerAdapter);
        timeSlotsViewPager.setOffscreenPageLimit(1);
        if (scrooled_position != -1)
            timeSlotsViewPager.setCurrentItem(scrooled_position);
        handlingPreviousAndNextPages();

    }

    private void confirmAppointmentBooking(String appointmentTime) {
        timeSlotListViewModel.bookAppointment(appointmentDayDetailsForSpecificProviderArrayList.get(timeSlotsViewPager.getCurrentItem()).getSessionCode(), appointmentTime,clientId).observe(getViewLifecycleOwner(), new Observer<ConfirmAppointmentResponse>() {
            @Override
            public void onChanged(ConfirmAppointmentResponse status) {
                if (status != null) {
                    if (status.getError().getErrorCode()==0) {
                        Toast.makeText(getContext(), "booking success", Toast.LENGTH_SHORT).show();
                        Bundle bundle = new Bundle();
                        bundle.putString(ARG_PROVIDER_NAME, providerNameTxtView.getText().toString());
                        bundle.putString(ARG_BOOK_DATE, appointmentDayDetailsForSpecificProviderArrayList.get(timeSlotsViewPager.getCurrentItem()).getDate());
                        bundle.putString(ARG_BOOK_TIME, selectedTimeSlot.getTime());
                        bundle.putString(ARG_SPECIALITY_CODE, specialityCode);
                        navController.navigate(R.id.action_timeSlotList_to_confirmAppointment, bundle);
                    }
                    Toast.makeText(getContext(), status.getError().getErrorMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    @Override
    public void onClickAtPosition(TimeSlot timeSlot) {
        selectedTimeSlot = timeSlot;
        Toast.makeText(getContext(), timeSlot.getTime() + " " + timeSlot.getSlotId(), Toast.LENGTH_SHORT).show();
//        timeSlotListViewModel.setSelectedTimeSlot(selectedTimeSlot);
        confirmBtn.setEnabled(true);

    }


}
