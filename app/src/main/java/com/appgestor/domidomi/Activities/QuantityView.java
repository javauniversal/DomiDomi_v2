package com.appgestor.domidomi.Activities;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.appgestor.domidomi.Entities.ProductoEditAdd;

import java.text.DecimalFormat;

public class QuantityView extends LinearLayout implements View.OnClickListener {

    private Drawable quantityBackground, addButtonBackground, removeButtonBackground;

    private String addButtonText, removeButtonText;

    private int quantity, maxQuantity, minQuantity;
    private int quantityPadding;

    private int quantityTextColor, addButtonTextColor, removeButtonTextColor;

    private Button mButtonAdd, mButtonRemove;
    private TextView mTextViewQuantity;

    private ProductoEditAdd productoEditAdd;
    private TextView textView;
    private TextView textView12;
    private TextView textView3;
    private TextView txt_total_adiciones;
    private ViewGroup parentactivity;
    private DecimalFormat format;

    public interface OnQuantityChangeListener {
        void onQuantityChanged(int newQuantity, boolean programmatically);

        void onLimitReached();
    }

    private OnQuantityChangeListener onQuantityChangeListener;

    public QuantityView(Context context) {
        super(context);
        init(null, 0);
    }

    public QuantityView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public QuantityView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void init(AttributeSet attrs, int defStyle) {
        final TypedArray a = getContext().obtainStyledAttributes(attrs, me.himanshusoni.quantityview.R.styleable.QuantityView, defStyle, 0);

        addButtonText = getResources().getString(me.himanshusoni.quantityview.R.string.qv_add);
        if (a.hasValue(me.himanshusoni.quantityview.R.styleable.QuantityView_qv_addButtonText)) {
            addButtonText = a.getString(me.himanshusoni.quantityview.R.styleable.QuantityView_qv_addButtonText);
        }
        addButtonBackground = ContextCompat.getDrawable(getContext(), me.himanshusoni.quantityview.R.drawable.qv_btn_selector);
        if (a.hasValue(me.himanshusoni.quantityview.R.styleable.QuantityView_qv_addButtonBackground)) {
            addButtonBackground = a.getDrawable(me.himanshusoni.quantityview.R.styleable.QuantityView_qv_addButtonBackground);
        }
        addButtonTextColor = a.getColor(me.himanshusoni.quantityview.R.styleable.QuantityView_qv_addButtonTextColor, Color.BLACK);

        removeButtonText = getResources().getString(me.himanshusoni.quantityview.R.string.qv_remove);
        if (a.hasValue(me.himanshusoni.quantityview.R.styleable.QuantityView_qv_removeButtonText)) {
            removeButtonText = a.getString(me.himanshusoni.quantityview.R.styleable.QuantityView_qv_removeButtonText);
        }
        removeButtonBackground = ContextCompat.getDrawable(getContext(), me.himanshusoni.quantityview.R.drawable.qv_btn_selector);
        if (a.hasValue(me.himanshusoni.quantityview.R.styleable.QuantityView_qv_removeButtonBackground)) {
            removeButtonBackground = a.getDrawable(me.himanshusoni.quantityview.R.styleable.QuantityView_qv_removeButtonBackground);
        }
        removeButtonTextColor = a.getColor(me.himanshusoni.quantityview.R.styleable.QuantityView_qv_removeButtonTextColor, Color.BLACK);

        quantity = a.getInt(me.himanshusoni.quantityview.R.styleable.QuantityView_qv_quantity, 0);
        maxQuantity = a.getInt(me.himanshusoni.quantityview.R.styleable.QuantityView_qv_maxQuantity, Integer.MAX_VALUE);
        minQuantity = a.getInt(me.himanshusoni.quantityview.R.styleable.QuantityView_qv_minQuantity, 0);

        quantityPadding = (int) a.getDimension(me.himanshusoni.quantityview.R.styleable.QuantityView_qv_quantityPadding, pxFromDp(24));
        quantityTextColor = a.getColor(me.himanshusoni.quantityview.R.styleable.QuantityView_qv_quantityTextColor, Color.BLACK);
        quantityBackground = ContextCompat.getDrawable(getContext(), me.himanshusoni.quantityview.R.drawable.qv_bg_selector);
        if (a.hasValue(me.himanshusoni.quantityview.R.styleable.QuantityView_qv_quantityBackground)) {
            quantityBackground = a.getDrawable(me.himanshusoni.quantityview.R.styleable.QuantityView_qv_quantityBackground);
        }

        a.recycle();
        int dp10 = pxFromDp(10);

        mButtonAdd = new Button(getContext());
        mButtonAdd.setGravity(Gravity.CENTER);
        mButtonAdd.setPadding(dp10, dp10, dp10, dp10);
        mButtonAdd.setMinimumHeight(0);
        mButtonAdd.setMinimumWidth(0);
        mButtonAdd.setMinHeight(0);
        mButtonAdd.setMinWidth(0);
        setAddButtonBackground(addButtonBackground);
        setAddButtonText(addButtonText);
        setAddButtonTextColor(addButtonTextColor);

        mButtonRemove = new Button(getContext());
        mButtonRemove.setGravity(Gravity.CENTER);
        mButtonRemove.setPadding(dp10, dp10, dp10, dp10);
        mButtonRemove.setMinimumHeight(0);
        mButtonRemove.setMinimumWidth(0);
        mButtonRemove.setMinHeight(0);
        mButtonRemove.setMinWidth(0);
        setRemoveButtonBackground(removeButtonBackground);
        setRemoveButtonText(removeButtonText);
        setRemoveButtonTextColor(removeButtonTextColor);

        mTextViewQuantity = new TextView(getContext());
        mTextViewQuantity.setGravity(Gravity.CENTER);
        setQuantityTextColor(quantityTextColor);
        setQuantity(quantity);
        setQuantityBackground(quantityBackground);
        setQuantityPadding(quantityPadding);

        setOrientation(HORIZONTAL);

        addView(mButtonRemove, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        addView(mTextViewQuantity, LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        addView(mButtonAdd, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

        mButtonAdd.setOnClickListener(this);
        mButtonRemove.setOnClickListener(this);
        mTextViewQuantity.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == mButtonAdd) {
            if (quantity + 1 > maxQuantity) {
                if (onQuantityChangeListener != null) onQuantityChangeListener.onLimitReached();
            } else {
                quantity += 1;
                mTextViewQuantity.setText(String.valueOf(quantity));
                cargarValorCantidad();
                if (onQuantityChangeListener != null)
                    onQuantityChangeListener.onQuantityChanged(quantity, false);
            }
        } else if (v == mButtonRemove) {
            if (quantity - 1 < minQuantity) {
                if (onQuantityChangeListener != null) onQuantityChangeListener.onLimitReached();
            } else {
                quantity -= 1;
                mTextViewQuantity.setText(String.valueOf(quantity));
                cargarValorCantidad();
                if (onQuantityChangeListener != null)
                    onQuantityChangeListener.onQuantityChanged(quantity, false);
            }
        } else if (v == mTextViewQuantity) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Digite la Cantidad");

            View inflate = LayoutInflater.from(getContext()).inflate(me.himanshusoni.quantityview.R.layout.qv_dialog_changequantity, null, false);
            final EditText et = (EditText) inflate.findViewById(me.himanshusoni.quantityview.R.id.qv_et_change_qty);
            et.setText(String.valueOf(quantity));

            builder.setView(inflate);
            builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String newQuantity = et.getText().toString();
                    if (isNumber(newQuantity)) {
                        int intNewQuantity = Integer.parseInt(newQuantity);
                        setQuantity(intNewQuantity);
                        cargarValorCantidad();

                    }
                }
            }).setNegativeButton("Cancelar", null);
            builder.show();
        }
    }


    public OnQuantityChangeListener getOnQuantityChangeListener() {
        return onQuantityChangeListener;
    }

    public void setValorTotal(ProductoEditAdd productoEditAdd, TextView valor_total, TextView valor_total_oculto, ViewGroup parent, TextView total_producto, TextView total_adiciones){
        this.productoEditAdd = productoEditAdd;
        textView = valor_total;
        textView12 = valor_total_oculto;
        textView3 = total_producto;
        format = new DecimalFormat("#,###.##");
        parentactivity = parent;
        txt_total_adiciones = total_adiciones;
    }

    public void cargarValorCantidad(){

        Double valorAdicion = loopQuestions(parentactivity, 0.0);

        Double sumaTotal = (productoEditAdd.getPrecio() * quantity) + valorAdicion;

        txt_total_adiciones.setText(String.format("$ %s", format.format(valorAdicion)));
        textView.setText(String.format("$ %s", format.format(sumaTotal)));
        textView12.setText(sumaTotal+"");
        textView3.setText(String.format("$ %s", format.format(productoEditAdd.getPrecio() * quantity)));

    }

    private Double loopQuestions(ViewGroup parent, Double CurrentSum) {
        for(int i = 0; i < parent.getChildCount(); i++) {
            View child = parent.getChildAt(i);

            if(child instanceof CheckBox) {
                //Support for Checkboxes
                CheckBox cb = (CheckBox)child;
                int answer = cb.isChecked() ? 1 : 0;
                if (answer == 1){
                    for(int f = 0; f < productoEditAdd.getAdicionesList().size(); f++) {
                        if(cb.getId() == productoEditAdd.getAdicionesList().get(f).getIdadicionales()){
                            CurrentSum += (productoEditAdd.getAdicionesList().get(f).getValor()) * productoEditAdd.getAdicionesList().get(f).getCantidadAdicion();
                            break;
                        }
                    }
                }
            }

            if(child instanceof ViewGroup) {
                //Nested Q&A
                ViewGroup group = (ViewGroup)child;
                CurrentSum = loopQuestions(group, CurrentSum);
            }
        }

        return CurrentSum;
    }

    public void setOnQuantityChangeListener(OnQuantityChangeListener onQuantityChangeListener) {
        this.onQuantityChangeListener = onQuantityChangeListener;
    }

    public Drawable getQuantityBackground() {
        return quantityBackground;
    }

    public void setQuantityBackground(Drawable quantityBackground) {
        this.quantityBackground = quantityBackground;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mTextViewQuantity.setBackground(quantityBackground);
        } else {
            mTextViewQuantity.setBackgroundDrawable(quantityBackground);
        }
    }

    public Drawable getAddButtonBackground() {
        return addButtonBackground;
    }

    public void setAddButtonBackground(Drawable addButtonBackground) {
        this.addButtonBackground = addButtonBackground;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mButtonAdd.setBackground(addButtonBackground);
        } else {
            mButtonAdd.setBackgroundDrawable(addButtonBackground);
        }
    }

    public Drawable getRemoveButtonBackground() {
        return removeButtonBackground;
    }

    public void setRemoveButtonBackground(Drawable removeButtonBackground) {
        this.removeButtonBackground = removeButtonBackground;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mButtonRemove.setBackground(removeButtonBackground);
        } else {
            mButtonRemove.setBackgroundDrawable(removeButtonBackground);
        }
    }

    public String getAddButtonText() {
        return addButtonText;
    }

    public void setAddButtonText(String addButtonText) {
        this.addButtonText = addButtonText;
        mButtonAdd.setText(addButtonText);
    }

    public String getRemoveButtonText() {
        return removeButtonText;
    }

    public void setRemoveButtonText(String removeButtonText) {
        this.removeButtonText = removeButtonText;
        mButtonRemove.setText(removeButtonText);
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        boolean limitReached = false;

        if (quantity > maxQuantity) {
            quantity = maxQuantity;
            limitReached = true;
            if (onQuantityChangeListener != null) onQuantityChangeListener.onLimitReached();
        }
        if (quantity < minQuantity) {
            quantity = minQuantity;
            limitReached = true;
            if (onQuantityChangeListener != null) onQuantityChangeListener.onLimitReached();
        }


        if (!limitReached && onQuantityChangeListener != null)
            onQuantityChangeListener.onQuantityChanged(quantity, true);

        this.quantity = quantity;
        mTextViewQuantity.setText(String.valueOf(this.quantity));
    }

    public int getMaxQuantity() {
        return maxQuantity;
    }

    public void setMaxQuantity(int maxQuantity) {
        this.maxQuantity = maxQuantity;
    }

    public int getMinQuantity() {
        return minQuantity;
    }

    public void setMinQuantity(int minQuantity) {
        this.minQuantity = minQuantity;
    }

    public int getQuantityPadding() {
        return quantityPadding;
    }

    public void setQuantityPadding(int quantityPadding) {
        this.quantityPadding = quantityPadding;
        mTextViewQuantity.setPadding(quantityPadding, 0, quantityPadding, 0);
    }

    public int getQuantityTextColor() {
        return quantityTextColor;
    }

    public void setQuantityTextColor(int quantityTextColor) {
        this.quantityTextColor = quantityTextColor;
        mTextViewQuantity.setTextColor(quantityTextColor);
    }

    public void setQuantityTextColorRes(int quantityTextColorRes) {
        this.quantityTextColor = ContextCompat.getColor(getContext(), quantityTextColorRes);
        mTextViewQuantity.setTextColor(quantityTextColor);
    }

    public int getAddButtonTextColor() {
        return addButtonTextColor;
    }

    public void setAddButtonTextColor(int addButtonTextColor) {
        this.addButtonTextColor = addButtonTextColor;
        mButtonAdd.setTextColor(addButtonTextColor);
    }

    public void setAddButtonTextColorRes(int addButtonTextColorRes) {
        this.addButtonTextColor = ContextCompat.getColor(getContext(), addButtonTextColorRes);
        mButtonAdd.setTextColor(addButtonTextColor);
    }

    public int getRemoveButtonTextColor() {
        return removeButtonTextColor;
    }

    public void setRemoveButtonTextColor(int removeButtonTextColor) {
        this.removeButtonTextColor = removeButtonTextColor;
        mButtonRemove.setTextColor(removeButtonTextColor);
    }

    public void setRemoveButtonTextColorRes(int removeButtonTextColorRes) {
        this.removeButtonTextColor = ContextCompat.getColor(getContext(), removeButtonTextColorRes);
        mButtonRemove.setTextColor(removeButtonTextColor);
    }

    private int dpFromPx(final float px) {
        return (int) (px / getResources().getDisplayMetrics().density);
    }

    private int pxFromDp(final float dp) {
        return (int) (dp * getResources().getDisplayMetrics().density);
    }


    private boolean isNumber(String string) {
        try {
            Integer.parseInt(string);
            return true;
        } catch (Exception e) {
            return false;
        }
    }


}

