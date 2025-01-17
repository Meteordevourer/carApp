package yunkeiot.com.carapp.ui.main;

import yunkeiot.com.carapp.R;
import yunkeiot.com.carapp.common.CAShareData;
import yunkeiot.com.carapp.common.CATools;
import yunkeiot.com.carapp.common.Logger;
import yunkeiot.com.carapp.entity.CAMyCarsEntity;
import yunkeiot.com.carapp.entity.Data.BaseEntity;
import yunkeiot.com.carapp.http.engine.CAHttpParams;
import yunkeiot.com.carapp.http.engine.RequestCallback;
import yunkeiot.com.carapp.ui.base.CABaseActivity;
import yunkeiot.com.carapp.ui.main.fragment.CAHomeFragment;
import yunkeiot.com.carapp.ui.main.fragment.CAHomeMainFragment;
import yunkeiot.com.carapp.ui.main.fragment.CALocationFragment;
import yunkeiot.com.carapp.ui.main.fragment.CAUserInfoFragment;

import android.Manifest;
import android.os.Bundle;
import android.widget.RadioGroup;
import com.google.gson.Gson;

public class CAMainActivity extends CABaseActivity {
    public static String TAG_HOME = "TAG_HOME", TAG_LOCATION = "TAG_LOCATION", TAG_USERINFO = "TAG_USERINFO";
    private CAHomeFragment homeFragment;
    private CAHomeMainFragment homeMainFragment;
    private CALocationFragment locationFragment;
    private CAUserInfoFragment userInfoFragment;
    private int vehiclePageNow = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    /**
     * onCreat: super.onCreat -> setContentView -> setStatusBarTranslucent -> initFragment -> setUpView -> setUpData
     */
    @Override
    public void setUpViews(){
        hideNavHeaderBack();
        //setNavMenuVisible(true,R.mipmap.main_message);
        checkAppPermission(Manifest.permission.ACCESS_FINE_LOCATION,1001);
        initFragments();
    }
    @Override
    public void setUpData(){

    }

    private void initFragments() {
        addFragment(R.id.container, homeFragment = new CAHomeFragment(), TAG_HOME);
        addFragment(R.id.container, locationFragment = new CALocationFragment(), TAG_LOCATION);
        addFragment(R.id.container,userInfoFragment = new CAUserInfoFragment(),TAG_USERINFO);
        commit();
        setCurrentFragmentByTag(TAG_HOME);
//        setNavTitle(R.string.nav_home_);

        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.mainButtonGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.button1:
                        setCurrentFragmentByTag(TAG_HOME);
                        setNavTitle(R.string.nav_home_);
                        break;
                    case R.id.button2:
                        setCurrentFragmentByTag(TAG_LOCATION);
                        setNavTitle(R.string.nav_location_);
                        break;
                    case R.id.button3:
                        setCurrentFragmentByTag(TAG_USERINFO);
                        setNavTitle(R.string.nav_userinfo_);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getMyCarsData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    public void getMyCarsData(){
        postRequest(CAHttpParams.getVehicleInfoListOfAll(vehiclePageNow), CAMyCarsEntity.class, new RequestCallback() {
            @Override
            public void onRequestSuccess(BaseEntity result, String jsonData) {
                CAMyCarsEntity myCarsEntity = (CAMyCarsEntity) result;
                if (myCarsEntity != null && myCarsEntity.getData()!=null){
                    CAShareData.saveUserAllVehicleInfoList(myCarsEntity.getData());
                    if (CATools.isEmpty(CAShareData.getDefaultCar())){
                        CAShareData.saveDefaultCar(myCarsEntity.getData().get(0).getPlateNumber());
                    }
                    if (homeFragment != null) {
                        homeFragment.refreshData();
                    }
                }
            }
            @Override
            public void onRequestFailed(String msg) {
                    showFailedDialog("账户车辆信息获取失败");
                }
        });
    }
}