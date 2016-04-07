package com.appgestor.domidomi.mockedFragments;


import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.appgestor.domidomi.Activities.ActCreatePerfil;
import com.appgestor.domidomi.Adapters.AppAdapterPerfil;
import com.appgestor.domidomi.DataBase.DBHelper;
import com.appgestor.domidomi.Entities.Ciudades;
import com.appgestor.domidomi.Entities.Cliente;
import com.appgestor.domidomi.R;
import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class FragmentPeril extends Fragment {

    private DBHelper mydb;
    private SwipeMenuListView mListView;
    private FloatingActionButton fab;
    private AppAdapterPerfil mAdapter;
    private TextView  txtentryR;
    private List<Cliente> mAppList = new ArrayList<>();

    public FragmentPeril() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_peril, container, false);

        mListView = (SwipeMenuListView) view.findViewById(R.id.listView);

        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        txtentryR = (TextView) view.findViewById(R.id.txtentryR);

        mydb = new DBHelper(getActivity());

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);

        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                // create "open" item
                SwipeMenuItem openItem = new SwipeMenuItem(getActivity());
                // set item background
                openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9, 0xCE)));
                // set item width
                openItem.setWidth(dp2px(90));
                // set item title
                openItem.setTitle("Editar");
                // set item title fontsize
                openItem.setTitleSize(18);
                // set item title font color
                openItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(openItem);
                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(getActivity());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(dp2px(90));
                // set a icon
                //deleteItem.setIcon(R.drawable.ic_delete);
                deleteItem.setTitle("Eliminar");

                deleteItem.setTitleSize(18);
                // set item title font color
                deleteItem.setTitleColor(Color.WHITE);

                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };

        mListView.setMenuCreator(creator);

        // step 2. listener item click event
        mListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(final int position, SwipeMenu menu, int index) {
                //AddProductCar item = mAppList.get(position);
                switch (index) {
                    case 0:
                        // Edit
                        editarPerfil(position);
                        break;
                    case 1:
                        // delete
                        deletePerfil(position);
                        break;
                }
                return false;
            }
        });

        // test item long click
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //deletePrduct(position);
                return false;
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Click action
                Intent intent = new Intent(getActivity(), ActCreatePerfil.class);
                startActivity(intent);
            }
        });

        llenarDatosPerfil();
    }

    private void editarPerfil(int position) {
        Bundle bundle = new Bundle();
        Intent intent = new Intent(getActivity(), ActCreatePerfil.class);
        bundle.putSerializable("value", mAppList.get(position));
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void deletePerfil(final int position) {
        new MaterialDialog.Builder(getActivity())
                .title("Eliminar perfil")
                .content("¿Está seguro de eliminar el perfil?")
                .positiveText("Aceptar")
                .negativeText("Cancelar")
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        //Aceptar
                        if (!mydb.DeletePerfil(mAppList.get(position).getCodigo())) {
                            Toast.makeText(getActivity(), "Problemas al eliminar", Toast.LENGTH_SHORT).show();
                        } else {
                            mAppList.remove(position);
                            mAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        //Cancelar
                        dialog.dismiss();
                    }
                }).show();
    }

    private void llenarDatosPerfil() {
        mAppList = mydb.getUsuarioAll();
        if (mAppList.size() > 0){
            txtentryR.setVisibility(View.GONE);
            mAdapter = new AppAdapterPerfil(getActivity(), mAppList);
            mListView.setAdapter(mAdapter);
        } else {
            txtentryR.setVisibility(View.VISIBLE);
        }

    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_register, menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.addUser:
                Intent intent = new Intent(getActivity(), ActCreatePerfil.class);
                startActivity(intent);
                return true;
        }

        return false;
    }

    @Override
    public void onResume(){
        llenarDatosPerfil();
        super.onResume();
    }
}
