package  android.trwea.com.trwea;


import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.SearchView;
import android.text.TextUtils;
import android.trwea.com.trwea.db.Area;
import android.trwea.com.trwea.util.HttpUtil;
import android.trwea.com.trwea.util.Utility;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ChooseAreaFragment extends Fragment {

    private List<Area> CityList = new ArrayList<>();
    private SearchView SearchCityView;
    private ListView CityListView;
    private CityAdapter city_adapter;

    private ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Area test_a=new Area();
        CityList.add(test_a);
        View view = inflater.inflate(R.layout.activity_choose_area_fragment, container, false);
        SearchCityView = (SearchView) view.findViewById(R.id.searchView);
        CityListView = (ListView) view.findViewById(R.id.city_listView);
        city_adapter=new CityAdapter(getContext(),android.R.layout.simple_list_item_1,CityList);
        CityListView.setAdapter(city_adapter);
        CityListView.setTextFilterEnabled(true);
        SearchCityView.setIconifiedByDefault(false);//let the search box shown all the time
        SearchCityView.setSubmitButtonEnabled(true);

        SearchCityView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(TextUtils.isEmpty(query)){
                    Toast.makeText(getActivity(),"请输入城市名",Toast.LENGTH_SHORT).show();
                }
                else{
                    requestCity(query);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return view;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        CityListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String weatherId = CityList.get(position).getWeatherId();
                if (getActivity() instanceof MainActivity) {
                    Intent intent = new Intent(getActivity(), WeatherActivity.class);
                    intent.putExtra("weather_id", weatherId);
                    startActivity(intent);
                    getActivity().finish();
                } else if (getActivity() instanceof WeatherActivity) {
                    WeatherActivity activity = (WeatherActivity) getActivity();
                    activity.drawerLayout.closeDrawers();
                    activity.swipeRefresh.setRefreshing(true);
                    activity.requestWeather(weatherId);
                }
            }
        });
    }
    public void requestCity(final String CityName) {
        showProgressDialog();
        String weatherUrl = "https://api.heweather.com/v5/search?city=" + CityName + "&key=93de0d6004d345beaf5f5753bde4761a";
        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
//                e.printStackTrace();
                closeProgressDialog();
                System.out.println("获取城市信息失败");
//                Toast.makeText(getActivity(), "获取城市信息失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                final  List<Area> temp=Utility.handleCityResponse(responseText);
              // CityList = Utility.handleCityResponse(responseText);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(temp!=null && "ok".equals(temp.get(0).getStatus())){
                            CityList.clear();
                          for(int i=0;i<temp.size();i++){
                               CityList.add(temp.get(i));
                           }
                            city_adapter.notifyDataSetChanged();
                        }
                        else {}
                        closeProgressDialog();
                    }
                });

            }
        });

    }

    public class CityAdapter extends ArrayAdapter<Area> {

        public CityAdapter(Context context, int textViewResourceId,
                           List<Area> objects) {
            super(context, textViewResourceId, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Area city = getItem(position);
            View view = LayoutInflater.from(getContext()).inflate(R.layout.city_item, parent, false);
            TextView cityName = (TextView) view.findViewById(R.id.city_name);
            TextView cityProvince = (TextView) view.findViewById(R.id.city_province);
            cityName.setText(city.getAreaName());
            cityProvince.setText(city.getProvince());
            return view;

        }
    }
    private void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("正在加载...");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }
    private void closeProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }
}