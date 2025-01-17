package yunkeiot.com.carapp.ui.main.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONObject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import yunkeiot.com.carapp.R;
import yunkeiot.com.carapp.common.CAShareData;
import yunkeiot.com.carapp.common.CATools;
import yunkeiot.com.carapp.common.Logger;
import yunkeiot.com.carapp.entity.CAMyCarsEntity;
import yunkeiot.com.carapp.entity.CATravelNowDateEntity;
import yunkeiot.com.carapp.entity.Data.BaseEntity;
import yunkeiot.com.carapp.http.engine.CAHttpParams;
import yunkeiot.com.carapp.http.engine.RequestCallback;
import yunkeiot.com.carapp.ui.adapter.CAMainFunctionGridViewAdapter;
import yunkeiot.com.carapp.ui.base.CABaseFragment;
import yunkeiot.com.carapp.ui.funcMenu.CAAlarmActivity;
import yunkeiot.com.carapp.ui.funcMenu.CATrailsActivity;
import yunkeiot.com.carapp.ui.view.CAGridView;


public class CAHomeMainFragment extends CABaseFragment {
    private CAGridView gridView;
    private TextView tower,locate,waste,miles;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState, R.layout.fragment_home_main_layout);
    }

    @Override
    public void setUpViews(){
        super.setUpViews();
        gridView = (CAGridView)findViewById(R.id.main_functions_grid_view);
        gridView.setAdapter(new CAMainFunctionGridViewAdapter(this.parent));

        tower = (TextView)findViewById(R.id.main_tower_text);
        waste = (TextView)findViewById(R.id.main_waste_text);
        miles = (TextView)findViewById(R.id.main_miles_text);
        locate = (TextView)findViewById(R.id.main_locate_text);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0://排行榜
                        break;
                    case 1://车辆行程
                        parent.startActivity(CATrailsActivity.class);
                        break;
                    case 2://驾驶行为
                        break;
                    case 3://车辆告警
                        parent.startActivity(CAAlarmActivity.class);
                        break;
                    case 4://违章
                        break;
                    case 5://统计报表
                        break;

                }
            }
        });
    }

    @Override
    public void setUpData(){
        super.setUpData();
        getDefaultOne();
    }
    @Override
    public void refreshData() {
        super.refreshData();
        getDefaultOne();
    }
    private void getDefaultOne(){
        String defaultCar = CAShareData.getDefaultCar();
        if (!CATools.isEmpty(defaultCar)){
            postRequest(CAHttpParams.getTravelNowDate(defaultCar), CATravelNowDateEntity.class, new RequestCallback() {
                @Override
                public void onRequestSuccess(BaseEntity result, String jsonData) {
                    CATravelNowDateEntity travelNowDate = (CATravelNowDateEntity) result;
                }
                @Override
                public void onRequestFailed(String msg) {
                }
            });
        }
    }
}
