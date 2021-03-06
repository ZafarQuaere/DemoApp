package com.zaf.econnecto.ui.fragments;

import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zaf.econnecto.R;
import com.zaf.econnecto.network_call.response_model.login.Data;
import com.zaf.econnecto.ui.presenters.ProfilePresenter;
import com.zaf.econnecto.ui.presenters.operations.IFragProfile;


public class FragmentProfile extends BaseFragment<ProfilePresenter> implements IFragProfile {


    private View view;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    protected ProfilePresenter initPresenter() {
        return new ProfilePresenter(getActivity(),this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         view = inflater.inflate(R.layout.activity_my_business, container, false);
        getPresenter().updateUI();
        return view;
    }

    @Override
    public void updateUI(Data data) {
       /* if (data != null){
            EditText editName = (EditText)view.findViewById(R.id.editUserName);
            EditText editMobile = (EditText)view.findViewById(R.id.editMobile);
            EditText editEmailId = (EditText)view.findViewById(R.id.editEmailId);
            EditText editDealerId = (EditText)view.findViewById(R.id.editDealerId);
            editName.setText(data.getFirstName());
            editMobile.setText(data.getActive()+"");
            editEmailId.setText(data.getBusinessStatus()+"");
            editDealerId.setText(getString(R.string.dealerId)+": "+data.getLastName());
        }*/
    }

    @Override
    public void onValidationError(String msg) {

    }

    @Override
    public void callApi() {

    }
}
