package com.appgestor.domidomi.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.appgestor.domidomi.Entities.AddProductCar;
import com.appgestor.domidomi.Entities.Adiciones;
import com.appgestor.domidomi.Entities.Cliente;
import com.appgestor.domidomi.Entities.PedidoWebCabeza;
import com.appgestor.domidomi.Entities.ProductoEditAdd;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "MyDBName.db";

    public DBHelper(Context context){
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String sqlPerfil = "CREATE TABLE perfil (id integer primary key AUTOINCREMENT, nombre text, celular text, " +
                " telefono text, calle_carrera text, dir_1 text, dir_2 text, dir_3 text, ciudad text," +
                " zona text, incluir int, barrio text, dirReferencia text, oficina text, numerooficina int)";

        String sqlPedido = "CREATE TABLE carrito (id integer primary key AUTOINCREMENT, codeproduct int, nameProduct text, " +
                "                        quantity int, valueunitary REAL, valueoverall REAL, comment text, idcompany int, idsede int, urlimagen text, " +
                "                        nombresede text, horainicio text, horafinal text, costodomi REAL, valorminimo REAL)";

        String sqlHistoryhead = "CREATE TABLE historyhead (id integer primary key AUTOINCREMENT, nombreUsuario text, idCompany int, direccion text, " +
                " direccionReferen text, celular text, longitud REAL, latitud REAL)";

        String sqlHistorydetail = "CREATE TABLE historydetailm (id integer primary key AUTOINCREMENT, idEncabezado int, idProduct int, codeProcut int, nameProduct text, " +
                " quantity int, valueunitary REAL, valueoverall REAL, comment text, idcompany int)";

        String sqlAdiciones = "CREATE TABLE adiciones (id integer primary key AUTOINCREMENT, idAdicion int, descripcion text, " +
                " tipo text, valor REAL, estado int, idproductos int, idSede int,  " +
                " idEmpresa int, idCarrito int, cantidadAdicion int) ";

        String sqlIntro = "CREATE TABLE intro (id integer primary key AUTOINCREMENT, idintro text )";


        String sqlProductoCarrito = "CREATE TABLE carrito_producto (id integer primary key AUTOINCREMENT, id_producto INT, descripcion TEXT, " +
                                    " precio REAL, cantidad int, foto TEXT, estado INT, id_menu_movil INT, total_compra REAL, comentario TEXT, id_sede INT, " +
                                    " id_empresa INT, nombre_sede TEXT, hora_inicial TEXT, hora_final TEXT, valor_minimo REAL, valor_domicilio REAL, ingrediente TEXT)";



        String sqlAdicionesCarCompra = "CREATE TABLE adiciones_car_compra (id integer primary key AUTOINCREMENT, idAdicion int, descripcion text, " +
                " tipo text, valor REAL, estado int, idproductos int, idSede int,  " +
                " idEmpresa int, idCarrito int, cantidadAdicion int) ";

        db.execSQL(sqlPerfil);
        db.execSQL(sqlPedido);
        db.execSQL(sqlHistoryhead);
        db.execSQL(sqlHistorydetail);
        db.execSQL(sqlAdiciones);
        db.execSQL(sqlIntro);
        db.execSQL(sqlProductoCarrito);
        db.execSQL(sqlAdicionesCarCompra);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS perfil");
        db.execSQL("DROP TABLE IF EXISTS carrito");
        db.execSQL("DROP TABLE IF EXISTS historyhead");
        db.execSQL("DROP TABLE IF EXISTS historydetailm");
        db.execSQL("DROP TABLE IF EXISTS adiciones");
        db.execSQL("DROP TABLE IF EXISTS intro");
        db.execSQL("DROP TABLE IF EXISTS carrito_producto");
        db.execSQL("DROP TABLE IF EXISTS adiciones_car_compra");
        this.onCreate(db);

    }

    public boolean insertProductoCarrito(ProductoEditAdd data){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        int idCarritoCompra = 0;

        try {
            values.put("id_producto", data.getCodigo_producto());
            values.put("descripcion", data.getDescripcion());
            values.put("precio", data.getPrecio());
            values.put("cantidad", data.getCantidad());
            values.put("foto", data.getFoto());
            values.put("estado", data.getEstado());
            values.put("id_menu_movil", data.getIdmenumovil());
            values.put("total_compra", data.getValor_total());
            values.put("comentario", data.getComentario());
            values.put("id_sede", data.getId_sede());
            values.put("id_empresa", data.getId_empresa());
            values.put("nombre_sede", data.getNombre_sede());
            values.put("hora_inicial", data.getHora_inicial());
            values.put("hora_final", data.getHora_final());
            values.put("valor_minimo", data.getValor_minimo());
            values.put("valor_domicilio", data.getCosto_envio());
            values.put("ingrediente", data.getIngredientes());

            db.insert("carrito_producto", null, values);
            idCarritoCompra = ultimoRegistro("carrito_producto");

            if(data.getAdicionesListselet() != null && data.getAdicionesListselet().size() > 0) {

                ContentValues valueAdicion = new ContentValues();
                for(int f = 0; f < data.getAdicionesListselet().size(); f++) {
                    try {

                        valueAdicion.put("idAdicion", data.getAdicionesListselet().get(f).getIdadicionales());
                        valueAdicion.put("descripcion", data.getAdicionesListselet().get(f).getDescripcion());
                        valueAdicion.put("tipo", data.getAdicionesListselet().get(f).getTipo());
                        valueAdicion.put("valor", data.getAdicionesListselet().get(f).getValor());
                        valueAdicion.put("estado", data.getAdicionesListselet().get(f).getEstado());
                        valueAdicion.put("idproductos", data.getAdicionesListselet().get(f).getIdproductos());
                        valueAdicion.put("idSede", data.getAdicionesListselet().get(f).getIdSede());
                        valueAdicion.put("idEmpresa", data.getAdicionesListselet().get(f).getIdEmpresa());
                        valueAdicion.put("idCarrito", idCarritoCompra);
                        valueAdicion.put("cantidadAdicion", data.getAdicionesListselet().get(f).getCantidadAdicion());

                        db.insert("adiciones", null, valueAdicion);

                    }catch (SQLiteConstraintException e){

                        return false;
                    }
                }
            }

            if(data.getAdicionesList() != null && data.getAdicionesList().size() > 0) {
                ContentValues valueAdicion = new ContentValues();
                for(int f = 0; f < data.getAdicionesList().size(); f++) {
                    try {
                        valueAdicion.put("idAdicion", data.getAdicionesList().get(f).getIdadicionales());
                        valueAdicion.put("descripcion", data.getAdicionesList().get(f).getDescripcion());
                        valueAdicion.put("tipo", data.getAdicionesList().get(f).getTipo());
                        valueAdicion.put("valor", data.getAdicionesList().get(f).getValor());
                        valueAdicion.put("estado", data.getAdicionesList().get(f).getEstado());
                        valueAdicion.put("idproductos", data.getAdicionesList().get(f).getIdproductos());
                        valueAdicion.put("idSede", data.getAdicionesList().get(f).getIdSede());
                        valueAdicion.put("idEmpresa", data.getAdicionesList().get(f).getIdEmpresa());
                        valueAdicion.put("idCarrito", idCarritoCompra);
                        valueAdicion.put("cantidadAdicion", data.getAdicionesList().get(f).getCantidadAdicion());

                        db.insert("adiciones_car_compra", null, valueAdicion);

                    } catch (SQLiteConstraintException e){

                        return false;
                    }
                }
            }

            Log.d("carrito_producto", data.toString());
            db.close();

        }catch (SQLiteConstraintException e){
            Log.d("carrito_producto", "failure to insert word,", e);
            return false;
        }

        return true;
    }

    public boolean updateProducto(ProductoEditAdd data){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        try {
            values.put("id_producto", data.getCodigo_producto());
            values.put("descripcion", data.getDescripcion());
            values.put("precio", data.getPrecio());
            values.put("cantidad", data.getCantidad());
            values.put("foto", data.getFoto());
            values.put("estado", data.getEstado());
            values.put("id_menu_movil", data.getIdmenumovil());
            values.put("total_compra", data.getValor_total());
            values.put("comentario", data.getComentario());
            values.put("id_sede", data.getId_sede());
            values.put("id_empresa", data.getId_empresa());
            values.put("nombre_sede", data.getNombre_sede());
            values.put("hora_inicial", data.getHora_inicial());
            values.put("hora_final", data.getHora_final());
            values.put("valor_minimo", data.getValor_minimo());
            values.put("valor_domicilio", data.getCosto_envio());

            db.update("carrito_producto", values, String.format("id = %1$s", data.getAuto_incremental()), null);

            int c = db.delete("adiciones", "idCarrito = ? AND idSede = ? AND idEmpresa = ? ",
                    new String[] {String.valueOf(data.getAuto_incremental()), String.valueOf(data.getId_sede()), String.valueOf(data.getId_empresa())});

            if(data.getAdicionesListselet() != null && data.getAdicionesListselet().size() > 0) {
                ContentValues valueAdicion = new ContentValues();
                for(int f = 0; f < data.getAdicionesListselet().size(); f++) {
                    try {
                        valueAdicion.put("idAdicion", data.getAdicionesListselet().get(f).getIdadicionales());
                        valueAdicion.put("descripcion", data.getAdicionesListselet().get(f).getDescripcion());
                        valueAdicion.put("tipo", data.getAdicionesListselet().get(f).getTipo());
                        valueAdicion.put("valor", data.getAdicionesListselet().get(f).getValor());
                        valueAdicion.put("estado", data.getAdicionesListselet().get(f).getEstado());
                        valueAdicion.put("idproductos", data.getAdicionesListselet().get(f).getIdproductos());
                        valueAdicion.put("idSede", data.getAdicionesListselet().get(f).getIdSede());
                        valueAdicion.put("idEmpresa", data.getAdicionesListselet().get(f).getIdEmpresa());
                        valueAdicion.put("idCarrito", data.getAuto_incremental());
                        valueAdicion.put("cantidadAdicion", data.getAdicionesListselet().get(f).getCantidadAdicion());

                        db.insert("adiciones", null, valueAdicion);

                    } catch (SQLiteConstraintException e){
                        return false;
                    }
                }
            }

            int b = db.delete("adiciones_car_compra", "idCarrito = ? AND idSede = ? AND idEmpresa = ? ",
                    new String[] {String.valueOf(data.getAuto_incremental()), String.valueOf(data.getId_sede()), String.valueOf(data.getId_empresa())});

            if(data.getAdicionesList() != null && data.getAdicionesList().size() > 0) {
                ContentValues valueAdicion = new ContentValues();
                for(int f = 0; f < data.getAdicionesList().size(); f++) {
                    try {
                        valueAdicion.put("idAdicion", data.getAdicionesList().get(f).getIdadicionales());
                        valueAdicion.put("descripcion", data.getAdicionesList().get(f).getDescripcion());
                        valueAdicion.put("tipo", data.getAdicionesList().get(f).getTipo());
                        valueAdicion.put("valor", data.getAdicionesList().get(f).getValor());
                        valueAdicion.put("estado", data.getAdicionesList().get(f).getEstado());
                        valueAdicion.put("idproductos", data.getAdicionesList().get(f).getIdproductos());
                        valueAdicion.put("idSede", data.getAdicionesList().get(f).getIdSede());
                        valueAdicion.put("idEmpresa", data.getAdicionesList().get(f).getIdEmpresa());
                        valueAdicion.put("idCarrito", data.getAuto_incremental());
                        valueAdicion.put("cantidadAdicion", data.getAdicionesList().get(f).getCantidadAdicion());

                        db.insert("adiciones_car_compra", null, valueAdicion);
                    } catch (SQLiteConstraintException e){
                        return false;
                    }
                }
            }

        }catch (SQLiteConstraintException e){
            Log.d("carrito_producto", "failure to insert word,", e);
            return false;
        }
        return true;
    }

    public List<ProductoEditAdd> selectProductoCarrito(int empresa, int sede){

        List<ProductoEditAdd> productoEditAddList = new ArrayList<>();
        int id_carrito = 0;
        String sql = "SELECT id, id_producto, descripcion, precio, cantidad, foto, estado, id_menu_movil, total_compra, id_empresa," +
                "       comentario, id_sede, nombre_sede, hora_inicial, hora_final, valor_minimo, valor_domicilio, ingrediente" +
                "       FROM carrito_producto WHERE id_sede = "+sede+" AND id_empresa = "+empresa;

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(sql, null);
        ProductoEditAdd productoEditAdd;
        if (cursor.moveToFirst()) {

            do {
                productoEditAdd = new ProductoEditAdd();
                id_carrito = Integer.parseInt(cursor.getString(0));
                productoEditAdd.setAuto_incremental(cursor.getInt(0));
                productoEditAdd.setCodigo_producto(Integer.parseInt(cursor.getString(1)));
                productoEditAdd.setDescripcion(cursor.getString(2));
                productoEditAdd.setPrecio(Double.parseDouble(cursor.getString(3)));
                productoEditAdd.setCantidad(Integer.parseInt(cursor.getString(4)));
                productoEditAdd.setFoto(cursor.getString(5));
                productoEditAdd.setEstado(Integer.parseInt(cursor.getString(6)));
                productoEditAdd.setIdmenumovil(Integer.parseInt(cursor.getString(7)));
                productoEditAdd.setValor_total(Double.parseDouble(cursor.getString(8)));
                productoEditAdd.setId_empresa(Integer.parseInt(cursor.getString(9)));
                productoEditAdd.setComentario(cursor.getString(10));
                productoEditAdd.setId_sede(Integer.parseInt(cursor.getString(11)));
                productoEditAdd.setNombre_sede(cursor.getString(12));
                productoEditAdd.setHora_inicial(cursor.getString(13));
                productoEditAdd.setHora_final(cursor.getString(14));
                productoEditAdd.setValor_minimo(Double.parseDouble(cursor.getString(15)));
                productoEditAdd.setCosto_envio(Double.parseDouble(cursor.getString(16)));
                productoEditAdd.setIngredientes(cursor.getString(17));

                String sql2 = "SELECT id, idAdicion, descripcion, tipo, valor, estado, idproductos, idSede, idEmpresa, idCarrito, cantidadAdicion " +
                        "        FROM adiciones WHERE idCarrito = "+id_carrito+" AND idSede = "+sede+" AND idEmpresa = "+empresa;
                Cursor cursor2 = db.rawQuery(sql2, null);
                List<Adiciones> adicionesListSelect = new ArrayList<>();
                Adiciones adiciones;
                if (cursor2.moveToFirst()) {
                    do {
                        adiciones = new Adiciones();
                        adiciones.setAutoIncrementAdicion(cursor2.getInt(0));
                        adiciones.setIdadicionales(Integer.parseInt(cursor2.getString(1)));
                        adiciones.setDescripcion(cursor2.getString(2));
                        adiciones.setTipo(cursor2.getString(3));
                        adiciones.setValor(Double.parseDouble(cursor2.getString(4)));
                        adiciones.setEstado(Integer.parseInt(cursor2.getString(5)));
                        adiciones.setIdproductos(Integer.parseInt(cursor2.getString(6)));
                        adiciones.setIdSede(Integer.parseInt(cursor2.getString(7)));
                        adiciones.setIdEmpresa(Integer.parseInt(cursor2.getString(8)));
                        adiciones.setIdCarritoCompra(Integer.parseInt(cursor2.getString(9)));
                        adiciones.setCantidadAdicion(Integer.parseInt(cursor2.getString(10)));
                        adicionesListSelect.add(adiciones);
                    } while (cursor2.moveToNext());

                }

                productoEditAdd.setAdicionesListselet(adicionesListSelect);

                String sql3 = "SELECT id, idAdicion, descripcion, tipo, valor, estado, idproductos, idSede, idEmpresa, idCarrito, cantidadAdicion " +
                        "        FROM adiciones_car_compra WHERE idCarrito = "+id_carrito+" AND idSede = "+sede+" AND idEmpresa = "+empresa;
                Cursor cursor3 = db.rawQuery(sql3, null);
                Adiciones adiciones3;
                List<Adiciones> adicionesListCompleta = new ArrayList<>();
                if (cursor3.moveToFirst()) {
                    do {
                        adiciones3 = new Adiciones();
                        adiciones3.setAutoIncrementAdicion(cursor3.getInt(0));
                        adiciones3.setIdadicionales(Integer.parseInt(cursor3.getString(1)));
                        adiciones3.setDescripcion(cursor3.getString(2));
                        adiciones3.setTipo(cursor3.getString(3));
                        adiciones3.setValor(Double.parseDouble(cursor3.getString(4)));
                        adiciones3.setEstado(Integer.parseInt(cursor3.getString(5)));
                        adiciones3.setIdproductos(Integer.parseInt(cursor3.getString(6)));
                        adiciones3.setIdSede(Integer.parseInt(cursor3.getString(7)));
                        adiciones3.setIdEmpresa(Integer.parseInt(cursor3.getString(8)));
                        adiciones3.setIdCarritoCompra(Integer.parseInt(cursor3.getString(9)));
                        adiciones3.setCantidadAdicion(Integer.parseInt(cursor3.getString(10)));
                        adicionesListCompleta.add(adiciones3);
                    } while (cursor3.moveToNext());

                }

                productoEditAdd.setAdicionesList(adicionesListCompleta);

                productoEditAddList.add(productoEditAdd);
            } while(cursor.moveToNext());
        }

        return productoEditAddList;
    }

    public boolean insertIntro(String data){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        try {
            values.put("idintro",data);
            db.insert("intro", null, values);
        }catch (SQLiteConstraintException e){
            Log.d("data", "failure to insert word,", e);
            return false;
        }

        return true;

    }

    public boolean getIntro(){
        Cursor cursor;
        boolean indicador = false;
        String sql = "SELECT * FROM intro LIMIT 1";
        SQLiteDatabase db = this.getWritableDatabase();
        cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            indicador = true;
        }
        return indicador;
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

    public int ultimoRegistro(String table) {
        int _id = 0;
        String sql = "SELECT id FROM "+ table +" ORDER BY id DESC LIMIT 1";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            do {
                _id = Integer.parseInt(cursor.getString(0));
            } while(cursor.moveToNext());
        }
        return _id;
    }

    public boolean insertUsuario(Cliente data) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        try {
            values.put("nombre", data.getNombre());
            values.put("celular", data.getCelular());
            values.put("telefono", data.getTelefono());
            values.put("calle_carrera", data.getCalle_carrera());
            values.put("dir_1", data.getDir_1());
            values.put("dir_2", data.getDir_2());
            values.put("dir_3", data.getDir_3());
            values.put("ciudad", data.getCiudad());
            values.put("zona", data.getZona());
            values.put("barrio", data.getBarrio());
            values.put("dirReferencia", data.getDirReferencia());
            values.put("oficina", data.getOficina());
            values.put("numerooficina", data.getNum_oficina());

            db.insert("perfil", null, values);
            db.close();
        }catch (SQLiteConstraintException e){
            Log.d("data", "failure to insert word,", e);
            return false;
        }

        return true;
    }

    public List<Cliente> getUsuarioAll(){
        List<Cliente> clientes = new ArrayList<>();

        String sql = "SELECT * FROM perfil";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        Cliente cliente;
        if (cursor.moveToFirst()) {
            do {
                cliente = new Cliente();
                cliente.setCodigo(Integer.parseInt(cursor.getString(0)));
                cliente.setNombre(cursor.getString(1));
                cliente.setCelular(cursor.getString(2));
                cliente.setTelefono(cursor.getString(3));

                cliente.setCalle_carrera(cursor.getString(4));
                cliente.setDir_1(cursor.getString(5));
                cliente.setDir_2(cursor.getString(6));
                cliente.setDir_3(cursor.getString(7));

                cliente.setCiudad(cursor.getString(8));
                cliente.setZona(cursor.getString(9));
                cliente.setBarrio(cursor.getString(11));
                cliente.setDirReferencia(cursor.getString(12));
                cliente.setOficina(cursor.getString(13));
                cliente.setNum_oficina(Integer.parseInt(cursor.getString(14)));

                clientes.add(cliente);
            } while(cursor.moveToNext());
        }

        return clientes;

    }

    public boolean deleteUsuario(){
        SQLiteDatabase db = this.getWritableDatabase();
        int p = db.delete("perfil", null, null);
        db.close();
        return p > 0;
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
            values.put("idsede", data.getIdsede());
            values.put("urlimagen", data.getUrlimagen());
            values.put("nombresede", data.getNameSede());

            values.put("horainicio", data.getHoraInicioEmpresa());
            values.put("horafinal", data.getHoraFinalEmpresa());
            values.put("costodomi", data.getCostoEnvio());
            values.put("valorminimo", data.getValorMinimo());

            db.insert("carrito", null, values);

            if(data.getAdicionesList() != null && data.getAdicionesList().size() > 0){

                int idCarritoCompra = ultimoRegistro("carrito");

                ContentValues valueAdicion = new ContentValues();
                for(int f = 0; f < data.getAdicionesList().size(); f++) {
                    try {

                        valueAdicion.put("idAdicion", data.getAdicionesList().get(f).getIdadicionales());
                        valueAdicion.put("descripcion", data.getAdicionesList().get(f).getDescripcion());
                        valueAdicion.put("tipo", data.getAdicionesList().get(f).getTipo());
                        valueAdicion.put("valor", data.getAdicionesList().get(f).getValor());
                        valueAdicion.put("estado", data.getAdicionesList().get(f).getEstado());
                        valueAdicion.put("idproductos", data.getAdicionesList().get(f).getIdproductos());
                        valueAdicion.put("idSede", data.getAdicionesList().get(f).getIdSede());
                        valueAdicion.put("idEmpresa", data.getAdicionesList().get(f).getIdEmpresa());
                        valueAdicion.put("idCarrito", idCarritoCompra);
                        valueAdicion.put("cantidadAdicion", data.getAdicionesList().get(f).getCantidadAdicion());

                        db.insert("adiciones", null, valueAdicion);

                    }catch (SQLiteConstraintException e){

                        return false;
                    }
                }
            }

            Log.d("carrito", data.toString());

            db.close();

        }catch (SQLiteConstraintException e){
            Log.d("data", "failure to insert word,", e);
            return false;
        }

        return true;
    }

    public boolean DeletePerfil(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        int p = db.delete("perfil", "id = ? ", new String[]{String.valueOf(id)});
        db.close();
        return p > 0;
    }

    public boolean UpdatePerfil(int id, Cliente cliente){

        ContentValues valores = new ContentValues();
        SQLiteDatabase db = this.getWritableDatabase();

        valores.put("nombre", cliente.getNombre());
        valores.put("celular", cliente.getCelular());
        valores.put("telefono", cliente.getTelefono());
        valores.put("calle_carrera", cliente.getCalle_carrera());
        valores.put("dir_1", cliente.getDir_1());
        valores.put("dir_2", cliente.getDir_2());
        valores.put("dir_3", cliente.getDir_3());
        valores.put("ciudad",cliente.getCiudad());
        valores.put("zona", cliente.getZona());
        valores.put("barrio", cliente.getBarrio());
        valores.put("dirReferencia", cliente.getDirReferencia());
        valores.put("oficina", cliente.getOficina());
        valores.put("numerooficina", cliente.getNum_oficina());

        int p = db.update("perfil", valores, String.format("id = %1$s", id), null);
        db.close();
        return p > 0;
    }

    public List<Adiciones> getAdiciones(int idcarrito, int idempresa, int idsede){
        ArrayList<Adiciones> addAdiciones = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();

        String columns[] = {"id","idAdicion","descripcion","tipo","valor","estado","idproductos","idSede", "idEmpresa", "idCarrito", "cantidadAdicion"};

        Cursor cursor = db.query("adiciones",columns, "idCarrito = ? AND idEmpresa = ? AND idSede = ?",
                new String[] {String.valueOf(idcarrito),String.valueOf(idempresa),String.valueOf(idsede)}, null, null, null, null);

        Adiciones adiciones;
        if (cursor.moveToFirst()) {

            do {
                adiciones = new Adiciones();

                adiciones.setAutoIncrementAdicion(Integer.parseInt(cursor.getString(0)));
                adiciones.setIdadicionales(Integer.parseInt(cursor.getString(1)));
                adiciones.setDescripcion(cursor.getString(2));
                adiciones.setTipo(cursor.getString(3));
                adiciones.setValor(Double.parseDouble(cursor.getString(4)));
                adiciones.setEstado(Integer.parseInt(cursor.getString(5)));
                adiciones.setIdproductos(Integer.parseInt(cursor.getString(6)));
                adiciones.setIdSede(Integer.parseInt(cursor.getString(7)));
                adiciones.setIdEmpresa(Integer.parseInt(cursor.getString(8)));
                adiciones.setIdCarritoCompra(Integer.parseInt(cursor.getString(9)));
                adiciones.setCantidadAdicion(Integer.parseInt(cursor.getString(10)));

                addAdiciones.add(adiciones);

            } while(cursor.moveToNext());

        }
        return addAdiciones;
    }

    public List<ProductoEditAdd> getProductCarAll(){

        List<ProductoEditAdd> addProduct = new ArrayList<>();
        String sql = "SELECT id, id_producto, descripcion, id_empresa, id_sede, nombre_sede FROM carrito_producto GROUP BY id_sede";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql, null);

        ProductoEditAdd productCar;

        if (cursor.moveToFirst()) {
            do {
                productCar = new ProductoEditAdd();

                productCar.setAuto_incremental(cursor.getInt(0));
                productCar.setCodigo_producto(cursor.getInt(1));
                productCar.setDescripcion(cursor.getString(2));
                productCar.setId_empresa(cursor.getInt(3));
                productCar.setId_sede(Integer.parseInt(cursor.getString(4)));
                productCar.setNombre_sede(cursor.getString(5));

                addProduct.add(productCar);

            } while(cursor.moveToNext());
        }

        return addProduct;

    }

    public List<AddProductCar> getProductCar(int idEmpresa, int idSede) {
        ArrayList<AddProductCar> addProduct = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();

        String columns[] = {"id","codeproduct","nameProduct","quantity","valueunitary","valueoverall","comment","idcompany", "idsede", "urlimagen"};

        Cursor cursor = db.query("carrito",columns, "idcompany = ? AND idsede = ? ", new String[] {String.valueOf(idEmpresa), String.valueOf(idSede)}, null, null, null, null);

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
                productCar.setIdsede(Integer.parseInt(cursor.getString(8)));
                productCar.setUrlimagen(cursor.getString(9));

                addProduct.add(productCar);

            } while(cursor.moveToNext());
        }
        return addProduct;
    }

    public boolean UpdateProduct(int id, int idCompany, int idsede, AddProductCar data){

        ContentValues valores = new ContentValues();

        Double valorAdiciones = 0.0;

        List<Adiciones> adicionesList = getAdiciones(data.getIdProduct(), idCompany, idsede);

        if(adicionesList != null){
            for(int i = 0; i < adicionesList.size(); i++) {
                valorAdiciones = valorAdiciones + adicionesList.get(i).getValor();
            }
        }

        Double valorTotal = (valorAdiciones * id) + (data.getValueunitary() * id);

        valores.put("quantity", id);
        valores.put("valueoverall", valorTotal);

        SQLiteDatabase db = this.getWritableDatabase();
        int p = db.update("carrito", valores, String.format("idcompany = %1$s AND idsede = %2$s AND id = %3$s", idCompany, idsede, data.getIdProduct()), null);
        db.close();
        return p > 0;
    }

    public boolean DeleteProduct(int idSede, int idEmpresa, int idCarrito){
        SQLiteDatabase db = this.getWritableDatabase();
        int a = db.delete("carrito_producto", "id = ? AND id_sede = ?", new String[] {String.valueOf(idCarrito), String.valueOf(idSede)});

        int b = db.delete("adiciones_car_compra", "idCarrito = ? AND idSede = ? AND idEmpresa = ? ",
                new String[] {String.valueOf(idCarrito), String.valueOf(idSede), String.valueOf(idEmpresa)});

        int c = db.delete("adiciones", "idCarrito = ? AND idSede = ? AND idEmpresa = ? ",
                new String[] {String.valueOf(idCarrito), String.valueOf(idSede), String.valueOf(idEmpresa)});

        db.close();
        return a > 0;
    }

    public boolean DeleteProductAll(int id_empresa, int id_sede) {
        SQLiteDatabase db = this.getWritableDatabase();
        int a = db.delete("carrito_producto", "id_sede = ?", new String[] {String.valueOf(id_sede)});

        int b = db.delete("adiciones_car_compra", "idSede = ? AND idEmpresa = ? ",
                new String[] {String.valueOf(id_sede), String.valueOf(id_empresa)});

        int c = db.delete("adiciones", "idSede = ? AND idEmpresa = ? ",
                new String[] {String.valueOf(id_sede), String.valueOf(id_empresa)});

        db.close();
        return a > 0;
    }

    public int getCantidadProducto(int empresa, int sede){

        int cantidad = 0;
        String sql = "SELECT COUNT(*) FROM carrito_producto WHERE id_sede = ? AND id_empresa = ?";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql, new String[] {String.valueOf(sede), String.valueOf(empresa)});

        if (cursor.moveToFirst()) {
            cantidad = cursor.getInt(0);
        }

        return cantidad;

    }

}

