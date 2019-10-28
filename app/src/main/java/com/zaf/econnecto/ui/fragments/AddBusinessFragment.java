package com.zaf.econnecto.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.zaf.econnecto.R;
import com.zaf.econnecto.ui.interfaces.DialogButtonClick;
import com.zaf.econnecto.ui.presenters.AddBizPresenter;
import com.zaf.econnecto.ui.presenters.operations.IAddBiz;
import com.zaf.econnecto.utils.LogUtils;


public class AddBusinessFragment extends BaseFragment<AddBizPresenter> implements IAddBiz, AdapterView.OnItemSelectedListener {

    private View view;
    private String[] categoryArray;
    private String[] chargesArray;
    private Spinner spinnerCategory;
    private Spinner spinnerCharges;
    private Context mContext;
    private Button btnAddBizns;
    private EditText editBizWebsite;
    private EditText editEmailId;
    private EditText editPhone2;
    private EditText editPhone1;
    private EditText editAddress;
    private EditText editAwards;
    private EditText editFoundYear;
    private EditText editBizDetailDescrip;
    private EditText editBizShortDescrip;
    private EditText editBizName;
    private String bizCategory;
    private String bizCharges;
    private EditText editBizAmount;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override
    protected AddBizPresenter initPresenter() {
        return new AddBizPresenter(getActivity(), this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_add_biz, container, false);
        mContext = getActivity();
        categoryArray = getResources().getStringArray(R.array.biz_category);
        chargesArray = getResources().getStringArray(R.array.charges_uniit);
        initSpinners(view);
        initUI(view);
        return view;
    }

    private void initSpinners(View view) {
        spinnerCategory = (Spinner) view.findViewById(R.id.spinnerCategory);
        spinnerCharges = (Spinner) view.findViewById(R.id.spinnerCharges);
        spinnerCategory.setOnItemSelectedListener(this);
        spinnerCharges.setOnItemSelectedListener(this);
        ArrayAdapter categoryAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, categoryArray);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(categoryAdapter);

        ArrayAdapter chargesdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, chargesArray);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCharges.setAdapter(chargesdapter);
    }

    private void initUI(View view) {

        editBizName = (EditText) view.findViewById(R.id.editBizName);
        editBizShortDescrip = (EditText) view.findViewById(R.id.editBizShortDescrip);
        editBizDetailDescrip = (EditText) view.findViewById(R.id.editBizDetailDescrip);
        editFoundYear = (EditText) view.findViewById(R.id.editFoundYear);
        editAwards = (EditText) view.findViewById(R.id.editAwards);
        editAddress = (EditText) view.findViewById(R.id.editAddress);
        editPhone1 = (EditText) view.findViewById(R.id.editPhone1);
        editPhone2 = (EditText) view.findViewById(R.id.editPhone2);
        editBizWebsite = (EditText) view.findViewById(R.id.editBizWebsite);
        editEmailId = (EditText) view.findViewById(R.id.editEmailId);
        editBizAmount = (EditText) view.findViewById(R.id.editBizAmount);
        btnAddBizns = (Button) view.findViewById(R.id.btnAddBizns);
        btnAddBizns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateFields();
            }
        });

    }


    @Override
    public void addBusiness(String msg) {
        LogUtils.showDialogSingleActionButton(mContext, mContext.getString(R.string.ok), msg, new DialogButtonClick() {
            @Override
            public void onOkClick() {
                getActivity().onBackPressed();
            }

            @Override
            public void onCancelClick() {
            }
        });
    }

    @Override
    public void onValidationError(String msg) {
        LogUtils.showErrorDialog(mContext, getString(R.string.ok), msg);
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.spinnerCategory:
                bizCategory = parent.getItemAtPosition(position).toString();
                //  LogUtils.showToast(mContext,bizCategory );
                break;
            case R.id.spinnerCharges:
                bizCharges = parent.getItemAtPosition(position).toString();
                // LogUtils.showToast(mContext,bizCharges );
                break;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void validateFields() {
        String bizName = editBizName.getText().toString().trim();
        String shortDesc = editBizShortDescrip.getText().toString().trim();
        String detailDesc = editBizDetailDescrip.getText().toString().trim();
        String foundYear = editFoundYear.getText().toString().trim();
        String awards = editAwards.getText().toString().trim();
        String address = editAddress.getText().toString().trim();
        String phone1 = editPhone1.getText().toString().trim();
        String phone2 = editPhone2.getText().toString().trim();
        String email = editEmailId.getText().toString().trim();
        String website = editBizWebsite.getText().toString().trim();
        String amount = editBizAmount.getText().toString().trim();
        getPresenter().validateFields(bizName, shortDesc, bizCategory, detailDesc, foundYear, awards, address, phone1, phone2, email, website, bizCharges, amount);

    }
}
