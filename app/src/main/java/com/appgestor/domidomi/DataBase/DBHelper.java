package com.appgestor.domidomi.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.appgestor.domidomi.Entities.AddProductCar;
import com.appgestor.domidomi.Entities.Cliente;
import com.appgestor.domidomi.Entities.PedidoWebCabeza;

import java.util.ArrayList;
import java.util.List;

import static com.appgestor.domidomi.Entities.Cliente.setApellidoS;
import static com.appgestor.domidomi.Entities.Cliente.setCelularS;
import static com.appgestor.domidomi.Entities.Cliente.setDireccionS;
import static com.appgestor.domidomi.Entities.Cliente.setEmailS;
import static com.appgestor.domidomi.Entities.Cliente.setEstadoS;
import static com.appgestor.domidomi.Entities.Cliente.setImageByteArrayS;
import static com.appgestor.domidomi.Entities.Cliente.setNombreS;


public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "MyDBName.db";

    public DBHelper(Context context){
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlPerfil = "CREATE TABLE perfil (id integer primary key AUTOINCREMENT, nombre text, apellido text, "+
                                    " email text, celular text, direccion text, estado integer, foto BLOB )";

        String sqlPedido = "CREATE TABLE carrito (id integer primary key AUTOINCREMENT, codeproduct int, nameProduct text, " +
                           "                        quantity int, valueunitary REAL, valueoverall REAL, comment text, idcompany int, urlimagen text)";

        String sqlHistoryhead = "CREATE TABLE historyhead (id integer primary key AUTOINCREMENT, nombreUsuario text, idCompany int, direccion text, "+
                                        " direccionReferen text, celular text, longitud REAL, latitud REAL)";

        String sqlHistorydetail = "CREATE TABLE historydetailm (id integer primary key AUTOINCREMENT, idEncabezado int, idProduct int, codeProcut int, nameProduct text, "+
                                        " quantity int, valueunitary REAL, valueoverall REAL, comment text, idcompany int)";

        db.execSQL(sqlPerfil);
        db.execSQL(sqlPedido);
        db.execSQL(sqlHistoryhead);
        db.execSQL(sqlHistorydetail);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS perfil");
        db.execSQL("DROP TABLE IF EXISTS carrito");
        db.execSQL("DROP TABLE IF EXISTS historyhead");
        db.execSQL("DROP TABLE IF EXISTS historydetailm");
        this.onCreate(db);

    }

    public boolean getPerfilValida(){
        Cursor cursor;
        boolean indicador = false;
        String sql = "SELECT * FROM perfil LIMIT 1";
        SQLiteDatabase db = this.getWritableDatabase();
        cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            indicador = true;
        }
        return indicador;
    }

    public boolean insertHistoryhead(PedidoWebCabeza data){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        try {
            values.put("nombreUsuario",data.getNombreUsuairo());
            values.put("idCompany",data.getIdCompany());
            values.put("direccion",data.getDireccionp());
            values.put("direccionReferen",data.getDireccionReferen());
            values.put("celular",data.getCelularp());
            values.put("longitud",data.getLongitud());
            values.put("latitud",data.getLatitud());
            db.insert("historyhead", null, values);
        }catch (SQLiteConstraintException e){
            Log.d("data", "failure to insert word,", e);
            return false;
        }

        return true;
    }

    public boolean insertHistorydetail(AddProductCar data){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        try {
            values.put("idEncabezado", data.getIdAutoIncrement());
            values.put("idProduct",data.getIdProduct());
            values.put("codeProcut",data.getCodeProcut());
            values.put("nameProduct",data.getNameProduct());
            values.put("quantity",data.getQuantity());
            values.put("valueunitary",data.getValueunitary());
            values.put("valueoverall",data.getValueoverall());
            values.put("comment",data.getComment());
            values.put("idcompany",data.getIdcompany());

            db.insert("historydetailm", null, values);
        }catch (SQLiteConstraintException e){
            Log.d("data", "failure to insert word,", e);
            return false;
        }
        return true;
    }

    public int ultimoRegistro(){
        int _id = 0;
        String sql = "SELECT id FROM historyhead ORDER BY id DESC LIMIT 1";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            do {
                _id = Integer.parseInt(cursor.getString(0));
            } while(cursor.moveToNext());
        }
        return _id;
    }

    public boolean insertUsuario  (Cliente data){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        try {
            values.put("nombre", data.getNombre());
            values.put("apellido", data.getApellido());
            values.put("email", data.getEmail());
            values.put("celular", data.getCelular());
            values.put("direccion", data.getDireccion());
            values.put("estado", "Activo");
            values.put("foto", data.getImageByteArray());

            db.insert("perfil", null, values);
            Log.d("perfil", data.toString());
            db.close();
        }catch (SQLiteConstraintException e){
            Log.d("data", "failure to insert word,", e);
            return false;
        }

        return true;
    }

    public boolean getClienteAll(){
        String sql = "SELECT * FROM perfil LIMIT 1";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            do {
                setNombreS(cursor.getString(1));
                setApellidoS(cursor.getString(2));
                setCelularS(cursor.getString(4));
                setEmailS(cursor.getString(3));
                setDireccionS(cursor.getString(5));
                setEstadoS(cursor.getString(6));
                setImageByteArrayS(cursor.getBlob(7));
            } while(cursor.moveToNext());
        }else{
            return false;
        }
        return true;
    }

    public boolean insertProduct(AddProductCar data){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        try {
            values.put("codeproduct", data.getCodeProcut());
            values.put("nameProduct", data.getNameProduct());
            values.put("quantity", data.getQuantity());
            values.put("valueunitary", String.valueOf(data.getValueunitary()));
            values.put("valueoverall", String.valueOf(data.getValueoverall()));
            values.put("comment", data.getComment());
            values.put("idcompany", data.getIdcompany());
            values.put("urlimagen", data.getUrlimagen());

            db.insert("carrito", null, values);
            Log.d("carrito", data.toString());
            db.close();
        }catch (SQLiteConstraintException e){
            Log.d("data", "failure to insert word,", e);
            return false;
        }

        return true;
    }


    public List<AddProductCar> getProductCar(int parameter) {
        ArrayList<AddProductCar> addProduct = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();

        String columns[] = {"id","codeproduct","nameProduct","quantity","valueunitary","valueoverall","comment","idcompany","urlimagen"};
        Cursor cursor = db.query("carrito",columns, "idcompany = ?",new String[] {String.valueOf(parameter)}, null, null, null, null);

        AddProductCar productCar;
        if (cursor.moveToFirst()) {
            do {
                productCar = new AddProductCar();
                productCar.setIdProduct(Integer.parseInt(cursor.getString(0)));
                productCar.setCodeProcut(Integer.parseInt(cursor.getString(1)));
                productCar.setNameProduct(cursor.getString(2));
                productCar.setQuantity(Integer.parseInt(cursor.getString(3)));
                productCar.setValueunitary(Double.parseDouble(cursor.getString(4)));
                productCar.setValueoverall(Double.parseDouble(cursor.getString(5)));
                productCar.setComment(cursor.getString(6));
                productCar.setIdcompany(Integer.parseInt(cursor.getString(7)));
                productCar.setUrlimagen(cursor.getString(8));

                addProduct.add(productCar);
            } while(cursor.moveToNext());
        }
        return addProduct;
    }

    public boolean DeleteProduct(int id, int idCompany){
        SQLiteDatabase db = this.getWritableDatabase();
        int p = db.delete("carrito", "id = ? AND idcompany = ? ", new String[]{String.valueOf(id), String.valueOf(idCompany)});
        db.close();
        return p > 0;
    }

    public boolean DeleteProductAll(int idCompany){
        SQLiteDatabase db = this.getWritableDatabase();
        int p = db.delete("carrito", "idcompany = ? ", new String[] {String.valueOf(idCompany)});
        db.close();
        return p > 0;
    }


}

