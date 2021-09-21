package com.example.g_summerassignmnet.Testbooth_location;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.example.g_summerassignmnet.Common.APiInterface;
import com.example.g_summerassignmnet.Common.Util;
import com.example.g_summerassignmnet.R;
import com.example.g_summerassignmnet.my.JsonParser;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.g_summerassignmnet.R.layout.activity_map_main;

public class MapMainActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.OnConnectionFailedListener, RoutingListener
{

    private GoogleMap mMap;
    private BottomSheetDialog bottomSheetDialog;

    Location myLocation = null;
    protected LatLng start = null;
    protected LatLng end = null;
    boolean firsttime=true,api=false;

    //to get location permissions.
    private final static int LOCATION_REQUEST_CODE = 23;
    boolean locationPermission = false;

    //polyline object
    private List<Polyline> polylines = null;
    ArrayList<LocationModel> locationlist=new ArrayList<>();
    private APiInterface aPiInterface;
    private FloatingActionButton btndist;

    double lat=0,log=0;
    int index=0,passindex=0;

    ArrayList<String> dist=new ArrayList<>();
    ArrayList<String> source=new ArrayList<>();
    ArrayList<String> destination=new ArrayList<>();
    ArrayList<String> time=new ArrayList<>();
    ArrayList<Float> fl;
    LatLng ltlng;
    LinearLayout lin;
    private TextView txcurrent,txdest,txtime,txtkm;
    private ImageView imgclick;

    ProgressDialog pDialog;
    JSONArray array=null;
    JsonParser sjsp=new JsonParser();
    ArrayList<TestboothModel> testbootharray;
    String b_name="",b_address="",b_latitude="",b_longtitude="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_map_main);

        requestPermision();

        new fetch_location().execute();

        btndist=findViewById(R.id.btndist);
        bottomSheetDialog=new BottomSheetDialog(MapMainActivity.this);
        View view=getLayoutInflater().inflate(R.layout.info_root,null);
        lin=view.findViewById(R.id.layout);
        txcurrent=view.findViewById(R.id.txtcurrent);
        txdest=view.findViewById(R.id.txtdest);
        txtime=view.findViewById(R.id.txttime);
        txtkm=view.findViewById(R.id.txtkm);
        imgclick=view.findViewById(R.id.imgclick);
        imgclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {


            }
        });

        bottomSheetDialog.setContentView(view);

//        locationlist.add(new LocationModel(R.drawable.testoffice_adobespark,23.022228,72.662597));
//        locationlist.add(new LocationModel(R.drawable.testoffice_adobespark,23.028342,72.662986));
//        locationlist.add(new LocationModel(R.drawable.testoffice_adobespark,23.017930,72.666206));


            Retrofit retrofit = new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .baseUrl("https://maps.googleapis.com/")
                    .build();

            aPiInterface = retrofit.create(APiInterface.class);

        btndist.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                if(!dist.isEmpty())
                {

                    fl = new ArrayList<>();
                    for (int j = 0; j < dist.size(); j++)
                    {
                        String ss = dist.get(j);
                        String[] sp = ss.split(" ");
                        String s1 = sp[0];
                        Float f = Float.parseFloat(s1);
                        fl.add(f);
                    }
                    LatLng latlang2[] = new LatLng[locationlist.size()];
                    Float min = 0f;
                    Float mm = Collections.min(fl);
                    for (int k = 0; k < fl.size(); k++)
                    {
                        if (fl.get(k)==mm)
                        {
                            index = k;
                            Log.e("Index=",index+"");
                        }
                    }
                    Log.e("Float fl=",fl+"");
                    Findroutes(ltlng, latlang2[index] = new LatLng(locationlist.get(index).getLat(), locationlist.get(index).getLog()));
                    Toast.makeText(MapMainActivity.this, "=>"+fl.get(index)+"="+locationlist.get(index).getLat()+"=Index:"+index+"", Toast.LENGTH_SHORT).show();

                    txcurrent.setText(source.get(index) + "");
                    txdest.setText(destination.get(index) + "");
                    txtime.setText(time.get(index));
                    txtkm.setText(fl.get(index) + "km");
                    bottomSheetDialog.show();

                    if (!fl.isEmpty())
                    {
                        fl.clear();
                        index = 0;
                    }

                }
                else
                {
                    Toast.makeText(MapMainActivity.this, "please wait...", Toast.LENGTH_SHORT).show();
                }

            }

        });
        //init google map fragment to show map.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    private void requestPermision()
    {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    LOCATION_REQUEST_CODE);
        }
        else
            {
                locationPermission = true;
            }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode)
        {
            case LOCATION_REQUEST_CODE:
                {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    //if permission granted.
                    locationPermission = true;
                    getMyLocation();
                }
                else
                    {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    }
                return;
            }
        }
    }

    private void getMyLocation()
    {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        mMap.setMyLocationEnabled(true);

        mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener()
        {
            @Override
            public void onMyLocationChange(Location location)
            {
                myLocation=location;
                lat=location.getLatitude();
                log=location.getLongitude();
                 ltlng=new LatLng(location.getLatitude(),location.getLongitude());
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(
                        ltlng, 16f);
                MarkerOptions marko=new MarkerOptions().position(ltlng).title("me");
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(ltlng,14));

                if(firsttime)
                {

                    for (int i = 0; i < locationlist.size(); i++)
                    {

                        LatLng latlang1[] = new LatLng[locationlist.size()];
                        MarkerOptions markerOptions1 = new MarkerOptions().position(latlang1[i] = new LatLng(locationlist.get(i).getLat(), locationlist.get(i).getLog())).title("Testbooth").icon(BitmapDescriptorFactory.fromResource(R.drawable.booth90));
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(ltlng, 14));
                        mMap.addMarker(markerOptions1).showInfoWindow();
                        getDistance(String.valueOf(lat) + "," + String.valueOf(log), locationlist.get(i).getLat() + "," + locationlist.get(i).getLog());

                    }
                    firsttime=false;
                }
            }
        });

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng)
            {

//                end=latLng;
//
//                mMap.clear();
//
//                start=new LatLng(myLocation.getLatitude(),myLocation.getLongitude());
//                //start route finding
//                Findroutes(start,end);

            }
        });
    }
    // function to find Routes.
    public void Findroutes(LatLng Start, LatLng End)
    {

        if(Start==null || End==null)
        {
            Toast.makeText(MapMainActivity.this,"Unable to get location", Toast.LENGTH_LONG).show();
        }
        else
        {
            Routing routing = new Routing.Builder()
                    .travelMode(AbstractRouting.TravelMode.DRIVING)
                    .withListener(this)
                    .alternativeRoutes(true)
                    .waypoints(Start, End)
                    .key("AIzaSyCxCUtVr1jj4tBeTKTNsj9NF5AybZT9VQE")  //also define your api key here.
                    .build();
            routing.execute();
        }
    }

    @Override
    public void onRoutingFailure(RouteException e)
    {

        Log.e("EXeption=",e+"");
        View parentLayout = findViewById(android.R.id.content);
        Snackbar snackbar= Snackbar.make(parentLayout, e.toString(), Snackbar.LENGTH_LONG);
        snackbar.show();

    }

    @Override
    public void onRoutingStart()
    {
        Toast.makeText(MapMainActivity.this,"Finding Route...",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRoutingSuccess(ArrayList<Route> arrayList_root, int shortestRouteIndex)
    {
        CameraUpdate center = CameraUpdateFactory.newLatLng(start);
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(16);

        if(polylines!=null)
        {
            polylines.clear();
        }

        PolylineOptions polyOptions = new PolylineOptions();
        LatLng polylineStartLatLng=null;
        LatLng polylineEndLatLng=null;

        polylines = new ArrayList<>();
        //add route(s) to the map using polyline
        for (int i = 0; i <arrayList_root.size(); i++)
        {
            if(i==shortestRouteIndex)
            {

                polyOptions.color(getResources().getColor(R.color.teal_200));
                polyOptions.width(7);
                polyOptions.addAll(arrayList_root.get(shortestRouteIndex).getPoints());
                Polyline polyline = mMap.addPolyline(polyOptions);
                polylineStartLatLng=polyline.getPoints().get(0);
                int k=polyline.getPoints().size();
                polylineEndLatLng=polyline.getPoints().get(k-1);

            }
            else
                {

                }
        }

        //Add Marker on route starting position
        MarkerOptions startMarker = new MarkerOptions();
        startMarker.position(polylineStartLatLng);
        startMarker.title("My Location");
        mMap.addMarker(startMarker);
        //Add Marker on route ending position
        MarkerOptions endMarker = new MarkerOptions();
        endMarker.position(polylineEndLatLng);
        endMarker.icon(BitmapDescriptorFactory.fromResource(R.drawable.booth_park));
        endMarker.title("Destination");
        mMap.addMarker(endMarker);

    }

    @Override
    public void onRoutingCancelled() {
        Findroutes(start,end);
    }

    @Override
    public void onConnectionFailed(@NonNull @NotNull ConnectionResult connectionResult) {
        Findroutes(start,end);
    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        mMap = googleMap;

        if(locationPermission)
        {
            getMyLocation();
        }
    }


    //get distance

    private void getDistance(String origin,String dest)
    {
        aPiInterface.getDistance(getString(R.string.mapkey),origin,dest,"driving")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new SingleObserver<Result>()
        {
            @Override
            public void onSubscribe(@NotNull Disposable d)
            {

            }

            @Override
            public void onSuccess(@NotNull Result result)
            {

                Toast.makeText(MapMainActivity.this, "Result is successfull", Toast.LENGTH_SHORT).show();

                    dist.add(result.getRows().get(0).getElements().get(0).getDistance().getText());
                    source.add(result.getOrigin_addresses().get(0) + "");
                    destination.add(result.getDestination_address().get(0) + "");
                    time.add(result.getRows().get(0).getElements().get(0).getDuration().getText());
                    Log.e("Row:",result.getRows().get(0).toString());
                    Log.e("RS=",result.getOrigin_addresses().get(0)+"");
                    Log.e("RD=",result.getDestination_address().get(0)+"");


            }

            @Override
            public void onError(@NotNull Throwable e)
            {

            }

        });
    }

    class fetch_location extends AsyncTask<String,String,String>
    {
        protected void onPreExecute()
        {
            super.onPreExecute();
            pDialog=new ProgressDialog(MapMainActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
        @Override
        protected String doInBackground(String... strings) {
            testbootharray=new ArrayList<>();
            try
            {
                JSONObject obj=sjsp.getJSONFromUrl(Util.onlinetestbooth_Url);
                array=obj.getJSONArray(Testbooth_Tag.Tag_array);
                for(int i=0;i< array.length();i++)
                {
                    JSONObject o=array.getJSONObject(i);
                    b_name=o.getString(Testbooth_Tag.Tag_b_name);
                    b_address=o.getString(Testbooth_Tag.Tag_b_address);
                    b_latitude=o.getString(Testbooth_Tag.Tag_b_lat);
                    b_longtitude=o.getString(Testbooth_Tag.Tag_b_log);

                    testbootharray.add(new TestboothModel(b_name,b_address,b_latitude,b_longtitude));

                }
            }
            catch (Exception e)
            {
                Log.e("Exception is:",e+"");
            }

            return null;
        }
        protected void onPostExecute(String file_url)
        {
            pDialog.dismiss();
            if(testbootharray.isEmpty())
            {
                Toast.makeText(MapMainActivity.this, "something went wrong!", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Log.e("TestboothArray=",testbootharray+"");
                Toast.makeText(MapMainActivity.this, "result ok", Toast.LENGTH_SHORT).show();
                for(int i=0;i<testbootharray.size();i++)
                {
                    locationlist.add(new LocationModel(R.drawable.testoffice_adobespark,Double.parseDouble(testbootharray.get(i).getB_lat()),Double.parseDouble(testbootharray.get(i).getB_log())));
                }
                api=true;
            }

        }
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        dist.clear();
        source.clear();
        destination.clear();
        time.clear();
    }
}