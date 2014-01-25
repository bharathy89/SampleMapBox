package com.example.samplemapbox;

import java.util.ArrayList;

import org.osmdroid.api.IMapController;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.OverlayItem.HotspotPlace;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;

import com.mapbox.mapboxsdk.MapView;
import com.mapbox.mapboxsdk.Marker;
import com.testflightapp.lib.TestFlight;

public class MainActivity extends Activity implements LocationListener {
	private IMapController mapController;
	private GeoPoint startingPoint = new GeoPoint(29.618544, -82.374051);
	private MapView mv;
	private Marker myMarker;
	private MyLocationNewOverlay myLocationOverlay;
	private String street = "bharathy89.h2gk484b";
	private LocationManager locManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		TestFlight.takeOff(getApplication(), "b1425515-299c-4aaf-b85e-b9a7c99b0fa5");
		
		setContentView(R.layout.activity_main);
		locManager = (LocationManager)getSystemService(LOCATION_SERVICE);
		mv = (MapView)findViewById(R.id.mapview);
		mv.setUseDataConnection(true);
		replaceMapView(street);

		//mv.setURL(terrain);
		mapController = mv.getController();
		mapController.setZoom(20);
		mapController.animateTo(startingPoint);
		
		ArrayList<OverlayItem> items = new ArrayList<OverlayItem>();
		myMarker = mv.addMarker(startingPoint.getLatitude(), startingPoint.getLongitude(), "text", "text");

		
		myMarker = new Marker(mv, "text", "text", startingPoint);
		//myMarker.setMarker(this.getResources().getDrawable(R.drawable.star));
		myMarker.setMarkerHotspot(HotspotPlace.CENTER);
		items.add(myMarker);

		Overlay overlay = new ItemizedOverlayWithFocus<OverlayItem>(this.getApplicationContext(), items,
				new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {

			@Override
			public boolean onItemSingleTapUp(final int index, final OverlayItem item) {
				AlertDialog ad = new AlertDialog.Builder(MainActivity.this)
				.setTitle(item.getTitle())
				.setMessage(item.getSnippet())
				.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) { 
						// continue with delete
					}
				})
				.setIcon(android.R.drawable.ic_dialog_alert)
				.show();
				return true; // We 'handled' this event.
			}

			@Override
			public boolean onItemLongPress(final int index, final OverlayItem item) {
				return false;
			}
		});

		mv.getOverlays().set(0,overlay);
		mv.invalidate();
		
	}
	
	public void onResume(){
		super.onResume();
		locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 400, 1, this);
	}
	
	public void onPause() {
		super.onPause();
		
	}
	
	protected void replaceMapView(String layer){
		mv.switchToLayer(layer);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		GeoPoint p = new GeoPoint((int) (((double) location.getLatitude() / 1E5) * 1E6),
				(int) (((double) location.getLongitude() / 1E5) * 1E6));
		myMarker.getPoint().setCoordsE6(p.getLatitudeE6(), p.getLongitudeE6());
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

}
