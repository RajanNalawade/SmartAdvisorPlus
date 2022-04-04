package sbilife.com.pointofsale_bancaagency.branchlocator;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import sbilife.com.pointofsale_bancaagency.ParseXML;
import sbilife.com.pointofsale_bancaagency.R;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;

// Created by O0604 on 01/08/2017.

public class BranchLocatorMapFragmentActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    private MapView mapView;
    private GoogleMap mGoogleMap;
    private String retVal;
    private int size;
    private ParseXML objParse;
    private GPSTracker gpsTracker;
    private ArrayList<Marker> marker;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.branch_locator_map_layout);
        //this.getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,R.layout.layout_custom_title_need_analysis);

        //new CommonMethods().setApplicationToolbarMenu1( this, "Branch Locator");
        gpsTracker = new GPSTracker(this);
        Intent i = getIntent();
        retVal = i.getStringExtra("retVal");
        size = i.getIntExtra("size", 0);
        marker = new ArrayList<>();
        mapView = findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        objParse = new ParseXML();

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        mGoogleMap.clear();
        showMarkerCurrentLocation();
        showBankBranchWithMarker();
        mGoogleMap.setOnMarkerClickListener(this);
        mGoogleMap.setInfoWindowAdapter(new MyInfoWindowAdapter(this.getLayoutInflater()));
    }

    private void showMarkerCurrentLocation() {
        if (gpsTracker.canGetLocation()) {
            Double latitude = gpsTracker.getLatitude();
            Double longitude = gpsTracker.getLongitude();
            Log.e("Individual Location", ": " + latitude + "  " + longitude);
            LatLng latLng = new LatLng(latitude, longitude);
            mGoogleMap.addMarker(new MarkerOptions().position(latLng).draggable(false).title("You are here.")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        }
    }

    private void showBankBranchWithMarker() {
        double firstSBIInsuranceLatitude = 0.0d, firstSBIInsuranceLongitude = 0.0d;
        for (int j = 0; j < size; j++) {
            if (objParse.parseXmlTag(retVal, "Row" + j) != null) {
                if (j == 0) {
                    firstSBIInsuranceLatitude = Double.parseDouble(objParse.parseXmlTag(retVal, "Latitude" + j));
                    firstSBIInsuranceLongitude = Double.parseDouble(objParse.parseXmlTag(retVal, "Longitude" + j));
                }
//                latitude = Double.parseDouble(objParse.parseXmlTag(retVal,
//                        "Latitude" + j));
//                longitude = Double.parseDouble(objParse.parseXmlTag(retVal,
//                        "Longitude" + j));
//                addMarker(latitude,longitude,
//                        "SBI LIFE INSURANCE COMPANY LTD",
//                        objParse.parseXmlTag(retVal, "BR_ADD2" + j)
//                                + "\n"
//                                + objParse.parseXmlTag(retVal, "BR_ADD3"
//                                + j)
//                                + "\n"
//                                + objParse.parseXmlTag(retVal, "BR_PIN_CD"
//                                + j)
//                                + "\n Telephone : "
//                                + objParse
//                                .parseXmlTag(retVal, "TEL_NO" + j));
                addMarkerOnMap(Double.parseDouble(objParse.parseXmlTag(retVal, "Latitude" + j)), Double.parseDouble(objParse.parseXmlTag(retVal, "Longitude" + j)),
                        "SBI LIFE INSURANCE COMPANY LTD", objParse.parseXmlTag(retVal, "BR_ADD2" + j) + "\n" + objParse.parseXmlTag(retVal, "BR_ADD3"
                                + j) + objParse.parseXmlTag(retVal, "BR_PIN_CD"
                                + j) + "\n Telephone: " + objParse
                                .parseXmlTag(retVal, "TEL_NO" + j));
            }
        }
        //mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(firstSBIInsuranceLatitude,firstSBIInsuranceLongitude)));
        //mGoogleMap.moveCamera(CameraUpdateFactory.zoomTo(10.0f));

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(firstSBIInsuranceLatitude, firstSBIInsuranceLongitude)).
                        zoom(10).build();
        mGoogleMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(cameraPosition));
    }

    private void addMarkerOnMap(Double latitude, Double longitude, String Title, String Address) {
        marker.add(mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).
                anchor(0.5f, 0.5f).draggable(false).title(Title).snippet(Address)));
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Log.e("Marker Click", " On Click" + marker.getTitle() + ": " + marker.getSnippet());
        return false;
    }

    class MyInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
        private View myContentView;
        final LayoutInflater inflater;

        MyInfoWindowAdapter(LayoutInflater inflater) {
            this.inflater = inflater;
        }

        @Override
        public View getInfoWindow(Marker marker) {
            myContentView = inflater.inflate(R.layout.map_marker_info_layout, null);
            TextView branch_name_info, branch_vicinity_info;
            branch_name_info = myContentView.findViewById(R.id.branch_name_info);
            branch_vicinity_info = myContentView.findViewById(R.id.branch_vicinity_info);
            branch_name_info.setText(marker.getTitle());
            branch_vicinity_info.setText(marker.getSnippet());
            return myContentView;
        }

        @Override
        public View getInfoContents(Marker marker) {
            return null;
        }
    }


    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}
