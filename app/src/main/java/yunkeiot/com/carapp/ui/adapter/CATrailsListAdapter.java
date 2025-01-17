package yunkeiot.com.carapp.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.List;

import yunkeiot.com.carapp.R;
import yunkeiot.com.carapp.common.CATools;
import yunkeiot.com.carapp.entity.CATravelListEntity;

public class CATrailsListAdapter extends MyBaseAdapter {
    private Context mContext;
    private List<CATravelListEntity.DataBean> dataList;
    private String lastDate;

    public CATrailsListAdapter(Context context){
        super(context);
        mContext = context;
    }

    public void setData(List<CATravelListEntity.DataBean> dataList) {
        this.dataList = dataList;
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return this.dataList == null ? 0 : this.dataList.size();
    }

    @Override
    public CATravelListEntity.DataBean getItem(int i) {
        return this.dataList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return super.getItemId(i);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null){
            view = LayoutInflater.from(mContext).inflate(R.layout.trails_item_layout,viewGroup,false);
            viewHolder = new ViewHolder();
            viewHolder.tvMonth = (LinearLayout) view.findViewById(R.id.travel_month);
            viewHolder.tvStartTime = (TextView)view.findViewById(R.id.travel_start_time);
            viewHolder.startAddress = (TextView)view.findViewById(R.id.travel_sAddress);
            viewHolder.endAddress = (TextView)view.findViewById(R.id.travel_eAddress);
            viewHolder.mileage = (TextView)view.findViewById(R.id.travel_miles);
            viewHolder.avgSpeed = (TextView)view.findViewById(R.id.travel_avgSpeed);
            viewHolder.maxSpeed = (TextView)view.findViewById(R.id.travel_maxSpeed);
            viewHolder.driverTime = (TextView)view.findViewById(R.id.travel_time);
            view.setTag(viewHolder);
        }else {
            viewHolder =(ViewHolder) view.getTag();
        }
        CATravelListEntity.DataBean item = getItem(i);
        viewHolder.tvMonth.setVisibility(View.GONE);
        viewHolder.tvStartTime.setText(CATools.getLocalTimeStyleYMD(item.getTravelStartTime()));
        viewHolder.startAddress.setText(item.getStartGpsLocation().getAddressText());
        viewHolder.endAddress.setText(item.getEndGpsLocation().getAddressText());
        viewHolder.mileage.setText(item.getMileage()==0.0?"0km":String.format("%.2f",item.getMileage()) + "km");
        viewHolder.driverTime.setText(driverTimeFix(item.getDriverTime()));
        viewHolder.avgSpeed.setText(String.format("%.1f",item.getAvgSpeed()) + "km/h");
        viewHolder.maxSpeed.setText(String.format("%.1f",item.getMaxSpeed()) + "km/h");
        return view;
    }
    class ViewHolder {
        TextView tvStartTime;
        TextView startAddress;
        TextView endAddress;
        TextView mileage;
        TextView driverTime;
        TextView avgSpeed;
        TextView maxSpeed;
        LinearLayout tvMonth;
    }

    private String driverTimeFix(float time){
        int driverTime = (int)time;
        int min = 0,sec = 0,hour = 0;
        if(driverTime<60)
        {
            if(driverTime == 0)
                return "0秒";
            return driverTime + "秒";
        }
        else if(driverTime>=60&&driverTime<3600)
        {
            min =(driverTime-driverTime%60)/60;
            sec = driverTime%60;
            return min + "分" + sec + "秒";
        }
        else if(driverTime>=3600)
        {
            min=(driverTime-driverTime%60)/60;
            sec=driverTime%60;
            hour=(min - min%60)/60;
            min = min%60;
            return hour+"时"+min+"分";
        }
        else
            return "暂无";
    }

}
