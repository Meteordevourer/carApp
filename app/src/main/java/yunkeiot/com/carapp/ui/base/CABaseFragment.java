package yunkeiot.com.carapp.ui.base;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;



import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import yunkeiot.com.carapp.R;
import yunkeiot.com.carapp.common.Logger;
import yunkeiot.com.carapp.entity.Data.BaseEntity;
import yunkeiot.com.carapp.http.engine.HttpRequestEngine;
import yunkeiot.com.carapp.http.engine.RequestCallback;


import static yunkeiot.com.carapp.http.engine.ErrorCode.ERROR_CODE_TOKEN_ERROR1;
import static yunkeiot.com.carapp.http.engine.ErrorCode.ERROR_CODE_TOKEN_ERROR2;

public class CABaseFragment extends Fragment {
    public final String TAG = getClass().toString();
    public View viewGroup;
    public CABaseActivity parent;

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private Map<String,Fragment> fragmentMap = new LinkedHashMap<>();
    private String preFragmentTag,currentTag;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState, int layoutId) {
        return viewGroup = inflater.inflate(layoutId, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view,savedInstanceState);
        parent = (CABaseActivity) getActivity();
        this.initFragment();
        this.setUpViews();
        this.setUpData();
    }

    private void initFragment(){
        this.fragmentManager = getChildFragmentManager();
        this.fragmentTransaction = this.fragmentManager.beginTransaction();
    }

    public void addFragment(int containerId,Fragment fragment,String tag){
        fragmentTransaction.add(containerId,fragment,tag);
        fragmentMap.put(tag,fragment);
        fragmentTransaction.hide(fragment);
    }

    public void setCurrentFragmentByTag(String tag){
        if (tag != null){
            preFragmentTag = currentTag;
            currentTag = tag;
            fragmentTransaction = fragmentManager.beginTransaction();
            Iterator<String> keys = fragmentMap.keySet().iterator();
            while (keys.hasNext()){
                String key = keys.next();
                if (!key.equals(tag)){
                    fragmentTransaction.hide(fragmentMap.get(key));
                }
            }
            fragmentTransaction.show(fragmentMap.get(tag)).commitAllowingStateLoss();
;        }
    }

    public void commit(){
        this.fragmentTransaction.commit();
    }

    public String getCurrentTag(){
        return this.currentTag;
    }

    public View findViewById(int resId){
        return viewGroup.findViewById(resId);
    }

    public void hideNavHeaderBack(){
        View view = findViewById(R.id.nav_back);
        if (view != null){
            view.setVisibility(View.GONE);
        }
    }

    /**
     * 设置标题
     *
     * @param  title
     */

    public void setNavTitle(String title){
        TextView tvTitle = (TextView) findViewById(R.id.nav_title);
        if (tvTitle != null){
            tvTitle.setText(title);
        }
    }

    public void setNavTitle(int resId){
        if(resId > 0){
            setNavTitle(getString(resId));
        }
    }

    public void setUpViews() {

    }

    public void setUpData() {

    }

    public void refreshData() {

    }


    /**
     * Post网络请求
     *
     * @param params
     * @param type
     * @param callback
     */
    public void postRequest(Map<String, String> params, final Type type,
                            final RequestCallback callback) {
        HttpRequestEngine.getInstance().postRequest(this.getActivity(), params, type, new RequestCallback() {
            @Override
            public void onRequestSuccess(BaseEntity result, String jsonData) {
                callback.onRequestSuccess(result, jsonData);
                if (result != null) {
                    checkIsError(Integer.parseInt(result.getCode()));
                }
            }

            @Override
            public void onRequestFailed(String msg) {
                callback.onRequestFailed(msg);
            }
        });
    }
    private void checkIsError(int state) {
        if (state == ERROR_CODE_TOKEN_ERROR1 || state == ERROR_CODE_TOKEN_ERROR2) {
//            EventBus.getDefault().post(new Object());
        }
    }

    public void setCommonEmptyViewVisible(boolean visible) {
        if (findViewById(R.id.commonEmptyView) != null) {
            findViewById(R.id.commonEmptyView).setVisibility(!visible ? View.VISIBLE : View.GONE);
        }
    }



}
