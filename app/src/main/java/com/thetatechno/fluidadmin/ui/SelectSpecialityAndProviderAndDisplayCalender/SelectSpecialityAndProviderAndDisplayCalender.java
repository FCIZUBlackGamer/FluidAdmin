package com.thetatechno.fluidadmin.ui.SelectSpecialityAndProviderAndDisplayCalender;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.thetatechno.fluidadmin.R;
import com.thetatechno.fluidadmin.databinding.FragmentBookAppointmentBinding;
import com.thetatechno.fluidadmin.model.ClientData;
import com.thetatechno.fluidadmin.model.ClientModelForRegister;
import com.thetatechno.fluidadmin.model.appointment_model.AppointmentCalenderDaysListData;
import com.thetatechno.fluidadmin.model.appointment_model.AppointmentDayDetails;
import com.thetatechno.fluidadmin.model.branches_model.BranchesResponseModel;
import com.thetatechno.fluidadmin.model.staff_model.Staff;
import com.thetatechno.fluidadmin.model.staff_model.StaffData;
import com.thetatechno.fluidadmin.model.branches_model.Branch;
import com.thetatechno.fluidadmin.model.branches_model.BranchesResponse;
import com.thetatechno.fluidadmin.model.code_model.Code;
import com.thetatechno.fluidadmin.model.code_model.CodeList;
import com.thetatechno.fluidadmin.ui.EspressoTestingIdlingResource;
import com.thetatechno.fluidadmin.utils.Constants;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SelectSpecialityAndProviderAndDisplayCalender extends Fragment {
    SelectSpecialityAndProviderAndDisplayCalenderViewModel selectSpecialityAndProviderAndDisplayCalenderViewModel;
    private ArrayList<Code> specialitiesList = new ArrayList<>();
    private ArrayList<Branch> sitesList = new ArrayList<>();
    private ArrayList<Staff> providerList = new ArrayList<>();
    private ArrayList<ClientModelForRegister> clientList = new ArrayList<>();
    private FragmentBookAppointmentBinding binding;
    private String specialityCode = "";
    private String siteCode = "";
    private String providerId = "";
    private String bookDate;
    private String date = "";
    private ArrayList<AppointmentDayDetails> appointmentDayDetailsArrayList = new ArrayList<>();
    private ArrayList<AppointmentDayDetails> providerWorkingInSelectedDayList = new ArrayList<>();
    private static String TAG = "bookAppointment";
    private static String ARG_CLIENT_ID = "clientId";
    private static String ARG_PROVIDER_LIST = "providerList";
    private static String ARG_BOOK_DATE = "bookDate";
    private static String ARG_SPECIALITY_CODE = "specialityCode";
    private static String ARG_SITE_ID = "siteId";
    private String clientId;
    private NavController navController;
    private List<Calendar> selectedDays;
    String daysBundelKey = "SelectedDays";

    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public SelectSpecialityAndProviderAndDisplayCalender() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (this.getArguments() != null) {
            clientId = getArguments().getString(ARG_CLIENT_ID);
        }
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                onCancelOrBackButtonPressed();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);

    }

    @Override
    public void onResume() {
        super.onResume();
//        Calendar c = Calendar.getInstance();
//        Log.e("This Month",c.getTime().getMonth()+"");
//        binding.calendarView.setMinimumDate(c);

        binding.clientListTxtView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                clientId = ((ClientModelForRegister) parent.getItemAtPosition(position)).getClientId();
            }
        });

        binding.specialityList.setOnItemClickListener((parent, view, position, id) -> {
            specialityCode = ((Code) parent.getItemAtPosition(position)).getCode();
            providerId = "";
            binding.providerListTxt.setText("");
            getProviderList();
            hideKeyboardFrom(requireActivity(), binding.getRoot());

        });
        binding.providerListTxt.setOnItemClickListener((parent, view, position, id) -> {
            providerId = ((Staff) parent.getItemAtPosition(position)).getStaffId();
            getCalenderData();
            hideKeyboardFrom(requireActivity(), binding.getRoot());
        });
        binding.siteList.setOnItemClickListener((parent, view, position, id) -> {
            siteCode = ((Branch) parent.getItemAtPosition(position)).getSiteId();
            if (appointmentDayDetailsArrayList.size() == 0) {
                selectSpecialityAndProviderAndDisplayCalenderViewModel.getScheduledCalenderDaysList(date, specialityCode, providerId, Constants.APPOINTMENT_LENGTH, "N", siteCode).observe(getViewLifecycleOwner(), calenderDaysListDataObserver);

            } else {
                selectSpecialityAndProviderAndDisplayCalenderViewModel.getScheduledCalenderDaysList(date, specialityCode, providerId, Constants.APPOINTMENT_LENGTH, "N", siteCode);
            }
            hideKeyboardFrom(getActivity(), binding.getRoot());
        });

    }

    @Override
    public void onDestroyView() {
        selectSpecialityAndProviderAndDisplayCalenderViewModel.getSpecialities().removeObserver(codeListObserver);
        selectSpecialityAndProviderAndDisplayCalenderViewModel.getSites().removeObserver(siteListObserver);
        selectSpecialityAndProviderAndDisplayCalenderViewModel.getAllProvidersInSpecificSpeciality(specialityCode).removeObserver(providerListObserver);
        selectSpecialityAndProviderAndDisplayCalenderViewModel.getScheduledCalenderDaysList(date, specialityCode, providerId, Constants.APPOINTMENT_LENGTH, "N", siteCode).removeObserver(calenderDaysListDataObserver);
        super.onDestroyView();

    }

    @Override
    public void onStart() {
        super.onStart();
        if (selectedDays != null) {
            binding.calendarView.setSelectedDates(selectedDays);
            selectSpecialityAndProviderAndDisplayCalenderViewModel.getAllProvidersInSpecificSpeciality(specialityCode).observe(getViewLifecycleOwner(), providerListObserver);
        }
        if (getArguments() != null && getArguments().getSerializable(daysBundelKey) != null) {
            selectedDays = (List<Calendar>) getArguments().getSerializable(daysBundelKey);
            binding.calendarView.setSelectedDates(selectedDays);
        }
        binding.calendarView.setOnDayClickListener(eventDay -> {
            Calendar clickedDayCalendar = eventDay.getCalendar();

            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat formattedDate
                    = new SimpleDateFormat("dd-MM-yyyy");
            String dateFormatted
                    = formattedDate.format(
                    clickedDayCalendar.getTime());
            providerWorkingInSelectedDayList.clear();
            for (AppointmentDayDetails appointmentDayDetails : appointmentDayDetailsArrayList) {
                if (appointmentDayDetails.getDate().equals(dateFormatted)) {
                    providerWorkingInSelectedDayList.add(appointmentDayDetails);
                }
            }
            appointmentDayDetailsArrayList.clear();
            if (providerWorkingInSelectedDayList.size() > 0) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(daysBundelKey, (Serializable) selectedDays);
                bundle.putString(ARG_BOOK_DATE, providerWorkingInSelectedDayList.get(0).getDate());
                bundle.putString(ARG_SPECIALITY_CODE, specialityCode);
                bundle.putParcelableArrayList(ARG_PROVIDER_LIST, providerWorkingInSelectedDayList);
                bundle.putString(ARG_CLIENT_ID, clientId);
                bundle.putString(ARG_SITE_ID, siteCode);
                navController.navigate(R.id.action_selectSpecialityAndProviderAndDisplayCalender_to_timeSlotList, bundle);

            }

        });

        binding.calendarView.setOnPreviousPageChangeListener(() -> {
            Calendar calender = binding.calendarView.getCurrentPageDate();
            SimpleDateFormat formattedDate
                    = new SimpleDateFormat("dd-MM-yyyy");
            String formatedDate = formattedDate.format(calender.getTime());
            date = formatedDate;
            selectSpecialityAndProviderAndDisplayCalenderViewModel.getScheduledCalenderDaysList(date, specialityCode, providerId, Constants.APPOINTMENT_LENGTH, "N", siteCode);


        });

        binding.calendarView.setOnForwardPageChangeListener(() -> {
            Toast.makeText(getActivity(), "Load next days of month", Toast.LENGTH_SHORT).show();
            Calendar calender = binding.calendarView.getCurrentPageDate();
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat formattedDate
                    = new SimpleDateFormat("dd-MM-yyyy");
            String dateFormatted = formattedDate.format(calender.getTime());
            date = dateFormatted;
            selectSpecialityAndProviderAndDisplayCalenderViewModel.getScheduledCalenderDaysList(date, specialityCode, providerId, Constants.APPOINTMENT_LENGTH, "N", siteCode);

        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_book_appointment, container, false);
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        Log.i(TAG, "onCreateView method ");

        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        selectSpecialityAndProviderAndDisplayCalenderViewModel.getSpecialities().observe(getViewLifecycleOwner(), codeListObserver);
        selectSpecialityAndProviderAndDisplayCalenderViewModel.getSites().observe(getViewLifecycleOwner(), siteListObserver);
        if (!specialityCode.isEmpty())
            selectSpecialityAndProviderAndDisplayCalenderViewModel.getAllProvidersInSpecificSpeciality(specialityCode).observe(getViewLifecycleOwner(), providerListObserver);
        selectSpecialityAndProviderAndDisplayCalenderViewModel.getAllClients().observe(getViewLifecycleOwner(), clientListObserver);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        selectSpecialityAndProviderAndDisplayCalenderViewModel = ViewModelProviders.of(this).get(SelectSpecialityAndProviderAndDisplayCalenderViewModel.class);
        Log.i(TAG, "onViewCreated view ");
    }


    private void getCalenderData() {
        EspressoTestingIdlingResource.increment();
        if(appointmentDayDetailsArrayList.size()==0)
            selectSpecialityAndProviderAndDisplayCalenderViewModel.getScheduledCalenderDaysList(date, specialityCode, providerId, Constants.APPOINTMENT_LENGTH, "N", siteCode).observe(getViewLifecycleOwner(),calenderDaysListDataObserver);
        else
        selectSpecialityAndProviderAndDisplayCalenderViewModel.getScheduledCalenderDaysList(date, specialityCode, providerId, Constants.APPOINTMENT_LENGTH, "N", siteCode);
        EspressoTestingIdlingResource.decrement();

    }

    private void getProviderList() {

        if (providerList.size() == 0) {
            selectSpecialityAndProviderAndDisplayCalenderViewModel.getAllProvidersInSpecificSpeciality(specialityCode).observe(getViewLifecycleOwner(), providerListObserver);

        } else {
            selectSpecialityAndProviderAndDisplayCalenderViewModel.getAllProvidersInSpecificSpeciality(specialityCode);
        }
        if (appointmentDayDetailsArrayList.size() == 0) {
            selectSpecialityAndProviderAndDisplayCalenderViewModel.getScheduledCalenderDaysList(date, specialityCode, providerId, Constants.APPOINTMENT_LENGTH, "N", siteCode).observe(getViewLifecycleOwner(), calenderDaysListDataObserver);

        } else {
            selectSpecialityAndProviderAndDisplayCalenderViewModel.getScheduledCalenderDaysList(date, specialityCode, providerId, Constants.APPOINTMENT_LENGTH, "N", siteCode);
        }

    }

    private List<Calendar> getDaysBundelKey() {
        List<Calendar> calendars = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(Calendar.getInstance().getTime());
        int today = calendar.get(Calendar.DAY_OF_MONTH);
        int numDaysInMonth = getNumDaysInThisMonth(calendar, Calendar.MONTH, Calendar.YEAR);

        for (int i = today; i < numDaysInMonth; i++) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(Calendar.getInstance().getTime());
            cal.add(Calendar.DAY_OF_MONTH, i);
            cal.add(Calendar.HOUR_OF_DAY, -24);
            SimpleDateFormat formattedDate
                    = new SimpleDateFormat("dd-MM-yyyy");

            String dateFormatted
                    = formattedDate.format(
                    cal.getTime());

            for (AppointmentDayDetails appointmentDayDetails : appointmentDayDetailsArrayList) {
                if (appointmentDayDetails.getDate().equals(dateFormatted) && appointmentDayDetails.getAvailableSlots() > 0) {
                    calendars.add(cal);
                    break;
                }

            }
        }

        Log.e("TAG", "Num Days Available: " + calendars.size());
        if (calendars.size() > 0) {
            Log.e("TAG", "First Date: " + calendars.get(0).getTime().toString());
            Log.e("TAG", "Last Date: " + calendars.get(calendars.size() - 1).getTime().toString());
        }
        return calendars;
    }

    private List<Calendar> getDaysKey(AppointmentCalenderDaysListData appointments) {
        List<Calendar> calendars = new ArrayList<>();
        Calendar calendar;
        Date todayDate = new Date();
        for (int i = 0; i < appointments.getDayDetailsList().size(); i++) {
            SimpleDateFormat formattedDate
                    = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);

            try {
                calendar = Calendar.getInstance();
                Date appointmentDate = formattedDate.parse(appointments.getDayDetailsList().get(i).getDate());
                if (appointmentDate.after(todayDate) || appointmentDate.equals(todayDate))
                    calendar.setTime(appointmentDate);
                calendars.add(calendar);
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
        Log.i(TAG,calendars.size()+"");
        return calendars;
    }

    private int getNumDaysInThisMonth(Calendar calendar, int month, int year) {
        calendar.set(year, month, 1);
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    private void onCancelOrBackButtonPressed() {
        navController.popBackStack();
    }

    private Observer<BranchesResponseModel> siteListObserver = new Observer<BranchesResponseModel>() {
        @Override
        public void onChanged(BranchesResponseModel model) {
            EspressoTestingIdlingResource.increment();
            if (model.getBranchesResponse() != null) {
                if (model.getBranchesResponse().getBranchList() != null) {
                    sitesList = (ArrayList<Branch>) model.getBranchesResponse().getBranchList();
                    ArrayAdapter<Branch> adapter =
                            new ArrayAdapter<Branch>(getContext(), R.layout.dropdown_menu_popup_item, sitesList);
                    binding.siteList.setAdapter(adapter);
                }

                if (sitesList != null && sitesList.size() == 1) {
                    siteCode = sitesList.get(0).getSiteId();
                    binding.siteList.setVisibility(View.GONE);
                    binding.siteLayout.setVisibility(View.GONE);
                } else {
                    binding.siteList.setVisibility(View.VISIBLE);
                    binding.siteLayout.setVisibility(View.VISIBLE);
                }

            }
            EspressoTestingIdlingResource.decrement();
        }
    };
    private Observer<CodeList> codeListObserver = new Observer<CodeList>() {
        @Override
        public void onChanged(CodeList codeList) {
            EspressoTestingIdlingResource.increment();
            if (codeList != null && codeList.getCodeList() != null) {
                specialitiesList = (ArrayList<Code>) codeList.getCodeList();
                ArrayAdapter<Code> adapter =
                        new ArrayAdapter<Code>(getContext(), R.layout.dropdown_menu_popup_item, specialitiesList);
                binding.specialityList.setAdapter(adapter);
            }
            EspressoTestingIdlingResource.decrement();
            if (specialitiesList != null && specialitiesList.size() == 1) {
                specialityCode = specialitiesList.get(0).getCode();
                binding.specialityList.setVisibility(View.GONE);
                binding.specialityLayout.setVisibility(View.GONE);
            } else {
                binding.specialityList.setVisibility(View.VISIBLE);
                binding.specialityLayout.setVisibility(View.VISIBLE);
            }
        }
    };

    private Observer<StaffData> providerListObserver = new Observer<StaffData>() {
        @Override
        public void onChanged(StaffData staffData) {
            EspressoTestingIdlingResource.increment();


            if (staffData.getStaffList() != null) {
                providerList = (ArrayList<Staff>) staffData.getStaffList();
                ArrayAdapter<Staff> adapter =
                        new ArrayAdapter<Staff>(getContext(), R.layout.dropdown_menu_popup_item, providerList);
                binding.providerListTxt.setAdapter(adapter);
                for (int i = 0; i < providerList.size(); i++) {
                    if (providerId.equals(providerList.get(i).getStaffId())) {
//                        Toast.makeText(requireActivity(), providerList.get(i).getFirstName(), Toast.LENGTH_SHORT).show();
                        binding.providerListTxt.setText(providerList.get(i).getFirstName() + " " + providerList.get(i).getFamilyName());
                    }
                }
            } else {
                binding.providerListTxt.setText("");
                providerId = "";
            }

            if (providerList != null && providerList.size() == 1) {
                providerId = providerList.get(0).getStaffId();
                binding.providerListTxt.setVisibility(View.GONE);
                binding.providerListLayout.setVisibility(View.GONE);
            } else {
                binding.providerListTxt.setVisibility(View.VISIBLE);
                binding.providerListLayout.setVisibility(View.VISIBLE);
            }
            EspressoTestingIdlingResource.decrement();

        }

    };
    private Observer<ClientData> clientListObserver = new Observer<ClientData>() {
        @Override
        public void onChanged(ClientData clientData) {
            EspressoTestingIdlingResource.increment();
            if (clientData != null) {
                if (clientData.getPersonList() != null)
                    clientList = (ArrayList<ClientModelForRegister>) clientData.getPersonList();
                if (clientList.size() == 1) {
                    binding.clientListLayout.setVisibility(View.GONE);
                } else {
                    binding.clientListLayout.setVisibility(View.VISIBLE);
                }
                ArrayAdapter<ClientModelForRegister> adapter =
                        new ArrayAdapter<ClientModelForRegister>(getContext(), R.layout.dropdown_menu_popup_item, clientList);
                binding.clientListTxtView.setAdapter(adapter);

            }
            EspressoTestingIdlingResource.decrement();


        }
    };

    private Observer<AppointmentCalenderDaysListData> calenderDaysListDataObserver = new Observer<AppointmentCalenderDaysListData>() {
        @Override
        public void onChanged(AppointmentCalenderDaysListData appointmentCalenderDaysListData) {
            EspressoTestingIdlingResource.increment();
            if(appointmentCalenderDaysListData!=null) {
                if (appointmentCalenderDaysListData.getDayDetailsList() != null) {
                    appointmentDayDetailsArrayList = (ArrayList<AppointmentDayDetails>) appointmentCalenderDaysListData.getDayDetailsList();
                    selectedDays = getDaysKey(appointmentCalenderDaysListData);
                    binding.calendarView.setSelectedDates(selectedDays);
                }
            }
            EspressoTestingIdlingResource.decrement();
        }
    };


}
