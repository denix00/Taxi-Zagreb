package hr.tvz.taxizagreb;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;

import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
 
public class MainActivity extends FragmentActivity {
 	
	double polazisteLat;
	double polazisteLng;
	double odredisteLat;
	double odredisteLng;
	
	/**
	 * CollectionPagerAdapter je derivat android.support.v4.app.FragmentStatePagerAdaptera koji omogu�ava stvaranje 
	 * i uni�tavanje fragmenata po potrebi. Koristi se zbog toga �to pri navigaciji izme�u velikih fragmenata, odnosno,
	 * skupina fragmenata mo�e do�i do zatrpavanja memorije ure�aja.
	 */
    CollectionPagerAdapter mCollectionPagerAdapter;
 
    /**
     * ViewPager �e prikazivati fragmente
     */
    ViewPager mViewPager;
 
    
    /**
     * Standardna metoda koja se pokre�e sa pokretanjem aplikacije za inicijalizaciju aplikacije.
     */
    @Override
	public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
 
    /**
     * Kreiranje adaptera koji po potrebi vra�a fragment koji predstavlja objekt iz kolekcije.
     */
    mCollectionPagerAdapter = new CollectionPagerAdapter(
        getSupportFragmentManager());
 
    /**
     * Postavljanje glavne trake aplikacije.
     */
    final ActionBar actionBar = getActionBar();
    
 
    /**
     * Za ovu relativno jednostavnu aplikaciju, tipka za povratak na prethodnu razinu u 
     * glavnoj traci aplikacije nije potrebna, pa se ovom naredbom onemogu�uje.
     *   
     */
    actionBar.setDisplayHomeAsUpEnabled(false);
 
    
    /**
     * Inicijalizacija ViewPagera i spajanje adaptera na njega
     */
    mViewPager = (ViewPager) findViewById(R.id.pager);
    mViewPager.setAdapter(mCollectionPagerAdapter);
    
    }
 
    /**
     * Klasa CollectionPagerAdaptera naslje�uje FragmentStatePagerAdapter koji predstavlja
     * fragment objekta kolekcije.
     */
    public class CollectionPagerAdapter extends FragmentStatePagerAdapter {
 
	/**
	 * Broj prozora koji se nalaze u aplikaciji, odnosno, broj ekrana.
	 */
    final int NUM_ITEMS = 3;
 
    public CollectionPagerAdapter(FragmentManager fm) {
        super(fm);
    }
 
    /**
     * Vra�a odre�eni fragment
     */
    @Override
    public Fragment getItem(int i) {
        Fragment fragment = new TabFragment();
        Bundle args = new Bundle();
        args.putInt(TabFragment.ARG_OBJECT, i);
        fragment.setArguments(args);
        return fragment;
    }
 
    @Override
    public int getCount() {
        return NUM_ITEMS;
    }
  
    /**
     * Metoda vra�a naziv prozora ovisno o njegovom redoslijedu u aplikaciji
     */
    @Override
    public CharSequence getPageTitle(int position) {
 
        String tabLabel = null;
        switch (position) {
        case 0:
        tabLabel = getString(R.string.label1);
        break;
        case 1:
        tabLabel = getString(R.string.label2);
        break;
        case 2:
        tabLabel = getString(R.string.label3);
        break;
        }
 
        return tabLabel;
    }
    }
 
    /**
     * Klasa koja postavlja izglede svih ekrana aplikacije, odnosno, ovisno koji je odabran.
     */
    public static class TabFragment extends Fragment {
 
    public static final String ARG_OBJECT = "object";
 
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
 
        Bundle args = getArguments();
        int position = args.getInt(ARG_OBJECT);
 
        int tabLayout = 0;
        switch (position) {
        case 0:
        tabLayout = R.layout.tab1;
        break;
        case 1:
        tabLayout = R.layout.tab2;
        break;
        case 2:
        tabLayout = R.layout.tab3;
        break;
        }
 
        View rootView = inflater.inflate(tabLayout, container, false);
 
        return rootView;
    }
    }
    
    /**
     * Metoda koja se aktivira na pritisak bilo kojeg gumba (zajedni�ka je svima). Uz pomo� switcha
     * preko id-ieva gumba se prepoznaje koji gumb je pritisnut te se izvr�avaju naredbe navedene kod
     * pojedinog casea.
     * 
     * @param v
     */
    
    public void clickHandler(View v) 
	{
    	
		switch (v.getId()) {
		
			/**
			 * Iz integer resurs datoteke izvla�i se broj telefona te se proslije�uje kao argument
			 * metodi za pozivanje 
			 */
		case R.id.btnZoviCammeo:
	    	Resources resCam = getResources();
	    	int brojCam = resCam.getInteger(R.integer.telBrojCammeo);
	   
			call(brojCam);
			break;
			
			/**
			 * Iz integer resurs datoteke izvla�i se broj telefona te se proslije�uje kao argument
			 * metodi za pozivanje 
			 */
		case R.id.btnZoviRadioTaxi:
	    	Resources resRad = getResources();
	    	int brojRad = resRad.getInteger(R.integer.telBrojRadioTaxi);
	   
			call(brojRad);
			break;
			
			/**
			 * Iz integer resurs datoteke izvla�i se broj telefona te se proslije�uje kao argument
			 * metodi za pozivanje 
			 */
		case R.id.btnZoviEkoTaxi:
	    	Resources resEko = getResources();
	    	int brojEko = resEko.getInteger(R.integer.telBrojEkoTaxi);
	   
			call(brojEko);
			break;
			
		case R.id.btnCalcRoute:
			
			/**
			 * Metoda provjerava da li je dostupna internetska veza te ako je, izvr�avavaju se naredbe u bloku nakon uvjeta.
			 */
			if(isNetworkAvailable())
			{
			
					EditText polazisteET = (EditText)findViewById(R.id.txtPolaziste);
					EditText odredisteET = (EditText)findViewById(R.id.txtOdrediste);
					
					//***TESTIRANJE*** Automatski unos polazi�ne i odredi�ne adrese
				//	polazisteET.setText("jarunska ulica 2, zagreb");
				//	odredisteET.setText("ulica grada vukovara 33, zagreb");
					//**************TESTIRANJE**************
					
					/**
					 * Provjera da li postoji unos u poljima za polaziste i odrediste, ako ne, prikazuje se poruka upozorenja
					 */
					if(polazisteET.getText().toString().isEmpty() || odredisteET.getText().toString().isEmpty())
					{
						//Toast.makeText(this, "Please enter the starting and the destination points", Toast.LENGTH_SHORT).show();
						Toast.makeText(this, R.string.toastPrazneAdrese, Toast.LENGTH_SHORT).show();
						break;
					}
					
					/**
					 * provjera da li se na kraju naziva ulica nalazi "zagreb" kako bi se ogranicila pretraga na zagreb, ako ne, tada se dodaje ", zagreb" na kraj unosa
					 * potom se polje za unos osvje�ava.
					 */
					 
					
					/**
					 * Medoda chechStreetName provjerava unose u polja za polazi�nu i odredi�nu adresu.
					 */
					checkStreetName(polazisteET);
					checkStreetName(odredisteET);
					
								
					/**
					 * Deklaracija Geocoder objekta i lista adresa.
					 */
					Geocoder geocoder = new Geocoder(this);
					List<Address> addressPolList = null;
					List<Address> addressOdredList = null;
				
				
					/**
					 * Try blok za polazi�te.  getFromLocationName vra�a listu adresa. U ovom slu�aju, ograni�en je da vrati samo jednu.
					 */
					try
					{
						String strPolaziste = polazisteET.getText().toString();
						addressPolList = geocoder.getFromLocationName(strPolaziste, 1);
						
						/**
						 * Iz liste adresa izvla�i prvu, provjerava da li ima geografsku �irinu i visinu te globalnim varijablama za 
						 * dodjeljuje te vrijednosti
						 */
						Address addressPol = addressPolList.get(0);
						
						if(addressPol.hasLatitude() && addressPol.hasLongitude())
						{
							polazisteLat = addressPol.getLatitude();
							polazisteLng = addressPol.getLongitude();
							
							//****TEST**** U textviewu ispod Calculate Route buttona ispisuje se polazi�na geografska �irina i visina
							//TextView txt1 = (TextView)findViewById(R.id.textView1);
							//txt1.setText(polazisteLat + " " + polazisteLng);
						}
						
					}catch (IOException e)
					{
						Toast.makeText(this, R.string.toastLocationNotFound, Toast.LENGTH_SHORT).show();
						e.printStackTrace();
					}


					/**
					 * Try blok za odredi�te.  getFromLocationName vra�a listu adresa. U ovom slu�aju, ograni�en je da vrati samo jednu.
					 */
					try
					{
						String strOdrediste = odredisteET.getText().toString();
						addressOdredList = geocoder.getFromLocationName(strOdrediste, 1);
						
						/**
						 * Iz liste adresa izvla�i prvu, provjerava da li ima geografsku �irinu i visinu te globalnim varijablama za 
						 * dodjeljuje te vrijednosti
						 */
						Address addressOdred = addressOdredList.get(0);
						
						if(addressOdred.hasLatitude() && addressOdred.hasLongitude())
						{
							odredisteLat = addressOdred.getLatitude();
							odredisteLng = addressOdred.getLongitude();
							
							//****TEST**** U textviewu ispod Calculate Route buttona ispisuje se odredi�na geografska �irina i visina
							//TextView txt2 = (TextView)findViewById(R.id.textView2);
							//txt2.setText(odredisteLat + " " + odredisteLng);
						}
						
					}catch (IOException e)
					{
						Toast.makeText(this, R.string.toastLocationNotFound, Toast.LENGTH_SHORT).show();
						e.printStackTrace();
					}

					
					/**
					 * Nakon �to su dobivene geografske �irine i visine koji slu�e za smje�tanje markera na Google kartu i ra�unanje
					 * razdaljine izme�u to�aka, omogu�ava se gumb koji otvara Google kartu kako bi je korisnik mogao otvoriti
					 */
					Button btnMap = (Button)findViewById(R.id.btnKarta);
					btnMap.setEnabled(true);
					
					
					/**
					 * Poziv metode za izra�un razdaljine izme�u goegrafskih to�aka polazi�ta i odredi�ta
					 */
					
					double distancaBroj = distanceCalculation(polazisteLat, polazisteLng, odredisteLat, odredisteLng);
					
					//****TEST**** U textviewu ispod Calculate Route buttona ispisuje se razdaljina izme�u geografskih to�aka
					//TextView distanca = (TextView)findViewById(R.id.textView3);
					//distanca.setText(String.valueOf(distancaBroj));
					
					
					/**
					 * Poziv metode za izra�un cijena i prikazbu njih i razdaljine
					 */
					showPricesAndDistance(distancaBroj);
					
					/**
					 * Obavijest korisniku da je ruta izra�unata
					 */
					Toast.makeText(this, R.string.toastRouteCalc, Toast.LENGTH_SHORT).show();
				}
				
				else
				{
					Toast.makeText(this, R.string.toastConnectInternet, Toast.LENGTH_LONG).show();
				}
			
				
			break;
			
			/**
			 * Klikom na gumb za prikaz karte, stvara se novi intent, u njega se dodaju informacije potrebne za 
			 * postavljanje markera na Google kartu koje �e biti proslije�ene aktivnosti koja sadr�i Google kartu,
			 * te se na kraju ta aktivnost pokre�e.
			 */
		case R.id.btnKarta:
			Intent i = new Intent(getBaseContext(),MapViewActivity.class);
			i.putExtra("polazisteLat", polazisteLat);
			i.putExtra("polazisteLng", polazisteLng);
			i.putExtra("odredisteLat", odredisteLat);
			i.putExtra("odredisteLng", odredisteLng);
			startActivity(i);
			break;

			/**
			 * Klikom na gumb Reset Fields prazne se polja za unos
			 */
		case R.id.btnResetFields:
			EditText polazisteET = (EditText)findViewById(R.id.txtPolaziste);
			polazisteET.setText("");
			EditText odredisteET = (EditText)findViewById(R.id.txtOdrediste);
			odredisteET.setText("");
			break;
		}
		
	}
    
    /**
     * Metoda za pozivanje brojeva telefona. 
     * @param phone
     */
    public void call(int phone)
    {
    	/** Stvaranje intenta koji pokrece aktivnost ACTION_CALL */
        Intent intent = new Intent("android.intent.action.CALL");

        /** Stvaranje uri objekta u koji se sprema telefonski broj */
        Uri telBroj = Uri.parse("tel:" + phone );

        /** Postavljanje stvorenog uri objekta u intent */
        intent.setData(telBroj);

        /**Pokretanje aktivnosti za zvanje */
        startActivity(intent);
    }
    

    /**
     * Metoda provjerava da li u nazivu ulice postoji rije� Zagreb kako bi se ograni�ili rezultati pretrage, te ukoliko
     * ne postoji, tada se "zagreb" dodaje na kraj.
     * String naziva ulice se prije provjere pretvara u mala slova kako bi se pojednostavila provjera.
     * 
     * Nakon svega se osvje�ava polje s novim stringom
     * 
     * @param ulicaET
     */
    public void checkStreetName(EditText ulicaET)
    {
    	String ulica = ulicaET.getText().toString();
    	ulica = ulica.toLowerCase(Locale.getDefault());
    	
    	if( ! ulica.contains("zagreb"))
    	{
    		ulica = ulica.concat(", zagreb");
    	}
    	
    	ulicaET.setText(ulica);
    }
    
    /**
     * Metoda koja ra�una razdaljinu izme�u goeografskih to�aka, odnosno ulica, pomo�u Haversinove formule i vra�a rezultat.
     * 
     * @param StartP
     * @param EndP
     * @return
     */
    public double distanceCalculation(double polLat, double polLng, double odredLat, double odredLng) {
        int Radius=6371;//radius zemlje u kilometrima         
        
        double distanceLat = Math.toRadians(odredLat-polLat);
        double distanceLon = Math.toRadians(odredLng-polLng);
        double a = Math.sin(distanceLat/2) * Math.sin(distanceLat/2) +
        Math.cos(Math.toRadians(polLat)) * Math.cos(Math.toRadians(odredLat)) *
        Math.sin(distanceLon/2) * Math.sin(distanceLon/2);
        double c = 2 * Math.asin(Math.sqrt(a));

        return Radius * c;
     }
    
    /**
     * Metoda koja provjerava da li ure�aj ima dostupnu internetsku vezu, odnosno, da li je spojen na internet. 
     * Ako ima, vra�a 1.
     * 
     * @return
     */
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    
    /**
     * Metoda koja na osnovu razdaljine lokacija ra�una cijenu usluge za pojedine taxi prijevoznike koju prikazuje u formatu ##0.00 
     * gdje # ozna�ava da se znamenka mo�e prikazati ili ne, a 0 da ako nema znamenke, da se poka�e 0.
     * Na kraju se ra�una i pribli�no trajanje putovanja.
     * 
     * @param distanca
     */
    void showPricesAndDistance(double distanca)
    {
    	Resources res = getResources();
    	DecimalFormat decFormat = new DecimalFormat("##0.00"); 
    	
    	TextView cammeoCijenaTxt = (TextView)findViewById(R.id.txtCijenaCammeo);
    	double cammeoStart = Double.parseDouble(res.getString(R.string.cammeoStart));
    	double cammeoFree = Double.parseDouble(res.getString(R.string.cammeoFree));
    	double cammeoKm = Double.parseDouble(res.getString(R.string.cammeoKilometar));
    	
    	double cammeoCijenaUk = cammeoStart + (distanca > cammeoFree ? cammeoKm * (distanca - cammeoFree) : 0 ); 	
    	cammeoCijenaTxt.setText("Price: " + decFormat.format(cammeoCijenaUk) + " HRK");
    	
    	
    	
    	TextView ekoCijenaTxt = (TextView)findViewById(R.id.txtCijenaEkoTaxi);
    	double ekoStart = Double.parseDouble(res.getString(R.string.ekoTaxiStart));
    	double ekoFree = Double.parseDouble(res.getString(R.string.ekoTaxiFree));
    	double ekoKm = Double.parseDouble(res.getString(R.string.ekoTaxiKilometar));
    	
    	double ekoCijenaUk = ekoStart + (distanca > ekoFree ? ekoKm * (distanca - ekoFree) : 0 ); 	
    	ekoCijenaTxt.setText("Price: " + decFormat.format(ekoCijenaUk) + " HRK");
    	
  
    	
    	TextView radioCijenaTxt = (TextView)findViewById(R.id.txtCijenaRadioTaxi);
    	double radioStart = Double.parseDouble(res.getString(R.string.radioTaxiStart));
    	double radioFree = Double.parseDouble(res.getString(R.string.radioTaxiFree));
    	double radioKm = Double.parseDouble(res.getString(R.string.radioTaxiKilometar));
    	
    	double radioCijenaUk = radioStart + (distanca > radioFree ? radioKm * (distanca - radioFree) : 0 ); 	
    	radioCijenaTxt.setText("Price: " + decFormat.format(radioCijenaUk) + " HRK");
    	
    	
    	TextView txtPribUdaljenost = (TextView)findViewById(R.id.txtPribUdaljenost);
    	txtPribUdaljenost.setText("Distance: " + decFormat.format(distanca) + " km"); 
    	
    	TextView txtTrajanjePuta = (TextView)findViewById(R.id.txtTrajanjePutovanja);
    	int trajanjePutaPoKm = res.getInteger(R.integer.trajanjePoKm);
    	txtTrajanjePuta.setText("Travel time: " + (int)(distanca * trajanjePutaPoKm) + " min");
    	
    }
}

