package com.zaf.econnecto.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zaf.econnecto.R;
import com.zaf.econnecto.ui.presenters.HelpNAboutPresenter;
import com.zaf.econnecto.ui.presenters.operations.IHelpAbout;


public class HelpNAboutFragment extends BaseFragment<HelpNAboutPresenter> implements IHelpAbout {

    private View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected HelpNAboutPresenter initPresenter() {
        return new HelpNAboutPresenter(getActivity(), this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_help_n_about, container, false);
        initUi();
        return view;
    }

    private void initUi() {
        TextView textWebsite = (TextView) view.findViewById(R.id.textWebsite);
        TextView text_fb_link = (TextView) view.findViewById(R.id.text_fb_link);
        TextView text_insta_link = (TextView) view.findViewById(R.id.text_insta_link);
        textWebsite.setMovementMethod(LinkMovementMethod.getInstance());
        text_fb_link.setMovementMethod(LinkMovementMethod.getInstance());
        text_insta_link.setMovementMethod(LinkMovementMethod.getInstance());
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }


    @Override
    public void onWebsiteClick() {

    }
}
