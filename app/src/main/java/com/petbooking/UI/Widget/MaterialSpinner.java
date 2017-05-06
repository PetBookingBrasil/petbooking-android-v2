package com.petbooking.UI.Widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.petbooking.R;

import java.util.List;

/**
 * Created by Luciano José on 06/05/2017.
 */

public class MaterialSpinner extends RelativeLayout {

    private Context mContext;
    private ArrayAdapter<String> mAdapter;
    private ImageView mIvSpinnerIcon;
    private ImageButton mBtnInfo;
    private fr.ganfra.materialspinner.MaterialSpinner mSpinner;

    public MaterialSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        View view = View.inflate(context, R.layout.widget_material_spinner, this);

        mBtnInfo = (ImageButton) view.findViewById(R.id.btn_info);
        mIvSpinnerIcon = (ImageView) view.findViewById(R.id.spinner_icon);
        mSpinner = (fr.ganfra.materialspinner.MaterialSpinner) view.findViewById(R.id.spinner);

        if (attrs != null) {
            TypedArray prop = context.getTheme()
                    .obtainStyledAttributes(attrs, R.styleable.MaterialSpinner, 0, 0);

            Drawable icon = null;
            boolean enableInfo = false;
            String hint = "";
            int entries = -1;

            try {
                hint = prop.getString(R.styleable.MaterialSpinner_hint);
                icon = prop.getDrawable(R.styleable.MaterialSpinner_icon);
                entries = prop.getResourceId(R.styleable.MaterialSpinner_entries, -1);
                enableInfo = prop.getBoolean(R.styleable.MaterialSpinner_enableInfo, false);
            } catch (Exception e) {
                Log.e("SPINNER", "There was an error loading attributes.");
            } finally {
                prop.recycle();
            }

            if (!enableInfo) {
                mBtnInfo.setVisibility(View.GONE);
            }

            setHint(hint);

            if (icon != null) {
                setIcon(icon);
            }

            if (entries != -1) {
                String[] array = context.getResources().getStringArray(entries);
                setItems(array);
            }
        }
    }

    /**
     * Set Spinner options with
     * String array
     *
     * @param itens
     */
    public void setItems(String[] itens) {
        mAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, itens);
        mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(mAdapter);
    }

    /**
     * Set Spinner options with
     * String list
     *
     * @param itens
     */
    public void setItems(List<String> itens) {
        mAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, itens);
        mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(mAdapter);
    }


    /**
     * Set Spinner options with
     * Reference
     *
     * @param itens
     */
    public void setItems(int itens) {
        mAdapter = new ArrayAdapter<>(mContext, android.R.layout.simple_spinner_item, mContext.getResources().getStringArray(itens));
        mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(mAdapter);
    }


    /**
     * Set Input hint
     *
     * @param hint
     */
    public void setHint(String hint) {
        mSpinner.setHint(hint);
    }

    /**
     * Set Image Icon
     *
     * @param icon
     */
    public void setIcon(Drawable icon) {
        mIvSpinnerIcon.setImageDrawable(icon);
    }

    /**
     * Listener for Item selected
     *
     * @param onItemSelectedListener
     */
    public void setOnItemSelectedListener(AdapterView.OnItemSelectedListener onItemSelectedListener) {
        mSpinner.setOnItemSelectedListener(onItemSelectedListener);
    }

    /**
     * Get Selected Item
     *
     * @return
     */
    public String getSelectedItem() {
        return mSpinner.getSelectedItem().toString();
    }

    /**
     * Get Selected Position
     *
     * @return
     */
    public int getPosition() {
        return mSpinner.getSelectedItemPosition();
    }

    /**
     * Set Action for Info Button
     *
     * @param onInfoClickListener
     */
    public void setOnInfoClickListener(View.OnClickListener onInfoClickListener) {
        mBtnInfo.setOnClickListener(onInfoClickListener);
    }
}
