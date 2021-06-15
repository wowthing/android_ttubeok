package com.example.ttubeog;

import androidx.annotation.NonNull;
import androidx.annotation.UiThread;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.UiSettings;
import com.naver.maps.map.overlay.InfoWindow;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.util.FusedLocationSource;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    private BottomNavigationView mBottomNV;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
    private FusedLocationSource locationSource;
    private NaverMap naverMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //네이버 지도 설정
        FragmentManager fm = getSupportFragmentManager();
        MapFragment mapFragment = (MapFragment)fm.findFragmentById(R.id.map);
        if (mapFragment == null) {
            mapFragment = MapFragment.newInstance();
            fm.beginTransaction().add(R.id.map, mapFragment).commit();

            mapFragment.getMapAsync(this);
        }
        locationSource = new FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE);


        mBottomNV = findViewById(R.id.nav_view);
        mBottomNV.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() { //NavigationItemSelecte
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                BottomNavigate(menuItem.getItemId());

                return true;
            }
        });
        mBottomNV.setSelectedItemId(R.id.navigation_1);
    }

    private void BottomNavigate(int id) {  //BottomNavigation 페이지 변경
        String tag = String.valueOf(id);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        Fragment currentFragment = fragmentManager.getPrimaryNavigationFragment();
        if (currentFragment != null) {
            fragmentTransaction.hide(currentFragment);
        }

        Fragment fragment = fragmentManager.findFragmentByTag(tag);
        if (fragment == null) {
            if (id == R.id.navigation_1) {
                fragment = new BottomFragment1();

            } else if (id == R.id.navigation_2){

                fragment = new BottomFragment2();
            }else {
                fragment = new BottomFragment3();
            }

            fragmentTransaction.add(R.id.content_layout, fragment, tag);
        } else {
            fragmentTransaction.show(fragment);
        }

        fragmentTransaction.setPrimaryNavigationFragment(fragment);
        fragmentTransaction.setReorderingAllowed(true);
        fragmentTransaction.commitNow();
    }

    //위치 권한 설정
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (locationSource.onRequestPermissionsResult(
                requestCode, permissions, grantResults)) {
            if (!locationSource.isActivated()) { // 권한 거부됨
                naverMap.setLocationTrackingMode(LocationTrackingMode.None);
            }
            return;
        }
        super.onRequestPermissionsResult(
                requestCode, permissions, grantResults);
    }

    //지도 옵션 설정
    @UiThread
    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {

        //위치 추적
        this.naverMap = naverMap;
        naverMap.setLocationSource(locationSource);
        naverMap.setLocationTrackingMode(LocationTrackingMode.Follow);

        UiSettings uiSettings = naverMap.getUiSettings();
        uiSettings.setZoomControlEnabled(true); //줌
        uiSettings.setLocationButtonEnabled(true); //현 위치

        InfoWindow infoWindow = new InfoWindow();
        infoWindow.setAdapter(new InfoWindow.DefaultTextAdapter(this) {
            @NonNull
            @Override
            public CharSequence getText(@NonNull InfoWindow infoWindow) {
                // 정보 창이 열린 마커의 tag를 텍스트로 노출하도록 반환
                return (CharSequence)infoWindow.getMarker().getTag();
            }
        });

        naverMap.setOnMapClickListener((coord, point) -> {
            infoWindow.close();
        });

        //마커, 클릭이벤트 설정
        //창포원
        Marker marker1 = new Marker();
        marker1.setPosition(new LatLng(37.6895437, 127.0478874));
        marker1.setMap(naverMap);
        marker1.setTag("서울 창포원");
        marker1.setOnClickListener(overlay -> {
            infoWindow.open(marker1);
            Intent course_info = new Intent(MainActivity.this, information.class);
            String get_title = "course_1";
            course_info.putExtra("get_title", get_title);
            startActivity(course_info);
            return true;
        });

        //묵동천
        Marker marker2 = new Marker();
        marker2.setPosition(new LatLng(37.6183388, 127.0791978));
        marker2.setMap(naverMap);
        marker2.setTag("묵동천");
        marker2.setOnClickListener(overlay -> {
            infoWindow.open(marker2);
            Intent course_info = new Intent(MainActivity.this, information.class);
            String get_title = "course_2";
            course_info.putExtra("get_title", get_title);
            startActivity(course_info);
            return true;
        });

        //아차산 생태공원
        Marker marker3 = new Marker();
        marker3.setPosition(new LatLng(37.5516974, 127.1008531));
        marker3.setMap(naverMap);
        marker3.setTag("아차산 생태공원");
        marker3.setOnClickListener(overlay -> {
            infoWindow.open(marker3);
            Intent course_info = new Intent(MainActivity.this, information.class);
            String get_title = "course_3";
            course_info.putExtra("get_title", get_title);
            startActivity(course_info);
            return true;
        });

        //월드컵공원
        Marker marker4 = new Marker();
        marker4.setPosition(new LatLng(37.5718241, 126.87886));
        marker4.setMap(naverMap);
        marker4.setTag("월드컵 공원");
        marker4.setOnClickListener(overlay -> {
            infoWindow.open(marker4);
            Intent course_info = new Intent(MainActivity.this, information.class);
            String get_title = "course_4";
            course_info.putExtra("get_title", get_title);
            startActivity(course_info);
            return true;
        });

        //남산골 한옥마을
        Marker marker5 = new Marker();
        marker5.setPosition(new LatLng(37.5591288, 126.993686));
        marker5.setMap(naverMap);
        marker5.setTag("남산골 한옥마을");
        marker5.setOnClickListener(overlay -> {
            infoWindow.open(marker5);
            Intent course_info = new Intent(MainActivity.this, information.class);
            String get_title = "course_5";
            course_info.putExtra("get_title", get_title);
            startActivity(course_info);
            return true;
        });

        //어린이대공원
        Marker marker6 = new Marker();
        marker6.setPosition(new LatLng(37.5499072, 127.0808931));
        marker6.setMap(naverMap);
        marker6.setTag("어린이 대공원");
        marker6.setOnClickListener(overlay -> {
            infoWindow.open(marker6);
            Intent course_info = new Intent(MainActivity.this, information.class);
            String get_title = "course_6";
            course_info.putExtra("get_title", get_title);
            startActivity(course_info);
            return true;
        });

        //몽촌토성
        Marker marker7 = new Marker();
        marker7.setPosition(new LatLng(37.5224029, 127.119191));
        marker7.setMap(naverMap);
        marker7.setTag("몽촌토성");
        marker7.setOnClickListener(overlay -> {
            infoWindow.open(marker7);
            Intent course_info = new Intent(MainActivity.this, information.class);
            String get_title = "course_7";
            course_info.putExtra("get_title", get_title);
            startActivity(course_info);
            return true;
        });

        //개화산 무장애 숲길
        Marker marker8 = new Marker();
        marker8.setPosition(new LatLng(37.5818649, 126.8043313));
        marker8.setMap(naverMap);
        marker8.setTag("개화산 무장애 숲길");
        marker8.setOnClickListener(overlay -> {
            infoWindow.open(marker8);
            Intent course_info = new Intent(MainActivity.this, information.class);
            String get_title = "course_8";
            course_info.putExtra("get_title", get_title);
            startActivity(course_info);
            return true;
        });

        //고덕산 무장애 자락길
        Marker marker9 = new Marker();
        marker9.setPosition(new LatLng(37.5573765, 127.1582111));
        marker9.setMap(naverMap);
        marker9.setTag("고덕산 무장애 자락길");
        marker9.setOnClickListener(overlay -> {
            infoWindow.open(marker9);
            Intent course_info = new Intent(MainActivity.this, information.class);
            String get_title = "course_9";
            course_info.putExtra("get_title", get_title);
            startActivity(course_info);
            return true;
        });

        //관악산 무장애숲길
        Marker marker10 = new Marker();
        marker10.setPosition(new LatLng(37.4550837, 126.9471626));
        marker10.setMap(naverMap);
        marker10.setTag("관악산 무장애 숲길");
        marker10.setOnClickListener(overlay -> {
            infoWindow.open(marker10);
            Intent course_info = new Intent(MainActivity.this, information.class);
            String get_title = "course_10";
            course_info.putExtra("get_title", get_title);
            startActivity(course_info);
            return true;
        });

        //남산 순환 나들길
        Marker marker11 = new Marker();
        marker11.setPosition(new LatLng(37.5519464, 126.988092));
        marker11.setMap(naverMap);
        marker11.setTag("남산 순환 나들길");
        marker11.setOnClickListener(overlay -> {
            infoWindow.open(marker11);
            Intent course_info = new Intent(MainActivity.this, information.class);
            String get_title = "course_11";
            course_info.putExtra("get_title", get_title);
            startActivity(course_info);
            return true;
        });

        //호암 늘솔길
        Marker marker12 = new Marker();
        marker12.setPosition(new LatLng(37.4525509, 126.9255453));
        marker12.setMap(naverMap);
        marker12.setTag("호암 늘솔길");
        marker12.setOnClickListener(overlay -> {
            infoWindow.open(marker12);
            Intent course_info = new Intent(MainActivity.this, information.class);
            String get_title = "course_12";
            course_info.putExtra("get_title", get_title);
            startActivity(course_info);
            return true;
        });

        //도봉옛길
        Marker marker13 = new Marker();
        marker13.setPosition(new LatLng(37.6861487, 127.0361749));
        marker13.setMap(naverMap);
        marker13.setTag("도봉옛길");
        marker13.setOnClickListener(overlay -> {
            infoWindow.open(marker13);
            Intent course_info = new Intent(MainActivity.this, information.class);
            String get_title = "course_13";
            course_info.putExtra("get_title", get_title);
            startActivity(course_info);
            return true;
        });

        //서달산 자락길
        Marker marker14 = new Marker();
        marker14.setPosition(new LatLng(37.496521, 126.9616101));
        marker14.setMap(naverMap);
        marker14.setTag("서달산 자락길");
        marker14.setOnClickListener(overlay -> {
            infoWindow.open(marker14);
            Intent course_info = new Intent(MainActivity.this, information.class);
            String get_title = "course_14";
            course_info.putExtra("get_title", get_title);
            startActivity(course_info);
            return true;
        });

        //아차산 무장애숲길
        Marker marker15 = new Marker();
        marker15.setPosition(new LatLng(37.5528788, 127.0997696));
        marker15.setMap(naverMap);
        marker15.setTag("아차산 무장애 숲길");
        marker15.setOnClickListener(overlay -> {
            infoWindow.open(marker15);
            Intent course_info = new Intent(MainActivity.this, information.class);
            String get_title = "course_15";
            course_info.putExtra("get_title", get_title);
            startActivity(course_info);
            return true;
        });

        //북촌 한옥마을
        Marker marker16 = new Marker();
        marker16.setPosition(new LatLng(37.5813636, 126.9850572));
        marker16.setMap(naverMap);
        marker16.setTag("북촌 한옥마을");
        marker16.setOnClickListener(overlay -> {
            infoWindow.open(marker16);
            Intent course_info = new Intent(MainActivity.this, information.class);
            String get_title = "course_16";
            course_info.putExtra("get_title", get_title);
            startActivity(course_info);
            return true;
        });

        //인왕산 자락길
        Marker marker17 = new Marker();
        marker17.setPosition(new LatLng(37.5758544, 126.9688307));
        marker17.setMap(naverMap);
        marker17.setTag("인왕산 자락길");
        marker17.setOnClickListener(overlay -> {
            infoWindow.open(marker17);
            Intent course_info = new Intent(MainActivity.this, information.class);
            String get_title = "course_17";
            course_info.putExtra("get_title", get_title);
            startActivity(course_info);
            return true;
        });
    }
}