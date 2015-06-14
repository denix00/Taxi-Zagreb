package hr.tvz.taxizagreb;
import java.io.Serializable;

import android.app.Activity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapViewActivity extends Activity implements Serializable{

	/**
	 * Deklaracija Google mape
	 */
  private GoogleMap map;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.map);
    
    /**
     * Dohva�anje podataka koji su proslije�eni pri pokretanju intenta iz MainActivity klase
     */
	Bundle extra = this.getIntent().getExtras();
	double polazisteLat = extra.getDouble("polazisteLat");
	double polazisteLng = extra.getDouble("polazisteLng");
	double odredisteLat = extra.getDouble("odredisteLat");
	double odredisteLng = extra.getDouble("odredisteLng");
	
	
	/**
	 * Kreiranje LatLng objekata koji �e se koristiti za pozicioniranje markera na Google kartu
	 */
	LatLng polazisteLatLng = new LatLng(polazisteLat, polazisteLng);
	LatLng odredisteLatLng = new LatLng(odredisteLat, odredisteLng);
    
    /**
     * Inicijalizacija Google karte
     */
    map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
    
    /**
     * Postavljanje pozicije markera, njegov naziv i tekst koji se prika�e kada se klikne na njega,
     * te dodjeljivanje ikone koja �e predstavljati marker.
     */
    Marker polazisteMarker = map.addMarker(new MarkerOptions()
    		.position(polazisteLatLng)
    		.title("Start point")
    		.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_pol)));
    
    Marker odredisteMarker = map.addMarker(new MarkerOptions()
    		.position(odredisteLatLng)
    		.title("End point")
    		.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_odred)));
    
   /**
    * Centriranje ekrana na polazi�nu to�ku.
    */
    map.moveCamera(CameraUpdateFactory.newLatLng(polazisteLatLng));

    /**
     * Zumiranje kamere na 12. razinu koje �e trajati 2000 ms
     */
    map.animateCamera(CameraUpdateFactory.zoomTo(12), 2000, null);
    
    
  }
} 