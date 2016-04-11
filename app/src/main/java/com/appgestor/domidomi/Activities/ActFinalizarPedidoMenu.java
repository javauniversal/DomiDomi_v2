package com.appgestor.domidomi.Activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.appgestor.domidomi.DataBase.DBHelper;
import com.appgestor.domidomi.Entities.AddProductCar;
import com.appgestor.domidomi.Entities.Ciudades;
import com.appgestor.domidomi.Entities.Cliente;
import com.appgestor.domidomi.Entities.MedioPago;
import com.appgestor.domidomi.Entities.PedidoWebCabeza;
import com.appgestor.domidomi.Mascara.MaskEditTextChangedListener;
import com.appgestor.domidomi.Mascara.PesoEditTextChangedListener;
import com.appgestor.domidomi.R;
import com.google.gson.Gson;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import dmax.dialog.SpotsDialog;

import static com.appgestor.domidomi.Entities.MedioPago.getMedioPagoListstatic;


public class ActFinalizarPedidoMenu extends AppCompatActivity implements View.OnClickListener {


    private Bundle bundle;
    private EditText edit_nombres;
    private EditText edit_cell;
    private EditText edit_phone;
    private EditText edit_a;
    private EditText edit_b;
    private EditText edit_c;
    private EditText editBarrioCliente;
    private EditText editDirReferencia;

    private EditText edit_ofi;
    private Spinner spinner_dir;
    private Spinner spinner_oficina;
    private Spinner spinner_ciudades;
    private Spinner spinner_zona;
    private String[] dir1_parant;
    private String[] ofic_parant;
    private String[] dir1Zona_parant;
    private String Calle_Carrera;
    private String oficina_spinn;
    private String selecte_ciudad;
    private List<Ciudades> ciudades;
    private RelativeLayout zonaLayout;
    private DBHelper mydb;
    private LinearLayout numeroLayout;

    public static final String TAG = "MyTag";
    private EditText editEfectivo;
    private PedidoWebCabeza objeto = new PedidoWebCabeza();
    List<Address> returnedaddresses;
    Geocoder geocoder;
    private Double latitud = 0.0;
    private Double longitud = 0.0;
    private RequestQueue rq;
    private String zona_dir;
    private String adressString;
    private LinearLayout input_layout_Efectivo_liner;
    private List<Cliente> mAppList = new ArrayList<>();
    private AlertDialog alertDialog;
    ArrayAdapter<Ciudades> ciudadesArrayAdapter;
    LinearLayout ll;
    private TextView total_pedido;
    private DecimalFormat format;
    private LinearLayout root;
    private Cliente publicCliente = new Cliente();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_finalizar_pedido);

        alertDialog = new SpotsDialog(this, R.style.Custom);

        mydb = new DBHelper(this);

        Intent intent = getIntent();
        bundle = intent.getExtras();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        geocoder = new Geocoder(this, Locale.ENGLISH);

        root = (LinearLayout) findViewById(R.id.llMediosPagos);

        editEfectivo = (EditText) findViewById(R.id.editEfectivo);
        PesoEditTextChangedListener maskValor = new PesoEditTextChangedListener("###,###,##0", editEfectivo);
        editEfectivo.addTextChangedListener(maskValor);

        zonaLayout = (RelativeLayout) findViewById(R.id.relativeLayout_zona);
        numeroLayout = (LinearLayout) findViewById(R.id.numeroLayout);

        input_layout_Efectivo_liner = (LinearLayout) findViewById(R.id.input_layout_Efectivo_liner);


        edit_ofi = (EditText) findViewById(R.id.edit_ofi);

        spinner_ciudades = (Spinner) findViewById(R.id.spinner_ciudad);
        spinner_dir = (Spinner) findViewById(R.id.spinner_dir);
        spinner_oficina = (Spinner) findViewById(R.id.spinner_ofi);
        spinner_zona = (Spinner) findViewById(R.id.spinner_zona);

        edit_nombres = (EditText) findViewById(R.id.edit_nombres);
        edit_nombres.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);

        edit_cell = (EditText) findViewById(R.id.edit_cell);
        edit_phone = (EditText) findViewById(R.id.edit_phone);
        edit_a = (EditText) findViewById(R.id.edit_a);
        edit_b = (EditText) findViewById(R.id.edit_b);
        edit_c = (EditText) findViewById(R.id.edit_c);
        editBarrioCliente = (EditText) findViewById(R.id.editBarrioCliente);
        editDirReferencia = (EditText) findViewById(R.id.editDirReferencia);
        total_pedido = (TextView) findViewById(R.id.total_pedido);

        Button myAceptar = (Button) findViewById(R.id.button_accept);
        myAceptar.setOnClickListener(this);

        Button myCancelar = (Button) findViewById(R.id.button_cancel);
        myCancelar.setOnClickListener(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        format = new DecimalFormat("#,###.##");
        sumarValoresFinales(mydb.getProductCar(bundle.getInt("empresa"), bundle.getInt("sede")));

        loadAdress();

        loadOficina();

        loadCiudades();

        CargarMedioPago();

        CargarDatosPerfil();

    }

    private void CargarDatosPerfil(){
        mAppList = mydb.getUsuarioAll();
        if (mAppList.size() > 0){
            if (mAppList.size() == 1){
                //Se implementa de inmediato
                publicCliente = mAppList.get(0);
                loadLlenarEditar(mAppList.get(0));

            } else {
                // Escoge el perfil
                LayoutInflater inflater = getLayoutInflater();
                View dialoglayout = inflater.inflate(R.layout.categoria_dialog_filter, null);
                ListView listview = (ListView) dialoglayout.findViewById(R.id.lv);
                listview.setAdapter(new BaseAdapter() {
                    @Override
                    public int getCount() {
                        if (mAppList == null) {
                            return 0;
                        } else {
                            return mAppList.size();
                        }
                    }

                    @Override
                    public Object getItem(int position) {
                        return mAppList.get(position);
                    }

                    @Override
                    public long getItemId(int position) {
                        return 0;
                    }

                    @Override
                    public View getView(final int position, View convertView, ViewGroup parent) {
                        ViewHolder holder;
                        if (convertView == null) {
                            holder = new ViewHolder();
                            convertView = View.inflate(ActFinalizarPedidoMenu.this, R.layout.item_categoria, null);
                            holder.tv = (TextView) convertView.findViewById(R.id.tv);
                            holder.tvd = (TextView) convertView.findViewById(R.id.tvd);
                            convertView.setTag(holder);
                        } else {
                            holder = (ViewHolder) convertView.getTag();
                        }

                        final Cliente cliente = mAppList.get(position);
                        /*holder.cb.setOnCheckedChangeListener(new SmoothCheckBox.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(SmoothCheckBox checkBox, boolean isChecked) {
                                cliente.isChecked = isChecked;

                            }
                        });*/
                        holder.tvd.setText(cliente.getNombre());
                        if (cliente.getOficina().equals("Ninguno")){
                            holder.tv.setText(String.format("%1s %2s # %3s - %4s", cliente.getCalle_carrera(), cliente.getDir_1(),
                                    cliente.getDir_2(), cliente.getDir_3()));

                        } else {
                            holder.tv.setText(String.format("%1s %2s # %3s - %4s %5s %6s", cliente.getCalle_carrera(), cliente.getDir_1(),
                                    cliente.getDir_2(), cliente.getDir_3(), cliente.getOficina(), cliente.getNum_oficina()));

                        }

                        //holder.cb.setChecked(cliente.isChecked);

                        return convertView;
                    }

                    class ViewHolder {
                        //SmoothCheckBox cb;
                        TextView tvd;
                        TextView tv;
                    }
                });

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setCancelable(false);
                builder.setTitle("Seleccionar perfil");
                builder.setView(dialoglayout);
                final AlertDialog ad = builder.show();

                listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Cliente clienteSelect = (Cliente) parent.getAdapter().getItem(position);
                        //clienteSelect.isChecked = !clienteSelect.isChecked;
                        //SmoothCheckBox checkBox = (SmoothCheckBox) view.findViewById(R.id.scb);
                        //checkBox.setChecked(clienteSelect.isChecked, true);
                        publicCliente = clienteSelect;
                        loadLlenarEditar(clienteSelect);
                        ad.dismiss();
                    }
                });

            }
        }
    }

    private void loadLlenarEditar(Cliente thumbs) {

        edit_nombres.setText(thumbs.getNombre());
        edit_cell.setText(thumbs.getCelular());
        edit_phone.setText(thumbs.getTelefono());

        List<String> strListAd = new ArrayList<>(Arrays.asList(dir1_parant));
        spinner_dir.setSelection(strListAd.indexOf(thumbs.getCalle_carrera()));

        edit_a.setText(thumbs.getDir_1());
        edit_b.setText(thumbs.getDir_2());
        edit_c.setText(thumbs.getDir_3());

        List<String> strListAdofic = new ArrayList<>(Arrays.asList(ofic_parant));
        spinner_oficina.setSelection(strListAdofic.indexOf(thumbs.getOficina()));

        edit_ofi.setText(thumbs.getNum_oficina() + "");

        selectValue(ciudades, thumbs.getCiudad(), spinner_ciudades);

        editBarrioCliente.setText(thumbs.getBarrio());
        editDirReferencia.setText(thumbs.getDirReferencia());

    }

    private void selectValue(List<Ciudades> ciudad, String value, Spinner spinner) {
        for (int i = 0; i < ciudad.size(); i++) {
            if (ciudad.get(i).getNombreCiudad().equals(value)) {
                spinner.setSelection(i);
                break;
            }
        }
    }

    private String limpiarMascara(EditText editText){
        String editLimpio = editText.getText().toString().replaceAll("[R$]", "").replaceAll("[,]", "").replaceAll("[.]", "");
        return editLimpio;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.button_accept:
                if (!validarCampos()){
                    resulFinalValidate();
                }
                break;

            case R.id.button_cancel:
                finish();
                break;
        }
    }

    private void resulFinalValidate() {

        if (isValidNumber(limpiarMascara(editEfectivo)))
            editEfectivo.setText("0");

        if (zonaLayout.getVisibility() == View.VISIBLE) {
            adressString = Calle_Carrera + " " + edit_a.getText().toString().trim() + " # " + edit_b.getText().toString().trim() + " - " + edit_c.getText().toString().trim() + " " + zona_dir + " " + selecte_ciudad;
        } else {
            adressString = Calle_Carrera + " " + edit_a.getText().toString().trim() + " # " + edit_b.getText().toString().trim() + " - " + edit_c.getText().toString().trim() + " " + selecte_ciudad;
        }

        alertDialog.show();
        loadAdressLatLon(adressString);
    }

    public void loadAdressLatLon(final String adressObten) {
        new AsyncTask<String[], Long, Long>() {
            @Override
            protected Long doInBackground(String[]... params) {
                try {

                    returnedaddresses = geocoder.getFromLocationName(adressObten, 1);

                    if (returnedaddresses.size() > 0) {
                        latitud = returnedaddresses.get(0).getLatitude();
                        longitud = returnedaddresses.get(0).getLongitude();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    alertDialog.dismiss();
                }
                return null;
            }

            protected void onPreExecute() {
            }

            @Override
            public void onProgressUpdate(Long... value) {
            }

            @Override
            protected void onPostExecute(Long result) {

                if (returnedaddresses != null) {
                    enviarPedido();
                }

            }

        }.execute();
    }

    public void enviarPedido() {

        String url = String.format("%1$s%2$s", getString(R.string.url_base), "pruebaJson");
        rq = Volley.newRequestQueue(this);

        StringRequest jsonRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response

                        if(mydb.DeleteProductAll(bundle.getInt("empresa"), bundle.getInt("sede"))){
                            alertDialog.dismiss();
                            Toast.makeText(ActFinalizarPedidoMenu.this, response, Toast.LENGTH_LONG).show();

                            startActivity(new Intent(ActFinalizarPedidoMenu.this, ActEstadoPedido.class));
                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                            finish();

                        } else {
                            alertDialog.dismiss();
                            Toast.makeText(ActFinalizarPedidoMenu.this, "Problemas con el pedido.", Toast.LENGTH_LONG).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        alertDialog.dismiss();
                        startActivity(new Intent(ActFinalizarPedidoMenu.this, DetailsActivity.class).putExtra("STATE", "ERROR"));
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

                if ( Build.VERSION.SDK_INT >= 23){
                    objeto.setImeiPhone(Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID));
                } else {
                    objeto.setImeiPhone(telephonyManager.getDeviceId());
                }

                objeto.setNombreUsuairo(edit_nombres.getText().toString());
                objeto.setCelularp(edit_cell.getText().toString());

                if (oficina_spinn.equals("Ninguno")) {
                    objeto.setDireccionp(adressString);
                } else {
                    objeto.setDireccionp(adressString+" "+oficina_spinn+" "+edit_ofi.getText().toString());
                }

                objeto.setDireccionReferen("Barrio: "+editBarrioCliente.getText().toString()+" "+editDirReferencia.getText().toString());

                //objeto.setMedioPago(masterItem.getIdmediopago());

                objeto.setValorPago(Double.valueOf(limpiarMascara(editEfectivo)));
                objeto.setLatitud(latitud);
                objeto.setLongitud(longitud);
                objeto.setIdEmpresaP(bundle.getInt("empresa"));
                objeto.setIdSedeP(bundle.getInt("sede"));

                List<AddProductCar> mAppList = mydb.getProductCar(bundle.getInt("empresa"), bundle.getInt("sede"));

                for (int i = 0; i < mAppList.size(); i++) {
                    mAppList.get(i).setAdicionesList(mydb.getAdiciones(mAppList.get(i).getIdProduct(), mAppList.get(i).getIdcompany(), mAppList.get(i).getIdsede()));
                }

                ArrayList<MedioPago> medioPagoList = new ArrayList<>();

                for (int i = 0; i < root.getChildCount(); i++) {
                    View child = root.getChildAt(i);
                    if (child instanceof CheckBox) {
                        CheckBox cb = (CheckBox) child;
                        int answer = cb.isChecked() ? 1 : 0;
                        if (answer == 1) {
                            MedioPago medioPago = new MedioPago();
                            medioPago.setDescripcion(cb.getText().toString());
                            medioPago.setIdmediopago(cb.getId());

                            medioPagoList.add(medioPago);
                        }
                    }
                }

                objeto.setMediosPagosList(medioPagoList);

                objeto.setProducto(mAppList);
                String parJSON = new Gson().toJson(objeto, PedidoWebCabeza.class);

                params.put("pedido", parJSON);

                return params;
            }
        };
        rq.add(jsonRequest);
    }

    public boolean validarCampos() {

        boolean indicadorValidate = false;

        if (isValidNumber(edit_nombres.getText().toString().trim())){
            edit_nombres.setFocusable(true);
            edit_nombres.setFocusableInTouchMode(true);
            edit_nombres.requestFocus();
            edit_nombres.setText("");
            edit_nombres.setError("Este campo es obligatorio");
            indicadorValidate = true;
        } else if(isValidNumberCell(edit_cell.getText().toString().trim(), edit_phone.getText().toString().trim())) {
            edit_cell.setFocusable(true);
            edit_cell.setFocusableInTouchMode(true);
            edit_cell.requestFocus();
            edit_cell.setText("");
            edit_cell.setError("Este campo es obligatorio");
            indicadorValidate = true;
        } else if (isValidNumber(edit_a.getText().toString().trim())) {
            edit_a.setFocusable(true);
            edit_a.setFocusableInTouchMode(true);
            edit_a.requestFocus();
            edit_a.setText("");
            edit_a.setError("Este campo es obligatorio");
            indicadorValidate = true;
        } else if (isValidNumber(edit_b.getText().toString().trim())) {
            edit_b.setFocusable(true);
            edit_b.setFocusableInTouchMode(true);
            edit_b.requestFocus();
            edit_b.setText("");
            edit_b.setError("Este campo es obligatorio");
            indicadorValidate = true;
        } else if (isValidNumber(edit_c.getText().toString().trim())) {
            edit_c.setFocusable(true);
            edit_c.setFocusableInTouchMode(true);
            edit_c.requestFocus();
            edit_c.setText("");
            edit_c.setError("Este campo es obligatorio");
            indicadorValidate = true;
        } else if (isValidNumberOficina(edit_ofi.getText().toString().trim())) {
            edit_ofi.setFocusable(true);
            edit_ofi.setFocusableInTouchMode(true);
            edit_ofi.requestFocus();
            edit_ofi.setText("");
            edit_ofi.setError("Este campo es obligatorio");
            indicadorValidate = true;
        } else if (isValidNumber(editBarrioCliente.getText().toString().trim())){
            editBarrioCliente.setFocusable(true);
            editBarrioCliente.setFocusableInTouchMode(true);
            editBarrioCliente.requestFocus();
            editBarrioCliente.setText("");
            editBarrioCliente.setError("Este campo es obligatorio");
            indicadorValidate = true;
        } else if (validateCheckBox()) {
            for(int i = 0; i < root.getChildCount(); i++) {
                View child = root.getChildAt(i);
                if (child instanceof CheckBox) {
                    CheckBox cb = (CheckBox) child;
                    cb.setError("Marque un medio de pago");
                }
            }
            indicadorValidate = true;
        } else if (input_layout_Efectivo_liner.getVisibility() == View.VISIBLE && isValidNumber(limpiarMascara(editEfectivo))) {
            editEfectivo.setFocusable(true);
            editEfectivo.setFocusableInTouchMode(true);
            editEfectivo.requestFocus();
            editEfectivo.setText("");
            editEfectivo.setError("Por favor digite la cantidad con la que va apagar");
            indicadorValidate = true;
        } else if (input_layout_Efectivo_liner.getVisibility() == View.VISIBLE && validateCantidadValor()) {
            editEfectivo.setFocusable(true);
            editEfectivo.setFocusableInTouchMode(true);
            editEfectivo.requestFocus();
            editEfectivo.setText("");
            editEfectivo.setError("La cantidad con la que paga no supera el monto total del pedido");
            indicadorValidate = true;
        }

        return indicadorValidate;
    }

    private boolean validateCheckBox() {

        for(int i = 0; i < root.getChildCount(); i++) {
            View child = root.getChildAt(i);
            if (child instanceof CheckBox) {
                CheckBox cb = (CheckBox) child;
                int answer = cb.isChecked() ? 1 : 0;
                if (answer == 1){
                    return false;
                }
                //cb.setError("Marque un medio de pago");
            }
        }

        return true;
    }

    private boolean validateCantidadValor() {
        int contador = 0;
        boolean bandera = false;
        for (int i = 0; i < root.getChildCount(); i++) {
            View child = root.getChildAt(i);
            if (child instanceof CheckBox) {
                CheckBox cb = (CheckBox) child;
                int answer = cb.isChecked() ? 1 : 0;
                if (answer == 1) {
                    contador++;
                }
            }
        }

        if (contador == 1){
            List<AddProductCar> mAppList = mydb.getProductCar(bundle.getInt("empresa"), bundle.getInt("sede"));
            double valorFinalPedido = 0;
            for (int e = 0; e < mAppList.size(); e++) {
                valorFinalPedido = valorFinalPedido + mAppList.get(e).getValueoverall();
            }

            valorFinalPedido = valorFinalPedido + bundle.getDouble("cosEnvio");
            bandera = Double.parseDouble(limpiarMascara(editEfectivo)) < valorFinalPedido;
        }

        return bandera;
    }

    private boolean isValidNumberOficina(String trim) {
        return numeroLayout.getVisibility() == View.VISIBLE && isValidNumber(trim);
    }

    private boolean isValidNumberCell(String dataCell, String dataPhone){
        if (dataCell == null || dataCell.length() == 0){
            if (dataPhone == null || dataPhone.length() == 0){
                return true;
            }
        }

        return false;
    }

    private boolean isValidNumber(String number){return number == null || number.length() == 0;}

    private void CargarMedioPago() {

        if (getMedioPagoListstatic() != null){

            ll = (LinearLayout) findViewById(R.id.llMediosPagos);

            int mediosPAgos = getMedioPagoListstatic().size();
            for (int i = 0; i < mediosPAgos; i++) {
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(0, 70, 0, 0);
                CheckBox cb = new CheckBox(this);
                cb.setText(getMedioPagoListstatic().get(i).getDescripcion());
                cb.setId(getMedioPagoListstatic().get(i).getIdmediopago());
                if (ll != null) {
                    ll.addView(cb);
                }

                cb.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        for (int i = 0; i < root.getChildCount(); i++) {
                            View child = root.getChildAt(i);
                            if (child instanceof CheckBox) {
                                CheckBox cb = (CheckBox) child;
                                cb.setError(null);
                            }
                        }

                        if (((CheckBox) v).isChecked()) {
                            if (((CheckBox) v).getText().equals("Efectivo") || ((CheckBox) v).getText().equals("efectivo")) {
                                input_layout_Efectivo_liner.setVisibility(View.VISIBLE);
                            }
                        } else {
                            if (((CheckBox) v).getText().equals("Efectivo") || ((CheckBox) v).getText().equals("efectivo")) {
                                input_layout_Efectivo_liner.setVisibility(View.GONE);
                                editEfectivo.setText("0");
                            }
                        }
                    }
                });
            }

        }
    }

    private void sumarValoresFinales(List<AddProductCar> data){

        if(data.size() > 0){
            double dValor = 0;

            for (int i = 0; i < data.size(); i++) {
                dValor = dValor + data.get(i).getValueoverall();
            }

            dValor = dValor + bundle.getDouble("cosEnvio");

            total_pedido.setText(String.format("Total a Pagar: $ %s", format.format(dValor)));

        }else {
            total_pedido.setText(String.format("Total a Pagar: $ %s", 0));
        }
    }

    private void loadAdress() {

        dir1_parant = new String[]{"Avenida", "Avenida Calle", "Avenida Carrera", "Calle", "Carrera", "Circular", "Circunvalar",
                "Diagonal", "Manzana", "Transversal", "Vía"};

        ArrayAdapter<String> prec1 = new ArrayAdapter<>(this, R.layout.textview_spinner, dir1_parant);
        spinner_dir.setAdapter(prec1);
        spinner_dir.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Calle_Carrera = parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }

    private void loadOficina() {
        ofic_parant = new String[]{"Apartamento", "Bodega", "Casa", "Local", "Ninguno", "Oficina"};

        ArrayAdapter<String> oficina = new ArrayAdapter<>(this, R.layout.textview_spinner, ofic_parant);
        spinner_oficina.setAdapter(oficina);
        spinner_oficina.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                oficina_spinn = parent.getItemAtPosition(position).toString();

                if (oficina_spinn.equals("Ninguno")) {
                    numeroLayout.setVisibility(View.GONE);
                    edit_ofi.setText("");
                    edit_ofi.setError(null);
                } else {
                    edit_ofi.setError(null);
                    numeroLayout.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void loadCiudades() {
        ciudades = new ArrayList<>();
        ciudades.add(new Ciudades(1, "Barranquilla", 1));
        ciudades.add(new Ciudades(2, "Bogotá", 1));
        ciudades.add(new Ciudades(3, "Bucaramanga", 1));
        ciudades.add(new Ciudades(4, "Cali", 1));
        ciudades.add(new Ciudades(5, "Cartagena", 1));
        ciudades.add(new Ciudades(6, "Cúcuta", 1));
        ciudades.add(new Ciudades(7, "Manizales", 1));
        ciudades.add(new Ciudades(8, "Medellín", 1));
        ciudades.add(new Ciudades(9, "Montería", 1));
        ciudades.add(new Ciudades(10, "Neiva", 1));
        ciudades.add(new Ciudades(11, "Pasto", 1));
        ciudades.add(new Ciudades(12, "Pereira", 1));
        ciudades.add(new Ciudades(13, "Santa Marta", 1));
        ciudades.add(new Ciudades(14, "Valledupar", 1));

        ArrayAdapter<Ciudades> prec3 = new ArrayAdapter<>(this, R.layout.textview_spinner, ciudades);
        spinner_ciudades.setAdapter(prec3);
        spinner_ciudades.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selecte_ciudad = parent.getItemAtPosition(position).toString();

                if (selecte_ciudad.equals("Medellín")) {
                    zonaLayout.setVisibility(View.VISIBLE);
                    loadLlenarZona();
                } else {
                    zonaLayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }

        });
    }

    private void loadLlenarZona() {
        dir1Zona_parant = new String[]{"Bello", "Caldas", "Envigado",  "Itaguí", "La Estrella", "Medellín", "Sabaneta"};
        ArrayAdapter<String> prec1 = new ArrayAdapter<>(this, R.layout.textview_spinner, dir1Zona_parant);
        spinner_zona.setAdapter(prec1);
        List<String> strListZona = new ArrayList<>(Arrays.asList(dir1Zona_parant));
        spinner_zona.setSelection(strListZona.indexOf(publicCliente.getZona()));
        spinner_zona.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                zona_dir = parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }

        });

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (rq != null) {
            rq.cancelAll(TAG);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (rq != null) {
            rq.cancelAll(TAG);
        }
    }

    @Override
    public void onStart() {
        super.onStart();

    }

}
