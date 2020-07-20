package com.thetatechno.fluidadmin.ui.branches;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.thetatechno.fluidadmin.model.branches_model.BranchesResponse;
import com.thetatechno.fluidadmin.model.branches_model.BranchesResponseModel;
import com.thetatechno.fluidadmin.network.repositories.BranchesRepository;
import com.thetatechno.fluidadmin.utils.App;
import com.thetatechno.fluidadmin.utils.PreferenceController;

public class BranchesViewModel extends ViewModel {
   private BranchesRepository branchesRepository = new BranchesRepository();
    MutableLiveData<BranchesResponseModel> getAllBranches()
    {
        return branchesRepository.getAllBranches(PreferenceController.getInstance(App.getContext()).get(PreferenceController.LANGUAGE).toUpperCase());
    }
}
