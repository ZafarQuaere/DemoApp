package com.zaf.econnecto.ui.presenters;

import android.content.Context;

import com.zaf.econnecto.R;
import com.zaf.econnecto.ui.presenters.operations.IBaseDataOperation;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Request;

/**
 * The base class of the presenter with data request. This presenter is holding a list of {@link
 * Request}. All the requests will be cancelled in {@link #onStop()}
 * <p/>
 * Created by lding on 26/04/2016.
 */
public class BaseDataPresenter extends BasePresenter {

    private final IBaseDataOperation mOperations;
    private final List<Request> mRequests;

    public BaseDataPresenter(Context context, IBaseDataOperation operation) {
        super(context);
        mRequests = new ArrayList<>();
        mOperations = operation;
    }

    protected <T extends Request> T start(T request) {
        return start(request, 0);
    }

    /**
     * Call this function to start the request. The request will be added into the request pool.
     *
     * @param request The request to start
     * @param description of the request
     * @param <T> the request type
     * @return the request object
     */
    protected <T extends Request> T start(T request, int description) {
        String desStr = description > 0 ? getContext().getString(description) : null;
        mOperations.onPreRequest(desStr);
        // avoid duplicate the same request in the requests pool
        mRequests.remove(request);
        // register session manager
       /* if (request instanceof SessionManagerHolder) {
            ((SessionManagerHolder) request).registerSessionManager(App.getSessionManager());
        }

        // insert GUID in request (username is saved as GUID)
        if (request instanceof GuidInjector) {
            ((GuidInjector) request).insertGuid(App.getStorage().getUsername());

        }*/
        mRequests.add(request);
        //request.start();


        return request;
    }

    @Override
    public void onStop() {
        super.onStop();
        mOperations.onRequestFinish();
        cancelRequests();
    }

    private void cancelRequests() {
        for (Request request : mRequests) {
           // request.cancel();
        }
        mRequests.clear();
    }

    protected String getNetworkErrorMessage() {
        //return getContext().getString(R.string.error_network_unavailable);
        return getContext().getString(R.string.error_network_unavailable);
    }

    protected String getNetworkOrServerErrorMsg(){
        return getContext().getString(R.string.error_network_unavailable);
    }
}
