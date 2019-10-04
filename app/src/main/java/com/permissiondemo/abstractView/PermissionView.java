package com.permissiondemo.abstractView;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.method.ScrollingMovementMethod;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.permissiondemo.R;

public class PermissionView extends ConstraintLayout {


    private ConstraintLayout viewRoot;
    private ImageButton lockImageView;
    private ImageButton listImageView;
    private TextView nameTextView;
    private TextView descTextView;
    private View bottomView;

    public PermissionView(Context context) {
        super(context);
        initView(context);
    }

    public PermissionView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
        setAttributes(context, attrs);

    }

    public PermissionView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
        setAttributes(context, attrs);
    }

    private void initView(Context context) {
        inflate(context, R.layout.permission_view, this);
        viewRoot = this.findViewById(R.id.root_view);

        lockImageView = this.findViewById(R.id.lockDrawable);
        listImageView = this.findViewById(R.id.listDrawable);

        nameTextView = this.findViewById(R.id.name);
        descTextView = this.findViewById(R.id.description);

        bottomView = this.findViewById(R.id.bottomView);


    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setAttributes(Context context, AttributeSet attrs) {

        try {


            TypedArray xmlAttrs = context.getTheme().obtainStyledAttributes(
                    attrs, R.styleable.PermissionView, 0, 0);


            setName(xmlAttrs.getString(R.styleable.PermissionView_name));
            setDescription(xmlAttrs.getString(R.styleable.PermissionView_description));


            setNameColor(xmlAttrs.getColor(R.styleable.PermissionView_nameColor, 0xff000000));
            setDescriptionColor(xmlAttrs.getColor(R.styleable.PermissionView_descriptionColor, 0xff000000));

            int bgColor = xmlAttrs.getResourceId(R.styleable.PermissionView_view_background_color, 0);
            if (bgColor > 0)
                viewRoot.setBackgroundColor(ContextCompat.getColor(context, bgColor));


            setLockImageView(xmlAttrs.getDrawable(R.styleable.PermissionView_drawLock));
            setLockResource(xmlAttrs.getResourceId(R.styleable.PermissionView_resLock, 0));
            setLockTint(xmlAttrs.getResourceId(R.styleable.PermissionView_tintLock, 0));

            setListImageView(xmlAttrs.getDrawable(R.styleable.PermissionView_drawList));
            setListResource(xmlAttrs.getResourceId(R.styleable.PermissionView_resList, 0));
            setListTint(xmlAttrs.getResourceId(R.styleable.PermissionView_tintList, 0));


            setTextScrollable(nameTextView, xmlAttrs.getBoolean(R.styleable.PermissionView_textScroll, false));

            setVisibility(nameTextView, xmlAttrs.getString(R.styleable.PermissionView_namevisibility));
            setVisibility(descTextView, xmlAttrs.getString(R.styleable.PermissionView_descriptionvisibility));
            setVisibility(lockImageView, xmlAttrs.getString(R.styleable.PermissionView_lockvisibility));
            setVisibility(listImageView, xmlAttrs.getString(R.styleable.PermissionView_listvisibility));

            bottomView.setBackgroundColor(xmlAttrs.getColor(R.styleable.PermissionView_viewColor, 0x00000000));

            setVisibility(viewRoot, "visible");


            xmlAttrs.recycle();

        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    public void setName(String name) {
        if (name != null)
            setText(nameTextView, name);
    }

    public void setNameColor(int color) {
        setTextColor(nameTextView, color);
    }

    public void setDescription(String description) {
        if (description != null) {
            setText(descTextView, description);
            setVisibility(descTextView, "visible");
        } else {
            setVisibility(descTextView, "gone");
        }
    }

    public void setDescriptionColor(int color) {
        setTextColor(descTextView, color);
    }

    public void setLockImageView(Drawable resDrawable) {
        if (resDrawable != null) {
            setImageDrawable(lockImageView, resDrawable);
        }
    }

    public void setLockResource(int res) {
        if (res > 0) {
            setImageResource(lockImageView, res);
        }
    }

    public void setLockTint(int res) {
        if (res > 0) {
            setTint(lockImageView, res);
        }
    }


    public void setListImageView(Drawable resDrawable) {
        if (resDrawable != null) {
            setImageDrawable(listImageView, resDrawable);
        }
    }

    public void setListResource(int res) {
        if (res > 0) {
            setImageResource(listImageView, res);
        }
    }


    public void setListTint(int res) {
        if (res > 0) {
            setTint(listImageView, res);
        }
    }


    private void setTint(ImageView view, int color) {
        view.setColorFilter(ContextCompat.getColor(getContext(), color));
    }

    private void setImageResource(ImageView view, int res) {
        view.setImageDrawable(ContextCompat.getDrawable(view.getContext(), res));
    }

    private void setImageDrawable(ImageView view, Drawable resDrawable) {
        view.setImageDrawable(resDrawable);
    }

    private void setTextColor(TextView view, int color) {
        if (color == 0)
            view.setTextColor(0xff000000);
        else
            view.setTextColor(color);
    }

    private void setText(TextView view, String text) {
        view.setText(text);
    }

    private void setVisibility(View view, String visibility) {

        int visible = View.VISIBLE;
        if (visibility != null) {
            if (visibility.equals("gone")) {
                visible = View.GONE;
            } else if (visibility.equals("invisible")) {
                visible = View.INVISIBLE;
            }
        }
        view.setVisibility(visible);
    }

    private void setTextScrollable(TextView view, boolean isScroll) {
        if (isScroll) {
            view.setMovementMethod(new ScrollingMovementMethod());
        }

    }

    public void setListImageViewClick(OnClickListener click) {
        listImageView.setOnClickListener(click);
    }


}
