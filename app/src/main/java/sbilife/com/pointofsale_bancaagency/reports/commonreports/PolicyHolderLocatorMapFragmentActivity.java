package sbilife.com.pointofsale_bancaagency.reports.commonreports;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.commons.io.IOUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import sbilife.com.pointofsale_bancaagency.ParseXML;
import sbilife.com.pointofsale_bancaagency.R;
import sbilife.com.pointofsale_bancaagency.ServiceURL;
import sbilife.com.pointofsale_bancaagency.adapters.PolicySummaryAdapter;
import sbilife.com.pointofsale_bancaagency.adapters.PolicySurrenderFlagAdapter;
import sbilife.com.pointofsale_bancaagency.adapters.UserPolicyListAdapter;
import sbilife.com.pointofsale_bancaagency.branchlocator.GPSTracker;
import sbilife.com.pointofsale_bancaagency.common.CommonMethods;
import sbilife.com.pointofsale_bancaagency.reports.RenewalCallingRemarksActivity;

//import com.google.maps.android.SphericalUtil;

public class PolicyHolderLocatorMapFragmentActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener, View.OnClickListener, UpdateAltMobileNoCommonAsyncTask.UpdateAltMobNoInterface {
    private MapView mapView;
    private GoogleMap mGoogleMap;
    private ArrayList<ParseXML.AgentPoliciesRenewalListMonthwiseGio> AgentPoliciesRenewalListMonthwiseGioList;
    private ArrayList<ParseXML.AgentPoliciesRenewalListMonthwiseGio> AgentPoliciesRenewalPremiumwiseFilteredList;
    private ArrayList<ParseXML.AgentPoliciesRenewalListMonthwiseGio> AgentPoliciesRenewalSummaryList;
    private ArrayList<ParseXML.BranchAddress> sameCityBranchList;
    private ArrayList<ParseXML.BranchAddress> sameCityBranchFilteredList;
    private ParseXML objParse;
    private GPSTracker gpsTracker;
    private ArrayList<Marker> marker;
    private CommonMethods mCommonMethods;
    private TextView mFilterByPremiunAmtBtn, mFilterByDistance, mFilterClear,mBranchLocatorBtn,mPremiumPoliciesBtn,mSummaryBtn,mSurrenderBtn
            ,mProbableCommissionBtn;
    private LinearLayout menu;
    private TextView menuText;
    private ImageView menuImage;
    private HashMap hashMap;
    private LatLng latLng;
    private final CharSequence[] values = {" Below 10000", " 10000 to 20000 ", " 20000 to 25000 ", " 25000 to 50000 ", "Above 50000", "All"};
    private final CharSequence[] distancevalues = {" Below 5KM", " 5KM to 10KM ", " 10KM to 20KM ", " 20KM to 50KM ", "Above 50KM", "All"};
    private final String TAG = PolicyHolderLocatorMapFragmentActivity.class.getSimpleName();
    //private ProgressDialog progressDialog;
    private Context mContext;
    private final String NAMESPACE = "http://tempuri.org/";
    private final String URL = ServiceURL.SERVICE_URL;

    private String strCIFBDMUserId;
    private String strCIFBDMEmailId;
    private String strCIFBDMMObileNo;

    private String btnClicked = null;
    private int finalPosition = 0;
    private String spnRewmonths_text;
    private ProgressDialog mProgressDialog;

    private FundValueAsyncTask fundValueAsyncTask;
    private GetPremiumAmountCommonAsync getPremiumAmountCommonAsync;
    private SendRenewalSMSAsynTask sendRenewalSMSAsynTask;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.branch_locator_map_layout);
        //this.getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.layout_policy_filter_options);
//        tv_sr_name  = findViewById(R.id.tv_sr_name);
//        tv_sr_name.setText("Branch Locator");
        gpsTracker = new GPSTracker(this);
        Intent i = getIntent();
        AgentPoliciesRenewalListMonthwiseGioList = (ArrayList<ParseXML.AgentPoliciesRenewalListMonthwiseGio>) i.getSerializableExtra("retVal");
        int size = i.getIntExtra("size", 0);
        spnRewmonths_text = i.getStringExtra("spinnerSelectedItem");

        marker = new ArrayList<>();
        mapView = findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        objParse = new ParseXML();
        mCommonMethods = new CommonMethods();
        mContext = this;


        mFilterByPremiunAmtBtn = findViewById(R.id.btn_filter_premium_amount);
        mFilterByDistance = findViewById(R.id.btn_filter_by_distance);
        mFilterClear = findViewById(R.id.btn_filter_clear);
        mFilterClear.setSelected(true);

        mBranchLocatorBtn = findViewById(R.id.btn_branch_locator);
        mPremiumPoliciesBtn = findViewById(R.id.btn_policies);
        mSummaryBtn = findViewById(R.id.btn_summary);
        mPremiumPoliciesBtn.setSelected(true);

        LinearLayout llFilterOptions = findViewById(R.id.ll_filter_options);
        menu = findViewById(R.id.btn_menu);
        menuImage = findViewById(R.id.txt_menu_img);
        menuText = findViewById(R.id.txt_menu);

        mSurrenderBtn = findViewById(R.id.btn_surrender);
        mSurrenderBtn.setVisibility(View.GONE);
        mProbableCommissionBtn = findViewById(R.id.btn_probable_commission);
        //mProbableCommissionBtn.setVisibility(View.GONE);


        mFilterByPremiunAmtBtn.setOnClickListener(this);
        mFilterByDistance.setOnClickListener(this);
        mFilterClear.setOnClickListener(this);
        mBranchLocatorBtn.setOnClickListener(this);
        mPremiumPoliciesBtn.setOnClickListener(this);
        mSummaryBtn.setOnClickListener(this);
        menu.setOnClickListener(this);
        mSurrenderBtn.setOnClickListener(this);
        mProbableCommissionBtn.setOnClickListener(this);

        getUserDetails();
        Intent intent = getIntent();
        String bdmCifCOde = intent.getStringExtra("strBDMCifCOde");
        if (bdmCifCOde != null) {
            strCIFBDMUserId = bdmCifCOde;
        }

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btn_filter_premium_amount:
                mFilterByPremiunAmtBtn.setSelected(true);
                mFilterByDistance.setSelected(false);
                mFilterClear.setSelected(false);
                //CreateAlertDialogWithRadioButtonGroup();
                if(btnClicked == null || btnClicked.equalsIgnoreCase("btn_policies")) {
                    CreateAlertDialogWithRadioButtonGroup(AgentPoliciesRenewalListMonthwiseGioList);
                }else if(btnClicked.equalsIgnoreCase("btn_summary") || btnClicked.equalsIgnoreCase("btn_surrender_summary")){
                    CreateAlertDialogWithRadioButtonGroup(AgentPoliciesRenewalSummaryList);
                }
                break;
            case R.id.btn_filter_by_distance:
                mFilterByDistance.setSelected(true);
                mFilterByPremiunAmtBtn.setSelected(false);
                mFilterClear.setSelected(false);
                if(btnClicked == null || btnClicked.equalsIgnoreCase("btn_policies")){
                    CreateAlertDialogWithRadioButtonGroupForDistanceFilter(AgentPoliciesRenewalListMonthwiseGioList);
                }else if(btnClicked.equalsIgnoreCase("btn_branch_locator")){
                    CreateAlertDialogWithRadioButtonGroupForBranchDistanceFilter();
                }else if(btnClicked.equalsIgnoreCase("btn_summary") || btnClicked.equalsIgnoreCase("btn_surrender_summary")){
                    CreateAlertDialogWithRadioButtonGroupForDistanceFilter(AgentPoliciesRenewalSummaryList);
                }

                break;
            case R.id.btn_filter_clear:
                mGoogleMap.clear();
                showMarkerCurrentLocation();
                mFilterClear.setSelected(true);
                mFilterByPremiunAmtBtn.setSelected(false);
                mFilterByDistance.setSelected(false);
                if(btnClicked == null || btnClicked.equalsIgnoreCase("btn_policies") || btnClicked.equalsIgnoreCase("btn_summary")){
                    showPoliciesWithMarker(AgentPoliciesRenewalListMonthwiseGioList);
                }else if(btnClicked.equalsIgnoreCase("btn_surrender_summary")){
                    showPoliciesWithMarkerAndSurrenderInfo(AgentPoliciesRenewalListMonthwiseGioList);
                }else if(btnClicked.equalsIgnoreCase("btn_branch_locator")){
                    showBankBranchWithMarker(sameCityBranchList);
                }

                break;
            case R.id.btn_branch_locator:
                mGoogleMap.clear();
                mPremiumPoliciesBtn.setSelected(false);
                mSummaryBtn.setSelected(false);
                mBranchLocatorBtn.setSelected(true);
                btnClicked = "btn_branch_locator";
                mSurrenderBtn.setSelected(false);
                mProbableCommissionBtn.setSelected(false);

                mFilterClear.setSelected(true);
                mFilterByDistance.setSelected(false);
                mFilterByPremiunAmtBtn.setSelected(false);

                //tv_sr_name.setVisibility(View.VISIBLE);
                mFilterByPremiunAmtBtn.setVisibility(View.GONE);
                getStateAndCity(latLng.latitude,latLng.longitude);
                //Toast.makeText(this,"Coming soon",Toast.LENGTH_LONG).show();
                break;
            case R.id.btn_policies:

                mGoogleMap.clear();
                mPremiumPoliciesBtn.setSelected(true);
                mBranchLocatorBtn.setSelected(false);
                mSummaryBtn.setSelected(false);
                mSurrenderBtn.setSelected(false);
                mProbableCommissionBtn.setSelected(false);
                //tv_sr_name.setVisibility(View.GONE);
                btnClicked = "btn_policies";
                mFilterClear.setSelected(true);
                mFilterByPremiunAmtBtn.setVisibility(View.VISIBLE);

                mFilterClear.setSelected(true);
                mFilterByDistance.setSelected(false);
                mFilterByPremiunAmtBtn.setSelected(false);

                showMarkerCurrentLocation();
                showPoliciesWithMarker(AgentPoliciesRenewalListMonthwiseGioList);
                break;
            case R.id.btn_summary:

                mGoogleMap.clear();
                mPremiumPoliciesBtn.setSelected(false);
                mBranchLocatorBtn.setSelected(false);
                mSummaryBtn.setSelected(true);
                mSurrenderBtn.setSelected(false);
                mProbableCommissionBtn.setSelected(false);
                btnClicked = "btn_summary";

                mFilterClear.setSelected(true);
                mFilterByDistance.setSelected(false);
                mFilterByPremiunAmtBtn.setSelected(false);

                showMarkerCurrentLocation();
                filterBasedOnPincode(AgentPoliciesRenewalListMonthwiseGioList);

                break;
            case R.id.btn_surrender:

                mGoogleMap.clear();
                mPremiumPoliciesBtn.setSelected(false);
                mBranchLocatorBtn.setSelected(false);
                mSummaryBtn.setSelected(false);
                mSurrenderBtn.setSelected(true);
                mProbableCommissionBtn.setSelected(false);

                mFilterClear.setSelected(true);
                mFilterByDistance.setSelected(false);
                mFilterByPremiunAmtBtn.setSelected(false);

                showMarkerCurrentLocation();
                btnClicked = "btn_surrender_summary";
                //showPoliciesWithMarkerAndSurrenderInfo(AgentPoliciesRenewalListMonthwiseGioList);
                filterBasedOnSurrenderFlag(AgentPoliciesRenewalListMonthwiseGioList);


                break;
            case R.id.btn_probable_commission:

                mGoogleMap.clear();
                mPremiumPoliciesBtn.setSelected(false);
                mBranchLocatorBtn.setSelected(false);
                mSummaryBtn.setSelected(false);
                mSurrenderBtn.setSelected(false);
                mProbableCommissionBtn.setSelected(true);

                mFilterClear.setSelected(true);
                mFilterByDistance.setSelected(false);
                mFilterByPremiunAmtBtn.setSelected(false);

                displayPoliciesAlert(AgentPoliciesRenewalListMonthwiseGioList);

                break;

            case R.id.btn_menu:
                if(!menu.isSelected()){
                    animateButtons(menu.isSelected());
                    menu.setSelected(true);
                    menuImage.setImageResource(R.drawable.ic_clear_black_24dp);
                    menuText.setText("Close");
                }else {
                    animateButtons(menu.isSelected());
                    menu.setSelected(false);
                    menuImage.setImageResource(R.drawable.ic_menu);
                    menuText.setText("Menu");
                }

                break;
        }

    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        mGoogleMap.clear();
        showMarkerCurrentLocation();
        showPoliciesWithMarker(AgentPoliciesRenewalListMonthwiseGioList);
        mGoogleMap.setOnMarkerClickListener(this);
        mGoogleMap.setInfoWindowAdapter(new MyInfoWindowAdapter(this.getLayoutInflater()));
        mGoogleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker markers) {

                //Toast.makeText(PolicyHolderLocatorMapFragmentActivity.this,,Toast.LENGTH_LONG).show();
            }
        });
    }

    private void showMarkerCurrentLocation() {
        if (gpsTracker.canGetLocation()) {
            Double latitude = gpsTracker.getLatitude();
            Double longitude = gpsTracker.getLongitude();
            Log.d(TAG, "showMarkerCurrentLocation: " + latitude + " == " + longitude);
            latLng = new LatLng(latitude, longitude);
            mGoogleMap.addMarker(new MarkerOptions().position(latLng).draggable(false).title("You are here.")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        }
    }

    private void showPoliciesWithMarker(ArrayList<ParseXML.AgentPoliciesRenewalListMonthwiseGio> AgentPoliciesRenewalList) {
        hashMap = new HashMap();
        double firstSBIInsuranceLatitude = 0.0d, firstSBIInsuranceLongitude = 0.0d;
        for (int j = 0; j < AgentPoliciesRenewalList.size(); j++) {
            if (j == 0) {
                firstSBIInsuranceLatitude = AgentPoliciesRenewalList.get(j).getPOLICYLATITUDE();
                firstSBIInsuranceLongitude = AgentPoliciesRenewalList.get(j).getPOLICYLONGITUDE();
            }

            addMarkerOnMap(AgentPoliciesRenewalList.get(j).getPOLICYLATITUDE(), AgentPoliciesRenewalList.get(j).getPOLICYLONGITUDE(),
                    AgentPoliciesRenewalList.get(j).getHOLDERPERSONFIRSTNAME() + " " + AgentPoliciesRenewalList.get(j).getHOLDERPERSONLASTNAME(),
                    AgentPoliciesRenewalList.get(j).getPERMANENTADDRESS1() + " " +
                            AgentPoliciesRenewalList.get(j).getPERMANENTADDRESS2() + "\n" +
                            AgentPoliciesRenewalList.get(j).getPERMANENTADDRESS3() + " - " +
                            AgentPoliciesRenewalList.get(j).getPERMANENTPOSTCODE() + "\n Mobile No: " +
                            AgentPoliciesRenewalList.get(j).getCONTACTMOBILE() + " \n Premium Amount: " +
                            AgentPoliciesRenewalList.get(j).getPREMIUMGROSSAMOUNT(), AgentPoliciesRenewalList.get(j));

            Log.d(TAG, "showBankBranchWithMarker: " + AgentPoliciesRenewalList.get(j).getPOLICYDISTANCE());

        }
        //mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(firstSBIInsuranceLatitude,firstSBIInsuranceLongitude)));
        //mGoogleMap.moveCamera(CameraUpdateFactory.zoomTo(10.0f));

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(firstSBIInsuranceLatitude, firstSBIInsuranceLongitude)).
                        zoom(4).build();
        mGoogleMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(cameraPosition));
    }

    private void addMarkerOnMap(Double latitude, Double longitude, String Title, String Address, ParseXML.AgentPoliciesRenewalListMonthwiseGio agentPoliciesRenewalListMonthwiseGio) {
        Drawable ragFlagMarker = null;
        if (agentPoliciesRenewalListMonthwiseGio.getRAG_FLAG().equalsIgnoreCase("RED") || agentPoliciesRenewalListMonthwiseGio.getRAG_FLAG().equalsIgnoreCase("LIGHT RED") || agentPoliciesRenewalListMonthwiseGio.getRAG_FLAG().equalsIgnoreCase("DARK RED")) {
            ragFlagMarker = getResources().getDrawable(R.drawable.icon_red);
        }
        else if (agentPoliciesRenewalListMonthwiseGio.getRAG_FLAG().equalsIgnoreCase("LIGHT RED")) {
            ragFlagMarker = getResources().getDrawable(R.drawable.icon_light_red);
        }

        else if (agentPoliciesRenewalListMonthwiseGio.getRAG_FLAG().equalsIgnoreCase("GREEN")) {
            ragFlagMarker = getResources().getDrawable(R.drawable.icon_green);
        }
        else if (agentPoliciesRenewalListMonthwiseGio.getRAG_FLAG().equalsIgnoreCase("AMBER")) {
            ragFlagMarker = getResources().getDrawable(R.drawable.icon_amber);
        }else {
            ragFlagMarker = getResources().getDrawable(R.drawable.icon_amber);
        }

        View customMarkerView = this.getLayoutInflater().inflate(R.layout.marker_layout, null, false);
        ImageView image = (ImageView) customMarkerView.findViewById(R.id.main_image);
        image.setImageDrawable(ragFlagMarker);
        TextView surrenderText = (TextView) customMarkerView.findViewById(R.id.surrender_text);
        surrenderText.setVisibility(View.GONE);

        customMarkerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        customMarkerView.layout(0, 0, customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight());
        customMarkerView.buildDrawingCache();
        Bitmap returnedBitmap = Bitmap.createBitmap(customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN);
        Drawable drawable = customMarkerView.getBackground();
        if (drawable != null)
            drawable.draw(canvas);
        customMarkerView.draw(canvas);

        //Bitmap bm = tv.getDrawingCache();


        Marker policymarker = mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).
                anchor(0.5f, 0.5f).draggable(false).title("Name : " + Title).snippet("Address : " + Address)
        .icon(BitmapDescriptorFactory.fromBitmap(returnedBitmap)));
        hashMap.put(policymarker, agentPoliciesRenewalListMonthwiseGio);
        marker.add(policymarker);
    }

    private void showPoliciesWithMarkerAndSurrenderInfo(ArrayList<ParseXML.AgentPoliciesRenewalListMonthwiseGio> AgentPoliciesRenewalList) {
        hashMap = new HashMap();
        double firstSBIInsuranceLatitude = 0.0d, firstSBIInsuranceLongitude = 0.0d;
        for (int j = 0; j < AgentPoliciesRenewalList.size(); j++) {
            if (j == 0) {
                firstSBIInsuranceLatitude = AgentPoliciesRenewalList.get(j).getPOLICYLATITUDE();
                firstSBIInsuranceLongitude = AgentPoliciesRenewalList.get(j).getPOLICYLONGITUDE();
            }

            addMarkerOnMapwithSurrenderInfo(AgentPoliciesRenewalList.get(j).getPOLICYLATITUDE(), AgentPoliciesRenewalList.get(j).getPOLICYLONGITUDE(),
                    AgentPoliciesRenewalList.get(j).getHOLDERPERSONFIRSTNAME() + " " + AgentPoliciesRenewalList.get(j).getHOLDERPERSONLASTNAME(),
                    AgentPoliciesRenewalList.get(j).getPERMANENTADDRESS1() + " " +
                            AgentPoliciesRenewalList.get(j).getPERMANENTADDRESS2() + "\n" +
                            AgentPoliciesRenewalList.get(j).getPERMANENTADDRESS3() + " - " +
                            AgentPoliciesRenewalList.get(j).getPERMANENTPOSTCODE() + "\n Mobile No: " +
                            AgentPoliciesRenewalList.get(j).getCONTACTMOBILE() + " \n Premium Amount: " +
                            AgentPoliciesRenewalList.get(j).getPREMIUMGROSSAMOUNT(), AgentPoliciesRenewalList.get(j));

            //Log.d(TAG, "showBankBranchWithMarker: " + AgentPoliciesRenewalList.get(j).getPOLICYDISTANCE());

        }
        //mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(firstSBIInsuranceLatitude,firstSBIInsuranceLongitude)));
        //mGoogleMap.moveCamera(CameraUpdateFactory.zoomTo(10.0f));

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(firstSBIInsuranceLatitude, firstSBIInsuranceLongitude)).
                        zoom(4).build();
        mGoogleMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(cameraPosition));
    }

    private void addMarkerOnMapwithSurrenderInfo(Double latitude, Double longitude, String Title, String Address, ParseXML.AgentPoliciesRenewalListMonthwiseGio agentPoliciesRenewalListMonthwiseGio) {


        Drawable  ragFlagMarker = null;
        if (agentPoliciesRenewalListMonthwiseGio.getRAG_FLAG().equalsIgnoreCase("RED") || agentPoliciesRenewalListMonthwiseGio.getRAG_FLAG().equalsIgnoreCase("LIGHT RED") || agentPoliciesRenewalListMonthwiseGio.getRAG_FLAG().equalsIgnoreCase("DARK RED")) {
            ragFlagMarker = getResources().getDrawable(R.drawable.icon_red);
        }
        else if (agentPoliciesRenewalListMonthwiseGio.getRAG_FLAG().equalsIgnoreCase("LIGHT RED")) {
            ragFlagMarker = getResources().getDrawable(R.drawable.icon_light_red);
        }

        else if (agentPoliciesRenewalListMonthwiseGio.getRAG_FLAG().equalsIgnoreCase("GREEN")) {
            ragFlagMarker = getResources().getDrawable(R.drawable.icon_green);
        }
        else if (agentPoliciesRenewalListMonthwiseGio.getRAG_FLAG().equalsIgnoreCase("AMBER")) {
            ragFlagMarker = getResources().getDrawable(R.drawable.icon_amber);
        }else {
            ragFlagMarker = getResources().getDrawable(R.drawable.icon_amber);
        }

        View customMarkerView = this.getLayoutInflater().inflate(R.layout.marker_layout, null, false);
        ImageView image = (ImageView) customMarkerView.findViewById(R.id.main_image);
        image.setImageDrawable(ragFlagMarker);
        TextView surrenderText = (TextView) customMarkerView.findViewById(R.id.surrender_text);
        if(!agentPoliciesRenewalListMonthwiseGio.getSURRENDER_FLAG().equalsIgnoreCase("")){
            surrenderText.setVisibility(View.VISIBLE);
            surrenderText.setBackgroundColor(Color.WHITE);
            surrenderText.setText(agentPoliciesRenewalListMonthwiseGio.getSURRENDER_FLAG());
        }
        customMarkerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        customMarkerView.layout(0, 0, customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight());
        customMarkerView.buildDrawingCache();
        Bitmap returnedBitmap = Bitmap.createBitmap(customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN);
        Drawable drawable = customMarkerView.getBackground();
        if (drawable != null)
            drawable.draw(canvas);
        customMarkerView.draw(canvas);



        Marker policymarker = mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).
                anchor(0.5f, 0.5f).draggable(false).title("Name : " + Title).snippet("Address : " + Address)
                .icon(BitmapDescriptorFactory.fromBitmap(returnedBitmap)));
        hashMap.put(policymarker, agentPoliciesRenewalListMonthwiseGio);
        marker.add(policymarker);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        marker.hideInfoWindow();
        Object object = hashMap.get(marker);
        if(object instanceof ParseXML.AgentPoliciesRenewalListMonthwiseGio){
            ParseXML.AgentPoliciesRenewalListMonthwiseGio agentPoliciesRenewalListMonthwiseGio = (ParseXML.AgentPoliciesRenewalListMonthwiseGio) hashMap.get(marker);
            if (agentPoliciesRenewalListMonthwiseGio != null) {
                showOptions(agentPoliciesRenewalListMonthwiseGio);
            }
        }else if(object instanceof ParseXML.BranchAddress){
            ParseXML.BranchAddress branchAddress = (ParseXML.BranchAddress) hashMap.get(marker);
            if (branchAddress != null) {
                showOptions(branchAddress);
            }
        }

        //Log.e("Marker Click", " On Click" + agentPoliciesRenewalListMonthwiseGio.getHOLDERPERSONLASTNAME() + ": " + agentPoliciesRenewalListMonthwiseGio.getHOLDERPERSONFIRSTNAME());
        if(object == null){
            return false;
        }else {
        return true;
    }
    }


    class MyInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
        final LayoutInflater inflater;

        MyInfoWindowAdapter(LayoutInflater inflater) {
            this.inflater = inflater;
        }

        @Override
        public View getInfoWindow(Marker marker) {
            View myContentView = inflater.inflate(R.layout.map_marker_info_layout, null);
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

    private void showOptions(final ParseXML.AgentPoliciesRenewalListMonthwiseGio agentPoliciesRenewalListMonthwiseGio) {

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_LIGHT);


        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_renewal_map, null);
        alertDialogBuilder.setView(dialogView);
        final AlertDialog alert = alertDialogBuilder.create();
        alert.show();
        TextView branch_name_info, branch_vicinity_info, customer_policy_no, textviewContactOffice,
                textviewPremiumAmount, tvActualPremium, buttonFundValue, buttonUpdatAltMobile;
        Button btnCall,btnSMS,btnDirection,btnCRM;

        branch_name_info = dialogView.findViewById(R.id.branch_name_info);
        branch_vicinity_info = dialogView.findViewById(R.id.branch_vicinity_info);
        customer_policy_no = dialogView.findViewById(R.id.tv_policy_number);
        textviewPremiumAmount = dialogView.findViewById(R.id.textviewPremiumAmount);
        textviewContactOffice = dialogView.findViewById(R.id.textviewContactOffice);
        tvActualPremium = dialogView.findViewById(R.id.tvActualPremium);
        buttonFundValue = dialogView.findViewById(R.id.buttonFundValue);
        buttonUpdatAltMobile = dialogView.findViewById(R.id.buttonUpdatAltMobile);

        LinearLayout LLofficeContact = dialogView.findViewById(R.id.LLofficeContact);
        LinearLayout llOfficeContactMaster = dialogView.findViewById(R.id.llOfficeContactMaster);

        btnCall = dialogView.findViewById(R.id.btn_policy_call);
        btnSMS = dialogView.findViewById(R.id.btn_policy_sms);
        btnDirection = dialogView.findViewById(R.id.btn_policy_direction);
        btnCRM = dialogView.findViewById(R.id.btn_policy_crm);


        branch_name_info.setText("Customer details");
        branch_name_info.setHeight(60);
        branch_name_info.setTextColor(Color.parseColor("#00a1e3"));
       // branch_name_info.setVisibility(View.GONE);
        customer_policy_no.setText("Policy Number : "+agentPoliciesRenewalListMonthwiseGio.getPOLICYNUMBER());
        branch_vicinity_info.setText("Name : "+agentPoliciesRenewalListMonthwiseGio.getHOLDERPERSONFIRSTNAME()
                + " " + agentPoliciesRenewalListMonthwiseGio.getHOLDERPERSONLASTNAME()
                // + "\nPremium Amount : " + agentPoliciesRenewalListMonthwiseGio.getPREMIUMGROSSAMOUNT()
                + "\nPayment Mechanism : " + agentPoliciesRenewalListMonthwiseGio.getPOLICYPAYMENTMECHANISM()
                + "\nResidual Amount : "+agentPoliciesRenewalListMonthwiseGio.getRESIDUAL_AMOUNT()
                + "\nStatus : "+agentPoliciesRenewalListMonthwiseGio.getPOLICYCURRENTSTATUS()
                + "\nDue Date : "+agentPoliciesRenewalListMonthwiseGio.getPREMIUMFUP()
                + "\nDOC : " + agentPoliciesRenewalListMonthwiseGio.getPOLICYRISKCOMMENCEMENTDATE()
                +"\nAddress : "+ agentPoliciesRenewalListMonthwiseGio.getPERMANENTADDRESS1()
                +" "+agentPoliciesRenewalListMonthwiseGio.getPERMANENTADDRESS2()
                + " "+agentPoliciesRenewalListMonthwiseGio.getPERMANENTADDRESS3()
                +" "+agentPoliciesRenewalListMonthwiseGio.getPERMANENTCITY()
                +" "+agentPoliciesRenewalListMonthwiseGio.getPERMANENTSTATE()
                +" "+agentPoliciesRenewalListMonthwiseGio.getPERMANENTPOSTCODE());

        textviewPremiumAmount.setText(agentPoliciesRenewalListMonthwiseGio.getPREMIUMGROSSAMOUNT());
        tvActualPremium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String selectedPolicyNumber = agentPoliciesRenewalListMonthwiseGio.getPOLICYNUMBER();
                if (agentPoliciesRenewalListMonthwiseGio.getPOLICYCURRENTSTATUS().equalsIgnoreCase("Lapse") ||
                        agentPoliciesRenewalListMonthwiseGio.getPOLICYCURRENTSTATUS().equalsIgnoreCase("Technical Lapse")) {
                    getPremiumAmountCommonAsync = new GetPremiumAmountCommonAsync(selectedPolicyNumber, mContext,
                            PolicyHolderLocatorMapFragmentActivity.this::getPremiumInterfaceMethod);
                    getPremiumAmountCommonAsync.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                } else {
                    String msg = "Gross Premium Amount is - " + agentPoliciesRenewalListMonthwiseGio.getPREMIUMGROSSAMOUNT();
                    mCommonMethods.showMessageDialog(mContext, msg);
                }
            }
        });

        if (TextUtils.isEmpty(agentPoliciesRenewalListMonthwiseGio.getCONTACTOFFICE())) {
            llOfficeContactMaster.setVisibility(View.GONE);
        } else {
            llOfficeContactMaster.setVisibility(View.VISIBLE);
            textviewContactOffice.setText(agentPoliciesRenewalListMonthwiseGio.getCONTACTOFFICE());
            LLofficeContact.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alert.dismiss();
                    // makeCall();
                    String mobileNumber = agentPoliciesRenewalListMonthwiseGio.getCONTACTOFFICE();
                    if (!TextUtils.isEmpty(mobileNumber)) {
                        mCommonMethods.callMobileNumber(mobileNumber, mContext);
                    }

                }
            });

            textviewContactOffice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alert.dismiss();
                    // makeCall();
                    String mobileNumber = agentPoliciesRenewalListMonthwiseGio.getCONTACTOFFICE();
                    if (!TextUtils.isEmpty(mobileNumber)) {
                        mCommonMethods.callMobileNumber(mobileNumber, mContext);
                    }

                }
            });

        }


        buttonFundValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String holderId = agentPoliciesRenewalListMonthwiseGio.getHOLDERID();
                String policyNumber = agentPoliciesRenewalListMonthwiseGio.getPOLICYNUMBER();
                fundValueAsyncTask = new FundValueAsyncTask(mContext, holderId, policyNumber,
                        PolicyHolderLocatorMapFragmentActivity.this::getFundValueInterfaceMethod);
                fundValueAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        });

        buttonUpdatAltMobile.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                mCommonMethods.hideKeyboard(buttonUpdatAltMobile, mContext);
                final String premiumFUP = agentPoliciesRenewalListMonthwiseGio.getPREMIUMFUP();
                final String policyNumber = agentPoliciesRenewalListMonthwiseGio.getPOLICYNUMBER();
                mCommonMethods.updateAltMobileAlert(mContext, premiumFUP, policyNumber,
                        PolicyHolderLocatorMapFragmentActivity.this);

            }
        });


        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert.dismiss();
                // makeCall();
                String mobileNumber = agentPoliciesRenewalListMonthwiseGio.getCONTACTMOBILE();
                if (!TextUtils.isEmpty(mobileNumber)) {
                    mCommonMethods.callMobileNumber(mobileNumber, mContext);
                }

            }
        });

        if (agentPoliciesRenewalListMonthwiseGio.getPOLICYCURRENTSTATUS().equalsIgnoreCase("Lapse")) {
            btnSMS.setVisibility(View.GONE);
        }


        btnSMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert.dismiss();


                //commonMethods.hideKeyboard(, context);
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.MyAlertDialogStyle);
                builder.setTitle("Choose Communication Medium");
                finalPosition = 0;
                //final String[] languagesArray = {"English", "Hindi", "Telugu"};
//                    final String[] languagesArray = {"English"};
                //final String[] commMediumArray = {"SMS", "Email"};
                final String[] commMediumArray = {"SMS"};
                // cow
                builder.setSingleChoiceItems(commMediumArray, finalPosition, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finalPosition = which;
                    }
                });

                builder.setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String commMedium = commMediumArray[finalPosition];
                        if (mCommonMethods.isNetworkConnected(mContext)) {
                            if (commMedium.equalsIgnoreCase("SMS")) {

                                final String policyNumber = agentPoliciesRenewalListMonthwiseGio.getPOLICYNUMBER();
                                final String mobileNumber = agentPoliciesRenewalListMonthwiseGio.getCONTACTMOBILE();
                                final String dueDate = agentPoliciesRenewalListMonthwiseGio.getPREMIUMFUP();
                                final String status = agentPoliciesRenewalListMonthwiseGio.getPOLICYCURRENTSTATUS();
                                final String amount = agentPoliciesRenewalListMonthwiseGio.getPREMIUMGROSSAMOUNT();


                                sendRenewalSMSAsynTask = new SendRenewalSMSAsynTask(policyNumber, mobileNumber, dueDate,
                                        status, amount, "English",
                                        mContext, PolicyHolderLocatorMapFragmentActivity.this::getSMSDetailsInterfaceMethod);
                                sendRenewalSMSAsynTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                            }


                        } else {
                            mCommonMethods.showMessageDialog(mContext, mCommonMethods.NO_INTERNET_MESSAGE);
                        }
                    }
                });
                builder.setNegativeButton("Cancel", null);

                AlertDialog dialog = builder.create();
                dialog.setCancelable(false);
                dialog.show();


                //showSMSAlert(agentPoliciesRenewalListMonthwiseGio);
            }
        });

        btnDirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert.dismiss();
                goToGoogleMap(agentPoliciesRenewalListMonthwiseGio.getPOLICYLATITUDE(),agentPoliciesRenewalListMonthwiseGio.getPOLICYLONGITUDE());
            }
        });

        btnCRM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert.dismiss();
                showDispositionAlert(agentPoliciesRenewalListMonthwiseGio);
            }
        });



    }

    private void showDispositionAlert(final ParseXML.AgentPoliciesRenewalListMonthwiseGio agentPoliciesRenewalListMonthwiseGio){
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_LIGHT);


        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dispositon_alert_dialog, null);
        alertDialogBuilder.setView(dialogView);
        final AlertDialog alert = alertDialogBuilder.create();
        alert.show();

        final TextView updateRemarkBtn,viewRemarkBtn;
        final LinearLayout updateRemarkLayout,viewRemarkLayout;
        final Button btn_submit_disposition_remark,sbiLifeRemarkBtn,callCenterRemarkBtn,salesRemarkBtn;

        final Spinner dispotionSpinner = dialogView.findViewById(R.id.spinner_disposition);
        final Spinner subdispotionSpinner = dialogView.findViewById(R.id.spinner_subdisposition);
        final EditText dispositionRemarkText = dialogView.findViewById(R.id.edt_disposition_remark);
        btn_submit_disposition_remark = dialogView.findViewById(R.id.btn_submit_disposition_remark);
        sbiLifeRemarkBtn = dialogView.findViewById(R.id.btn_sbi_life_remark);
        callCenterRemarkBtn = dialogView.findViewById(R.id.btn_call_center_remark);
        salesRemarkBtn = dialogView.findViewById(R.id.btn_sales_remark);

        updateRemarkBtn = dialogView.findViewById(R.id.btn_update_remark);
        updateRemarkBtn.setSelected(true);
        viewRemarkBtn = dialogView.findViewById(R.id.btn_view_remark);
        viewRemarkBtn.setSelected(false);
        updateRemarkLayout = dialogView.findViewById(R.id.ll_update_remark_parent);
        viewRemarkLayout = dialogView.findViewById(R.id.ll_view_remark_parent);

        final ArrayAdapter<CharSequence> langAdapter = new ArrayAdapter<CharSequence>(mContext, R.layout.spinner_text,getResources().getStringArray(R.array.disposition_array));
        langAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown);
        dispotionSpinner.setAdapter(langAdapter);

        dispotionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String[] subDispositionArray = null;
                switch (i){
                    case 0:
                        dispositionRemarkText.setVisibility(View.GONE);
                        btn_submit_disposition_remark.setVisibility(View.GONE);
                        subDispositionArray = new String[]{""};
                        break;
                    case 1:
                        subDispositionArray = new String[]{"Select Sub Disposition","Callback_Call To Speak Later on for Retention","Left Message",
                                "Received by Customer Representative. Asked for call back","Out Of Station"};
                        break;
                    case 2:
                        subDispositionArray = new String[]{"Select Sub Disposition","Already Paid To Branch","Drop Box","Online","MP Online",
                                "Paid To Advisor/ CIF before 15 days","Paid To Advisor/ CIF within 15 to 30 days"};
                        break;
                    case 3:
                        subDispositionArray = new String[]{"Select Sub Disposition","Other Language / language barier"};
                        break;
                    case 4:
                        subDispositionArray = new String[]{"Select Sub Disposition","Advisor Owned Policy","Customer Expired",
                                "Do Not Disturb","Employee Owned Policy","Applied for Surrender the policy"};
                        break;
                    case 5:
                        subDispositionArray = new String[]{"Select Sub Disposition","Intrested but financial problem","Not Intested due to financial problem"};
                        break;
                    case 6:
                        subDispositionArray = new String[]{"Select Sub Disposition","Address Changes","Earlier complaint raised_No solution received",
                                "Mode Change","Other","Policy Document Not Recd","Fund value / bonus statement"};
                        break;
                    case 7:
                        subDispositionArray = new String[]{"Select Sub Disposition","Requesting for Pickup","Will pay in Grace period",
                                "Promise To Pay","Will Pay Later","Interested to pay online"};
                        break;
                    case 8:
                        subDispositionArray = new String[]{"Select Sub Disposition","features & Benefits notexplained","Misselling false promises made",
                                "Misselling force selling","Misselling high allocation charges","Misselling wrong product sold",
                                "Sold As Single Premium or LPPT","Policy Against KCC","Poilcy Against Loan"};
                        break;
                    case 9:
                        subDispositionArray = new String[]{"Select Sub Disposition","Beep Tone","DIALER_SIT_TONE","Number Does Not Exist",
                                "Customers Has Relocated (Abroad)","Out Of Service"};
                        break;
                    case 10:
                        subDispositionArray = new String[]{"Select Sub Disposition","Call Disconnected","Engage","Not Reachable","Ringing",
                                "Switched Off","Customer Busy"};
                        break;
                    case 11:
                        subDispositionArray = new String[]{"Select Sub Disposition","Completed  Locking Period","Customer Has Bought Competitor Policy",
                                "Low Return","Not Ready For Stating Reason","Refuse To Pay","Taken Another Policy With Sbi Life",
                                "Renewal premium reminder Notice not received","Not satisfied with advisor services","Overall services of the company"};
                        break;
                    case 12:
                        subDispositionArray = new String[]{"Select Sub Disposition","IA Number","Not Related Patry"};
                        break;
                    case 13:
                        subDispositionArray = new String[]{"Select Sub Disposition","Call Back","Already Paid","Language barier","Do Not Call",
                                "Financial Problem","Customer query / issue","Interested","Misselling","Permanent Non Contactable",
                                "Temporary Non Contactable","Not Interested","Wrong number"};
                        break;
                    case 14:
                        subDispositionArray = new String[]{"Select Sub Disposition","Contact Number Sourced","Forward for structured follow up"};
                        break;
                    case 15:
                        subDispositionArray = new String[]{"Select Sub Disposition","All Efforts Done"};
                        break;
                    case 16:
                        subDispositionArray = new String[]{"Select Sub Disposition","New"};
                        break;

                }

                ArrayAdapter<CharSequence> langAdapter = new ArrayAdapter<CharSequence>(mContext, R.layout.spinner_text,subDispositionArray);
                langAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown);
                subdispotionSpinner.setAdapter(langAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        subdispotionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String subDispositionString = subdispotionSpinner.getSelectedItem().toString();
                if(!(subDispositionString.equalsIgnoreCase("")) &&
                        !(subDispositionString.equalsIgnoreCase("Select Sub Disposition"))){
                    dispositionRemarkText.setVisibility(View.VISIBLE);
                    btn_submit_disposition_remark.setVisibility(View.VISIBLE);
                }else {
                    dispositionRemarkText.setVisibility(View.GONE);
                    btn_submit_disposition_remark.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                dispositionRemarkText.setVisibility(View.GONE);
                btn_submit_disposition_remark.setVisibility(View.GONE);
            }
        });

        updateRemarkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateRemarkBtn.setSelected(true);
                viewRemarkBtn.setSelected(false);

                viewRemarkLayout.setVisibility(View.GONE);
                updateRemarkLayout.setVisibility(View.VISIBLE);
            }
        });
        viewRemarkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewRemarkBtn.setSelected(true);
                updateRemarkBtn.setSelected(false);
                mCommonMethods.hideKeyboard(view,mContext);
                updateRemarkLayout.setVisibility(View.GONE);
                viewRemarkLayout.setVisibility(View.VISIBLE);
                dispositionRemarkText.setText("");
                dispotionSpinner.setSelection(0);
            }
        });
        callCenterRemarkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert.dismiss();
                String objectType = agentPoliciesRenewalListMonthwiseGio.getPOLICYCURRENTSTATUS();
                String DueDate = agentPoliciesRenewalListMonthwiseGio.getPREMIUMFUP();//lstAdapterList.get(index).getPremiumUp();
                String PAY_EX1_74 = agentPoliciesRenewalListMonthwiseGio.getPOLICYNUMBER();//lstAdapterList.get(index).getNo();
                String StatusCodeID = agentPoliciesRenewalListMonthwiseGio.getHOLDERID();//lstAdapterList.get(index).getHolderId();

                Intent intent = new Intent(PolicyHolderLocatorMapFragmentActivity.this, RenewalCallingRemarksActivity.class);
                intent.putExtra("objectType", objectType);
                intent.putExtra("DueDate", DueDate);
                intent.putExtra("PAY_EX1_74", PAY_EX1_74);
                intent.putExtra("StatusCodeID", StatusCodeID);
                startActivity(intent);
            }
        });
        sbiLifeRemarkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //alert.dismiss();
                GetRemarksAsyncTask getRemarksAsyncTask = new GetRemarksAsyncTask(agentPoliciesRenewalListMonthwiseGio.getPOLICYNUMBER());
                getRemarksAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        });
        salesRemarkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //alert.dismiss();
                GetRemarksAsyncTask getRemarksAsyncTask = new GetRemarksAsyncTask(agentPoliciesRenewalListMonthwiseGio.getPOLICYNUMBER());
                getRemarksAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        });
        btn_submit_disposition_remark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String disposition = dispotionSpinner.getSelectedItem().toString();
                String subdisposition = subdispotionSpinner.getSelectedItem().toString();
                String dispositionRemark = dispositionRemarkText.getText().toString();

                if(disposition.equalsIgnoreCase("Select Disposition")){
                    Toast.makeText(mContext,"Please select disposition option",Toast.LENGTH_LONG).show();
                }else if(subdisposition.equalsIgnoreCase("Select Sub Disposition")){
                    Toast.makeText(mContext,"Please select sub disposition option",Toast.LENGTH_LONG).show();
                }else if(dispositionRemark == null || dispositionRemark.equalsIgnoreCase("")){
                    Toast.makeText(mContext,"Please enter disposition remark",Toast.LENGTH_LONG).show();
                }else {
                    //saveCallingRemarks_smrt
                    alert.dismiss();
                    SubmitDispositionRemarkAsync submitDispositionRemarkAsync = new SubmitDispositionRemarkAsync(agentPoliciesRenewalListMonthwiseGio.getPOLICYNUMBER(),disposition,subdisposition,dispositionRemark);
                    submitDispositionRemarkAsync.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                }
            }
        });

    }


    private void showOptions(final ParseXML.BranchAddress branchAddress) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_LIGHT);
        alertDialogBuilder
                //.setMessage(Html.fromHtml("<font color='#00a1e3'>Contact details</font>" +"<br>"))
                .setCancelable(true)
                .setPositiveButton("Direction",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                                goToGoogleMap(branchAddress.getBRANCHLATITUDE(),branchAddress.getBRANCHLONGITUDE());

                            }
                        });

        alertDialogBuilder.setNegativeButton("Call",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        //makeCall(branchAddress.getTEL_NO());
                        String mobileNumber = branchAddress.getTEL_NO();
                        if (!TextUtils.isEmpty(mobileNumber)) {
                            mCommonMethods.callMobileNumber(mobileNumber, mContext);
                        }
                    }
                });
//        alertDialogBuilder.setNeutralButton("SMS",
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        dialog.dismiss();
//
//                        sendSMS(agentPoliciesRenewalListMonthwiseGio.getCONTACTMOBILE());
//                    }
//                });

        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_renewal_map, null);
        alertDialogBuilder.setView(dialogView);

        TextView branch_name_info, branch_vicinity_info,customer_policy_no;
        Button btnCall,btnSMS,btnDirection,btnCRM;

        branch_name_info = dialogView.findViewById(R.id.branch_name_info);
        branch_vicinity_info = dialogView.findViewById(R.id.branch_vicinity_info);
        customer_policy_no = dialogView.findViewById(R.id.tv_policy_number);

        btnCall = dialogView.findViewById(R.id.btn_policy_call);
        btnCall.setVisibility(View.GONE);
        btnSMS = dialogView.findViewById(R.id.btn_policy_sms);
        btnSMS.setVisibility(View.GONE);
        btnDirection = dialogView.findViewById(R.id.btn_policy_direction);
        btnDirection.setVisibility(View.GONE);
        btnCRM = dialogView.findViewById(R.id.btn_policy_crm);
        btnCRM.setVisibility(View.GONE);

        branch_name_info.setText("Branch details");
        branch_name_info.setHeight(50);
        branch_name_info.setTextColor(Color.parseColor("#00a1e3"));
       // branch_name_info.setVisibility(View.GONE);
        customer_policy_no.setText("SBI LIFE INSURANCE COMPANY LTD");
        branch_vicinity_info.setText("Address : "+ branchAddress.getBR_ADD1()+", "
                +" "+branchAddress.getBR_ADD2()
                + " "+branchAddress.getBR_ADD3()
                +" - "+branchAddress.getBR_PIN_CD()
                +" \n\nTel No : "+branchAddress.getTEL_NO());

        AlertDialog alert = alertDialogBuilder.create();
        alert.show();

    }

    private void goToGoogleMap(double latitude,double longitude) {
        Uri gmmIntentUri = Uri.parse("google.navigation:q=" + latitude + "," + longitude);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }

    /*private void sendSMS(final String mobileNo) {
        Uri uri = Uri.parse("smsto:" + mobileNo);
        Intent i = new Intent(Intent.ACTION_SENDTO, uri);
        i.putExtra("sms_body", "");
        i.setPackage("com.android.mms");
        startActivity(i);
    }*/

    private void makeCall(final String mobileNo) {
        try {
            Intent intent = new Intent(Intent.ACTION_DIAL,
                    Uri.parse("tel:" + mobileNo));

					/*Intent intent = new Intent(Intent.ACTION_CALL,
							Uri.parse("tel:" + mobileNumber));*/
            PolicyHolderLocatorMapFragmentActivity.this.startActivity(intent);
        } catch (SecurityException e) {
            mCommonMethods.showToast(PolicyHolderLocatorMapFragmentActivity.this, "There might be issue in Permission");
        } catch (Exception e) {
            mCommonMethods.showMessageDialog(PolicyHolderLocatorMapFragmentActivity.this, "Problem in calling please Try Again");
        }
    }

    private void CreateAlertDialogWithRadioButtonGroup(final ArrayList<ParseXML.AgentPoliciesRenewalListMonthwiseGio> AgentPoliciesRenewalListMonthwiseGioList) {

        AlertDialog alertDialog1;

        AlertDialog.Builder builder = new AlertDialog.Builder(PolicyHolderLocatorMapFragmentActivity.this,android.R.style.Theme_Dialog);

        builder.setTitle("Select Your Choice");

        builder.setSingleChoiceItems(values, -1, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int item) {

                mGoogleMap.clear();
                showMarkerCurrentLocation();

                switch (item) {
                    case 0:
                        AgentPoliciesRenewalPremiumwiseFilteredList = new ArrayList<>();
                        for (int i = 0; i < AgentPoliciesRenewalListMonthwiseGioList.size(); i++) {
                            if (AgentPoliciesRenewalListMonthwiseGioList.get(i).getPREMIUMGROSSAMOUNT() != null) {
                                double premiumAmt = Double.parseDouble(AgentPoliciesRenewalListMonthwiseGioList.get(i).getPREMIUMGROSSAMOUNT());
                                if (premiumAmt <= 10000) {
                                    AgentPoliciesRenewalPremiumwiseFilteredList.add(AgentPoliciesRenewalListMonthwiseGioList.get(i));
                                }
                            }
                        }
                        if (AgentPoliciesRenewalPremiumwiseFilteredList.size() != 0) {
                            //showPoliciesWithMarker(AgentPoliciesRenewalPremiumwiseFilteredList);
                            if(btnClicked != null && btnClicked.equalsIgnoreCase("btn_surrender_summary")){
                                showPoliciesWithMarkerAndSurrenderInfo(AgentPoliciesRenewalPremiumwiseFilteredList);
                            }else  {
                            showPoliciesWithMarker(AgentPoliciesRenewalPremiumwiseFilteredList);
                            }
                        } else {
                            Toast.makeText(PolicyHolderLocatorMapFragmentActivity.this, "No records found", Toast.LENGTH_LONG).show();
                        }
                        //Toast.makeText(PolicyHolderLocatorMapFragmentActivity.this, "First Item Clicked", Toast.LENGTH_LONG).show();
                        break;
                    case 1:
                        AgentPoliciesRenewalPremiumwiseFilteredList = new ArrayList<>();
                        for (int i = 0; i < AgentPoliciesRenewalListMonthwiseGioList.size(); i++) {
                            if (AgentPoliciesRenewalListMonthwiseGioList.get(i).getPREMIUMGROSSAMOUNT() != null) {
                                double premiumAmt = Double.parseDouble(AgentPoliciesRenewalListMonthwiseGioList.get(i).getPREMIUMGROSSAMOUNT());
                                if (premiumAmt > 10000 && premiumAmt <= 20000) {
                                    AgentPoliciesRenewalPremiumwiseFilteredList.add(AgentPoliciesRenewalListMonthwiseGioList.get(i));
                                }
                            }
                        }
                        if (AgentPoliciesRenewalPremiumwiseFilteredList.size() != 0) {
                            //showPoliciesWithMarker(AgentPoliciesRenewalPremiumwiseFilteredList);
                            if(btnClicked != null && btnClicked.equalsIgnoreCase("btn_surrender_summary")){
                                showPoliciesWithMarkerAndSurrenderInfo(AgentPoliciesRenewalPremiumwiseFilteredList);
                            }else  {
                            showPoliciesWithMarker(AgentPoliciesRenewalPremiumwiseFilteredList);
                            }
                        } else {
                            Toast.makeText(PolicyHolderLocatorMapFragmentActivity.this, "No records found", Toast.LENGTH_LONG).show();
                        }
                        //Toast.makeText(PolicyHolderLocatorMapFragmentActivity.this, "Second Item Clicked", Toast.LENGTH_LONG).show();
                        break;
                    case 2:
                        AgentPoliciesRenewalPremiumwiseFilteredList = new ArrayList<>();
                        for (int i = 0; i < AgentPoliciesRenewalListMonthwiseGioList.size(); i++) {
                            if (AgentPoliciesRenewalListMonthwiseGioList.get(i).getPREMIUMGROSSAMOUNT() != null) {
                                double premiumAmt = Double.parseDouble(AgentPoliciesRenewalListMonthwiseGioList.get(i).getPREMIUMGROSSAMOUNT());
                                if (premiumAmt > 20000 && premiumAmt <= 25000) {
                                    AgentPoliciesRenewalPremiumwiseFilteredList.add(AgentPoliciesRenewalListMonthwiseGioList.get(i));
                                }
                            }
                        }
                        if (AgentPoliciesRenewalPremiumwiseFilteredList.size() != 0) {
                            //showPoliciesWithMarker(AgentPoliciesRenewalPremiumwiseFilteredList);
                            if(btnClicked != null && btnClicked.equalsIgnoreCase("btn_surrender_summary")){
                                showPoliciesWithMarkerAndSurrenderInfo(AgentPoliciesRenewalPremiumwiseFilteredList);
                            }else  {
                            showPoliciesWithMarker(AgentPoliciesRenewalPremiumwiseFilteredList);
                            }
                        } else {
                            Toast.makeText(PolicyHolderLocatorMapFragmentActivity.this, "No records found", Toast.LENGTH_LONG).show();
                        }
                        //Toast.makeText(PolicyHolderLocatorMapFragmentActivity.this, "Third Item Clicked", Toast.LENGTH_LONG).show();
                        break;
                    case 3:
                        AgentPoliciesRenewalPremiumwiseFilteredList = new ArrayList<>();
                        for (int i = 0; i < AgentPoliciesRenewalListMonthwiseGioList.size(); i++) {
                            if (AgentPoliciesRenewalListMonthwiseGioList.get(i).getPREMIUMGROSSAMOUNT() != null) {
                                double premiumAmt = Double.parseDouble(AgentPoliciesRenewalListMonthwiseGioList.get(i).getPREMIUMGROSSAMOUNT());

                                if (premiumAmt > 25000 && premiumAmt <= 50000) {
                                    AgentPoliciesRenewalPremiumwiseFilteredList.add(AgentPoliciesRenewalListMonthwiseGioList.get(i));
                                }
                            }
                        }
                        if (AgentPoliciesRenewalPremiumwiseFilteredList.size() != 0) {
                            //showPoliciesWithMarker(AgentPoliciesRenewalPremiumwiseFilteredList);
                            if(btnClicked != null && btnClicked.equalsIgnoreCase("btn_surrender_summary")){
                                showPoliciesWithMarkerAndSurrenderInfo(AgentPoliciesRenewalPremiumwiseFilteredList);
                            }else  {
                            showPoliciesWithMarker(AgentPoliciesRenewalPremiumwiseFilteredList);
                            }
                        } else {
                            Toast.makeText(PolicyHolderLocatorMapFragmentActivity.this, "No records found", Toast.LENGTH_LONG).show();
                        }
                        //Toast.makeText(PolicyHolderLocatorMapFragmentActivity.this, "Third Item Clicked", Toast.LENGTH_LONG).show();
                        break;
                    case 4:
                        AgentPoliciesRenewalPremiumwiseFilteredList = new ArrayList<>();
                        for (int i = 0; i < AgentPoliciesRenewalListMonthwiseGioList.size(); i++) {
                            if (AgentPoliciesRenewalListMonthwiseGioList.get(i).getPREMIUMGROSSAMOUNT() != null) {
                                double premiumAmt = Double.parseDouble(AgentPoliciesRenewalListMonthwiseGioList.get(i).getPREMIUMGROSSAMOUNT());
                                if (premiumAmt > 50000) {
                                    AgentPoliciesRenewalPremiumwiseFilteredList.add(AgentPoliciesRenewalListMonthwiseGioList.get(i));
                                }
                            }
                        }
                        if (AgentPoliciesRenewalPremiumwiseFilteredList.size() != 0) {
                            //showPoliciesWithMarker(AgentPoliciesRenewalPremiumwiseFilteredList);
                            if(btnClicked != null && btnClicked.equalsIgnoreCase("btn_surrender_summary")){
                                showPoliciesWithMarkerAndSurrenderInfo(AgentPoliciesRenewalPremiumwiseFilteredList);
                            }else  {
                            showPoliciesWithMarker(AgentPoliciesRenewalPremiumwiseFilteredList);
                            }
                        } else {
                            Toast.makeText(PolicyHolderLocatorMapFragmentActivity.this, "No records found", Toast.LENGTH_LONG).show();
                        }
                        //Toast.makeText(PolicyHolderLocatorMapFragmentActivity.this, "Third Item Clicked", Toast.LENGTH_LONG).show();
                        break;
                    case 5:
                        mGoogleMap.clear();
                        showMarkerCurrentLocation();
                        //showPoliciesWithMarker(AgentPoliciesRenewalListMonthwiseGioList);
                        if(btnClicked != null && btnClicked.equalsIgnoreCase("btn_surrender_summary")){
                            showPoliciesWithMarkerAndSurrenderInfo(AgentPoliciesRenewalListMonthwiseGioList);
                        }else  {
                        showPoliciesWithMarker(AgentPoliciesRenewalListMonthwiseGioList);
                        }
                        break;
                }
                dialog.dismiss();
            }
        });
        alertDialog1 = builder.create();
        alertDialog1.show();

    }

    private void CreateAlertDialogWithRadioButtonGroupForDistanceFilter
            (final ArrayList<ParseXML.AgentPoliciesRenewalListMonthwiseGio> AgentPoliciesRenewalListMonthwiseGioList) {

        AlertDialog alertDialog1;

        AlertDialog.Builder builder = new AlertDialog.Builder(PolicyHolderLocatorMapFragmentActivity.this);

        builder.setTitle("Select Your Choice");

        builder.setSingleChoiceItems(distancevalues, -1, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int item) {

                mGoogleMap.clear();
                showMarkerCurrentLocation();

                switch (item) {
                    case 0:
                        AgentPoliciesRenewalPremiumwiseFilteredList = new ArrayList<>();
                        for (int i = 0; i < AgentPoliciesRenewalListMonthwiseGioList.size(); i++) {
                            if (AgentPoliciesRenewalListMonthwiseGioList.get(i).getPOLICYDISTANCE() != 0.0) {
                                double distnce = AgentPoliciesRenewalListMonthwiseGioList.get(i).getPOLICYDISTANCE();
                                if (distnce <= 5) {
                                    AgentPoliciesRenewalPremiumwiseFilteredList.add(AgentPoliciesRenewalListMonthwiseGioList.get(i));
                                }
                            }
                        }
                        if (AgentPoliciesRenewalPremiumwiseFilteredList.size() != 0) {
                            if(btnClicked != null && btnClicked.equalsIgnoreCase("btn_surrender_summary")){
                                showPoliciesWithMarkerAndSurrenderInfo(AgentPoliciesRenewalPremiumwiseFilteredList);
                            }else  {
                            showPoliciesWithMarker(AgentPoliciesRenewalPremiumwiseFilteredList);
                            }
                        } else {
                            Toast.makeText(PolicyHolderLocatorMapFragmentActivity.this, "No records found", Toast.LENGTH_LONG).show();
                        }
                        //Toast.makeText(PolicyHolderLocatorMapFragmentActivity.this, "First Item Clicked", Toast.LENGTH_LONG).show();
                        break;
                    case 1:
                        AgentPoliciesRenewalPremiumwiseFilteredList = new ArrayList<>();
                        for (int i = 0; i < AgentPoliciesRenewalListMonthwiseGioList.size(); i++) {
                            if (AgentPoliciesRenewalListMonthwiseGioList.get(i).getPOLICYDISTANCE() != 0.0) {
                                double distnce = AgentPoliciesRenewalListMonthwiseGioList.get(i).getPOLICYDISTANCE();
                                if (distnce > 5 && distnce <= 10) {
                                    AgentPoliciesRenewalPremiumwiseFilteredList.add(AgentPoliciesRenewalListMonthwiseGioList.get(i));
                                }
                            }
                        }
                        if (AgentPoliciesRenewalPremiumwiseFilteredList.size() != 0) {
                            if(btnClicked != null && btnClicked.equalsIgnoreCase("btn_surrender_summary")){
                                showPoliciesWithMarkerAndSurrenderInfo(AgentPoliciesRenewalPremiumwiseFilteredList);
                            }else  {
                            showPoliciesWithMarker(AgentPoliciesRenewalPremiumwiseFilteredList);
                            }
                        } else {
                            Toast.makeText(PolicyHolderLocatorMapFragmentActivity.this, "No records found", Toast.LENGTH_LONG).show();
                        }
                        //Toast.makeText(PolicyHolderLocatorMapFragmentActivity.this, "Second Item Clicked", Toast.LENGTH_LONG).show();
                        break;
                    case 2:
                        AgentPoliciesRenewalPremiumwiseFilteredList = new ArrayList<>();
                        for (int i = 0; i < AgentPoliciesRenewalListMonthwiseGioList.size(); i++) {
                            if (AgentPoliciesRenewalListMonthwiseGioList.get(i).getPOLICYDISTANCE() != 0.0) {
                                double distnce = AgentPoliciesRenewalListMonthwiseGioList.get(i).getPOLICYDISTANCE();
                                if (distnce > 10 && distnce <= 20) {
                                    AgentPoliciesRenewalPremiumwiseFilteredList.add(AgentPoliciesRenewalListMonthwiseGioList.get(i));
                                }
                            }
                        }
                        if (AgentPoliciesRenewalPremiumwiseFilteredList.size() != 0) {
                            if(btnClicked != null && btnClicked.equalsIgnoreCase("btn_surrender_summary")){
                                showPoliciesWithMarkerAndSurrenderInfo(AgentPoliciesRenewalPremiumwiseFilteredList);
                            }else  {
                            showPoliciesWithMarker(AgentPoliciesRenewalPremiumwiseFilteredList);
                            }
                        } else {
                            Toast.makeText(PolicyHolderLocatorMapFragmentActivity.this, "No records found", Toast.LENGTH_LONG).show();
                        }
                        //Toast.makeText(PolicyHolderLocatorMapFragmentActivity.this, "Third Item Clicked", Toast.LENGTH_LONG).show();
                        break;
                    case 3:
                        AgentPoliciesRenewalPremiumwiseFilteredList = new ArrayList<>();
                        for (int i = 0; i < AgentPoliciesRenewalListMonthwiseGioList.size(); i++) {
                            if (AgentPoliciesRenewalListMonthwiseGioList.get(i).getPOLICYDISTANCE() != 0.0) {
                                double distnce = AgentPoliciesRenewalListMonthwiseGioList.get(i).getPOLICYDISTANCE();

                                if (distnce > 20 && distnce <= 50) {
                                    AgentPoliciesRenewalPremiumwiseFilteredList.add(AgentPoliciesRenewalListMonthwiseGioList.get(i));
                                }
                            }
                        }
                        if (AgentPoliciesRenewalPremiumwiseFilteredList.size() != 0) {
                            if(btnClicked != null && btnClicked.equalsIgnoreCase("btn_surrender_summary")){
                                showPoliciesWithMarkerAndSurrenderInfo(AgentPoliciesRenewalPremiumwiseFilteredList);
                            }else  {
                            showPoliciesWithMarker(AgentPoliciesRenewalPremiumwiseFilteredList);
                            }
                        } else {
                            Toast.makeText(PolicyHolderLocatorMapFragmentActivity.this, "No records found", Toast.LENGTH_LONG).show();
                        }
                        //Toast.makeText(PolicyHolderLocatorMapFragmentActivity.this, "Third Item Clicked", Toast.LENGTH_LONG).show();
                        break;
                    case 4:
                        AgentPoliciesRenewalPremiumwiseFilteredList = new ArrayList<>();
                        for (int i = 0; i < AgentPoliciesRenewalListMonthwiseGioList.size(); i++) {
                            if (AgentPoliciesRenewalListMonthwiseGioList.get(i).getPOLICYDISTANCE() != 0.0) {
                                double distnce = AgentPoliciesRenewalListMonthwiseGioList.get(i).getPOLICYDISTANCE();
                                if (distnce > 50) {
                                    AgentPoliciesRenewalPremiumwiseFilteredList.add(AgentPoliciesRenewalListMonthwiseGioList.get(i));
                                }
                            }
                        }
                        if (AgentPoliciesRenewalPremiumwiseFilteredList.size() != 0) {
                            if(btnClicked != null && btnClicked.equalsIgnoreCase("btn_surrender_summary")){
                                showPoliciesWithMarkerAndSurrenderInfo(AgentPoliciesRenewalPremiumwiseFilteredList);
                            }else  {
                            showPoliciesWithMarker(AgentPoliciesRenewalPremiumwiseFilteredList);
                            }
                        } else {
                            Toast.makeText(PolicyHolderLocatorMapFragmentActivity.this, "No records found", Toast.LENGTH_LONG).show();
                        }
                        //Toast.makeText(PolicyHolderLocatorMapFragmentActivity.this, "Third Item Clicked", Toast.LENGTH_LONG).show();
                        break;
                    case 5:
                        mGoogleMap.clear();
                        showMarkerCurrentLocation();

                        if(btnClicked != null && btnClicked.equalsIgnoreCase("btn_surrender_summary")){
                            showPoliciesWithMarkerAndSurrenderInfo(AgentPoliciesRenewalListMonthwiseGioList);
                        }else  {
                        showPoliciesWithMarker(AgentPoliciesRenewalListMonthwiseGioList);
                        }
                        //showPoliciesWithMarker(AgentPoliciesRenewalListMonthwiseGioList);
                        break;
                }
                dialog.dismiss();
            }
        });
        alertDialog1 = builder.create();
        alertDialog1.show();

    }

    private void CreateAlertDialogWithRadioButtonGroupForBranchDistanceFilter() {

        AlertDialog alertDialog1;

        AlertDialog.Builder builder = new AlertDialog.Builder(PolicyHolderLocatorMapFragmentActivity.this);

        builder.setTitle("Select Your Choice");

        builder.setSingleChoiceItems(distancevalues, -1, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int item) {

                mGoogleMap.clear();
                showMarkerCurrentLocation();

                switch (item) {
                    case 0:
                        sameCityBranchFilteredList = new ArrayList<>();
                        for (int i = 0; i < sameCityBranchList.size(); i++) {
                            if (sameCityBranchList.get(i).getBRANCHDISTANCE() != 0.0) {
                                double distnce = sameCityBranchList.get(i).getBRANCHDISTANCE();
                                if (distnce <= 5) {
                                    Log.d(TAG, "onClick: "+sameCityBranchList.get(i).getBRANCHDISTANCE());
                                    sameCityBranchFilteredList.add(sameCityBranchList.get(i));
                                }
                            }
                        }
                        if (sameCityBranchFilteredList.size() != 0) {
                            showBankBranchWithMarker(sameCityBranchFilteredList);
                        } else {
                            Toast.makeText(PolicyHolderLocatorMapFragmentActivity.this, "No records found", Toast.LENGTH_LONG).show();
                        }
                        //Toast.makeText(PolicyHolderLocatorMapFragmentActivity.this, "First Item Clicked", Toast.LENGTH_LONG).show();
                        break;
                    case 1:
                        sameCityBranchFilteredList = new ArrayList<>();
                        for (int i = 0; i < sameCityBranchList.size(); i++) {
                            if (sameCityBranchList.get(i).getBRANCHDISTANCE() != 0.0) {
                                double distnce = sameCityBranchList.get(i).getBRANCHDISTANCE();
                                if (distnce > 5 && distnce <= 10) {
                                    Log.d(TAG, "onClick: "+sameCityBranchList.get(i).getBRANCHDISTANCE());
                                    sameCityBranchFilteredList.add(sameCityBranchList.get(i));
                                }
                            }
                        }
                        if (sameCityBranchFilteredList.size() != 0) {
                            showBankBranchWithMarker(sameCityBranchFilteredList);
                        } else {
                            Toast.makeText(PolicyHolderLocatorMapFragmentActivity.this, "No records found", Toast.LENGTH_LONG).show();
                        }
                        //Toast.makeText(PolicyHolderLocatorMapFragmentActivity.this, "Second Item Clicked", Toast.LENGTH_LONG).show();
                        break;
                    case 2:
                        sameCityBranchFilteredList = new ArrayList<>();
                        for (int i = 0; i < sameCityBranchList.size(); i++) {
                            if (sameCityBranchList.get(i).getBRANCHDISTANCE() != 0.0) {
                                double distnce = sameCityBranchList.get(i).getBRANCHDISTANCE();
                                if (distnce > 10 && distnce <= 20) {
                                    Log.d(TAG, "onClick: "+sameCityBranchList.get(i).getBRANCHDISTANCE());
                                    sameCityBranchFilteredList.add(sameCityBranchList.get(i));
                                }
                            }
                        }
                        if (sameCityBranchFilteredList.size() != 0) {
                            showBankBranchWithMarker(sameCityBranchFilteredList);
                        } else {
                            Toast.makeText(PolicyHolderLocatorMapFragmentActivity.this, "No records found", Toast.LENGTH_LONG).show();
                        }
                        //Toast.makeText(PolicyHolderLocatorMapFragmentActivity.this, "Third Item Clicked", Toast.LENGTH_LONG).show();
                        break;
                    case 3:
                        sameCityBranchFilteredList = new ArrayList<>();
                        for (int i = 0; i < sameCityBranchList.size(); i++) {
                            if (sameCityBranchList.get(i).getBRANCHDISTANCE() != 0.0) {
                                double distnce = sameCityBranchList.get(i).getBRANCHDISTANCE();

                                if (distnce > 20 && distnce <= 50) {
                                    Log.d(TAG, "onClick: "+sameCityBranchList.get(i).getBRANCHDISTANCE());
                                    sameCityBranchFilteredList.add(sameCityBranchList.get(i));
                                }
                            }
                        }
                        if (sameCityBranchFilteredList.size() != 0) {
                            showBankBranchWithMarker(sameCityBranchFilteredList);
                        } else {
                            Toast.makeText(PolicyHolderLocatorMapFragmentActivity.this, "No records found", Toast.LENGTH_LONG).show();
                        }
                        //Toast.makeText(PolicyHolderLocatorMapFragmentActivity.this, "Third Item Clicked", Toast.LENGTH_LONG).show();
                        break;
                    case 4:
                        sameCityBranchFilteredList = new ArrayList<>();
                        for (int i = 0; i < sameCityBranchList.size(); i++) {
                            if (sameCityBranchList.get(i).getBRANCHDISTANCE() != 0.0) {
                                double distnce = sameCityBranchList.get(i).getBRANCHDISTANCE();
                                if (distnce > 50) {
                                    Log.d(TAG, "onClick: "+sameCityBranchList.get(i).getBRANCHDISTANCE());
                                    sameCityBranchFilteredList.add(sameCityBranchList.get(i));
                                }
                            }
                        }
                        if (sameCityBranchFilteredList.size() != 0) {
                            showBankBranchWithMarker(sameCityBranchFilteredList);
                        } else {
                            Toast.makeText(PolicyHolderLocatorMapFragmentActivity.this, "No records found", Toast.LENGTH_LONG).show();
                        }
                        //Toast.makeText(PolicyHolderLocatorMapFragmentActivity.this, "Third Item Clicked", Toast.LENGTH_LONG).show();
                        break;
                    case 5:
                        mGoogleMap.clear();
                        showMarkerCurrentLocation();
                        showBankBranchWithMarker(sameCityBranchList);
                        break;
                }
                dialog.dismiss();
            }
        });
        alertDialog1 = builder.create();
        alertDialog1.show();

    }

    private void getStateAndCity(double latitude,double longitude) {
        try{
            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(this, Locale.getDefault());

            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5


            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName();
            Log.d(TAG, "getStateAndCity: "+city+" == "+state);
            if(city != null && state != null){
            addListenerOnSubmit(state.toUpperCase(),city.toUpperCase());
            }
        }catch (IOException ex){
            Toast.makeText(this,"Please try again later",Toast.LENGTH_LONG).show();
        }
    }

    private void addListenerOnSubmit(String state, String city) {

        LocationManager locationmanager = (LocationManager) this
                .getSystemService(Context.LOCATION_SERVICE);

        if (locationmanager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            String METHOD_NAME_Map = "getBranchInfo";
            String SOAP_ACTION_Map = "http://tempuri.org/getBranchInfo";
            AsynLoadMap objAsynLoadMap = new AsynLoadMap(mContext,
                    SOAP_ACTION_Map,
                    METHOD_NAME_Map, state, city);
            objAsynLoadMap.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            mCommonMethods.showGPSDisabledAlertToUser(mContext);
        }
    }

    class AsynLoadMap extends AsyncTask<String, Void, String> {
        final Context mContext;
        ProgressDialog progressDialog = null;
        String NAMESPACE = "";
        String URL = "";
        String SOAP_ACTION = "";
        String METHOD_NAME = "";
        String state = "";
        String city = "";
        String output;

        AsynLoadMap(Context mContext, String SOAP_ACTION,
                    String METHOD_NAME, String state, String city) {
            // TODO Auto-generated constructor stub

            this.NAMESPACE = "http://tempuri.org/";
            this.URL = "https://sbilposservices.sbilife.co.in/service.asmx?wsdl";
            this.SOAP_ACTION = SOAP_ACTION;
            this.METHOD_NAME = METHOD_NAME;
            this.mContext = mContext;

            this.state = state;
            this.city = city;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            /*
             * progressDialog =
             * ProgressDialog.show(BranchLocatorMainActivity.this,
             * "Please wait... ", "Loading", true, true);
             */

            progressDialog = new ProgressDialog(mContext,
                    ProgressDialog.THEME_HOLO_LIGHT);
            String Message = "Please wait...";
            progressDialog.setMessage(Html.fromHtml("<font color='#00a1e3'><b>"
                    + Message + "<b></font>"));
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setCancelable(false);
            progressDialog.setTitle("Loading");
            progressDialog.setMax(100);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub

            if (mCommonMethods.isNetworkConnected(mContext)) {
                try {

                    SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
                    request.addProperty("strState", state);
                    request.addProperty("strCity", city);
                    SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                            SoapEnvelope.VER11);
                    // Enable this envelope if service is written in dot net
                    envelope.dotNet = true;
                    envelope.setOutputSoapObject(request);
                    HttpTransportSE androidHttpTransport = new HttpTransportSE(
                            URL);

                    // allowAllSSL();
                    mCommonMethods.TLSv12Enable();

                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                            .permitAll().build();
                    StrictMode.setThreadPolicy(policy);

                    androidHttpTransport.call(SOAP_ACTION, envelope);
                    SoapPrimitive response = (SoapPrimitive) envelope
                            .getResponse();

                    // ParseXML prsObj = new ParseXML();
                    String result = response.toString();
                    // System.out.println("result " + result.toString());
                    output = objParse.parseXmlTag(result, "PolicyDetails");
                    if (output == null)
                        return "false";
                    else
                        return output;

                } catch (Exception e) {
                    e.printStackTrace();
                    return "Server not Found. Please try after some time.";
                }
            } else
                return "Please Activate Internet connection";
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                if (progressDialog.isShowing())
                    progressDialog.dismiss();

                if (!result.equals("false")) {

                    AsynGetLatLang objAsynLoadMap = new AsynGetLatLang(
                            mContext, progressDialog, output);
                    objAsynLoadMap.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                } else
                    Toast.makeText(mContext, "No Branch Found",
                            Toast.LENGTH_LONG).show();

            } catch (Exception e) {
                e.getMessage();
            }

        }

    }

    class AsynGetLatLang extends AsyncTask<String, Void, String> {
        final Context mContext;
        ProgressDialog progressDialog = null;
        double BranchLati, BranchLongi;
        StringBuilder retVal;
        int size;
        final String res;

        AsynGetLatLang(Context mContext, ProgressDialog progressDialog,
                       String result) {
            // TODO Auto-generated constructor stub

            res = result;
            this.mContext = mContext;
            this.progressDialog = progressDialog;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            retVal = new StringBuilder();
            progressDialog = new ProgressDialog(mContext,
                    ProgressDialog.THEME_HOLO_LIGHT);
            String Message = "Please wait...";
            progressDialog.setMessage(Html.fromHtml("<font color='#00a1e3'><b>"
                    + Message + "<b></font>"));
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setCancelable(false);
            progressDialog.setTitle("Loading");
            progressDialog.setMax(100);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            if (mCommonMethods.isNetworkConnected(mContext)) {

                try {
                    Geocoder geocoder = new Geocoder(mContext,
                            Locale.getDefault());
                    try {
                        List<String> strRes = objParse.parseParentNode(res, "Table");
                        sameCityBranchList = objParse.parseNodeBranchList(strRes);

                        //size = sameCityBranchList.size();
//                        Log.d("doInBackground", "doInBackground: "+size);
                        for (int i = 0; i < sameCityBranchList.size(); i++) {

                            if (sameCityBranchList.get(i).getBR_PIN_CD() != null) {
                                String locationAddress = sameCityBranchList.get(i).getBR_ADD1()
                                        +" "+sameCityBranchList.get(i).getBR_ADD2()
                                        + " "+sameCityBranchList.get(i).getBR_ADD3()
                                        +" "+sameCityBranchList.get(i).getBR_PIN_CD();
                                List addressList = geocoder
                                        .getFromLocationName(locationAddress, 1);
                                if (addressList != null
                                        && addressList.size() > 0) {
                                    Address address = (Address) addressList
                                            .get(0);
                                    BranchLati = address.getLatitude() ;
                                    BranchLongi = address.getLongitude();
//                                    Log.d(TAG, "doInBackground: "+locationAddress);
//                                    Log.d(TAG, "doInBackground:1 "+BranchLati+" == "+BranchLongi);
                                    sameCityBranchList.get(i).setBRANCHLATITUDE(BranchLati);
                                    sameCityBranchList.get(i).setBRANCHLONGITUDE(BranchLongi);

                                    String km = getDistance(latLng.latitude,latLng.longitude,BranchLati,BranchLongi).replace(",","").replace(" km","");
                                    if(km != null || km != ""){
                                        sameCityBranchList.get(i).setBRANCHDISTANCE(Double.parseDouble(km));
                                    }


                                }

                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    return "Server not Found. Please try after some time.";
                }

            } else
                return "Please Activate Internet connection";

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            try {
                if (progressDialog.isShowing())
                    progressDialog.dismiss();
                showMarkerCurrentLocation();
                showBankBranchWithMarker(sameCityBranchList);

            } catch (Exception e) {
                e.getMessage();
            }

        }

    }

    private String getDistance(final double lat1, final double lon1, final double lat2, final double lon2){
        String parsedDistance = "";
        String response;
        try {
            java.net.URL url = new URL("https://maps.googleapis.com/maps/api/directions/json?origin=" + lat1 + "," + lon1 + "&destination=" + lat2 + "," + lon2 + "&sensor=false&units=metric&mode=driving&&key=AIzaSyCqIpNkXExQ86zMXx7h_9txgsG70naoG2Y");
            final HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            InputStream in = new BufferedInputStream(conn.getInputStream());
            response = IOUtil.toString(in, "UTF-8");
            //Log.d("doInBackground", "doInBackground: "+response);
            JSONObject jsonObject = new JSONObject(response);
            JSONArray array = jsonObject.getJSONArray("routes");
            //Log.d("doInBackground", "doInBackground: "+array.toString());
            JSONObject routes = array.getJSONObject(0);
            JSONArray legs = routes.getJSONArray("legs");
            JSONObject steps = legs.getJSONObject(0);
            JSONObject distance = steps.getJSONObject("distance");
            parsedDistance = distance.getString("text");
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return parsedDistance;
    }

//    private void showMarkerCurrentLocation() {
//        if (gpsTracker.canGetLocation()) {
//            Double latitude = gpsTracker.getLatitude();
//            Double longitude = gpsTracker.getLongitude();
//            Log.e("Individual Location", ": " + latitude + "  " + longitude);
//            LatLng latLng = new LatLng(latitude, longitude);
//            mGoogleMap.addMarker(new MarkerOptions().position(latLng).draggable(false).title("You are here.")
//                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
//        }
//    }
//
    private void showBankBranchWithMarker(ArrayList<ParseXML.BranchAddress> sameCityBranchList) {
        hashMap = new HashMap();
        double firstSBIInsuranceLatitude=0.0d,firstSBIInsuranceLongitude=0.0d;
        for (int j = 0; j < sameCityBranchList.size(); j++) {
            if(j==0){
                firstSBIInsuranceLatitude= sameCityBranchList.get(j).getBRANCHLATITUDE();
                firstSBIInsuranceLongitude= sameCityBranchList.get(j).getBRANCHLONGITUDE();
            }

            addMarkerOnMap(sameCityBranchList.get(j).getBRANCHLATITUDE(), sameCityBranchList.get(j).getBRANCHLONGITUDE(),
                    sameCityBranchList.get(j).getBR_ADD1() + " " +
                            sameCityBranchList.get(j).getBR_ADD2() + "\n" +
                            sameCityBranchList.get(j).getBR_ADD3() + " - " +
                            sameCityBranchList.get(j).getBR_PIN_CD() + "\n Tel No: " +
                            sameCityBranchList.get(j).getTEL_NO(),sameCityBranchList.get(j));


        }
        //mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(firstSBIInsuranceLatitude,firstSBIInsuranceLongitude)));
        //mGoogleMap.moveCamera(CameraUpdateFactory.zoomTo(10.0f));

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(firstSBIInsuranceLatitude, firstSBIInsuranceLongitude)).
                        zoom(5).build();
        mGoogleMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(cameraPosition));
    }

    private void addMarkerOnMap(Double latitude, Double longitude, String Address, ParseXML.BranchAddress branchAddress){
        Marker branckMarker = mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(latitude,longitude)).
                anchor(0.5f,0.5f).draggable(false).title("SBI LIFE INSURANCE COMPANY LTD").snippet(Address));
        hashMap.put(branckMarker, branchAddress);
        marker.add(branckMarker);
    }

    private void getUserDetails() {
        CommonMethods.UserDetailsValuesModel userDetailsValuesModel = mCommonMethods
                .setUserDetails(this);
        strCIFBDMUserId = userDetailsValuesModel.getStrCIFBDMUserId();
        strCIFBDMEmailId = userDetailsValuesModel.getStrCIFBDMEmailId();
        String strCIFBDMPassword = userDetailsValuesModel.getStrCIFBDMPassword();
        strCIFBDMMObileNo = userDetailsValuesModel.getStrCIFBDMMObileNo();
    }

    class SubmitDispositionRemarkAsync extends AsyncTask<String, Void, String> {

        private volatile boolean running = true;
        private final String policyNo;
        private final String status;
        private final String substatus;
        private final String remarks;
        private String output;
        //Context mContext;
        ProgressDialog mProgressDialog;

        SubmitDispositionRemarkAsync(String policyNo, String status, String substatus, String remarks){
            this.policyNo = policyNo;
            this.status = status;
            this.substatus = substatus;
            this.remarks = remarks;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(mContext,ProgressDialog.THEME_HOLO_LIGHT);
            String Message = "Loading. Please wait...";
            mProgressDialog.setMessage(Html.fromHtml("<font color='#000000'><b>" + Message + "<b></font>"));
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setCancelable(true);
            mProgressDialog.setMax(100);
            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... data) {
            try {
                running = true;


                String METHOD_NAME_DISPOSITION_REMARK = "saveCallingRemarks_smrt";
                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME_DISPOSITION_REMARK);

                request.addProperty("policyno", policyNo);
                request.addProperty("status", status);
                request.addProperty("substatus", substatus);
                request.addProperty("remarks", remarks);
                request.addProperty("empid", strCIFBDMUserId);
                request.addProperty("strEmailId", "a@g.com");
                request.addProperty("strMobileNo", "0000000000");
                request.addProperty("strAuthKey", mCommonMethods.getStrAuth());

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                envelope.dotNet = true;

                envelope.setOutputSoapObject(request);

                mCommonMethods.TLSv12Enable();

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);

                HttpTransportSE androidHttpTranport = new HttpTransportSE(URL);
                String SOAP_ACTION_DISPOSITION_REMARK = "http://tempuri.org/saveCallingRemarks_smrt";
                androidHttpTranport.call(SOAP_ACTION_DISPOSITION_REMARK, envelope);

                SoapPrimitive sa;
                sa = (SoapPrimitive) envelope.getResponse();

                output = sa.toString();
                //Log.d(TAG, "doInBackground:1 "+outResponce);
                //ParseXML prsObj = new ParseXML();

            } catch (Exception e) {
                running = false;
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {

            if (mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }

            if (running) {
                if(output.equalsIgnoreCase("1")){
                    mCommonMethods.showMessageDialog(mContext,"Remarks updated");
                }else if(output.equalsIgnoreCase("2")){
                    mCommonMethods.showMessageDialog(mContext,"You are not authorized user.");
                }else {
                    mCommonMethods.showMessageDialog(mContext,"Remarks updation failed. Please try again later");
                }
            } else {
                mCommonMethods.showMessageDialog(mContext, "Server not responding..");
            }
        }

    }

    class GetRemarksAsyncTask extends AsyncTask<String, Void, String> {

        private volatile boolean running = true;
        private final String policyNo;
        private ArrayList<ParseXML.RenewalRemark> renewalRemarkArrayList;
        //Context mContext;
        ProgressDialog mProgressDialog;

        GetRemarksAsyncTask(String policyNo){
            this.policyNo = policyNo;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(mContext,ProgressDialog.THEME_HOLO_LIGHT);
            String Message = "Loading. Please wait...";
            mProgressDialog.setMessage(Html.fromHtml("<font color='#000000'><b>" + Message + "<b></font>"));
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setCancelable(true);
            mProgressDialog.setMax(100);
            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... data) {
            try {
                running = true;


                String METHOD_NAME_GET_REMARK = "getCallingRemarks_smrt";
                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME_GET_REMARK);

                request.addProperty("policyno", policyNo);
                request.addProperty("strEmailId", "a@g.com");
                request.addProperty("strMobileNo", "0000000000");
                request.addProperty("strAuthKey", mCommonMethods.getStrAuth());

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                envelope.dotNet = true;

                envelope.setOutputSoapObject(request);

                mCommonMethods.TLSv12Enable();

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);

                HttpTransportSE androidHttpTranport = new HttpTransportSE(URL);
                String SOAP_ACTION_DISPOSITION_REMARK = "http://tempuri.org/"+METHOD_NAME_GET_REMARK;
                androidHttpTranport.call(SOAP_ACTION_DISPOSITION_REMARK, envelope);

                SoapPrimitive sa;
                sa = (SoapPrimitive) envelope.getResponse();

                String output = sa.toString();
                //Log.d(TAG, "doInBackground:1 "+output);
                //ParseXML prsObj = new ParseXML();
                if (!output.contentEquals("<NewDataSet /> ")) {
                            /*<NewDataSet>
                              <QueryResults>
                                <PAY_EX1_91>Move to Call Centre</PAY_EX1_91>
                                <PAY_EX1_96>Forward for structured follow up</PAY_EX1_96>
                                <HTMLTEXT_280>LastModified By: 1point1 LastModified On: 02-07-2018 7:51:24 PM&lt;br /&gt;
                                                    Customer contact no is updated&lt;br /&gt;</HTMLTEXT_280>
                                <PAY_EX1_95>AMBER</PAY_EX1_95>
                                <ROWNUMBER>1</ROWNUMBER>
                                <Key>45693</Key>
                              </QueryResults>
                            </NewDataSet>*/
                    //SoapPrimitive sa = null;
                    try {
                        // sa = (SoapPrimitive) envelope.getResponse();
                        String inputpolicylist = output;

                        if (!inputpolicylist.equalsIgnoreCase("<NewDataSet /> ")) {

                            ParseXML parseXML = new ParseXML();
                            String DataResultXML = parseXML.parseXmlTag(
                                    inputpolicylist, "NewDataSet");
                            //DataResultXML = escapeXml(DataResultXML);
                            List<String> Node = parseXML.parseParentNode(
                                    DataResultXML, "Table");
                            renewalRemarkArrayList = parseXML
                                    .parseRenewalRemark(Node);

                        }

                    } catch (Exception e) {

                        mProgressDialog.dismiss();
                        running = false;
                    }
                }

            } catch (Exception e) {
                running = false;
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {

            if (mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }

            if (running) {
                if(renewalRemarkArrayList != null && renewalRemarkArrayList.size() > 0){
                    displayRenewalRemarks(renewalRemarkArrayList);

                } else {
                    mCommonMethods.showMessageDialog(mContext, mCommonMethods.NO_RECORD_FOUND);
                }
            } else {
                mCommonMethods.showMessageDialog(mContext, mCommonMethods.NO_RECORD_FOUND);
                //mCommonMethods.showMessageDialog(mContext, "Server not responding..");
            }
        }

    }

    private void displayRenewalRemarks(ArrayList<ParseXML.RenewalRemark> renewalRemarkArrayList) {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_LIGHT);


        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.remark_alert_dialog, null);
        alertDialogBuilder.setView(dialogView);
        final AlertDialog alert = alertDialogBuilder.create();
        alert.show();

        RecyclerView remarkRecyclerView = dialogView.findViewById(R.id.remark_list_rv);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        remarkRecyclerView.setLayoutManager(linearLayoutManager);

        RenewalRemarkAdapter renewalRemarkAdapter = new RenewalRemarkAdapter(renewalRemarkArrayList);
        remarkRecyclerView.setAdapter(renewalRemarkAdapter);



    }

    public class RenewalRemarkAdapter extends RecyclerView.Adapter<RenewalRemarkAdapter.ViewHolderAdapter> {

        private final ArrayList<ParseXML.RenewalRemark> lstAdapterList;

        RenewalRemarkAdapter(ArrayList<ParseXML.RenewalRemark> lstAdapterList) {
            this.lstAdapterList = lstAdapterList;
        }


        @Override
        public int getItemCount() {
            return lstAdapterList.size();
        }

        @Override
        public ViewHolderAdapter onCreateViewHolder(ViewGroup parent, int viewType) {
            View mView = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.remark_item_layout, parent, false);

            return new ViewHolderAdapter(mView);
        }

        @Override
        public void onBindViewHolder(final ViewHolderAdapter holder, int position) {
            ParseXML.RenewalRemark  renewalRemark = lstAdapterList.get(position);

            holder.txtDispositionRemark.setText(renewalRemark.getSTATUS());
            holder.txtSubDispositionRemark.setText(renewalRemark.getSUBSTATUS());
            holder.txtRemark.setText(renewalRemark.getREMARKS());
            holder.txtCreatedDate.setText(renewalRemark.getCREATED_DATE());
        }

        public class ViewHolderAdapter extends RecyclerView.ViewHolder {


            private final TextView txtDispositionRemark, txtSubDispositionRemark, txtRemark,txtCreatedDate;

            ViewHolderAdapter(View v) {
                super(v);
                txtDispositionRemark = v.findViewById(R.id.txt_disposition_remark);
                txtSubDispositionRemark = v.findViewById(R.id.txt_subdisposition_remark);
                txtRemark = v.findViewById(R.id.txt_remark);
                txtCreatedDate = v.findViewById(R.id.txt_remark_created_date);
            }

        }

    }

    private void showSMSAlert(ParseXML.AgentPoliciesRenewalListMonthwiseGio agentPoliciesRenewalListMonthwiseGio){
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Choose Language");
        final String[] languagesArray = {"English", "Hindi", "Telugu"};
        // cow
        final String policyNumber = agentPoliciesRenewalListMonthwiseGio.getPOLICYNUMBER();
        final String mobileNumber = agentPoliciesRenewalListMonthwiseGio.getCONTACTMOBILE();
        final String dueDate = agentPoliciesRenewalListMonthwiseGio.getPREMIUMFUP();
        final String status = agentPoliciesRenewalListMonthwiseGio.getPOLICYCURRENTSTATUS();
        final String amount = agentPoliciesRenewalListMonthwiseGio.getPREMIUMGROSSAMOUNT();
        builder.setSingleChoiceItems(languagesArray, finalPosition, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finalPosition = which;
            }
        });

        builder.setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String language = languagesArray[finalPosition];
                if (mCommonMethods.isNetworkConnected(mContext)) {
                    SendSmsAsync sendSmsAsync = new SendSmsAsync(policyNumber, mobileNumber, dueDate,
                            status, amount, language);
                    sendSmsAsync.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                } else {
                    mCommonMethods.showMessageDialog(mContext, mCommonMethods.NO_INTERNET_MESSAGE);
                }
            }
        });
        builder.setNegativeButton("Cancel", null);

        AlertDialog dialog = builder.create();
        //dialog.setCancelable(false);
        dialog.show();
    }

    class SendSmsAsync extends AsyncTask<String, String, String> {

        private volatile boolean running = true;
        private String response = "";

        private final String policyNumber;
        private final String mobileNumber;
        private final String dueDate;
        private final String status;
        private final String amount;
        private final String language;

        private ProgressDialog mProgressDialog;

        SendSmsAsync(String policyNumber, String mobileNumber, String dueDate, String status, String amount, String language) {
            this.policyNumber = policyNumber;
            this.mobileNumber = mobileNumber;
            this.dueDate = dueDate;
            this.status = status;
            this.amount = amount;
            this.language = language;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(mContext,ProgressDialog.THEME_HOLO_LIGHT);
            String Message = "Loading Please wait...";
            mProgressDialog.setMessage(Html.fromHtml("<font color='#000000'><b>" + Message + "<b></font>"));
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setCancelable(true);
            mProgressDialog.setMax(100);
            mProgressDialog.show();
        }

        @SuppressLint("SimpleDateFormat")
        @Override
        protected String doInBackground(String... aurl) {
            try {

                running = true;
                String METHOD_NAME_SEND_SMS = "SendRenewalDueSMS_SMRT";
                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME_SEND_SMS);
                request.addProperty("strPolicyNo", policyNumber);
                request.addProperty("strMobileNo", mobileNumber);
                request.addProperty("strDueDate", dueDate);
                request.addProperty("strStatus", status);
                request.addProperty("strAmt", amount);
                request.addProperty("strLanguage", language);
               /* request.addProperty("strMobileNo", "0000000000");
                request.addProperty("strEmailId", "a@g.com");
                request.addProperty("strAuthKey", mCommonMethods.getStrAuth());*/

                Log.d(TAG, "doInBackground: "+request.toString());
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                envelope.dotNet = true;

                envelope.setOutputSoapObject(request);

                mCommonMethods.TLSv12Enable();

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);

                HttpTransportSE androidHttpTranport = new HttpTransportSE(URL);
                String SOAP_ACTION = "http://tempuri.org/" + METHOD_NAME_SEND_SMS;
                androidHttpTranport.call(SOAP_ACTION, envelope);

                SoapPrimitive sa = (SoapPrimitive) envelope.getResponse();
                response = sa.toString();
                if (response.equalsIgnoreCase("1")) {
                    response = "success";
                } else {
                    response = "";
                }
            } catch (Exception e) {
                mProgressDialog.dismiss();
                running = false;
            }
            return null;

        }


        @Override
        protected void onPostExecute(String unused) {

            if (mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }

            if (running) {
                if (response.equalsIgnoreCase("success")) {
                    mCommonMethods.showMessageDialog(mContext, "Message sent successfully");
                } else {
                    mCommonMethods.showMessageDialog(mContext, "Message sending failed");
                }
            } else {
                mCommonMethods.showMessageDialog(mContext, "Message sending failed");
            }
        }
    }

    /*private void filterBasedOnPincode(ArrayList<ParseXML.AgentPoliciesRenewalListMonthwiseGio> agentPoliciesRenewalListMonthwiseGioList) {
        HashMap<String,ArrayList<ParseXML.AgentPoliciesRenewalListMonthwiseGio>> filteredList = new HashMap<>();

        for (int i=0;i<agentPoliciesRenewalListMonthwiseGioList.size();i++){
           boolean isConteinsKey =  filteredList.containsKey(agentPoliciesRenewalListMonthwiseGioList.get(i).getPERMANENTPOSTCODE());
           if(isConteinsKey){
               ArrayList<ParseXML.AgentPoliciesRenewalListMonthwiseGio> agentPoliciesRenewalListMonthwiseGios =
                       filteredList.get(agentPoliciesRenewalListMonthwiseGioList.get(i).getPERMANENTPOSTCODE());
               agentPoliciesRenewalListMonthwiseGios.add(agentPoliciesRenewalListMonthwiseGioList.get(i));
           }else {
               ArrayList<ParseXML.AgentPoliciesRenewalListMonthwiseGio> policiesRenewalListMonthwiseGios = new ArrayList<ParseXML.AgentPoliciesRenewalListMonthwiseGio>();
               policiesRenewalListMonthwiseGios.add(agentPoliciesRenewalListMonthwiseGioList.get(i));
               filteredList.put(agentPoliciesRenewalListMonthwiseGioList.get(i).getPERMANENTPOSTCODE().trim(),policiesRenewalListMonthwiseGios);
           }
        }


    }*/

    class DownloadBranchWiseRenewalListAsync extends AsyncTask<String, String, String> {

        private volatile boolean running = true;
        private String fromDate = "";
        private String toDate = "";
        String output;
        private ProgressDialog mProgressDialog;
        private ArrayList<ParseXML.RenewalListGioSummary> renewalListGioSummaries;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            mProgressDialog = new ProgressDialog(mContext, ProgressDialog.THEME_HOLO_LIGHT);
            String Message = "Loading Please wait...";
            mProgressDialog.setMessage(Html.fromHtml("<font color='#00a1e3'><b>" + Message + "<b></font>"));
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setCancelable(true);
            mProgressDialog.show();

            if (mProgressDialog != null && !mProgressDialog.isShowing()) {
                mProgressDialog.show();
            }



            SimpleDateFormat format1 = new SimpleDateFormat("MM-dd-yyyy",Locale.ENGLISH);

            if (spnRewmonths_text.contentEquals("Previous Month")) {
                Calendar aCalendar = Calendar.getInstance();
                aCalendar.set(Calendar.DATE, 1);
                aCalendar.add(Calendar.DAY_OF_MONTH, -1);
                Date lastDateOfPreviousMonth = aCalendar.getTime();
                toDate = format1.format(lastDateOfPreviousMonth);

                aCalendar.set(Calendar.DATE, 1);
                aCalendar.add(Calendar.MONTH, -5);
                Date firstDateOfPreviousMonth = aCalendar.getTime();
                fromDate = format1.format(firstDateOfPreviousMonth);
            } else if (spnRewmonths_text.contentEquals("Current Month")) {
                Calendar cale = Calendar.getInstance();
                cale.set(Calendar.DATE,
                        cale.getActualMaximum(Calendar.DATE));
                Date lastDateOfCurrentMonth = cale.getTime();
                toDate = format1.format(lastDateOfCurrentMonth);
                cale.set(Calendar.DATE,
                        cale.getActualMinimum(Calendar.DATE));
                Date firstDateOfCurrentMonth = cale.getTime();
                fromDate = format1.format(firstDateOfCurrentMonth);
            } else if (spnRewmonths_text.contentEquals("Next Month")) {
                Calendar calen = Calendar.getInstance();
                // calen.set(Calendar.MONTH, calen.get(Calendar.MONTH));
                calen.add(Calendar.MONTH, +1);
                calen.set(Calendar.DATE, calen.getActualMaximum(Calendar.DATE));
                Date lastDateOfNextMonth = calen.getTime();
                toDate = format1.format(lastDateOfNextMonth);

                calen.set(Calendar.DATE, calen.getActualMinimum(Calendar.DATE));

                Date firstDateOfNextMonth = calen.getTime();
                fromDate = format1.format(firstDateOfNextMonth);
            }
        }

        @SuppressLint("SimpleDateFormat")
        @Override
        protected String doInBackground(String... aurl) {
            try {

                running = true;
                SoapObject request;

                /*getbancaRenewalListBranchwise(string strBrcode, string strFromReqDate, string strToReqDate,
                string strEmailId, string strMobileNo, string strAuthKey) */

                String METHOD_NAME_BRANCHWISE_RENEWAL_LIST_GIO_SUMMARY = "getRenewalSummary_gio";
                request = new SoapObject(NAMESPACE, METHOD_NAME_BRANCHWISE_RENEWAL_LIST_GIO_SUMMARY);
                request.addProperty("strAgentNo", strCIFBDMUserId);
                request.addProperty("strFromReqDate", fromDate);
                request.addProperty("strToReqDate", toDate);
                request.addProperty("strEmailId", strCIFBDMEmailId);
                request.addProperty("strMobileNo", strCIFBDMMObileNo);

                request.addProperty("strAuthKey", "QzhCNDc0OTU4NzZDQjI3RTQ4OEMyNEQ3MUZCQjE2QTY=");

                mCommonMethods.TLSv12Enable();

                System.out.println("request:" + request.toString());
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);

                mCommonMethods.TLSv12Enable();


                HttpTransportSE androidHttpTranport = new HttpTransportSE(URL);
                String SOAP_ACTION= "http://tempuri.org/"+METHOD_NAME_BRANCHWISE_RENEWAL_LIST_GIO_SUMMARY;
                androidHttpTranport.call(SOAP_ACTION, envelope);
                System.out.println("envelope.getResponse() = "+envelope.getResponse());
                SoapPrimitive response = (SoapPrimitive)envelope.getResponse();
                if (!response.toString().contentEquals("<CIFPolicyList />")) {

                    ParseXML prsObj = new ParseXML();
                    String inputpolicylist = response.toString();
                    System.out.println("inputpolicylist = "+inputpolicylist);
                    inputpolicylist = prsObj.parseXmlTag(inputpolicylist, "CIFPolicyList");

                    String table = prsObj.parseXmlTag(inputpolicylist, "Table");

                    if (table != null) {

                        List<String> strRes = objParse.parseParentNode(inputpolicylist, "Table");
                        renewalListGioSummaries = objParse.parseNodeRenewalListGioSummary(strRes);

                    } else {
                        renewalListGioSummaries = null;
                    }

                } else {
                    renewalListGioSummaries = null;
                }

            } catch (Exception e) {
                mProgressDialog.dismiss();
                running = false;
            }
            return null;

        }


        @Override
        protected void onPostExecute(String unused) {
            if (mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }

            if (running) {
                if (renewalListGioSummaries != null) {
                    displaySummary(renewalListGioSummaries);
                } else {
                    mCommonMethods.showMessageDialog(mContext, "No Record found");

                }
            } else {
                mCommonMethods.showMessageDialog(mContext, "No Record found");

            }
        }
    }

    private void displaySummary(ArrayList<ParseXML.RenewalListGioSummary> renewalListGioSummaries) {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_LIGHT);


        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.summary_dialog_layout, null);
        alertDialogBuilder.setView(dialogView);
        final AlertDialog alert = alertDialogBuilder.create();
        alert.show();

        TextView closeDialog = dialogView.findViewById(R.id.txt_close_dialog);
        closeDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert.dismiss();
            }
        });

        RecyclerView remarkRecyclerView = dialogView.findViewById(R.id.summary_list_rv);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        remarkRecyclerView.setLayoutManager(linearLayoutManager);

//        PolicySummaryAdapter policySummaryAdapter = new PolicySummaryAdapter(renewalListGioSummaries);
//        remarkRecyclerView.setAdapter(policySummaryAdapter);
    }

    public void animateButtons(boolean isSelected) {
        // int[] imageButtonIds = {R.id.animateButton};
        int[] cardViewIds = {R.id.btn_policies_cardview, R.id.btn_branch_locator_cardview, R.id.btn_summary_cardview,
                R.id.btn_surrender_cardview,R.id.btn_probable_commission_cardview};

        int i = 1;
        Animation fadeAnimation = null;
        for (int viewId : cardViewIds) {
            // Button imageButton = (Button) findViewById(viewId);
            CardView textView = (CardView) findViewById(viewId);
            if(!isSelected){
                fadeAnimation = AnimationUtils.loadAnimation(this, R.anim.fading_effect);
                fadeAnimation.setStartOffset(i * 200);
                textView.setVisibility(View.VISIBLE);
            }else {
                fadeAnimation = AnimationUtils.loadAnimation(this, R.anim.fading_out);
                fadeAnimation.setStartOffset(i * 0);
                textView.setVisibility(View.GONE);
            }
            //imageButton.startAnimation(fadeAnimation);

            //int textViewId = cardViewIds[i-1];


            textView.startAnimation(fadeAnimation);

            i ++;
        }
    }

    private void  filterBasedOnSurrenderFlag(ArrayList<ParseXML.AgentPoliciesRenewalListMonthwiseGio> agentPoliciesRenewalListMonthwiseGioList){
        HashMap<String,ArrayList<ParseXML.AgentPoliciesRenewalListMonthwiseGio>> filteredList = new HashMap<>();

        for (int i=0;i<agentPoliciesRenewalListMonthwiseGioList.size();i++){
            boolean isConteinsKey =
                    filteredList.containsKey(agentPoliciesRenewalListMonthwiseGioList.get(i).getSURRENDER_FLAG().toLowerCase());
            if(isConteinsKey){
                ArrayList<ParseXML.AgentPoliciesRenewalListMonthwiseGio> agentPoliciesRenewalListMonthwiseGios =
                        filteredList.get(agentPoliciesRenewalListMonthwiseGioList.get(i).getSURRENDER_FLAG().toLowerCase());
                agentPoliciesRenewalListMonthwiseGios.add(agentPoliciesRenewalListMonthwiseGioList.get(i));
            }else {
                ArrayList<ParseXML.AgentPoliciesRenewalListMonthwiseGio> policiesRenewalListMonthwiseGios =
                        new ArrayList<ParseXML.AgentPoliciesRenewalListMonthwiseGio>();
                policiesRenewalListMonthwiseGios.add(agentPoliciesRenewalListMonthwiseGioList.get(i));
                filteredList.put(agentPoliciesRenewalListMonthwiseGioList.get(i).getSURRENDER_FLAG().toLowerCase().trim(),
                        policiesRenewalListMonthwiseGios);
            }
        }


        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_LIGHT);


        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.summary_dialog_layout, null);
        TextView colOneTitle = dialogView.findViewById(R.id.column_1_title);
        colOneTitle.setText("Flag");
        TextView colTwoTitle = dialogView.findViewById(R.id.column_2_title);
        colTwoTitle.setText("Count");
        TextView title = dialogView.findViewById(R.id.summery_title);
        TextView txt_summary_amount = dialogView.findViewById(R.id.txt_summary_amount);
        title.setText("Surrender Summary");
        txt_summary_amount.setVisibility(View.GONE);

        TextView closeDialog = dialogView.findViewById(R.id.txt_close_dialog);


        alertDialogBuilder.setView(dialogView);
        final AlertDialog alert = alertDialogBuilder.create();
        alert.show();

        closeDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert.dismiss();
            }
        });

        RecyclerView remarkRecyclerView = dialogView.findViewById(R.id.summary_list_rv);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        remarkRecyclerView.setLayoutManager(linearLayoutManager);

        PolicySurrenderFlagAdapter policySummaryAdapter = new PolicySurrenderFlagAdapter(filteredList, new PolicySurrenderFlagAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ArrayList<ParseXML.AgentPoliciesRenewalListMonthwiseGio> renewalListGioSummary) {
                alert.dismiss();
                AgentPoliciesRenewalSummaryList = null;
                AgentPoliciesRenewalSummaryList = renewalListGioSummary;
                showPoliciesWithMarkerAndSurrenderInfo(renewalListGioSummary);
            }
        });
        remarkRecyclerView.setAdapter(policySummaryAdapter);

    }

    /***************summary filter code start*****************/
    private void filterBasedOnPincode(ArrayList<ParseXML.AgentPoliciesRenewalListMonthwiseGio> agentPoliciesRenewalListMonthwiseGioList) {
        HashMap<String,ArrayList<ParseXML.AgentPoliciesRenewalListMonthwiseGio>> filteredList = new HashMap<>();

        for (int i=0;i<agentPoliciesRenewalListMonthwiseGioList.size();i++){
            boolean isConteinsKey =  filteredList.containsKey(agentPoliciesRenewalListMonthwiseGioList.get(i).getPERMANENTCITY().trim().toUpperCase());
            if(isConteinsKey){
                ArrayList<ParseXML.AgentPoliciesRenewalListMonthwiseGio> agentPoliciesRenewalListMonthwiseGios =
                        filteredList.get(agentPoliciesRenewalListMonthwiseGioList.get(i).getPERMANENTCITY().trim().toUpperCase());
                agentPoliciesRenewalListMonthwiseGios.add(agentPoliciesRenewalListMonthwiseGioList.get(i));
            }else {
                ArrayList<ParseXML.AgentPoliciesRenewalListMonthwiseGio> policiesRenewalListMonthwiseGios = new ArrayList<ParseXML.AgentPoliciesRenewalListMonthwiseGio>();
                policiesRenewalListMonthwiseGios.add(agentPoliciesRenewalListMonthwiseGioList.get(i));
                filteredList.put(agentPoliciesRenewalListMonthwiseGioList.get(i).getPERMANENTCITY().trim().toUpperCase(),policiesRenewalListMonthwiseGios);
            }
        }

        displaySummary(filteredList);

    }

    private void displaySummary(HashMap<String, ArrayList<ParseXML.AgentPoliciesRenewalListMonthwiseGio>> filteredList) {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_LIGHT);


        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.summary_dialog_layout, null);
        alertDialogBuilder.setView(dialogView);
        final AlertDialog alert = alertDialogBuilder.create();
        alert.show();

        TextView closeDialog = dialogView.findViewById(R.id.txt_close_dialog);
        closeDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert.dismiss();
            }
        });

        RecyclerView remarkRecyclerView = dialogView.findViewById(R.id.summary_list_rv);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        remarkRecyclerView.setLayoutManager(linearLayoutManager);

        PolicySummaryAdapter policySummaryAdapter = new PolicySummaryAdapter(filteredList, new PolicySummaryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ArrayList<ParseXML.AgentPoliciesRenewalListMonthwiseGio> renewalListGioSummary) {
                alert.dismiss();
                AgentPoliciesRenewalSummaryList = null;
                AgentPoliciesRenewalSummaryList = renewalListGioSummary;
                showPoliciesWithMarker(renewalListGioSummary);
            }
        });
        remarkRecyclerView.setAdapter(policySummaryAdapter);
    }
    /****************************summary filter code end****************/

    private void displayPoliciesAlert(final ArrayList<ParseXML.AgentPoliciesRenewalListMonthwiseGio> agentPoliciesRenewalListMonthwiseGioList) {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_LIGHT);


        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.summary_dialog_layout, null);
        alertDialogBuilder.setView(dialogView);
        final AlertDialog alert = alertDialogBuilder.create();
        alert.show();

        TextView colOneTitle = dialogView.findViewById(R.id.column_1_title);
        colOneTitle.setText("Policy No");
        TextView colTwoTitle = dialogView.findViewById(R.id.column_2_title);
        colTwoTitle.setText("City");
        TextView colThreeTitle = dialogView.findViewById(R.id.txt_summary_amount);
        colThreeTitle.setText("");
        TextView title = dialogView.findViewById(R.id.summery_title);
        title.setText("Policies List");

        TextView closeDialog = dialogView.findViewById(R.id.txt_close_dialog);
        closeDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert.dismiss();
            }
        });

        RecyclerView remarkRecyclerView = dialogView.findViewById(R.id.summary_list_rv);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        remarkRecyclerView.setLayoutManager(linearLayoutManager);

        UserPolicyListAdapter userPolicyListAdapter = new UserPolicyListAdapter(agentPoliciesRenewalListMonthwiseGioList, new UserPolicyListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ParseXML.AgentPoliciesRenewalListMonthwiseGio agentPoliciesRenewalListMonthwiseGio) {
                //alert.dismiss();
//                AgentPoliciesRenewalSummaryList = null;
//                AgentPoliciesRenewalSummaryList = renewalListGioSummary;
//                showPoliciesWithMarker(renewalListGioSummary);
                GetProbableCommissionAsyncTask getProbableCommissionAsyncTask = new GetProbableCommissionAsyncTask(agentPoliciesRenewalListMonthwiseGio.getPOLICYNUMBER());
                getProbableCommissionAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        });
        remarkRecyclerView.setAdapter(userPolicyListAdapter);
    }

    class GetProbableCommissionAsyncTask extends AsyncTask<String, String, String> {

        private volatile boolean running = true;
        private String errorString = "";
        private String CMS_COMM_AMT = "";
        private final String policyNumber;

        GetProbableCommissionAsyncTask(String policyNumber) {
            this.policyNumber = policyNumber;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(mContext, ProgressDialog.THEME_HOLO_LIGHT);
            String Message = "Loading Please wait...";
            mProgressDialog.setMessage(Html.fromHtml("<font color='#00a1e3'><b>" + Message + "<b></font>"));
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setCancelable(true);
            if (mProgressDialog != null && !mProgressDialog.isShowing()) {
                mProgressDialog.show();
            }

        }

        @SuppressLint("SimpleDateFormat")
        @Override
        protected String doInBackground(String... aurl) {
            try {

                running = true;
                String METHOD_NAME_PROBABLE_COMMISSION = "getProbableComm_smrt";
                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME_PROBABLE_COMMISSION);
                request.addProperty("strPolNo", policyNumber);


                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                envelope.dotNet = true;

                envelope.setOutputSoapObject(request);

                mCommonMethods.TLSv12Enable();

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);

                HttpTransportSE androidHttpTranport = new HttpTransportSE(URL);
                String SOAP_ACTION = "http://tempuri.org/" + METHOD_NAME_PROBABLE_COMMISSION;
                androidHttpTranport.call(SOAP_ACTION, envelope);

                Object response = envelope.getResponse();

                if (!response.toString().contentEquals("0")) {

                    SoapPrimitive sa = (SoapPrimitive) envelope.getResponse();

                    String inputpolicylist = sa.toString();

                    ParseXML prsObj = new ParseXML();

                    inputpolicylist = prsObj.parseXmlTag(
                            inputpolicylist, "Data");

                    errorString = new ParseXML().parseXmlTag(
                            inputpolicylist, "ScreenData");

                    if (errorString == null) {

                        String Node = prsObj.parseXmlTag(inputpolicylist, "Table");

                        CMS_COMM_AMT = prsObj.parseXmlTag(Node, "CMS_COMM_AMT");

                    }
                }


            } catch (Exception e) {
                mProgressDialog.dismiss();
                running = false;
            }
            return null;

        }


        @Override
        protected void onPostExecute(String unused) {

            if (mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }

            if (running) {

                if (errorString == null) {
                    mCommonMethods.showMessageDialog(mContext, "Probable Commission Amount "
                            + getString(R.string.Rs) + CMS_COMM_AMT);
                } else {
                    mCommonMethods.showMessageDialog(mContext, mCommonMethods.NO_RECORD_FOUND);
                }
            } else {
                mCommonMethods.showMessageDialog(mContext, mCommonMethods.NO_RECORD_FOUND);
            }
        }
    }


    public void getFundValueInterfaceMethod(List<String> Node, String policyNumber) {

        if (TextUtils.isEmpty(policyNumber)) {
            mCommonMethods.showMessageDialog(mContext, mCommonMethods.NO_RECORD_FOUND);
        } else {
            String result = "", fundValueStr = "";
            ParseXML parseXML = new ParseXML();
            List<ParseXML.XMLFundSwitchHolder> nodeData = parseXML.parseNodeElementFundSwitch(Node);
            for (ParseXML.XMLFundSwitchHolder node : nodeData) {
                System.out.println("sa.toString() = " + policyNumber);
                if (policyNumber.equalsIgnoreCase(node.getPOLICYNO())) {
                    fundValueStr = node.getFUNDVALUE();
                    System.out.println("sa.toString() = " + fundValueStr);
                    result = "Success";
                }
            }

            if (result.equalsIgnoreCase("Success")) {
                mCommonMethods.showMessageDialog(mContext, "Fund Value Is : " + fundValueStr);
            } else {
                mCommonMethods.showMessageDialog(mContext, mCommonMethods.NO_RECORD_FOUND);
            }
        }
    }

    void getPremiumInterfaceMethod(String premiumAmount, String result) {
        if (premiumAmount.equals("")) {
            mCommonMethods.showMessageDialog(mContext, result);
        } else {
            mCommonMethods.showMessageDialog(mContext, "Gross Premium Amount is - " + premiumAmount);
        }
    }

    void getSMSDetailsInterfaceMethod(String result) {
        if (result.equalsIgnoreCase("1")) {
            mCommonMethods.showMessageDialog(mContext, "Message sent successfully");
        } else {
            mCommonMethods.showMessageDialog(mContext, "Message sending failed");
        }
    }

    public void getUpdateAltMobResultMethod(String result) {
        if (result != null && result.equalsIgnoreCase("Success")) {
            mCommonMethods.showMessageDialog(mContext, "Mobile Number Updated Successfully");
        } else {
            mCommonMethods.showMessageDialog(mContext, "Mobile Number Not Updated.Please Try Again.");
        }
    }
}

